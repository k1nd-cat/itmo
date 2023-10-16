package net.server;

import data_base.DbConnector;
import net.data.Request;
import net.data.Response;
import worker.PersonStorage;
import worker.ServerDispatcher;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * With multithreading.
 */
public class Server {

    private static final Logger logger = Logger.getLogger("lab6");

    public void run(int port) {
        ServerSocketChannel serverSocket = null;
        try {
            Selector selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress("localhost", port));
            // set non blocking mode
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("waiting for client requests...");

            ExecutorService readRequestExecutor = Executors.newCachedThreadPool();
            ForkJoinPool requestExecutor = new ForkJoinPool(4);
            CLIProcessor cliProcessor = new CLIProcessor();
            RequestListener requestListener = new RequestListener(serverSocket, selector);

            // читать из командной строки
            readRequestExecutor.submit(cliProcessor::executeCli);

            while (true) {
                Future<SocketChannel> expectedChannel = readRequestExecutor.submit(requestListener::readClientRequest);
                SocketChannel client = expectedChannel.get();
                if (client == null) continue;
                Future<Request> expectedRequest = readRequestExecutor.submit(() -> new RequestReader(client).readClientRequest());
                Request request = expectedRequest.get();
                if (request == null) continue;
                Future<Response> expectedResponse = requestExecutor.submit(() -> ServerDispatcher.getDispatcher().perform(request));
                Response response = expectedResponse.get();
                if (response == null) continue;
                requestExecutor.submit(() -> new ResponseWriter(client, response).writeClientResponse());
            }

        } catch (Exception exc) {
            logger.log(Level.WARNING, "Error in net server, server will stop...", exc);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception exc) {
                    logger.log(Level.WARNING, "Cannot close server socket", exc);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new DbConnector().initializeCollection();
            // PersonStorage.initialState();
            Server server = new Server();
            server.run(4521);
        } catch (Exception exc) {
            logger.log(Level.WARNING, "Error in net server, server will stop...", exc);

        }
    }

}

package net.server;

import net.data.Request;
import net.data.Response;
import net.utils.ObjectReaderWriter;
import worker.PersonStorage;
import worker.ServerDispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger logger = Logger.getLogger("lab6");

    private static final String CLI_COMMAND_EXIT = "exit";
    private static final String CLI_COMMAND_SAVE = "save";

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

            while (true) {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        registerClientRequest(selector, serverSocket);
                    }
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(10000);
                        processClientRequest(buffer, key);
                        buffer.clear();
                    }
                    iter.remove();
                }
                if (System.in.available() > 0) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    var command = reader.readLine();
                    if (command.equals(CLI_COMMAND_EXIT)) {
                        logger.info("Server is stopped by user, good-bye!");
                        ServerDispatcher.getDispatcher().performSave();
                        System.exit(1);
                    } else if (command.equals(CLI_COMMAND_SAVE)) {
                        logger.info("Save data");
                        ServerDispatcher.getDispatcher().performSave();
                    } else {
                        logger.info("Unknown server command: " + command);
                    }
                }
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

    private void processClientRequest(ByteBuffer buffer, SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel client = (SocketChannel) key.channel();
        int read = client.read(buffer);
        if (read == -1) {
            logger.info("stop client processing");
            client.close();
        } else {
            Request request = readClientRequest(buffer);

//            System.out.println(request.getCommand());

            Response response = ServerDispatcher.getDispatcher().perform(request);

//            System.out.println(response);

            writeClientResponse(client, response);
        }
    }

    private Request readClientRequest(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        logger.info("reading client data");
        Request request = ObjectReaderWriter.deserializeObject(buffer.array(), Request.class);
        logger.info("received from client: " + request);
        return request;
    }

    private void writeClientResponse(SocketChannel client, Response response) throws IOException, ClassNotFoundException {
        byte[] responseBytes = ObjectReaderWriter.serializeObject(response);
        ByteBuffer buffer = ByteBuffer.wrap(responseBytes);
        writeClientResponseSize(client, buffer);
        client.write(buffer);
    }

    private void writeClientResponseSize(SocketChannel client, ByteBuffer responseBufferSize) throws IOException, ClassNotFoundException {
        byte[] responseBytes = ObjectReaderWriter.serializeObject(responseBufferSize.capacity());
        ByteBuffer buffer = ByteBuffer.wrap(responseBytes);
        client.write(buffer);
    }

    private void registerClientRequest(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        logger.info("start client processing, wait for data...");
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) {
        PersonStorage.initialState();
        Server server = new Server();
        server.run(4521);
    }

}

package net.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * With multithreading.
 */
public class RequestListener {

    private static final Logger logger = Logger.getLogger("lab6");

    Selector selector;
    ServerSocketChannel serverSocket;

    public RequestListener(ServerSocketChannel serverSocket, Selector selector) {
        this.serverSocket = serverSocket;
        this.selector = selector;
    }

    public SocketChannel readClientRequest() {
        try {
            SocketChannel client = null;
            selector.select(1000);
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    registerClientRequest();
                }
                if (key.isReadable()) {
                    client = (SocketChannel) key.channel();
                }
                iter.remove();
            }
            return client;
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private void registerClientRequest() throws IOException {
        logger.info("start client processing, wait for data...");
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

}

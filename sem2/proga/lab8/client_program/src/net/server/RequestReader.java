package net.server;

import net.data.Request;
import net.utils.ObjectReaderWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * With multithreading.
 */
public class RequestReader {

    private static final Logger logger = Logger.getLogger("lab6");

    private SocketChannel client;

    public RequestReader(SocketChannel client) {
        this.client = client;
    }

    public Request readClientRequest() {
        try {
            logger.info("Perform request reading in thread: " + Thread.currentThread().getName());
            return readClientRequest(client);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private Request readClientRequest(SocketChannel client) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = null;
        Request request = null;
        try {
            buffer = ByteBuffer.allocate(10000);
            int read = client.read(buffer);
            if (read == -1) {
                logger.info("stop client processing");
                client.close();
            } else {
                request = readClientRequest(buffer);
            }
        } finally {
            if (buffer != null) buffer.clear();
        }
        return request;
    }

    private Request readClientRequest(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        logger.info("reading client data");
        Request request = ObjectReaderWriter.deserializeObject(buffer.array(), Request.class);
        logger.info("received from client: " + request);
        return request;
    }
}

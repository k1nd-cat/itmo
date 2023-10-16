package net.server;

import net.data.Response;
import net.utils.ObjectReaderWriter;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class ResponseWriter {

    private static final Logger logger = Logger.getLogger("lab6");

    private SocketChannel client;
    private Response response;

    public ResponseWriter(SocketChannel client, Response response) {
        this.client = client;
        this.response = response;
    }

    public void writeClientResponse() {
        byte[] responseBytes = serializeResponse();
        ByteBuffer buffer = ByteBuffer.wrap(responseBytes);
        writeResponseSize(buffer);
        writeData(buffer);
    }

    public byte[] serializeResponse() {
        try {
            return ObjectReaderWriter.serializeObject(response);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private ByteBuffer writeResponseSize(ByteBuffer responseBufferSize) {
        try {
            byte[] responseBytes = ObjectReaderWriter.serializeObject(responseBufferSize.capacity());
            ByteBuffer buffer = ByteBuffer.wrap(responseBytes);
            client.write(buffer);
            return buffer;
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private void writeData(ByteBuffer buffer) {
        try {
            client.write(buffer);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
}
package net.client;

import net.data.Request;
import net.data.Response;
import net.utils.ObjectReaderWriter;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static final Logger logger = Logger.getLogger("lab6");
    private static final String ip = "localhost";
    private static final int port = 4521;
    private static final int timeoutSec = 30;

    private static Client client;

    public static Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    private Client() { }
    public Response sendRequest(Request request) throws Exception {
        SocketChannel client = null;
        try {
            long startRequest = System.currentTimeMillis(), lastAttempt = 0L;
            while (startRequest + timeoutSec * 1000 > System.currentTimeMillis()) {
                try {
                    client = SocketChannel.open(new InetSocketAddress(ip, port));
                    logger.info("send object to server");
                    System.out.println("1 " + request);
                    byte[] objectBytes = ObjectReaderWriter.serializeObject(request);
                    System.out.println(request);
                    ByteBuffer buffer = ByteBuffer.wrap(objectBytes);
                    client.write(buffer);
                    buffer.clear();

                    logger.info("read response from server");
                    buffer = ByteBuffer.allocate(81);
                    client.read(buffer);
                    Integer size = ObjectReaderWriter.deserializeObject(buffer.array(), Integer.class);
                    buffer = ByteBuffer.allocate(size);
                    client.read(buffer);
                    Response response = ObjectReaderWriter.deserializeObject(buffer.array(), Response.class);

                    return response;
                } catch (Exception exc) {
                    if (lastAttempt == 0L) {
                        System.out.print("Connecting to server.");
                        lastAttempt = System.currentTimeMillis();
                    } else if (lastAttempt + 3000L < System.currentTimeMillis()) {
                        System.out.print(".");
                        lastAttempt = System.currentTimeMillis();
                    }
                }
            }
            logger.warning("Cannot connect to server!");
        } catch (Exception exc) {
            logger.log(Level.WARNING, "Cannot connect to server!", exc);
            throw exc;
        } finally {
            if (client != null) try { client.close(); } catch (Exception exc) { }
        }
        throw new Exception("Cannot connect to server!");
    }
}

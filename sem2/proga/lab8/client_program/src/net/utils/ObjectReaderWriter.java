package net.utils;

import net.data.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectReaderWriter {

    @SuppressWarnings("unchecked")
    public static <T> T deserializeObject(byte[] buffer, Class<T> objectClass) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer));
        T obj = (T)in.readObject();
        in.close();
        return obj;
    }

    public static byte[] serializeObject(Object object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(object);
        out.close();
        return baos.toByteArray();
    }

}

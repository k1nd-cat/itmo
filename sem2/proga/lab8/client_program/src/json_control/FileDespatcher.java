package json_control;

import java.io.*;
import java.util.stream.Collectors;

/**
 * class for work with file
 */
public class FileDespatcher {
    /**
     * write info in file
     * @param fileName name file for write at it info
     * @param content content for write in file
     */
    public static void writeInFile(File fileName, String content) {
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(fileName));
            byte[] buff = content.getBytes();
            out.write(buff, 0, content.length());
        } catch (IOException exc) {
            System.err.println("Error when writing to a file" + exc.getMessage());
        } finally {
            if (out != null) try {
                out.close();
            } catch (IOException exc) {}
        }
    }

    /**
     * for read content from file
     * @param fileName file name
     * @return content
     */
    public static String readFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            return reader.lines().parallel().collect(Collectors.joining("\r"));
        } catch (IOException exc) {
            System.err.println("Error when reading a file" + exc.getMessage());
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException exc) {}
        }
        return null;
    }
}
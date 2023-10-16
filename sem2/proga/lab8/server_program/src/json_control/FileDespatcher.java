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
        try(BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));) {
            byte[] buff = content.getBytes();
            out.write(buff, 0, content.length());
        } catch (IOException exc) {
            System.err.println("Error when writing to a file" + exc.getMessage());
        }
    }

    /**
     * for read content from file
     * @param fileName file name
     * @return content
     */
    public static String readFile(String fileName) {

        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))
                    .lines().parallel().collect(Collectors.joining("\r"));
        } catch (IOException exc) {
            System.err.println("Error when reading a file" + exc.getMessage());
        }

        return null;
    }
}
package utils;

import java.util.Scanner;

public class InfoEntry {
    private static boolean cmd = true;
    private static String fileName;
    private static Scanner in;
    public static Scanner scanner() {
        if (cmd) in = new Scanner(System.in);
        else
            in = new Scanner(fileName);

        return in;
    }

    public static void setScanner() {
        in.close();
        cmd = true;
    }

    public static void setScanner(String _fileName) {
        in.close();
        cmd = false;
         fileName = _fileName;
    }
}

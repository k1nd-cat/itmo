package users;

import exceptions.NullStringException;
import net.client.Client;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;

import java.io.BufferedReader;
import java.io.IOException;

public class Identification {
    private UserInfo userInfo;
    private static Identification identification;

    private Identification() {}

    public static Identification getIdentification() {
        if (identification == null) identification = new Identification();
        return identification;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void readInfo(BufferedReader in) {
        while (true) {
            String answer = "";
            while (!(answer.equals("Y")) && !(answer.equals("N")))
                answer = readString(in, "Are you registered? [Y/N]");
            boolean result;
            if (answer.equals("Y")) {
                result = loginUser(in);
            } else {
                result = registerUser(in);
            }
            if (result) break;
        }
    }

    private String readString(BufferedReader in, String msg) {
        try {
            System.out.println(msg);
            String str = in.readLine();
            if (str == null) throw new NullStringException();
            return str;
        } catch (IOException exc) {
            System.err.println("Error in entered value\n" + exc.getMessage());
            return null;
        } catch (NullStringException exc) {
            System.err.println("Fatal error, forced shutdown\n" + exc);
            System.exit(-1);
        }

        return null;
    }

    private boolean registerUser(BufferedReader in) {
        String userName = null;
        boolean isUniqueUsername = false;
        while (!isUniqueUsername) {
            do {
                userName = readString(in, "Enter username");
            } while (userName == null);
            try {
                var command = ServerCommand.checkUserUnique(userName);
                isUniqueUsername = Client.getClient().sendRequest(new Request(command)).getCode() == Response.RESULT_OK;
                if (!isUniqueUsername) {
                    System.out.println("User name already exists, try another one");
                }
            } catch (Exception e) {
                System.out.println("Server error, try again later");
                return false;
            }
        }
        String password;
        do {
            password = readString(in, "Think a password (more than 5 symbols)");
        } while (password == null || password.trim().length() < 5);
        password = new Security().getPasswordHash(password);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userName);
        userInfo.setPassword(password);
        // Отправить логин и пароль на сервер для регистрации
        var command = ServerCommand.registerUser(userInfo);
        try {
            Integer id = Client.getClient().sendRequest(new Request(command)).getBody(Integer.class);
            userInfo.setId(id);
        } catch (Exception e) {
            System.out.println("Server error, try again later");
            return false;
        }
        this.userInfo = userInfo;
        System.out.println("Congratulations!!! You're registered");
        return true;
    }

    private boolean loginUser(BufferedReader in) {
        String userName = readString(in, "Enter user name");
        if (userName == null || userName.length() == 0) {
            System.out.println("Incorrect user name");
            return false;
        }
        UserInfo userInfo = null;
        var command = ServerCommand.loginUser(userName);
        try {
            userInfo = Client.getClient().sendRequest(new Request(command)).getBody(UserInfo.class);
        } catch (Exception e) {}
        if (userInfo == null) {
            System.out.println("Incorrect user name or password");
            return false;
        }
        String password = readString(in, "Enter password");
        boolean result = new Security().checkPassword(userInfo.getPassword(), password);
        if (!result) {
            System.out.println("Incorrect user name or password");
            return false;
        }
        System.out.println("Congratulations!!! You're logged in");
        this.userInfo = userInfo;
        return true;
    }
}

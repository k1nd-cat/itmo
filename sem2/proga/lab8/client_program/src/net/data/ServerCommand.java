package net.data;

import lombok.Getter;
import lombok.Setter;
import users.UserInfo;

import java.io.Serializable;

public class ServerCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    public static enum TYPE {
        add,
        info,
        add_if_max,
        add_if_min,
        remove_greater,
        get_last,
        get_first,
        average_of_height,
        clear,
        show,
        getAll,
        find_by_id,
        check_user_unique,
        register_user,
        login_user,
        update,
        remove
    }

    @Getter
    private TYPE type;


    public ServerCommand() {}

    protected void setType(TYPE type) {
        this.type = type;
    }

    public static ServerCommand add(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.add);
        command.setBody(body);
        return command;
    }

    public static ServerCommand addIfMax(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.add_if_max);
        command.setBody(body);
        return command;
    }

    public static ServerCommand addIfMin(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.add_if_min);
        command.setBody(body);
        return command;
    }

    public static ServerCommand removeGreater(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.remove_greater);
        command.setBody(body);
        return command;
    }

    public static ServerCommand update(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.update);
        command.setBody(body);
        return command;
    }

    public static ServerCommand info(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.info);
        command.setBody(body);
        return command;
    }

    public static ServerCommand remove(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.remove);
        command.setBody(body);
        return command;
    }

    public static ServerCommand getLast() {
        var command = new ServerCommand();
        command.setType(TYPE.get_last);
        return command;
    }

    public static ServerCommand getFirst() {
        var command = new ServerCommand();
        command.setType(TYPE.get_first);
        return command;
    }

    public static ServerCommand getAverageValue() {
        var command = new ServerCommand();
        command.setType(TYPE.average_of_height);
        return command;
    }

    public static ServerCommand clear(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.clear);
        command.setBody(body);
        return command;
    }

    public static ServerCommand show() {
        var command = new ServerCommand();
        command.setType(TYPE.show);
        return command;
    }

    public static ServerCommand getAll() {
        var command = new ServerCommand();
        command.setType(TYPE.getAll);
        return command;
    }

    public static ServerCommand findById(int id) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.find_by_id);
        command.setBody(id);
        return command;
    }

    public static ServerCommand checkUserUnique(String username) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.check_user_unique);
        command.setBody(username);
        return command;
    }

    public static ServerCommand registerUser(UserInfo userInfo) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.register_user);
        command.setBody(userInfo);
        return command;
    }

    public static ServerCommand loginUser(String userName) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.login_user);
        command.setBody(userName);
        return command;
    }

    @Override
    public String toString() {
        return "ServerCommand{" +
                "type=" + type +
                '}';
    }
}

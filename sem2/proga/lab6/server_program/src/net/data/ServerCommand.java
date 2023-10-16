package net.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class ServerCommand implements Serializable {

    public static enum TYPE {
        add,
        get_last,
        get_first,
        average_of_height,
        clear,
        show,
        find_by_id,
        info,
        min_by_location,
        remove,
        remove_greater,
        unique_hair,
        update
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

    public static ServerCommand clear() {
        var command = new ServerCommand();
        command.setType(TYPE.clear);
        return command;
    }

    public static ServerCommand show() {
        var command = new ServerCommand();
        command.setType(TYPE.show);
        return command;
    }

    public static ServerCommand findById(int id) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.find_by_id);
        command.setBody(id);
        return command;
    }

    public static ServerCommand info() {
        var command = new ServerCommand();
        command.setType(TYPE.info);
        return command;
    }

    public static ServerCommand minByLocation() {
        var command = new ServerCommand();
        command.setType(TYPE.min_by_location);
        return command;
    }

    public static ServerCommand removeById(int id) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.remove);
        command.setBody(id);
        return command;
    }

    public static ServerCommand removeGreater(int value) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.remove_greater);
        command.setBody(value);
        return command;
    }

    public static ServerCommand uniqueHair() {
        var command = new ServerCommand();
        command.setType(TYPE.unique_hair);
        return command;
    }

    public static ServerCommand update(Object body) {
        var command = new ServerCommandWithBody();
        command.setType(TYPE.update);
        command.setBody(body);
        return command;
    }

    @Override
    public String toString() {
        return "ServerCommand{" +
                "type=" + type +
                '}';
    }
}

package net.data;

import lombok.Getter;
import lombok.Setter;

public class ServerCommandWithBody extends ServerCommand {

    @Setter
    @Getter
    private Object body;

    @Override
    public String toString() {
        return "ServerCommandWithBody{" +
                "type=" + getType() +
                "body=" + body +
                '}';
    }
}

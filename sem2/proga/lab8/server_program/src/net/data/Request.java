package net.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Request implements Serializable {

    @Getter
    @Setter
    private ServerCommand command;

    public Request() { }

    public Request(ServerCommand command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                '}';
    }
}

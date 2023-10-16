package models;

import lombok.Getter;
import lombok.Setter;

public class Container<T> {
    @Getter
    @Setter
    private T value;

    public Container(T value) {
        this.value = value;
    }
}
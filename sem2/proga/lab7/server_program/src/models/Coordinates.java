package models;

import exceptions.WrongCoordinatesException;
import lombok.*;

import java.io.Serializable;

/**
 * coordinates class
 */
public class Coordinates implements Serializable {
    @Getter
    @NonNull
    private Long x; //Максимальное значение поля: 846, Поле не может быть null
    @Getter
    @NonNull
    private Float y; //Значение поля должно быть больше -859, Поле не может быть null

    /**
     * @param _x input x
     * @param _y input y
     */
    public Coordinates(Long _x, Float _y) {
        setX(_x);
        setY(_y);
    }

    /**
     * @param _x input for change x coordinate
     */
    public void setX(Long _x) {
        if (_x == null || _x > 846) throw new WrongCoordinatesException();
        x = _x;
    }

    /**
     * @param _y input for change y coordinate
     */
    public void setY(Float _y) {
        if (_y == null || _y <= -849) throw new WrongCoordinatesException();
        y = _y;
    }

    /**
     * @return get coordinate in string format
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
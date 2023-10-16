package models;

import java.io.Serializable;
import java.time.ZonedDateTime;
import exceptions.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import utils.ReturnerPersonsParameters;

/**
 * class with main object this project
 */
public class Person implements Comparable<Person>, Serializable {
    @Getter
    @NonNull
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Setter
    private static Integer currentLastId = 1;
    @Getter
    @NonNull
    private String name; //Поле не может быть null, Строка не может быть пустой
    @Getter
    @Setter
    @NonNull
    private Coordinates coordinates; //Поле не может быть null
    @Getter
    @NonNull
    private final ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @Getter
    private long height; //Значение поля должно быть больше 0
    @Getter
    @Setter
    private Color eyeColor; //Поле может быть null
    @Getter
    @Setter
    private Color hairColor; //Поле может быть null
    @Getter
    @Setter
    private Country nationality; //Поле может быть null
    @Getter
    @Setter
    private Location location; //Поле может быть null

    public Person(String _name, Coordinates _coordinates, long _height,  Color _eyeColor,
                  Color _hairColor, Country _nationality, Location _location) {
        setName(_name);
        setCoordinates(_coordinates);
        setHeight(_height);
        eyeColor = _eyeColor;
        hairColor = _hairColor;
        nationality = _nationality;
        location = _location;
        id = currentLastId++;
        creationDate = ZonedDateTime.now();
    }

    public Person(String name, long height, ReturnerPersonsParameters otherParameters) {
        setName(name);
        setHeight(height);
        setCoordinates(otherParameters.coordinates);
        setEyeColor(otherParameters.eyeColor);
        setHairColor(otherParameters.hairColor);
        setNationality(otherParameters.nationality);
        setLocation(otherParameters.location);
        id = currentLastId++;
        creationDate = ZonedDateTime.now();
    }

    public static void setCurrentLastId(int id) {
        currentLastId = id;
    }

    public void updateId() {
        id = currentLastId++;
    }

    public void setName(String _name) {
        if (_name == null || _name.trim().equals(""))
            throw new WrongStringException("Error in name");
        name = _name;
    }

    public void setCoordinates(Coordinates _coordinates) {
        if (_coordinates == null) throw new WrongCoordinatesException();
        coordinates = _coordinates;
    }

    public void setHeight(long _height) {
        if (_height <= 0) throw new WrongHeightException();
        height = _height;
    }

    public long getLocationSize() {
        return location.getY() - location.getX() - location.getZ();
    }

    @Override
    public int compareTo(Person o) {
        if (this == o) return 0;
        if (name.equals(o.name)) return Long.compare(id, o.id);
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        String person = "id:" + id +
                "\nname: " + name +
                "\nheight: " + height +
                "\ncreation date: " + creationDate.getDayOfMonth() + '.' + creationDate.getMonthValue() +
                '.' + (creationDate.getDayOfYear()) + ' ' + creationDate.getHour() +
                ':' + creationDate.getMinute() +
                "\ncoordinates: " + coordinates.toString();
        if (nationality != null) person += "\nnationality: " + nationality;
        if (eyeColor != null) person += "\neye color: " + eyeColor;
        if (hairColor != null) person += "\nhair color: " + hairColor;
        if (location != null) person += "\nlocation: " + location.toString();

        return person;
    }
}
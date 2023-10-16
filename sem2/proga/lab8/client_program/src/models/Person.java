package models;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import exceptions.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ui.i18n.I18nManager;
import users.Identification;
import utils.ReturnerPersonsParameters;

/**
 * class with main object this project
 */
public class Person implements Comparable<Person>, Serializable {

    private static final long serialVersionUID = 1L;

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
    @Setter
    @NonNull
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
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
    @Getter
    @Setter
    private Integer userId; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    public Person() {
        creationDate = ZonedDateTime.now();
    }

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

    public Person(Integer id, String _name, Coordinates _coordinates, long _height,  Color _eyeColor,
                  Color _hairColor, Country _nationality, Location _location) {
        setName(_name);
        setCoordinates(_coordinates);
        setHeight(_height);
        eyeColor = _eyeColor;
        hairColor = _hairColor;
        nationality = _nationality;
        location = _location;
        this.id = id;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStringValue(String key) {
        return
            key.equals("id") ? (getId() != null ? getId().toString() : "")
            : key.equals("name") ? getName()
            : key.equals("height") ? String.valueOf(getHeight())
            : key.equals("nationality") ? (getNationality() != null ? getNationality().name() : "")
            : key.equals("eyeColor") ? (getEyeColor() != null ? getEyeColor().name() : "")
            : key.equals("hairColor") ? (getHairColor() != null ? getHairColor().name() : "")
            : key.equals("coordsX") ? (getCoordinates() != null ? String.valueOf(getCoordinates().getX()) : "")
            : key.equals("coordsY") ? (getCoordinates() != null ? String.valueOf(getCoordinates().getY()) : "")
            : key.equals("locationX") ? (getLocation() != null ? String.valueOf(getLocation().getX()) : "")
            : key.equals("locationY") ? (getLocation() != null ? String.valueOf(getLocation().getY()) : "")
            : key.equals("locationZ") ? (getLocation() != null ? String.valueOf(getLocation().getZ()) : "")
            : key.equals("creationDate") ? (getCreationDate() != null ? DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(getCreationDate().toLocalDate()) : "") : "";
    }

    @Override
    public int compareTo(Person o) {
        if (this == o) return 0;
        if (name.equals(o.name)) return Long.compare(id, o.id);
        return name.compareTo(o.name);
    }

    public long getLocationSize() {
        if (location != null)
            return location.getY() / 2 + location.getX() / 2 + location.getZ() / 2;
        return 0;
    }

    @Override
    public String toString() {
        String person = "id:" + id +
                "\nname: " + name +
                "\nheight: " + height +
                "\ncreation date: " + creationDate.getDayOfMonth() + '.' + creationDate.getMonthValue() +
                '.' + (creationDate.getDayOfYear()) + ' ' + creationDate.getHour() +
                ':' + creationDate.getMinute() +
                (coordinates != null ? "\ncoordinates: " + coordinates.toString() : "");
        if (nationality != null) person += "\nnationality: " + nationality;
        if (eyeColor != null) person += "\neye color: " + eyeColor;
        if (hairColor != null) person += "\nhair color: " + hairColor;
        if (location != null) person += "\nlocation: " + location.toString();

        return person;
    }
}
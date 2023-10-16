import PersonFeatures.Color;
import PersonFeatures.Coordinates;
import PersonFeatures.Country;
import PersonFeatures.Location;

import java.time.LocalDateTime;

public class Person implements Comparable<Person> {
    private static long size;
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private double height; //Значение поля должно быть больше 0
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле может быть null

    public Person(String _name, Coordinates _coordinates, float _height, Color _eyeColor, Color _hairColor, Country _nationality, Location _location) {
        ++size;
        id = size;
        setName(_name);
        setCoordinates(_coordinates);
        creationDate = LocalDateTime.now();
        setHeight(_height);
        setEyeColor(_eyeColor);
        setHairColor(_hairColor);
        setNationality(_nationality);
        setLocation(_location);
    }

    public Person(Long _id, String _name, Coordinates _coordinates, LocalDateTime _creationDate, float _height, Color _eyeColor, Color _hairColor, Country _nationality, Location _location) {
        id = _id;
        setName(_name);
        setCoordinates(_coordinates);
        creationDate = _creationDate;
        setHeight(_height);
        setEyeColor(_eyeColor);
        setHairColor(_hairColor);
        setNationality(_nationality);
        setLocation(_location);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        if (_name.equals(null) || _name.isEmpty()) throw new IllegalArgumentException("Empty Name");
        name = _name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates _coordinates) {
        if (_coordinates == null) throw new NullPointerException("PersonFeatures.Coordinates can't be equal null");
        coordinates = _coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double _height) {
        if (_height <= 0) throw new IllegalArgumentException("Height must be more then 0");
        height = _height;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color _eyeColor) {
        if (_eyeColor == null) throw new NullPointerException("Eye PersonFeatures.Color can't be equal null");
        eyeColor = _eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color _hairColor) {
        if (_hairColor == null) throw new NullPointerException("Hair PersonFeatures.Color can't be equal null");
        hairColor = _hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country _nationality) {
        if (_nationality == null) throw new NullPointerException("Nationality can't be equal null");
        nationality = _nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location _location) {
        if (_location == null) throw new NullPointerException("PersonFeatures.Location can't be equal null");
        location = _location;
    }

    @Override
    public int compareTo(Person other) {
        Double thisHeight = this.height;
        Double otherHeight = other.height;
        return thisHeight.compareTo(otherHeight);
    }

    @Override
    public String toString() {
        StringBuffer person = new StringBuffer();
        person.append("Id: " + id + '\n');
        person.append("Name: " + name + '\n');
        person.append("PersonFeatures.Coordinates: " + coordinates.toString() + '\n');
        person.append("Creation date: " + creationDate + '\n');
        person.append("Height: " + height + '\n');
        person.append("Eye color: " + eyeColor + '\n');
        person.append("Hair color: " + hairColor + '\n');
        person.append("Nationality: " + nationality + '\n');
        person.append("PersonFeatures.Location: " + location.toString() + '\n');

        return person.toString();
    }
}
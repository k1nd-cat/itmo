package PersonFeatures;

public class Coordinates {
    private int x; //Максимальное значение поля: 846, Поле не может быть null
    private Integer y; //Значение поля должно быть больше -859

    public Coordinates(int _x, Integer _y) {
        setX(_x);
        setY(_y);
    }

    public int getX() {
        return x;
    }

    public void setX(int _x) {
        if (_x > 846) throw new IllegalArgumentException("Max x may not exceed 846");
        x = _x;
    }
    public Integer getY() {
        return y;
    }

    public void setY(Integer _y) {
        if (_y <= -859 || _y.equals(null)) throw new IllegalArgumentException("y must exceed -859 or y can't equals null");
        y = _y;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
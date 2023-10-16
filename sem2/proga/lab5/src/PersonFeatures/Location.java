package PersonFeatures;

public class Location {
    private Long x; //Поле не может быть null
    private long y;
    private float z;

    public Location(Long _x, long _y, float _z) {
        setX(_x);
        setY(_y);
        setZ(_z);
    }

    public Long getX() {
        return x;
    }

    public void setX(Long _x)
    {
        if (_x.equals(null)) throw new IllegalArgumentException("X can not be null");
        x = _x;
    }

    public long getY() {
        return y;
    }

    public void setY(long _y) {
        y = _y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float _z) {
        z = _z;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
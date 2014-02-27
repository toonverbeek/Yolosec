package shared;

/**
 *
 * @author Administrator
 */
public class CommSpaceship {

    private int id;
    private float x;
    private float y;
    private int d;

    public CommSpaceship(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

}

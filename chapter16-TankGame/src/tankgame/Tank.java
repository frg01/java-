package tankgame;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class Tank {
    private int x;//坦克横坐标
    private int y;//纵坐标
    private int direction;//坦克方向 0上1右2下3左
    private int speed = 5;
    boolean isLive = true;

    public void moveUp(){
        if (y > 0 ){
            y -= speed;
        }
    }
    public void moveRight(){
        if (x < 1000 -60){
            x += speed;
        }
    }
    public void moveDown(){
        if (y < 750 - 60){
            y += speed;
        }
    }
    public void moveLeft(){
        if (x > 0 ){
            x -= speed;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

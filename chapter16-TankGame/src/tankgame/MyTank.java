package tankgame;

import java.util.Vector;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class MyTank extends Tank {
    //定义一个Shot对象，表示一个射击（线程）
    Shot shot = null;
    Vector<Shot> shots = new Vector<>();

    public MyTank(int x, int y) {
        super(x, y);
    }

    public void shotEnemyTank() {
        if (shots.size() == 5){
            return;
        }
            switch (getDirection()) {
                case 0:
                    shot = new Shot(getX() + 20, getY(), 0);
                    break;
                case 1:
                    shot = new Shot(getX() + 60, getY() + 20, 1);
                    break;
                case 2:
                    shot = new Shot(getX() + 20, getY() + 60, 2);
                    break;
                case 3:
                    shot = new Shot(getX(), getY() + 20, 3);
                    break;
            }
            //子弹加加入集合
        shots.add(shot);
        //启动shot线程

        new Thread(shot).start();
    }
}

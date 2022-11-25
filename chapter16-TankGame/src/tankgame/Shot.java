package tankgame;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class Shot implements Runnable {
     int x;//子弹x坐标
     int y;//子弹y坐标
     int direction;//子弹方向
     int speed = 10;//子弹速度
    boolean isLive = true;//子弹是否存活

    public Shot(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (direction) {
                case 0://向上
                    y -= speed;
                    break;
                case 1://向右
                    x += speed;
                    break;
                case 2://向下
                    y += speed;
                    break;
                case 3://向左
                    x -= speed;
                    break;
            }
            System.out.println("x:" + x + "y:" + y);
            //当子弹碰到边界和敌人坦克时，结束这个线程
            if (!(x>0 && x < 1000 && y > 0 & y < 750 && isLive)) {
                isLive = false;
                break;
            }
        }
    }
}

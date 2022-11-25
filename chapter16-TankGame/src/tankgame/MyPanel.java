package tankgame;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.sun.xml.internal.fastinfoset.util.ValueArrayResourceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
//坦克大战绘图区域
@SuppressWarnings({"all"})
//让子弹不停重绘，将mypanel做成实现线程
public class MyPanel extends JPanel implements KeyListener, Runnable {
    //定义我的tank
    MyTank myTank = null;
    Vector<EnemyTank> enemyTanks = new Vector<>();
    //定义一个用于存放Node 对象的Vector，用于恢复敌人的坦克和坐标
    Vector<Node> nodes = new Vector<>();
    //定义一个Vector，用于存放炸弹
    //当子弹击中坦克时，加入一个Bomb对象到bombs中
    Vector<Bomb> bombs = new Vector<>();
    private int enemyTankNum = 4;

    //定义三张炸弹图片，用于显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;


    public MyPanel(String key) {
        //判断记录文件是否存在，不存在只能开启新游戏
        File file = new File(Recorder.getFilePath());
        if (file.exists()) {
            nodes = Recorder.recordEnemyTankNumAndXYD();
        }else{
            System.out.println("文件不存在，只能重新开始游戏");
            key = "1";
        }

        //将MyPanel对象的enemyTanks 设置给 Recorder 的 enemyTanks
        Recorder.setEnemyTanks(this.enemyTanks);

        myTank = new MyTank(500, 200);

        switch(key) {
            case "1":
                //置空击杀敌人数量
                Recorder.setAllEnemyTankNum(0);
                //创建敌人坦克
                for (int i = 0; i < enemyTankNum; i++) {
                    //创建敌人坦克
                    EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0);
                    //将enemyTanks设置给enemyTank，用来在enemyTank类里判断敌人坦克之间碰撞
                    enemyTank.setEnemyTanks(enemyTanks);
                    //设定方向
                    enemyTank.setDirection(2);
                    //启动敌人坦克线程
                    new Thread(enemyTank).start();
                    //给enemyTsnk加入一颗子弹,加入进Vector
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirection());
                    enemyTank.shots.add(shot);
                    //启动shot对象
                    new Thread(shot).start();
                    //加进集合
                    enemyTanks.add(enemyTank);
                }
                break;
            case "2":
                //创建敌人坦克
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    //创建敌人坦克
                    EnemyTank enemyTank = new EnemyTank(node.getX(),node.getY());
                    //将enemyTanks设置给enemyTank，用来在enemyTank类里判断敌人坦克之间碰撞
                    enemyTank.setEnemyTanks(enemyTanks);
                    //设定方向
                    enemyTank.setDirection(node.getDirection());
                    //启动敌人坦克线程
                    new Thread(enemyTank).start();
                    //给enemyTsnk加入一颗子弹,加入进Vector
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirection());
                    enemyTank.shots.add(shot);
                    //启动shot对象
                    new Thread(shot).start();
                    //加进集合
                    enemyTanks.add(enemyTank);
                }
                break;
            default:
                System.out.println("输入有误");
        }


        //初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));

        //这里播放指定的音乐
        new AePlayWave("src\\111.wav").start();
    }

    //显示我方击毁敌人坦克信息
    public void showInfo(Graphics g) {
        //画出玩家的总成绩
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您累计击毁敌方坦克",1020,30);
        drawTank(1020,60,g,0,1);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTankNum() + "",1080,100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);//填充矩形
        //右边信息
        showInfo(g);
        //画出坦克，封装到方法中
        if (myTank != null && myTank.isLive) {
            drawTank(myTank.getX(), myTank.getY(), g, myTank.getDirection(), 0);
        }
        //绘制我的坦克所有子弹
        for (int i = 0; i < myTank.shots.size(); i++) {
            Shot shot = myTank.shots.get(i);
            if (shot != null && shot.isLive == true) {
                g.fillOval(shot.x, shot.y, 3, 3);
            } else {
                myTank.shots.remove(i);
            }
        }
        //如果bombs集合中有对象，就画出
        for (int i = 0; i < bombs.size(); i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //取出炸弹
            Bomb bomb = bombs.get(i);
            //根据当前这个bomb对象的life值去画出对应的图片
            if (bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            //让炸弹生命减少
            bomb.lifeDown();
            //如果bomb的life为0，就从bombs集合中删除
            if (bomb.life == 0) {
                bombs.remove(bomb);
            }
        }
        //画出敌人的坦克,以及子弹
        for (int i = 0; i < enemyTanks.size(); i++) {

            EnemyTank enemyTank = enemyTanks.get(i);


            //判断当前坦克为存活就去画
            if (enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirection(), 1);
                //画出敌人所有坦克的子弹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    //取出子弹
                    Shot shot = enemyTank.shots.get(j);
                    if (shot.isLive) {
                        g.fillOval(shot.x, shot.y, 3, 3);
                    } else {//从Vector移除，不让其一直保存在Vector里
                        enemyTank.shots.remove(shot);
                    }
                }

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //处理wasa键
    @Override
    public void keyPressed(KeyEvent e) {
        //Tank方向改变移动
        if (e.getKeyCode() == KeyEvent.VK_W) {//按下W键
            myTank.setDirection(0);
            myTank.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            myTank.setDirection(1);
            myTank.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            myTank.setDirection(2);
            myTank.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            myTank.setDirection(3);
            myTank.moveLeft();
        }
        //如果按下的是J键，就发射一颗炮弹
        if (e.getKeyCode() == KeyEvent.VK_J) {
            //判断myTank的一颗子弹是否销毁
//                if (myTank.shot == null || !myTank.shot.isLive) {
//                    myTank.shotEnemyTank();
//                }
            //myTank的一颗子弹发射多颗
            myTank.shotEnemyTank();
        }

        //让面板重绘
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    //编写方法，画出坦克

    /**
     * @param x         坦克左上角x坐标
     * @param y         坦克左上角y坐标
     * @param g         画笔
     * @param direction 坦克方向
     * @param type      坦克类型（颜色）
     */
    public void drawTank(int x, int y, Graphics g, int direction, int type) {

        //根据不同类型坦克，区分颜色
        switch (type) {
            case 0://我们的坦克
                g.setColor(Color.cyan);
                break;
            case 1://敌人的坦克
                g.setColor(Color.yellow);
                break;
        }

        //根据塔克方向，来绘制坦克
        switch (direction) {
            case 0://表示向上
                g.fill3DRect(x, y, 10, 60, false);//左边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//坦克身子
                g.fill3DRect(x + 30, y, 10, 60, false);//右边轮子
                g.fillOval(x + 10, y + 20, 20, 20);//炮圆塔
                g.drawLine(x + 20, y + 30, x + 20, y);//炮筒
                break;
            case 1://表示向右
                g.fill3DRect(x, y, 60, 10, false);//左边轮子
                g.fill3DRect(x, y + 30, 60, 10, false);//右边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//坦克身子
                g.fillOval(x + 20, y + 10, 20, 20);//炮圆塔
                g.drawLine(x + 30, y + 20, x + 60, y + 20);//炮筒
                break;
            case 2://表示向下
                g.fill3DRect(x, y, 10, 60, false);//左边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//坦克身子
                g.fill3DRect(x + 30, y, 10, 60, false);//右边轮子
                g.fillOval(x + 10, y + 20, 20, 20);//炮圆塔
                g.drawLine(x + 20, y + 30, x + 20, y + 60);//炮筒
                break;
            case 3://表示向左
                g.fill3DRect(x, y, 60, 10, false);//左边轮子
                g.fill3DRect(x, y + 30, 60, 10, false);//右边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//坦克身子
                g.fillOval(x + 20, y + 10, 20, 20);//炮圆塔
                g.drawLine(x + 30, y + 20, x, y + 20);//炮筒
                break;
            default:
                System.out.println("暂时无处理");
        }
    }

    //需要把发射的子弹集合中所有子弹取出来和敌人所有坦克，进行判断
    public void hitEnemyTank() {
        for (int i = 0; i < myTank.shots.size(); i++) {
            Shot shot = myTank.shots.get(i);
            //判断是否击中了敌人坦克
            if (shot != null && shot.isLive) {
                //遍历敌人搜友坦克
                for (int j = 0; j < enemyTanks.size(); j++) {
                    EnemyTank enemyTank = enemyTanks.get(j);
                    hitTank(shot, enemyTank);
                }
            }
        }
    }

    //把敌人发射的所有子弹取出来，和我们的坦克，进行判断
    public void hitMyTank() {

        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot shot = enemyTank.shots.get(j);
                if (shot != null && shot.isLive) {
                    hitTank(shot, myTank);
                }
            }
        }
    }


    //编写方法，判断子弹是否击中坦克
    public void hitTank(Shot s, Tank tank) {
        //判断s 击中坦克
        switch (tank.getDirection()) {
            case 0:
            case 2:
                if (s.x > tank.getX() && s.x < tank.getX() + 40 &&
                        s.y > tank.getY() && s.y < tank.getY() + 60) {
                    s.isLive = false;
                    tank.isLive = false;
                    //子弹击中敌方时，就使用方法对allEnemyTankNum++
                    if (tank instanceof EnemyTank){
                        Recorder.addAllEnemyTankNum();
                    }
                    //我的子弹击中敌人坦克，把敌人坦克从集合中移除
                    enemyTanks.remove(tank);
                    //创建一个Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if (s.x > tank.getX() && s.x < tank.getX() + 60 &&
                        s.y > tank.getY() && s.y < tank.getY() + 40) {
                    s.isLive = false;
                    tank.isLive = false;
                    //子弹击中敌方时，就使用方法对allEnemyTankNum++
                    if (tank instanceof EnemyTank){
                        Recorder.addAllEnemyTankNum();
                    }
                    //我的子弹击中敌人坦克，把敌人坦克从集合中移除
                    enemyTanks.remove(tank);
                    //创建一个Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            //判断是否击中了敌人坦克
//            if (myTank.shot != null && myTank.shot.isLive){
//                //遍历敌人搜友坦克
//                for (int i = 0; i < enemyTanks.size(); i++) {
//                    EnemyTank enemyTank = enemyTanks.get(i);
//                    hitTank(myTank.shot, enemyTank);
//                }
//            }
            //敌人击中我的坦克
            hitMyTank();
            //我击中敌人坦克
            hitEnemyTank();
            this.repaint();
        }
    }
}

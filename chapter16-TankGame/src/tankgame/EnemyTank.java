package tankgame;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.Vector;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
@SuppressWarnings({"all"})
public class EnemyTank extends Tank implements Runnable {
    //    Tank enemyTank = new Tank(getX(),getY());
    int speed = 4;
    Vector<Shot> shots = new Vector<>();
    //创建集合接收所mypanel创建的坦克
    Vector<EnemyTank> enemyTanks = new Vector<>();
    boolean isLive = true;

    public EnemyTank(int x, int y) {
        super(x, y);
    }

    public void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }

    public boolean isTouchEnemyTank() {
        //判断当前坦克的方向
        switch (this.getDirection()) {
            case 0://判断对象坦克也在上方
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //从Vector中取出一个坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //自己与自己不去比较
                    if (enemyTank != this) {
                        //如果比较对象是上下方向
                        if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                            //从坦克接触的上两边顶点进行两次位置判断
                            //this.getX与this.getY() 左上角点位置
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 40 &&
                                    this.getY() <= enemyTank.getY() + 60 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                            //右上角顶点位置 this.getX() + 40,this.getY()
                            if (this.getX() + 40 >= enemyTank.getX() &&
                                    this.getX() + 40 <= enemyTank.getX() + 40 &&
                                    this.getY() <= enemyTank.getY() + 60 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                        }
                        //如果比较对象是左右方向
                        if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                            //从坦克接触的两边顶点进行两次位置判断
                            //this.getX 与this.getY() 左上角点位置
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 60 &&
                                    this.getY() <= enemyTank.getY() + 40 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                            //右上角顶点位置 this.getX() + 40,this.getY()
                            if (this.getX() + 60 >= enemyTank.getX() &&
                                    this.getX() + 60 <= enemyTank.getX() + 60 &&
                                    this.getY() <= enemyTank.getY() + 40 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 1://判断对象也在右边
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //从Vector中取出一个坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //自己与自己不去比较
                    if (enemyTank != this) {
                        //如果比较对象是上下方向
                        if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                            //从坦克接触的右两边顶点进行两次位置判断
                            //右上角点位置 this.getX() + 60 与 this.getY()
                            if (this.getX() + 60 >= enemyTank.getX() &&
                                    this.getX() + 60 <= enemyTank.getX() + 40 &&
                                    this.getY() <= enemyTank.getY() + 60 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                            //右下角顶点位置 this.getX() + 60,this.getY() + 40
                            if (this.getX() + 60 >= enemyTank.getX() &&
                                    this.getX() + 60 <= enemyTank.getX() + 40 &&
                                    this.getY() + 40 <= enemyTank.getY() + 60 &&
                                    this.getY() + 40 >= enemyTank.getY()) {
                                return true;
                            }
                        }
                        //如果比较对象是左右方向
                        if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                            //从坦克接触的两边顶点进行两次位置判断
                            //this.getX() + 60 与this.getY()  左上角点位置
                            if (this.getX() + 60 >= enemyTank.getX() &&
                                    this.getX() + 60 <= enemyTank.getX() + 60 &&
                                    this.getY() <= enemyTank.getY() + 40 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                            //右上角顶点位置 this.getX() + 60,this.getY() + 40
                            if (this.getX() + 60 >= enemyTank.getX() &&
                                    this.getX() + 60 <= enemyTank.getX() + 60 &&
                                    this.getY() + 40 <= enemyTank.getY() + 40 &&
                                    this.getY() + 40 >= enemyTank.getY()) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 2://判断对象也在下方
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //从Vector中取出一个坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //自己与自己不去比较
                    if (enemyTank != this) {
                        //如果比较对象是上下方向
                        if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                            //从坦克接触的右两边顶点进行两次位置判断
                            //左下角点位置 this.getX() 与 this.getY() +60
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 40 &&
                                    this.getY() + 60 <= enemyTank.getY() + 60 &&
                                    this.getY() + 60 >= enemyTank.getY()) {
                                return true;
                            }
                            //右下角顶点位置 this.getX() + 40与 this.getY() +60
                            if (this.getX() + 40 >= enemyTank.getX() &&
                                    this.getX() + 40 <= enemyTank.getX() + 40 &&
                                    this.getY() + 60 <= enemyTank.getY() + 60 &&
                                    this.getY() + 60 >= enemyTank.getY()) {
                                return true;
                            }
                        }
                        //如果比较对象是左右方向
                        if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                            //从坦克接触的两边顶点进行两次位置判断
                            //左下角顶点 this.getX() 与this.getY() + 60
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 60 &&
                                    this.getY() + 60 <= enemyTank.getY() + 40 &&
                                    this.getY() + 60 >= enemyTank.getY()) {
                                return true;
                            }
                            //右下角顶点位置 this.getX() + 40,this.getY() + 60
                            if (this.getX() + 40 >= enemyTank.getX() &&
                                    this.getX() + 40 <= enemyTank.getX() + 60 &&
                                    this.getY() + 60 <= enemyTank.getY() + 40 &&
                                    this.getY() + 60 >= enemyTank.getY()) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 3:
                //判断对象也在左方
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //从Vector中取出一个坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    //自己与自己不去比较
                    if (enemyTank != this) {
                        //如果比较对象是上下方向
                        if (enemyTank.getDirection() == 0 || enemyTank.getDirection() == 2) {
                            //从坦克接触的右两边顶点进行两次位置判断
                            //左上角点位置 this.getX() 与 this.getY()
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 40 &&
                                    this.getY() <= enemyTank.getY() + 60 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                            //右下角顶点位置 this.getX() 与 this.getY() + 40
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 40 &&
                                    this.getY() + 40 <= enemyTank.getY() + 60 &&
                                    this.getY() + 40 >= enemyTank.getY()) {
                                return true;
                            }
                        }
                        //如果比较对象是左右方向
                        if (enemyTank.getDirection() == 1 || enemyTank.getDirection() == 3) {
                            //从坦克接触的两边顶点进行两次位置判断
                            //左下角顶点 this.getX() 与this.getY()
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 60 &&
                                    this.getY() <= enemyTank.getY() + 40 &&
                                    this.getY() >= enemyTank.getY()) {
                                return true;
                            }
                            //右下角顶点位置 this.getX(),this.getY() + 40
                            if (this.getX() >= enemyTank.getX() &&
                                    this.getX() <= enemyTank.getX() + 60 &&
                                    this.getY() + 40 <= enemyTank.getY() + 40 &&
                                    this.getY() + 40 >= enemyTank.getY()) {
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }




@Override
public void run(){
        while(true){
        //如果shots.size() = 0,则创建一颗子弹放入shots中,并启动
        if(isLive&&shots.size()< 1){
        Shot s=null;
        switch(getDirection()){
        case 0:
        s=new Shot(getX()+20,getY(),0);
        break;
        case 1:
        s=new Shot(getX()+60,getY()+20,1);
        break;
        case 2:
        s=new Shot(getX()+20,getY()+60,2);
        break;
        case 3:
        s=new Shot(getX(),getY()+20,3);
        break;
        }
        shots.add(s);
        //启动
        new Thread(s).start();
        }
        //根据坦克的方向移动
        switch(getDirection()){
        case 0://上
        //让坦克保持一个方向走30步
        for(int i=0;i< 30;i++){
            if (!isTouchEnemyTank()) {
                moveUp();
            }
        //休眠50毫秒
        try{
        Thread.sleep(50);
        }catch(InterruptedException e){
        e.printStackTrace();
        }
        }
        break;
        case 1://右
        for(int i=0;i< 30;i++){
            if (!isTouchEnemyTank()) {
                moveRight();
            }
        try{
        Thread.sleep(50);
        }catch(InterruptedException e){
        e.printStackTrace();
        }
        }
        break;

        case 2://下
        for(int i=0;i< 30;i++){
            if (!isTouchEnemyTank()) {
                moveDown();
            }
        try{
        Thread.sleep(50);
        }catch(InterruptedException e){
        e.printStackTrace();
        }
        }
        break;

        case 3://左
        for(int i=0;i< 30;i++){
            if (!isTouchEnemyTank()) {
                moveLeft();
            }
        try{
        Thread.sleep(50);
        }catch(InterruptedException e){
        e.printStackTrace();
        }

        }
        break;

        }

        //然后在改变方向,随机改变方向
        setDirection((int)(Math.random()*4));
        //多线程要考虑什么时候退出
        if(!isLive){
        break;
        }
        }
        }
        }

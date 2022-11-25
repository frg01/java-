package tankgame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class hspTankGame extends JFrame {
    //定义MyPanel
    private MyPanel mp = null;
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        hspTankGame hspTankGame = new hspTankGame();
    }

    public hspTankGame() {//构造器初始化框
        System.out.println("请输入选择 1.新游戏  2：继续上局");
        String key = scanner.next();
        mp = new MyPanel(key);//初始化自己坦克位置
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp);//=this.add()
        this.setSize(1300,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(mp);
        this.setVisible(true);
        //在JFrame中增加监听或者响应关闭窗口处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("监听到关闭窗口了");
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}


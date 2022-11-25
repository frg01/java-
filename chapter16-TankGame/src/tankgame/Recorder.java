package tankgame;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.*;
import java.util.Vector;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * <p>
 * 记录相关的信息，与文件交互
 */
public class Recorder {
    //定义变量，击毁坦克数
    private static int allEnemyTankNum = 0;
    //定义IO对象
    private static FileWriter fw = null;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    //保存到src比较好
    private static String filePath = "src\\myRecord.txt";
    //定义Vector，指向MyPanel对象的 敌人坦克Vector
    private static Vector<EnemyTank> enemyTanks = null;
    //创建Vector集合用来接收Node对象
    private static Vector<Node> nodes = new Vector<>();

    //继续上局游戏时使用该方法
    public static Vector<Node> recordEnemyTankNumAndXYD() {
        try {
            br = new BufferedReader(new FileReader(filePath));
            //继续游戏时读取坦克数
            allEnemyTankNum = Integer.parseInt(br.readLine());
            String line = "";
            //循环读取文件,放入对象node，子啊将node放入nodes
            while ((line = br.readLine()) != null) {
                String[] s = line.split(" ");
                Node node = new Node(Integer.parseInt(s[0]),
                        Integer.parseInt(s[1]),
                        Integer.parseInt(s[2]));
                nodes.add(node);
            }
        } catch (IOException e) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nodes;

    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    //增加一个方法，当游戏退出时，我们将allEnemyTankNum 保存到myRecord.txt
    //保存敌人坦克的方向和坐标
    public static void keepRecord() {
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(allEnemyTankNum + "\r\n");
            //遍历敌人坦克的Vector，然后根据情况保存
            //OOP定义一个属性，通过setXxx得到敌人坦克的Vector
            for (int i = 0; i < enemyTanks.size(); i++) {
                //取出敌人坦克
                EnemyTank enemyTank = enemyTanks.get(i);
                if (enemyTank.isLive) {//建议判断
                    //保存改坦克信息
                    String record = enemyTank.getX() + " " + enemyTank.getY() + " " + enemyTank.getDirection();
                    //写入到文件
                    bw.write(record + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }

    public static void setAllEnemyTankNum(int allEnemyTankNum) {
        Recorder.allEnemyTankNum = allEnemyTankNum;
    }

    public static void addAllEnemyTankNum() {
        allEnemyTankNum++;
    }
}

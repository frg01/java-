package com.hspedu.qqServerService;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 该类用于管理和客户端通讯的线程
 */
public class ManageClientThreads {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    //返回hm
    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //添加线程到 hm 集合
    public static void addClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId, serverConnectClientThread);
    }

    //根据userId返回一个ServerConnectClientThread线程
    public static ServerConnectClientThread getServerConnectThread(String userId){
        return hm.get(userId);
    }
    //增加从集合中移除某个线程对象
    public static void removeServerConnectThread(String userId){
        hm.remove(userId);
    }
    //编写方法，返回在线用户列表
    public static String getOnlineUser(){
        //集合遍历hashmap的key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList  = "";
        while (iterator.hasNext()) {
            onlineUserList +=  iterator.next() + " ";
            
        }
        return onlineUserList;
    }
}

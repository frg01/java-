package com.hspedu.qqClient.service;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.HashMap;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class ManageClientConnectThread {
    //多个线程放入HashMap中，key是用户ID，value是线程
    private static HashMap<String ,ClientServerConnectThread> hm = new HashMap<>();

    //将某个线程加入到集合
    public static void addClientServerConnectThread(String userId,ClientServerConnectThread clientServerConnectThread){
        hm.put(userId,clientServerConnectThread);
    }

    public static ClientServerConnectThread getClientServerConnectThread(String userId) {
        return hm.get(userId);
    }
}

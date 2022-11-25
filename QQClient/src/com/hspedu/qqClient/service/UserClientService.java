package com.hspedu.qqClient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 完成用户登录验证，注册等
 */
public class UserClientService extends Thread{

    //因为可能在其他地方使用User信息，因此做成成员属性
    private static User u = new User();
    //socket在其他地方也要使用，所以做成属性
    private Socket socket;

    //userId 和 pwd 到服务器验证该用户是否合法
    public boolean checkUser(String userId ,String pwd){
        boolean b = false;
        //创建一个User对象
        u.setUserId(userId);
        u.setPassword(pwd);
        //连接到服务器

        try {
            socket = new Socket(InetAddress.getByName("192.168.1.3"), 9999);
            //得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象
        } catch (IOException e) {
            e.printStackTrace();
        }

        //读取从服务端回复的Massage对象
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message)ois.readObject();

            if (ms.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){//登陆成功

                //创建一个和服务器端保持通信的线程-> 创建一个类 ClientServerConnectThread
                ClientServerConnectThread clientServerConnectThread = new ClientServerConnectThread(socket);
                //启动客户端线程
                clientServerConnectThread.start();
                //为了客户端扩展，将线程放入集合处理
                ManageClientConnectThread.addClientServerConnectThread(userId,clientServerConnectThread);
                b = true;
                //显示离线消息
                System.out.println(ms.getSender() + "对您的离线消息说"+ ms.getContent());
            }else{//登陆失败,不能启动与服务器连通线程，关闭socket
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    //向服务端请求在线用户列表
    public void onlineFriend(){
        //发送一个Message,类型MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());

        //发送给服务器
        //应该得到当前的Socket 对应的 ObjectOutPutStream对象
        try {
            //从管理线程集合中，通过userId得到这个线程
            ClientServerConnectThread clientServerConnectThread = ManageClientConnectThread.getClientServerConnectThread(u.getUserId());
            //得到clientServerConnectThread关联的socket
            Socket socket = clientServerConnectThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

//            ObjectOutputStream oos = new ObjectOutputStream
//                    (ManageClientConnectThread.getClientServerConnectThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);//发送Message对象，向服务端要求在线用户列表
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //编写方法，退出客户端，并给服务器发送一个message对象
    public static void logout(){
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//一定要指明是哪个客户端id

        //发送message
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectThread.getClientServerConnectThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId() + "退出了系统");
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

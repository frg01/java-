package com.hspedu.qqServerService;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 这是服务器，监听9999，等待客户端的连接，并保持通信
 */
public class qqServer {
    private ServerSocket ss = null;
    //创建一个集合，存放多个用户，如果这些用户登录，就认为合法
    private static ConcurrentHashMap<String,User> validUsers = new ConcurrentHashMap<>();
    //接收离线消息的集合
    private static ConcurrentHashMap<String,ArrayList<Message>> offlineMessageDb = new ConcurrentHashMap<>();


    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("至尊宝",new User("至尊宝","123456"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456"));
    }
    //将离线用户的message保存到ConcurrentHashMap
    public static void storeMessage(String userId,Message message){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        offlineMessageDb.put(userId,messages);
    }

    //验证用户是否有效的方法
    private boolean checkUser(String userId,String passwd){
        User user = validUsers.get(userId);
        if (user == null){//说明userId不存在validUser的key中
            return false;
        }
        if (!user.getPassword().equals(passwd)){//userId正确，但密码错误
            return false;
        }
        return true;
    }

    public qqServer() {
        //端口可以写在配置文件中
        try {
            System.out.println("服务端，监听9999");
            //启动推送新闻的线程
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);

            //监听是循环的，当和某个客户端建立连接后
            while (true) {
                Socket socket = ss.accept();
                //得到socket关联的对象输入流
                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
                //得到socket关联的对象输出流
                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();//读取客户端发送的User对象

                //创建Message对象，准备回复客户端
                Message message = new Message();
                //验证
                if (checkUser(u.getUserId(),u.getPassword())) {//合法用户
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将message对象回复给客户端
                    //线程创建成功后，将客户端可能存在的离线消息发送过去,离线消息和messageType发送
                    if (offlineMessageDb.get(u.getUserId()) != null) {
                        ArrayList<Message> messages = offlineMessageDb.get(u.getUserId());
                        Iterator<Message> iterator = messages.iterator();
                        while (iterator.hasNext()) {
                            Message next =  iterator.next();
                            message.setSender(u.getUserId());
                            message.setContent(next.getContent());
                        }
                    }
                    oos.writeObject(message);

                    //创建一个线程，和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());
                    //启用线程
                    serverConnectClientThread.start();
                    //把线程对象放入到一个集合中
                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);

                } else {//登陆失败
                    System.out.println("用户：" + u.getUserId() + " 密码：" + u.getPassword() + "登陆失败");
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    //关闭socket
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //如果服务端退出while循环，说名服务器不再监听，需关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

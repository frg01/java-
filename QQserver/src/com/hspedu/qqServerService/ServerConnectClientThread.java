package com.hspedu.qqServerService;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 该类对应的对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread{

    private Socket socket;
    private String userId;//连接到服务端的用户id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//线程处于run状态，可以发送和接收消息

        while (true){
            System.out.println("服务端和客户端" + userId + "保持通信，读取数据");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();

                //后面使用message
                //根message的类型，做相应的业务处理
                if (message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端要在线用户列表
                    /*
                    在线用户列表 100 200 300
                     */
                    System.out.println(message.getSender() + "要在线用户列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    //返回Message,所以构建Message,返回客户端
                    Message message1 = new Message();
                    message1.setMessageType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
                    message1.setContent(onlineUser);
                    message1.setReceiver(message.getSender());
                    //返回到客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                }else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)){
                    //判断对方的线程是否存在，不存在则存到ConcurrentHashMap
                    if (!ManageClientThreads.getHm().containsKey(message.getReceiver())){
                        qqServer.storeMessage(message.getReceiver(),message);
                    }else {
                        //根据message获取id  在得到对应的线程
                        ServerConnectClientThread serverConnectThread = ManageClientThreads.getServerConnectThread(message.getReceiver());

                        //得到对应socket的对象输出流， 将message转发给指定的客户端
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectThread.socket.getOutputStream());
                        oos.writeObject(message);//转发，提示：入托客户不在线，保存在数据库，可以实现离线留言
                    }
                }
                else if (message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)){//客户端要退出了
                    System.out.println(message.getSender() + " 退出客户端");
                    //将客户端对应线程从集合中删除
                    ManageClientThreads.removeServerConnectThread(message.getSender());
                    socket.close();//关闭该线程持有的socket
                    //退出线程
                    break;//退出线程（run方法退出）
                }else if (message.getMessageType().equals(MessageType.MESSAGE_TOALL_MES)){
                    //遍历 管理现成的集合，所有线程的socket得到，对message进行转发
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        //取出在线用户的id
                        String onlineUserId = iterator.next().toString();
                        if (!(onlineUserId.equals(message.getSender()))){//排除发消息的用户
                            //进行转发
                            ObjectOutputStream oos =
                                    new ObjectOutputStream(hm.get(onlineUserId).socket.getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }else if (message.getMessageType().equals(MessageType.MESSAGE_FILE_MES)){
                    //判断对方的线程是否存在，不存在则存到ConcurrentHashMap
                    if (!ManageClientThreads.getHm().containsKey(message.getReceiver())) {
                        qqServer.storeMessage(message.getReceiver(), message);
                    }else {
                        //根据receiver的id 获取奥对应的线程，将message对象转发
                        OutputStream outputStream = ManageClientThreads.getServerConnectThread(message.getReceiver()).getSocket().getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                        //转发
                        oos.writeObject(message);
                    }
                }
                else{
                    System.out.println("其他类型message，暂不处理");
                }
            } catch (Exception e) {


            }
        }

    }
}
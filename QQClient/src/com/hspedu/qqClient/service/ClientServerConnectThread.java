package com.hspedu.qqClient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class ClientServerConnectThread extends Thread{
    //该线程需要持有Socket
    private Socket socket;

    //构造器可以接受一个对象
    public ClientServerConnectThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器通信，因此做成while循环
        while(true){
            System.out.println("客户端线程，等待从服务端发送的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();//如果服务器没有发送Message对象，线程会阻塞在这里

                //判断message类型，做相应的业务处理
                //如果的去到的是 服务端返回的在线用户列表
                if (message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n=========当前在线用户列表==========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i] + "在线");
                    }
                }else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)){
                    //把从服务器转发过来的 message 进行显示
                    System.out.println("\n" + message.getSender() + "对" +
                            message.getReceiver() + "说 " + message.getContent() +"   " + message.getSendTime());

                }else if (message.getMessageType().equals(MessageType.MESSAGE_TOALL_MES)){
                    //把群发消息从服务器 接收进行显示
                    System.out.println("\n" + message.getSender() + "对大家说 " + message.getContent());
                }else if (message.getMessageType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println("\n" + message.getSender() + "给" + message.getReceiver() +
                            "发文件：" + message.getSrc() + " 到我们的电脑目录 " + message.getDest());
                //取出message的文件字节数组，通过文件输出流写出到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFilebytes());
                    System.out.println("\n 保存文件成功~");
                }
                else{
                    System.out.println("其他类型message");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //为了方便得到Socket
    public Socket getSocket(){
        return socket;
    }
}

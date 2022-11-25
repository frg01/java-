package com.hspedu.qqClient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 该对象 和服务端 提供和消息相关的一些方法
 *
 */
public class MessageClientService {


    public void sendMessageToAll(String content ,String senderId){
        //构建Message
        Message message = new Message();
        message.setSender(senderId);
        message.setMessageType(MessageType.MESSAGE_TOALL_MES);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到message
        System.out.println(senderId + "对大家说 " + content);

        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectThread.getClientServerConnectThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param content 内容
     * @param senderId 发送用户id
     * @param receiverId 接收用户id
     */

    public void sendMessageToOne(String content ,String senderId, String receiverId){
        //构建Message
        Message message = new Message();
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setMessageType(MessageType.MESSAGE_COMM_MES);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到message
        System.out.println(senderId + "对" + receiverId + "说 " + content);

        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectThread.getClientServerConnectThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.hspedu.qqClient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.*;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 该类/对象完成文件传输服务
 */
public class FileClientService {
    /**
     *
     * @param src 源文件
     * @param dest  文件传输到那个目录
     * @param sendId 发送用户id
     * @param reciverId 接收用户id
     */

    public void sendFileToOne(String src, String dest,String sendId , String reciverId){

        //读取sr文件，封装到message
        Message message = new Message();
        message.setSender(sendId);
        message.setMessageType(MessageType.MESSAGE_FILE_MES);
        message.setReceiver(reciverId);
        message.setSrc(src);
        message.setDest(dest);

        //将文件读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];

        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);//将文件读入到程序的字节数组
            //将文件对应的字节数组设置到message对象
            message.setFilebytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //提示信息
        System.out.println("\n" + message.getSender() +
                "给" + reciverId + "发送文件" + src + "到对方的电脑目录 " + dest);

        //发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectThread.getClientServerConnectThread(message.getSender()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

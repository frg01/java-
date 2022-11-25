package com.hspedu.qqcommon;

import java.io.Serializable;

/**
 * @author: guorui fu
 * @versiion: 1.0
 *客户端与服务端通讯时的一个对象
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;//提高可兼容性

    private String sender;//发送者
    private String receiver;//接收者
    private String content;//发送内容
    private String sendTime;//发送时间
    private String messageType;//消息类型{在接口中定义消息类型}

    //进行扩展 和文件相关的成员
    private byte[] filebytes;
    private  int fileLens  = 0;
    private  String dest;//将文件传输到哪里
    private String src;//源文件路径

    public byte[] getFilebytes() {
        return filebytes;
    }

    public void setFilebytes(byte[] filebytes) {
        this.filebytes = filebytes;
    }

    public int getFileLens() {
        return fileLens;
    }

    public void setFileLens(int fileLens) {
        this.fileLens = fileLens;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
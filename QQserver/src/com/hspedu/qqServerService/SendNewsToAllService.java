package com.hspedu.qqServerService;

import com.hspedu.Utillity.Utility;
import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import sun.rmi.transport.ObjectTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class SendNewsToAllService implements Runnable{

    @Override
    public void run() {
        //为了推送多次新闻，使用while
        while(true) {
            System.out.println("请输入服务器要推送的新闻[输入exit,表示退出推送服务]");
            String news = Utility.readString(100);
            //构建一个消息 群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMessageType(MessageType.MESSAGE_TOALL_MES);
            if ("exit".equals(news)){
                System.out.println("退出推送新闻");
                break;
            }
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息 ，说 " + news);

            //遍历当前所有通讯线程  发送message
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onlineUserId = iterator.next().toString();
                ServerConnectClientThread serverConnectClientThread = hm.get(onlineUserId);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

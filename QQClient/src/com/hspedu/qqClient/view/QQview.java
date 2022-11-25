package com.hspedu.qqClient.view;

import com.hspedu.qqClient.Utility.Utility;
import com.hspedu.qqClient.service.*;
import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 客户端菜单界面
 */
public class QQview {

    private boolean loop = true;
    private String key = "";//接收用户键盘输入
    private UserClientService userClientService =   new UserClientService();//用于登陆服务器或注册
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();//该对象用于传输文件
    public static void main(String[] args) {
        new QQview().mainMenu();
        /*子线程退出解决方法
        1.在main线程调用方法，给服务器端发送一个退出系统的message 对象
        2.调用System.exit(0)//正常退出进程
         */

        System.out.println("客户端已退出系统");
    }

    //显示主菜单
    private void mainMenu() {

        while (loop) {
            System.out.println("===========欢迎登录网络通信系统==========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统系统");
            System.out.println("请输入你的选择：");
            key = Utility.readString(1);

            //根据用户输入，处理不同逻辑
            switch (key) {
                case "1":
                    System.out.println("请输入用户号：");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密码：");
                    String pwd = Utility.readString(50);
                    //验证用户是否违法，用户号和密码是否正确
                    //专门编写一个类，UserClientService[用户登陆注册]

                    if (userClientService.checkUser(userId,pwd)){
                        System.out.println("===========欢迎用户（" + userId +"）登陆成功==========");

                        //进入到二级菜单
                        while(loop){
                            System.out.println("\n===========网络通讯二级菜单 用户（" + userId + ")");
                            System.out.println("\t\t 1 显示用户在线列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入您的选择");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    //写以恶个方法，获取用户在线列表
                                    userClientService.onlineFriend();
                                    System.out.println("显示用户在线列表");
                                    break;
                                    case "2":
                                        System.out.println("请输入想对大家说的话：");
                                        String content1 = Utility.readString(100);
                                        //封装成方法
                                        messageClientService.sendMessageToAll(content1,userId);
                                    System.out.println("群发消息");
                                    break;
                                    case "3":
                                        System.out.println("请输入想聊天的用户名（在线）：");
                                        String receiver = Utility.readString(50);
                                        System.out.println("请输入想说的说：");
                                        String content = Utility.readString(100);
                                        //编写方法，私聊消息发送给服务器端
                                        messageClientService.sendMessageToOne(content,userId,receiver);
                                        System.out.println("私聊消息");
                                    break;

                                    case "4":
                                        System.out.print("请输入你想要发送文件的用户（在线用户）");
                                        String reciverId = Utility.readString(50);
                                        System.out.print("请输入您发送文件的路径（形式如 d:\\xx.jpg）");
                                        String src = Utility.readString(100);
                                        System.out.print("请输入把文件发送到对方的路径（形式如 e:\\yy.jpg）");
                                        String dest = Utility.readString(50);
                                        fileClientService.sendFileToOne(src,dest,userId,reciverId);
                                        break;
                                    case "9":
                                        //调用一个方法，给服务端发送一个退出系统的Message
                                        UserClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }else{//登录服务器失败
                        System.out.println("========登陆失败========");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }

}

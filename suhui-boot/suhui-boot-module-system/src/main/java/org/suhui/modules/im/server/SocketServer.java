package org.suhui.modules.im.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;


/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * @author uptop
 */
@ServerEndpoint("/websocket/{userName}")
@Service
public class SocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static Map<String,SocketServer> webSocketMap = new ConcurrentHashMap<String,SocketServer>();
    //离线消息
    public static Map<String, List<JSONObject>> offlineMsgMap = new ConcurrentHashMap<String,List<JSONObject>>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String userName;
    public String getUserName() {
        return userName;
    }
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,@PathParam(value="userName")String userName) {
        this.session = session;
        System.out.println("username---->>"+userName);
        this.setUserName(userName);
        this.setSession(session);
       // webSocketSet.add(this);     //加入set中
        webSocketMap.put(userName, this);
        addOnlineCount();           //在线数加1
        System.out.println("add-----有新连接加入！当前在线人数为" + getOnlineCount());
        if(offlineMsgMap.get(userName)!=null){
            List<JSONObject> jsonList=offlineMsgMap.get(userName);
            for(int i=0;i<jsonList.size();i++){
                try {
                    session.getBasicRemote().sendText(jsonList.get(i).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            offlineMsgMap.remove(userName);
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
       // webSocketSet.remove(this);  //从set中删除
        webSocketMap.remove(this.getUserName());
        subOnlineCount();           //在线数减1
        System.out.println("close-----有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
//        for (WebSocketTest item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public synchronized static void  sendMessage(int id,String message, String senduser,String receiveuser){
        SocketServer socketServer=webSocketMap.get(receiveuser);
            JSONObject json=new JSONObject();
            json.put("message", message);

            json.put("userKey", senduser);//发送者

            json.put("sendDateTime", "2019-09-03 12:12:12");
            json.put("template", "WebSocket");
            json.put("messageType", 0);
            json.put("type", "current");
            JSONObject  userinfo=new JSONObject();
            userinfo.put("userName", senduser);
            userinfo.put("headImg", "https://pic.cnblogs.com/face/1284270/20180115155117.png");
            json.put("userParameter", userinfo);

            JSONObject  parameter=new JSONObject();
            parameter.put("type", "text");
            parameter.put("len", 1);
            json.put("messageParameter", parameter);
        if(socketServer!=null){
            try {
                //{Content=你好, MessageFromKey=15098899952, MessageKey=15098899952, MessageType=0, Parameter={"type":"text"}, ClientToken=, Template=WebSocket}

//                	json.put("type", "current");
//                	json.put("id", id);
//                	json.put("MessageFromKey", senduser);
//                    json.put("MessageKey", receiveuser);
//                	json.put("Content", message);
//                    json.put("MessageType", 0);
//                    json.put("Parameter", "{\"type\":\"text\"}");
//                    json.put("ClientToken", "");
//                    json.put("Template", "WebSocket");
//                    json.put("sendDateTime", "2019-09-03 12:12:12");

                socketServer.getSession().getBasicRemote().sendText(json.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            System.out.println("-------------------------离线消息---------------------");
            List<JSONObject> list=offlineMsgMap.get(receiveuser);
            if(list==null||list.size()==0){
                List<JSONObject> list1=new ArrayList<JSONObject>();
                list1.add(json);
                offlineMsgMap.put(receiveuser, list1);
            }else{
                list.add(json);
                offlineMsgMap.put(receiveuser, list);
            }
        }
//        for (SocketServer item : webSocketSet) {
//            if(item.getUserName().equals(receiveuser)){
//                try {
//                    //{Content=你好, MessageFromKey=15098899952, MessageKey=15098899952, MessageType=0, Parameter={"type":"text"}, ClientToken=, Template=WebSocket}
//                	JSONObject json=new JSONObject();
////                	json.put("type", "current");
////                	json.put("id", id);
////                	json.put("MessageFromKey", senduser);
////                    json.put("MessageKey", receiveuser);
////                	json.put("Content", message);
////                    json.put("MessageType", 0);
////                    json.put("Parameter", "{\"type\":\"text\"}");
////                    json.put("ClientToken", "");
////                    json.put("Template", "WebSocket");
////                    json.put("sendDateTime", "2019-09-03 12:12:12");
//
//                    json.put("message", message);
//
//                    json.put("userKey", senduser);//发送者
//
//                    json.put("sendDateTime", "2019-09-03 12:12:12");
//                    json.put("template", "WebSocket");
//                    json.put("messageType", 0);
//                    json.put("type", "current");
//                    JSONObject  userinfo=new JSONObject();
//                    userinfo.put("userName", senduser);
//                    userinfo.put("headImg", "https://pic.cnblogs.com/face/1284270/20180115155117.png");
//                    json.put("userParameter", userinfo);
//
//                    JSONObject  parameter=new JSONObject();
//                    parameter.put("type", "text");
//                    parameter.put("len", 1);
//                    json.put("messageParameter", parameter);
//                    item.getSession().getBasicRemote().sendText(json.toString());
//                }catch (Exception e){
//                    e.printStackTrace();
//                    break;
//                }
//                break;
//            }
//        }
        //this.session.getBasicRemote().sendText(message);
    }
    /**
     *
     * 信息群发，我们要排除服务端自己不接收到推送信息
     * 所以我们在发送的时候将服务端排除掉
     * @param message
     */
    public synchronized static void sendAll(String message) {
//        for (SocketServer item : webSocketSet) {
//                try {
//                    item.getSession().getBasicRemote().sendText(message);
//                }catch (Exception e){
//                    continue;
//                }
//        }
    }
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SocketServer.onlineCount--;
    }
}

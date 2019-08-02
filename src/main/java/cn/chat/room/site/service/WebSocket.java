package cn.chat.room.site.service;

import cn.chat.room.site.pojo.Content;
import cn.chat.room.site.pojo.Message;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/webSocket")
public class WebSocket {

    private Session session;
    private String username;

    private static CopyOnWriteArrayList<WebSocket> webSockets = new CopyOnWriteArrayList<>();
    private static Map<String, String> map = new HashMap<>();

    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        //获取用户名
        String s = session.getQueryString();
        String urlUsername = s.split("=")[1];
        try {
            username = URLDecoder.decode(urlUsername, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //把SessionID和用户名放进集合里面
        map.put(session.getId(), username);
        System.out.println("在线人数：" + webSockets.size() + "  sessionId：" + session.getId() + "  " + username);
        String content = "\"" + username + "\" 上线了！";
        Message message = new Message(content, map);
        send(message.toJson());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        map.remove(session.getId());
        System.out.println("有新的断开，总数：" + webSockets.size() + "  sessionId：" + session.getId());
        String content = "\"" + username + "\"  离开了！";
        Message message = new Message(content, map);
        send(message.toJson());
    }

    @OnMessage
    public void onMessage(String json) {

        Content content = gson.fromJson(json, Content.class);


        if (content.getType() == 1) {
            //广播
            Message message = new Message();
            message.setContent(this.username, content.getMsg());
            message.setNames(map);
            send(message.toJson());
            System.out.println(message.toJson());
        } else {
            //单聊
            Message message = new Message();
            message.setContent(this.username, content.getMsg());
            message.setNames(map);

            String to = content.getTo();
            String tos[] = to.substring(0, to.length() - 1).split("-");
            List<String> lists = Arrays.asList(tos);
            for (WebSocket webSocket : webSockets) {
                if (lists.contains(webSocket.session.getId()) && webSocket.session.getId() != this.session.getId()) {
                    try {
                        webSocket.session.getBasicRemote().sendText(message.toJson());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void send(String message) {
        for (WebSocket webSocket : webSockets) {
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

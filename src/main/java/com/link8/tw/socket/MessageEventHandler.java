package com.link8.tw.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.google.gson.Gson;
import com.link8.tw.enums.TaskColumn;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.model.Task;
import com.link8.tw.model.User;
import com.link8.tw.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessageEventHandler {

    public static SocketIOServer socketIoServer;
    static Map<String, List<Client>> listClient = new HashMap<>();

    @Autowired
    TaskService taskService;

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {

    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {

    }

    @OnEvent(value = "join")
    public void joinEvent(SocketIOClient client, String account, String from) throws UserNotFoundException {
        if (listClient.get(account) == null) {
            List<Client> clients = new ArrayList<>();
            clients.add(new Client(client.getSessionId(), account, from));
            listClient.put(account, clients);
        } else {
            listClient.get(account).add(new Client(client.getSessionId(), account, from));
        }
        client.sendEvent("Trace", new Gson().toJson(taskService.getFollowList(account)));
        client.sendEvent("WaitDeal", new Gson().toJson(taskService.getMyList(account, from)));
        client.sendEvent("Doing", new Gson().toJson(taskService.getProcess(from)));
        client.sendEvent("Finished", new Gson().toJson(taskService.getFinishList(account)));
        client.sendEvent("TaskBox", new Gson().toJson(taskService.taskBox(account)));
    }


    /**
     * 給單人發信息
     */
//    public static void sendMessageToPeople(String account, String messageContent) {
//        for (Client client : listClient.get(account)) {
//            if (socketIoServer.getClient(clientId) == null) continue;
//            socketIoServer.getClient(clientId).sendEvent("messageevent", messageContent);
//        }
//    }

    /**
     * 給連線池中所有人發信息
     */
//    public static void sendMessageToListClient(String messageContent) {   //這裡就是向客戶端推訊息了
//        for (String key : listClient.keySet()) {
//            sendMessageToPeople(key, messageContent);
//        }
//    }
    public boolean checkConnect(String account) {
        if (listClient.get(account) != null) {
            for (Client client : listClient.get(account)) {
                if (socketIoServer.getClient(client.getId()) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 給連線池中所有人發訊息
     */
    public void sendMessageToAll(String message, User login) {
        for (String account : listClient.keySet()) {
            if (!account.equals(login.getAccount()))
                sendMessageToUser(account, message, login);
        }
    }

    /**
     * 給帳號發訊息
     */
    public void sendMessageToUser(String account, String message, User login) {
        if (listClient.get(account) != null && !account.equals(login.getAccount())) {
            for (Client client : listClient.get(account)) {
                if (socketIoServer.getClient(client.getId()) == null) continue;
                socketIoServer.getClient(client.getId()).sendEvent("message", message);
            }
        }
    }

    /**
     * 給帳號發送推播消息
     */
    public void sendNotifyToUser(String account, String message) {
        if (listClient.get(account) != null) {
            for (Client client : listClient.get(account)) {
                if (socketIoServer.getClient(client.getId()) == null) continue;
                socketIoServer.getClient(client.getId()).sendEvent("notify", message);
            }
        }
    }

    /**
     * 給連線池中所有人發任務
     */
    public void sendTaskToAll(TaskColumn column,String message) {
        for (String account : listClient.keySet()) {
            sendTaskToUser(account, column,message);
        }
    }

    /**
     * 給帳號發任務
     */
    public void sendTaskToUser(String account, TaskColumn column, String message) {
        if (listClient.get(account) != null) {
            for (Client client : listClient.get(account)) {
                if (socketIoServer.getClient(client.getId()) == null) continue;
                switch (column) {
                    case TRACE:
                        socketIoServer.getClient(client.getId()).sendEvent("Trace", message);
                        break;
                    case TASK_BOX:
                        socketIoServer.getClient(client.getId()).sendEvent("TaskBox", message);
                        break;
                    case WAIT_DEAL:
                        socketIoServer.getClient(client.getId()).sendEvent("WaitDeal", message);
                        break;
                    case FINISHED:
                        socketIoServer.getClient(client.getId()).sendEvent("Finished", message);
                        break;
                    case DOING:
                        if (client.getAccount().equals(client.getFrom())) {
                            socketIoServer.getClient(client.getId()).sendEvent("Doing", message);
                        }
                        break;
                }
            }
        }
    }
}

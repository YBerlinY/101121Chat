package sample.server;

import sample.Commands;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer extends Commands {
    private final AuthService authService;
    private final Map<String,ClientHandler> clients;

    public ChatServer() {
        this.authService = new SimpleAuthService();
        this.clients = new HashMap<>();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);
    }

    public void subscribe(ClientHandler client) {
        clients.put(client.getNick(),client);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client.getNick());
        broadcastClientsList();
    }
    public void sendMessageToClient(ClientHandler from,String nikTO, String msg){
        ClientHandler client= clients.get(nikTO);
       if (client!=null){
                client.sendMessage("Сообщение от " + from.getNick() + ": " + msg);
                from.sendMessage("Сообщение для " + nikTO + ": "+msg);
                return;
            }
        from.sendMessage("участника с ником: "+nikTO+" нет в чате.");
    }

    public void broadcastClientsList(){
        StringBuilder clientsCommand= new StringBuilder(CLIENTS_COMMAND+" ");
        for (ClientHandler client : clients.values()) {
            clientsCommand.append(client.getNick()).append(" ");
        }
        broadcast(clientsCommand.toString());
    }

    public void broadcast(String msg) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(msg);
        }
    }
}
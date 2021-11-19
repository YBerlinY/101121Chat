package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ChatClient extends Commands {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Controller controller;

    public ChatClient(Controller controller) {
        this.controller = controller;
    }

    public void openConnection() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        final String msgAuth = in.readUTF();
                        if (msgAuth.startsWith(AUTH_COMMAND+"OK")) {
                            final String[] split = msgAuth.split(" ");
                            final String nick = split[1];
                            controller.addMessage("Успешная авторизация под ником: " + nick);
                            controller.setAuth(true);
                            break;
                        }
                    }
                    while (true) {
                        final String message = in.readUTF();
                        System.out.println("От сервера получено сообщение :" + message);
                        if (message.startsWith(COMMAND_PREFIX)) {
                            if (END_COMMAND.equals(message)) {
                                controller.setAuth(false);
                                break;
                            }
                            if (message.startsWith(CLIENTS_COMMAND)) {
                                    final String[] tokens = message.replace(CLIENTS_COMMAND + " ", "").split(" ");
                                    final List<String> clients = Arrays.asList(tokens);
                                    controller.updateClientsList(clients);
                            }
                        }
                        controller.addMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.exit(0);
    }


    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
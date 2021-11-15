package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    public static void main(String[] args) {
        try  {ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Ожидаем подключение клиента... ");
            Socket socket = serverSocket.accept();
            System.out.println("Клеиент подключился ");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            final String messageS = in.readUTF();
                            System.out.println("Получено сообщение от клиента: " + messageS);
                            if (messageS.startsWith("/end")) {
                                out.writeUTF("/end");
                                break;
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            while (true) {
                Scanner scr = new Scanner(System.in);
                String messageS = scr.nextLine();
                out.writeUTF(messageS);
            }

        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
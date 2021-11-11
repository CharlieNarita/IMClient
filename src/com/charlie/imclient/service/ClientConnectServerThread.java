package com.charlie.imclient.service;

import com.charlie.imcommon.Message;
import com.charlie.imcommon.MessageType;

import java.io.*;
import java.net.*;

/**
 * @author AC
 * @version 1.0
 * @date 10/14/2021
 */
public class ClientConnectServerThread extends Thread {
    /*
    the thread hold a socket
    and the message
     */
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * thread need always connect to server for communicating
     * this thread hold a socket for connection
     * it is properly using while loop
     */
    @Override
    public void run() {
        System.out.println("Client thread is waiting for message from server...");
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                /*
                attention: readObject() method is blocking method
                thread will be blocking if no object from server
                 */
                Message message = (Message) ois.readObject();

                /*
                check the message type then treating...
                these situations below would be considered
                 */
                switch (message.getMsgType()) {
                    case MessageType.MESSAGE_RETURN_ONLINE_USERS_LIST:
                        String[] s = message.getContent().split(" ");
                        System.out.println("\n=====Online Users List=====");
                        int count = 0;
                        for (String str : s) {
                            System.out.println("User " + (++count) + ": " + str);
                        }
                        break;
                    case MessageType.MESSAGE_PRIVATE_CHAT:
                        System.out.println(message.getSender() + " say: " + message.getContent());
                        break;
                    case MessageType.MESSAGE_PUBLIC_CHAT:
                        System.out.println("\n" + message.getSender() + " say to everyone: " + message.getContent());
                        break;
                    case MessageType.MESSAGE_FILE:
                        byte[] buf = message.getFileBytes();
                        int fileLen = message.getFileLen();
                        String dest = message.getDest();

                        FileOutputStream fos = new FileOutputStream(dest);
                        fos.write(buf);
                        fos.flush();
                        fos.close();
                        System.out.println("file saved! file size = " + fileLen + " bytes");
                        break;
                    default:
                        System.out.println("message is other type");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}

package com.charlie.imclient.service;

import com.charlie.imcommon.Message;
import com.charlie.imcommon.MessageType;
import com.charlie.imcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * for user login or user register
 *
 * @author AC
 * @version 1.0
 * @date 10/14/2021
 */
public class UserClientService {
    /*
    UserClientService hold a User instance as an attribute
     */
    private User user = new User();

    /**
     * send a user object to server for verifying
     * then get the verification result return from server
     * init User instance by parameters userId and password
     *
     * @param userId
     * @param password
     * @return
     */
    public boolean checkUser(String userId, String password) {
        boolean isValid = false;
        //init the user object attributes
        user.setUserId(userId);
        user.setPassword(password);

        //connect local server
        try {
            //init socket by address and port
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            //get ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user); //send user object to server for verifying

            //read the received message object from channel which send by server
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            //get the user verification result
            isValid = MessageType.MESSAGE_LOGIN_SUCCEED.equals(message.getMsgType());

            if (isValid) {
                /*
                create a thread which hold a socket for keeping connect with server
                then put the thread into threads collection as a threads pool
                 */
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                ClientConnectServerThreadManager.addClientConnectServerThread(userId, clientConnectServerThread);
            } else {
                System.out.println("All resource closed...");
                //close all resource
                ois.close();
                oos.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    /**
     * request get online users list from server
     */
    public void getOnlineUsersList() {
        //send a message object which is MESSAGE_GET_ONLINE_USERS_LIST type
        Message requestMsg = new Message();
        requestMsg.setMsgType(MessageType.MESSAGE_GET_ONLINE_USERS_LIST);
        requestMsg.setSender(user.getUserId());

        sendMessage(requestMsg);
    }

    /**
     * client notify server to exit
     */
    public void logoutSystem() {
        //send logout or exit message to server
        Message logoutMsg = new Message();
        logoutMsg.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        logoutMsg.setSender(user.getUserId());

        sendMessage(logoutMsg);

        /*
        exit(0) means exit without exception
        the client thread will finish by this action automatically
         */
        System.exit(0);
    }


    /**
     * action send message
     *
     * @param message
     */
    private void sendMessage(Message message) {
        /*
        get current thread socket matched ObjectOutputStream
        1.define a ObjectOutputStream oos as null;
        2.get current thread which in thread collection pool by userId:
            Thread currentThread = ClientConnectServerThreadManager.getClientConnectServerThread(user.getUserId());
        3.get the OutputStream by current thread holding socket
            OutputStream os = currentThread.getSocket().getOutputStream();
        4.declare the OutputStream os as ObjectOutputStream
            oos = new ObjectOutputStream(os);
         */
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(ClientConnectServerThreadManager.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
            //send message to server
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

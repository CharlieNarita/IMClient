package com.charlie.imclient.service;

import com.charlie.imcommon.Message;
import com.charlie.imcommon.MessageType;

import java.io.*;
import java.util.Date;


/**
 * offer methods to operate file transmission
 *
 * @author AC
 * @version 1.0
 * @date 10/17/2021
 */
public class FileClientService {
    /**
     * @param src
     * @param dest
     * @param senderId
     * @param receiverId
     */
    public void sendFileToSingleUser(String src, String dest, String senderId, String receiverId) {
        //pack a file to message
        Message fileMsg = new Message();
        fileMsg.setMsgType(MessageType.MESSAGE_FILE);
        fileMsg.setSender(senderId);
        fileMsg.setReceiver(receiverId);
        fileMsg.setSrc(src);
        fileMsg.setDest(dest);
        fileMsg.setSendTime(new Date().toString());

        //read file
        FileInputStream fis = null;
        int fileLen = 0;
        byte[] buf = new byte[(int) new File(src).length()];
        try {
            fis = new FileInputStream(src);
            fileLen = fis.read(buf);      //read src file into byte[]
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //init message file byte[] as byte[] buf
        fileMsg.setFileBytes(buf);  //load src file into message byte[]
        fileMsg.setFileLen(fileLen);

        //send message to server
        sendMessage(fileMsg, senderId);

        System.out.println(fileMsg.getSender() + " send " + src + " to " + receiverId + " " + dest);
    }

    /**
     * @param message
     * @param senderId
     */
    private void sendMessage(Message message, String senderId) {
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
            oos = new ObjectOutputStream(ClientConnectServerThreadManager.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

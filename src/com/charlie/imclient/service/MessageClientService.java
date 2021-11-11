package com.charlie.imclient.service;

import com.charlie.imcommon.Message;
import com.charlie.imcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * offer service methods relate message operation
 *
 * @author AC
 * @version 1.0
 * @date 10/17/2021
 */
public class MessageClientService {
    /**
     * @param content
     * @param senderId
     * @param receiverId
     */
    public void sendPrivateChatMessage(String content, String senderId, String receiverId) {
        Message privateChatMsg = new Message();
        privateChatMsg.setMsgType(MessageType.MESSAGE_PRIVATE_CHAT);
        privateChatMsg.setContent(content);
        privateChatMsg.setSender(senderId);
        privateChatMsg.setReceiver(receiverId);
        privateChatMsg.setSendTime(new Date().toString());
        System.out.println(senderId + " say to " + receiverId + ": " + content);

        sendMessage(privateChatMsg, senderId);
    }

    /**
     * @param content
     * @param senderId
     */
    public void sendPublicChatMessage(String content, String senderId) {
        Message publicChatMsg = new Message();
        publicChatMsg.setMsgType(MessageType.MESSAGE_PUBLIC_CHAT);
        publicChatMsg.setContent(content);
        publicChatMsg.setSender(senderId);
        publicChatMsg.setSendTime(new Date().toString());
        System.out.println(senderId + " say to everyone: " + content);

        sendMessage(publicChatMsg, senderId);
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

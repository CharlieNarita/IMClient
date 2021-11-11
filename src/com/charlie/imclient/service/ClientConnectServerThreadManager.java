package com.charlie.imclient.service;

import java.util.HashMap;

/**
 * manage client connect server threads
 * use container to store these threads
 *
 * @author AC
 * @version 1.0
 * @date 10/14/2021
 */
public class ClientConnectServerThreadManager {
    /*
    put threads into the HashMap, the key is userId, the value is threads object
     */
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    /**
     * define a method for adding thread object into HashMap
     *
     * @param userId
     * @param clientConnectServerThread
     */
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }

    /**
     * define a method for getting thread object from HashMap
     *
     * @param userId
     * @return
     */
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }
}

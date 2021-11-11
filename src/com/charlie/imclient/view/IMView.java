package com.charlie.imclient.view;

import com.charlie.imclient.service.FileClientService;
import com.charlie.imclient.service.MessageClientService;
import com.charlie.imclient.service.UserClientService;
import com.charlie.imclient.utils.Utility;
import com.charlie.imcommon.Message;

/**
 * client menu view
 *
 * @author AC
 * @version 1.0
 * @date 10/13/2021
 */
@SuppressWarnings({"all"})
public class IMView {
    private boolean loop1st = true; //init main menu loop as true
    private boolean loop2nd = true;   //secondary menu loop true
    /*
    create a UserClientService instance
    this is MVC design frame
    IMView is View
    UserClientService/MessageClientService/FileClientService are controller
     */
    private UserClientService userClientService = new UserClientService();
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();

    //running main menu
    public static void main(String[] args) {
        new IMView().mainMenu();
    }

    /**
     * display the main menu
     * the structure contains 1st and 2nd level
     * two boolean variables control each level loop
     */
    private void mainMenu() {
        while (loop1st) {
            System.out.println("\n=====Welcome to Login Multi-User Instant Messaging System=====");
            System.out.println("\t\t\t\t\t 1 Login System");
            System.out.println("\t\t\t\t\t 9 Logout System");

            //read keyboard input
            System.out.print("Please enter your selection:");
            //receive keyboard input string
            String key = Utility.readString(1);    //read input by Utility tool class

            //process different logistic according user keyboard input
            switch (key) {
                case "1":
                    //input two attributes userId and password for verifing
                    System.out.print("Please enter your user id: ");
                    String userId = Utility.readString(50);
                    System.out.print("Please enter your password: ");
                    String password = Utility.readString(50);

                    /*
                    this vital step to create user object with input user id and password
                    then send user object to server for verify legal or not
                    this step need a lot of code, so that create class UserClientService
                    pass the userId and password then checkUser() method will create a user object
                    init check user result as false
                     */
                    boolean isValid = userClientService.checkUser(userId, password);
                    if (isValid) {
                        System.out.println("\n=====Welcome User " + userId + " Login succeed!=====");
                        //go to secondary level menu
                        while (loop2nd) {
                            System.out.println("\n=====User " + userId + " Interface Menu=====");
                            System.out.println("\t1 Display Online User List");
                            System.out.println("\t2 Group Message");
                            System.out.println("\t3 Private Message");
                            System.out.println("\t4 Send File");
                            System.out.println("\t9 Exit the Second-Level Menu");
                            System.out.print("Please enter your selection:");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    //invoke a method to display online user list
                                    userClientService.getOnlineUsersList();
                                    break;
                                case "2":
                                    System.out.print("Please enter public chat message to everyone: ");
                                    String publicContent = Utility.readString(256);
                                    messageClientService.sendPublicChatMessage(publicContent, userId);
                                    break;
                                case "3":
                                    System.out.print("Please enter private chat online friend: ");
                                    String receiverId = Utility.readString(50);
                                    System.out.print("Please your message: ");
                                    String privateContent = Utility.readString(256);
                                    messageClientService.sendPrivateChatMessage(privateContent, userId, receiverId);
                                    break;
                                case "4":
                                    String src = "src\\tempfile\\jdk_doc.pdf";
                                    String dest = "src\\tempfile\\jdk_doc_copy.pdf";
                                    System.out.print("Please enter file recive user: ");
                                    String fileReceiverId = Utility.readString(50);
                                    fileClientService.sendFileToSingleUser(src, dest, userId, fileReceiverId);
                                    break;
                                case "9":
                                    loop2nd = false;
                                    System.out.println("Exit the Second-Level Menu!");
                                    break;
                                default:
                            }
                        }
                    } else {
                        System.out.println("Login fail! Please check your user id or password");
                    }
                    break;
                case "9":
                    System.out.println("Logout System!");
                    userClientService.logoutSystem();
                    loop1st = false;
                    break;
                default:
            }
        }
    }
}

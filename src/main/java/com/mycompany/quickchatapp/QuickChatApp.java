/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchatapp;

/**
 *
 * @author lab_services_student
 */
import static com.mycompany.quickchatapp.Message.readStoredMessages;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONObject;

public class QuickChatApp {
    private static String username;
    private static String password;
    private static String cellphone;

    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static int totalMessages = 0;

    public static boolean checkUserName(String user) {
        return user.contains("_") && user.length() <= 5;
    }

    public static boolean checkPasswordComplexity(String pass) {
        return pass.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=<>?/.,:;\"']).{8,}$");
    }

    public static boolean checkCellPhoneNumber(String phone) {
        return phone.startsWith("+") && phone.length() <= 13;
    }

    public static String registerUser(String user, String pass, String phone) {
        if (!checkUserName(user)) {
            return "Username is incorrectly formatted, please ensure that your username contains an underscore and is no longer than five characters .";
        }
        if (!checkPasswordComplexity(pass)) {
            return "Password is incorrectly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(phone)) {
            return "Cellphone number incorrectly formatted or does not contain international code.";
        }

        username = user;
        password = pass;
        cellphone = phone;

        return "User registered successfully.";
    }

    public static boolean loginUser(String inputUser, String inputPass) {
        return inputUser.equals(username) && inputPass.equals(password);
    }

    public static String returnLoginStatus(String firstName, String lastName, boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public static void main(String[] args) {
        // Registering (Creating your account)
        String user = JOptionPane.showInputDialog("Enter a username:\n (must contain '_' and have 5 or less characters)");
        String pass = JOptionPane.showInputDialog("Enter a password:\n(Min 8 characters, 1 uppercase, 1 number, 1 special character)");
        String phone = JOptionPane.showInputDialog("Enter your SA cell number (with international code, e.g., +27831234567):");

        String registrationMessage = registerUser(user, pass, phone);
        JOptionPane.showMessageDialog(null, registrationMessage);

        if (!registrationMessage.equals("User registered successfully.")) {
            return;
        }

        // Logging in to the account
        String loginUser = JOptionPane.showInputDialog("LOGIN \n Enter your username:");
        String loginPass = JOptionPane.showInputDialog("LOGIN \n Enter your password:");
        boolean isLoggedIn = loginUser(loginUser, loginPass);

        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        String loginMessage = returnLoginStatus(firstName, lastName, isLoggedIn);
        JOptionPane.showMessageDialog(null, loginMessage);

        if (!isLoggedIn) {
            return;
        }

        // Show Quickchat menu after a succesful login
        JOptionPane.showMessageDialog(null, "Welcome to Quickchat!");

        int maxMessages = Integer.parseInt(JOptionPane.showInputDialog("How many messages would you like to send?"));
        int messagesSent = 0;

        while (true) {
            String[] options = {"Send Message", "Show Recently Sent Messages", "Quit"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "Quickchat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0 && messagesSent < maxMessages) {
                // The following codes are for when you want you send a message
                String recipient = JOptionPane.showInputDialog("Enter recipient's cell number (start with +, max 13 characters):");
                String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");

                Message msg = new Message(recipient, messageText, messagesSent);

                if (!msg.checkRecipientCell()) {
                    JOptionPane.showMessageDialog(null, "Invalid recipient number. Must start with + and be 13 characters.");
                    continue;
                }

                if (!msg.checkMessageLength()) {
                    JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
                    continue;
                }

                String[] sendOptions = {"Send", "Store for Later", "Discard"};
                int sendChoice = JOptionPane.showOptionDialog(null,
                        "Choose what to do with the message:",
                        "Message Action",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        sendOptions,
                        sendOptions[0]);

                if (sendChoice == 0) {
                    sentMessages.add(msg);
                    messagesSent++;
                    totalMessages++;
                    JOptionPane.showMessageDialog(null, msg.printMessageDetails());
                } else if (sendChoice == 1) {
                    
                    msg.storeMessageToJson();

                } else {
                    JOptionPane.showMessageDialog(null, "Message discarded.");
                }

            } else if (choice == 1) {
                readStoredMessages();
            } else if (choice == 2 || messagesSent >= maxMessages) {
                JOptionPane.showMessageDialog(null, "You sent " + totalMessages + " message(s). Goodbye!");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Message limit reached.");
            }
        }
    }
}

// Message Class
class Message {
    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;
    private int messageNumber;

    public Message(String recipient, String message, int messageNumber) {
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10)); // random digit
        }
        return sb.toString();
    }

    public boolean checkRecipientCell() {
        return recipient.startsWith("+") && recipient.length() <= 13;
    }

    public boolean checkMessageLength() {
        return message.length() <= 250;
    }

    public String createMessageHash() {
        String[] words = message.trim().split("\\s+");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;
        String prefix = messageID.substring(0, 2);
        return (prefix + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    public String printMessageDetails() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + message;
    }
    

public void storeMessageToJson() {
    try {
        JSONObject messageJson = new JSONObject();
        messageJson.put("messageID", messageID);
        messageJson.put("messageHash", messageHash);
        messageJson.put("recipient", recipient);
        messageJson.put("message", message);

        File file = new File("messages.json");
        FileWriter writer = new FileWriter(file, true); // Append mode
        writer.write(messageJson.toString() + System.lineSeparator()); // One JSON per line
        writer.close();

        JOptionPane.showMessageDialog(null, "Message stored in JSON successfully!");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Failed to store message in JSON.");
        e.printStackTrace();
    }
}


public static void readStoredMessages() {
    File file = new File("messages.json");

    if (!file.exists()) {
        JOptionPane.showMessageDialog(null, "No stored messages found.");
        return;
    }

    StringBuilder display = new StringBuilder("Stored Messages:\n\n");

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            JSONObject msg = new JSONObject(line);
            display.append("Message ID: ").append(msg.getString("messageID")).append("\n")
                   .append("Message Hash: ").append(msg.getString("messageHash")).append("\n")
                   .append("Recipient: ").append(msg.getString("recipient")).append("\n")
                   .append("Message: ").append(msg.getString("message")).append("\n\n");
        }

        JOptionPane.showMessageDialog(null, display.toString());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error reading messages.");
        e.printStackTrace();
    }
}

}



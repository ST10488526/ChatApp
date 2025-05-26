/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchatapp;

/**
 *
 * @author lab_services_student
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONObject;
import javax.swing.*;
import java.util.ArrayList;

public class QuickChatApp {
    // User account info
    private static String currentUser;
    private static String currentPass;
    private static String userPhone;
    
    // Message tracking
    private static ArrayList<Message> messageHistory = new ArrayList<>();
    private static int messagesSentCount = 0;

    // Check if username meets requirements
    public static boolean isValidUsername(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    //Password complexity
    public static boolean isStrongPassword(String password) {
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = !password.matches("[A-Za-z0-9]*");
        return password.length() >= 8 && hasUpper && hasNumber && hasSpecial;
    }

    // Validate phone number format
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.startsWith("+") && phone.length() <= 13;
    }

    // Handle user registration
    public static String registerNewUser(String username, String password, String phone) {
        if (!isValidUsername(username)) {
            return "Username is incorrectly formatted, please ensure that your username contains an underscore and is no longer than five characters.";
        }
        if (!isStrongPassword(password)) {
            return "Password is incorrectly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!isValidPhoneNumber(phone)) {
            return "Cellphone number incorrectly formatted or does not contain international code.";
        }

        currentUser = username;
        currentPass = password;
        userPhone = phone;

        return "User registered successfully.";
    }

    // Verify login
    public static boolean verifyLogin(String username, String password) {
        return username != null && password != null && 
               username.equals(currentUser) && 
               password.equals(currentPass);
    }

    // Generate login greeting
    public static String getLoginGreeting(String firstName, String lastName, boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // Main application flow
    public static void main(String[] args) {
        // Account setup
        String username = JOptionPane.showInputDialog("Enter a username:\n (must contain '_' and have 5 or less characters)");
        String password = JOptionPane.showInputDialog("Enter a password:\n(Min 8 characters, 1 uppercase, 1 number, 1 special character)");
        String phone = JOptionPane.showInputDialog("Enter your SA cell number (with international code, e.g., +27831234567):");

        String registrationResult = registerNewUser(username, password, phone);
        JOptionPane.showMessageDialog(null, registrationResult);

        if (!registrationResult.equals("User registered successfully.")) {
            return;
        }

        // Login process
        String loginUsername = JOptionPane.showInputDialog("LOGIN \n Enter your username:");
        String loginPassword = JOptionPane.showInputDialog("LOGIN \n Enter your password:");
        boolean loggedIn = verifyLogin(loginUsername, loginPassword);

        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        JOptionPane.showMessageDialog(null, getLoginGreeting(firstName, lastName, loggedIn));

        if (!loggedIn) {
            return;
        }

        // Start chatapp
        JOptionPane.showMessageDialog(null, "Welcome to Quickchat!");
        
        int messageLimit;
        try {
            messageLimit = Integer.parseInt(JOptionPane.showInputDialog("How many messages would you like to send?"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
            return;
        }

        while (true) {
            String[] menuOptions = {"Send Message", "Show Recently Sent Messages", "Quit"};
            int selectedOption = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "Quickchat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    menuOptions,
                    menuOptions[0]);

            if (selectedOption == 0 && messagesSentCount < messageLimit) {
                handleNewMessage();
            } 
            else if (selectedOption == 1) {
    new Message("", "", 0).showSavedMessages(); 
            } 
            else {
                JOptionPane.showMessageDialog(null, "You sent " + messagesSentCount + " message(s). Goodbye!");
                break;
            }
        }
    }

    // Handle sending a new message
    private static void handleNewMessage() {
        String phoneNumber = JOptionPane.showInputDialog("Enter recipient's cell number (start with +, max 13 characters):");
        String messageContent = JOptionPane.showInputDialog("Enter your message (max 250 characters):");

        Message newMessage = new Message(phoneNumber, messageContent, messagesSentCount);

        if (!newMessage.checkRecipientCell()) {
            JOptionPane.showMessageDialog(null, "Invalid recipient number. Must start with + and be 13 characters.");
            return;
        }

        if (!newMessage.checkMessageLength()) {
            JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
            return;
        }

        String[] messageOptions = {"Send", "Store for Later", "Discard"};
        int selectedAction = JOptionPane.showOptionDialog(null,
                "Choose what to do with the message:",
                "Message Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                messageOptions,
                messageOptions[0]);

        if (selectedAction == 0) {
            messageHistory.add(newMessage);
            messagesSentCount++;
            JOptionPane.showMessageDialog(null, newMessage.printMessageDetails());
        } 
        else if (selectedAction == 1) {
            newMessage.storeMessageToJson();
        }
    }
}


 // Handles the creating, storing, and showing of messages
class Message {
    private String id;          
    private String to;         
    private String content;     
    private String hash;        
    private int msgNum;         

    public Message(String to, String content, int msgNum) {
        this.to = to;
        this.content = content;
        this.msgNum = msgNum;
        this.id = makeRandomId();  
        this.hash = makeHash();     
    }

    // Creates a random 10 digit number for message ID
    private String makeRandomId() {
        Random r = new Random();
        StringBuilder idBuilder = new StringBuilder();
        // Build ID digit by digit
        for (int i = 0; i < 10; i++) {
            idBuilder.append(r.nextInt(10)); // Just add random numbers 0-9
        }
        return idBuilder.toString();
    }

    // Check if phone number looks right (starts with + and is 13 characters long)
    public boolean checkRecipientCell() {
        return to != null && to.startsWith("+") && to.length() <= 13;
    }

    // Make sure message isn't longer 250
    public boolean checkMessageLength() {
        return content != null && content.length() <= 250;
    }

    // Creates a unique fingerprint for the message using first/last words
    private String makeHash() {
        String[] words = content.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";  // Get first word
        String lastWord = words.length > 1 ? words[words.length-1] : firstWord; //Last word
        
        // Combine parts to make the hash
        return String.format("%s:%d:%s%s", 
            id.substring(0, 2),  // First 2 chars of ID
            msgNum,              // Message number
            firstWord,           // First content word
            lastWord).toUpperCase(); // Last content word, all uppercase
    }

    // Shows all message details in a nice format
    public String printMessageDetails() {
        return String.format(
            "Message ID: %s%nMessage Hash: %s%nRecipient: %s%nMessage: %s",
            id, hash, to, content
        );
    }

    
     //Saves the message to a JSON file so we can find it later
     
    public void storeMessageToJson() {
        try {
            // Package all the message data
            JSONObject msgData = new JSONObject();
            msgData.put("messageID", id);
            msgData.put("messageHash", hash);
            msgData.put("recipient", to);
            msgData.put("message", content);

            // Write it to the messages file
            try (FileWriter writer = new FileWriter("messages.json", true)) {
                writer.write(msgData + System.lineSeparator());
            }
        
            // Let user know it worked
            JOptionPane.showMessageDialog(null, "Message stored successfully!");
        } catch (Exception e) {
            // Means something went wrong
            JOptionPane.showMessageDialog(null, "Failed to store message.");
            e.printStackTrace(); // For debugging
        }
    }

    //Shows all saved messages from the file
    public static void showSavedMessages() {
        File file = new File("messages.json");
        
        // Check if we even have any messages saved
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "No stored messages found.");
            return;
        }

        StringBuilder display = new StringBuilder("Stored Messages:\n\n");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each saved message
            while ((line = reader.readLine()) != null) {
                JSONObject msg = new JSONObject(line);
                // Format each message nicely
                display.append("Message ID: ").append(msg.getString("messageID")).append("\n")
                       .append("Message Hash: ").append(msg.getString("messageHash")).append("\n")
                       .append("Recipient: ").append(msg.getString("recipient")).append("\n")
                       .append("Message: ").append(msg.getString("message")).append("\n\n");
            }

            // Show everything found
            JOptionPane.showMessageDialog(null, display.toString());
        } catch (Exception e) {
            // When something went wrong reading the file
            JOptionPane.showMessageDialog(null, "Error reading messages.");
            e.printStackTrace();
        }
    }
}
//References:

/*
OpenAI. (2025). ChatGPT (Version GPT-4), [Large language model]. Code assistance and generation for Java chat application including user registration, login, message handling, and JSON storage, accessed via ChatGPT on https://chat.openai.com on multiple dates in April 2025.


Oracle (2024) How to Use JOptionPane, Oracle Java Tutorials. Available at: https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html (Accessed: 16 May 2025).

GeeksforGeeks (2024) Validating Password Using Java, GeeksforGeeks. Available at: https://www.geeksforgeeks.org/how-to-validate-password-using-regular-expressions-in-java/ (Accessed: 19 May 2025).

W3Schools (2024) Java FileWriter Class, W3Schools. Available at: https://www.w3schools.com/java/java_files_create.asp (Accessed: 20 May 2025).

Baeldung (2023) Introduction to org.json in Java, Baeldung. Available at: https://www.baeldung.com/java-org-json (Accessed: 20 May 2025).

Java Code Junkie (2023) How to Generate Random Strings in Java, JavaCodeJunkie. Available at: https://www.javacodejunkie.com/java-generate-random-string/ (Accessed: 21 May 2025).



*/


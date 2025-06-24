/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchatapp;

/**
 *
 * @author lab_services_student
 */

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import org.json.JSONObject;

public class QuickChatApp {
    // These store the current user’s details
    private static String currentUser;
    private static String currentPass;
    private static String userPhone;

    // This keeps track of all messages that were sent
    private static List<Message> messageHistory = new ArrayList<>();

    public static void main(String[] args) {
        
        handleUserRegistration();
        handleUserLogin();

        JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");

        int messageLimit = getMessageLimit();

        // Loop lets user send multiple messages (based on the limit they chose)
        for (int i = 0; i < messageLimit; i++) {
            int choice = showMainMenu();

            if (choice == 1) {
                handleNewMessage(i); // Send message
            } else if (choice == 2) {
                Message.showSavedMessages(); // Show stored messages
                i--; // Don’t count this as a message
            } else if (choice == 3) {
                JOptionPane.showMessageDialog(null, "You sent " + messageHistory.size() + " message(s). Goodbye!");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
                i--; // Again, don’t count invalid choices
            }
        }
    }

    // Just a friendly greeting when the app starts
    private static void showWelcome() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat Application!");
    }

    // Gets how many messages the user wants to send
    private static int getMessageLimit() {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog("How many messages would you like to send?");
                int number = Integer.parseInt(input);
                if (number > 0) return number;
                JOptionPane.showMessageDialog(null, "Please enter a positive number.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "That wasn’t a valid number.");
            }
        }
    }

    // Shows the menu where the user picks what to do
    private static int showMainMenu() {
        String menu = """
                Choose an option by typing a number:
                1. Send a Message
                2. Show Stored Messages
                3. Quit""";
        try {
            return Integer.parseInt(JOptionPane.showInputDialog(menu));
        } catch (Exception e) {
            return -1;
        }
    }

    // Keeps asking the user to register correctly until they do
    private static void handleUserRegistration() {
        while (true) {
            String username = JOptionPane.showInputDialog("Register a username (must contain '_' and be max 5 characters):");
            String password = JOptionPane.showInputDialog("Enter a strong password:\n(Min 8 chars, 1 uppercase, 1 number, 1 special character)");
            String phone = JOptionPane.showInputDialog("Enter your phone number with international code (e.g., +27831234567):");

            String result = registerNewUser(username, password, phone);
            JOptionPane.showMessageDialog(null, result);
            if (result.equals("User registered successfully.")) break;
        }
    }

    // Keeps prompting for login until user enters correct credentials
    private static void handleUserLogin() {
        while (true) {
            String loginUser = JOptionPane.showInputDialog("LOGIN\nEnter your username:");
            String loginPass = JOptionPane.showInputDialog("LOGIN\nEnter your password:");

            boolean success = verifyLogin(loginUser, loginPass);
            String firstName = JOptionPane.showInputDialog("Enter your first name:");
            String lastName = JOptionPane.showInputDialog("Enter your last name:");

            String loginMessage = getLoginGreeting(firstName, lastName, success);
            JOptionPane.showMessageDialog(null, loginMessage);

            if (success) break;
        }
    }

    // Sends a message, stores it, or discards it depending on what user chooses
    private static void handleNewMessage(int messageNumber) {
        String phone = JOptionPane.showInputDialog("Enter recipient's number (start with +, max 13 chars):");
        String messageText = JOptionPane.showInputDialog("Type your message (max 250 characters):");

        Message message = new Message(phone, messageText, messageNumber);

        if (!message.checkRecipientCell()) {
            JOptionPane.showMessageDialog(null, "Invalid number. Must start with '+' and be up to 13 characters.");
            return;
        }

        if (!message.checkMessageLength()) {
            JOptionPane.showMessageDialog(null, "Message too long! Max 250 characters.");
            return;
        }

        String[] options = {"Send Now", "Store for Later", "Discard"};
        int action = JOptionPane.showOptionDialog(null,
                "Choose what to do with the message:",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (action) {
            case 0 -> {
                messageHistory.add(message);
                JOptionPane.showMessageDialog(null, message.printMessageDetails());
            }
            case 1 -> message.storeMessageToJson();
            default -> JOptionPane.showMessageDialog(null, "Message discarded.");
        }
    }

    // Checks if the username is valid
    public static boolean isValidUsername(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // Checks if password meets all complexity rules
    public static boolean isStrongPassword(String password) {
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = !password.matches("[A-Za-z0-9]*");
        return password.length() >= 8 && hasUpper && hasNumber && hasSpecial;
    }

    // Validates international phone number
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.startsWith("+") && phone.length() <= 13;
    }

    // Handles new user registration
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

    // Verifies login credentials
    public static boolean verifyLogin(String username, String password) {
        return username != null && password != null &&
               username.equals(currentUser) &&
               password.equals(currentPass);
    }

    // Gives a welcome message after logging in
    public static String getLoginGreeting(String firstName, String lastName, boolean success) {
        return success ?
               "Welcome " + firstName + " " + lastName + ", it is great to see you again." :
               "Username or password incorrect, please try again.";
    }
}


// ===============================
// MESSAGE CLASS STARTS HERE
// ===============================

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
        this.id = generateRandomId();
        this.hash = generateMessageHash();
    }

    // This makes a 10-digit random message ID
    private String generateRandomId() {
        Random rand = new Random();
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            idBuilder.append(rand.nextInt(10));
        }
        return idBuilder.toString();
    }

    // Checks if the recipient’s number is valid
    public boolean checkRecipientCell() {
        return to != null && to.startsWith("+") && to.length() <= 13;
    }

    // Makes sure message isn't longer than 250 chars
    public boolean checkMessageLength() {
        return content != null && content.length() <= 250;
    }

    // This makes a simple message hash based on the content
    private String generateMessageHash() {
        String trimmed = content.trim();
        int spaceIndex = trimmed.indexOf(" ");
        String firstWord = spaceIndex == -1 ? trimmed : trimmed.substring(0, spaceIndex);
        String lastWord = trimmed.contains(" ") ? trimmed.substring(trimmed.lastIndexOf(" ") + 1) : trimmed;
        return (id.substring(0, 2) + ":" + msgNum + ":" + firstWord + lastWord).toUpperCase();
    }

    // Nicely prints all details about the message
    public String printMessageDetails() {
        return "Message ID: " + id + "\n" +
               "Message Hash: " + hash + "\n" +
               "Recipient: " + to + "\n" +
               "Message: " + content;
    }

    // Saves the message to a file (in JSON format)
    public void storeMessageToJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("messageID", id);
            json.put("messageHash", hash);
            json.put("recipient", to);
            json.put("message", content);

            try (FileWriter writer = new FileWriter("messages.json", true)) {
                writer.write(json.toString() + System.lineSeparator());
            }

            JOptionPane.showMessageDialog(null, "Message stored successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to store message.");
            e.printStackTrace();
        }
    }

    // Shows messages that were saved before
    public static void showSavedMessages() {
        File file = new File("messages.json");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "No stored messages found.");
            return;
        }

        StringBuilder allMessages = new StringBuilder("Stored Messages:\n\n");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject obj = new JSONObject(line);
                allMessages.append("Message ID: ").append(obj.getString("messageID")).append("\n")
                           .append("Message Hash: ").append(obj.getString("messageHash")).append("\n")
                           .append("Recipient: ").append(obj.getString("recipient")).append("\n")
                           .append("Message: ").append(obj.getString("message")).append("\n\n");
            }

            JOptionPane.showMessageDialog(null, allMessages.toString());
        } catch (Exception e) {
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


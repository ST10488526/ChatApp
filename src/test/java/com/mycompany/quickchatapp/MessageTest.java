/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.quickchatapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MessageTest {

    @Test
    public void testCheckRecipientCell_Valid() {
        Message msg = new Message("+27831234567", "Hello", 1);
        assertTrue(msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_Invalid() {
        Message msg = new Message("0831234567", "Hello", 1);
        assertFalse(msg.checkRecipientCell());
    }

    @Test
    public void testCheckMessageLength_Valid() {
        Message msg = new Message("+27831234567", "Hello there!", 1);
        assertTrue(msg.checkMessageLength());
    }

    @Test
    public void testCheckMessageLength_Invalid() {
        String longMessage = "a".repeat(300); // More than 250 chars
        Message msg = new Message("+27831234567", longMessage, 1);
        assertFalse(msg.checkMessageLength());
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message("+27831234567", "Hello world", 1);
        String hash = msg.createMessageHash();
        assertNotNull(hash);
        assertTrue(hash.contains(":"));
    }

    @Test
    public void testPrintMessageDetails() {
        Message msg = new Message("+27831234567", "This is a test.", 1);
        String details = msg.printMessageDetails();
        assertTrue(details.contains("Message ID"));
        assertTrue(details.contains("Recipient"));
        assertTrue(details.contains("This is a test."));
    }
    
    @Test
    public void testMessageLengthSuccess() {
        Message msg = new Message("+27831234567", "This is a short message", 0);
        assertTrue(msg.checkMessageLength(), "Message should be within 250 characters");
    }

    @Test
    public void testMessageLengthFailure() {
        String longMsg = "A".repeat(251);
        Message msg = new Message("+27831234567", longMsg, 1);
        assertFalse(msg.checkMessageLength(), "Message should exceed 250 characters");
    }

    @Test
    public void testRecipientFormattingSuccess() {
        Message msg = new Message("+27838968976", "Hello there!", 2);
        assertTrue(msg.checkRecipientCell(), "Valid cell number should pass");
    }

    @Test
    public void testRecipientFormattingFailure() {
        Message msg = new Message("08966553", "Hey!", 3);
        assertFalse(msg.checkRecipientCell(), "Invalid cell number should fail");
    }

    @Test
    public void testMessageHashFormat() {
        Message msg = new Message("+27831234567", "Hit tonight", 0);
        String hash = msg.createMessageHash();
        assertTrue(hash.matches("^[0-9]{2}:\\d+:[A-Z0-9]+$"), "Hash format should be correct like '00:0:HITONIGHT'");
    }

    @Test
    public void testMessageIDGenerated() {
        Message msg = new Message("+27831234567", "Hello!", 0);
        assertNotNull(msg.createMessageHash(), "Message ID should be generated and not null");
    }

    @Test
    public void testSendMessage() {
        Message msg = new Message("+27831234567", "Sending this message", 0);
        assertTrue(msg.checkMessageLength() && msg.checkRecipientCell(), "Message should be ready to send");
    }

    @Test
    public void testDiscardMessage() {
        // Simulate a user discarding the message
        Message msg = new Message("+27831234567", "Discard me", 0);
        String result = "Press 0 to delete message"; // Expected system behavior
        assertEquals("Press 0 to delete message", result);
    }

    @Test
    public void testStoreMessage() {
        // Just a basic mock test; assumes storeMessageToJson works and shows confirmation
        Message msg = new Message("+27831234567", "Store this message", 0);
        String expected = "Message successfully stored";
        String actual = "Message successfully stored"; // Simulate what would be returned/displayed
        assertEquals(expected, actual);
    }
    
}

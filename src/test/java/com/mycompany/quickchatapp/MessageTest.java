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
        String longMessage = "a".repeat(300); 
        Message msg = new Message("+27831234567", longMessage, 1);
        assertFalse(msg.checkMessageLength());
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
    public void testMessageLengthBoundary() {
        String exact250 = "a".repeat(250);
        Message msg = new Message("+27831234567", exact250, 1);
        assertTrue(msg.checkMessageLength());
    }

    @Test
    public void testRecipientFormattingSuccess() {
        Message msg = new Message("+27838968976", "Hello there!", 2);
        assertTrue(msg.checkRecipientCell());
    }

    @Test
    public void testRecipientFormattingFailure() {
        Message msg = new Message("08966553", "Hey!", 3);
        assertFalse(msg.checkRecipientCell());
    }

    @Test
    public void testMessageReadyToSend() {
        Message msg = new Message("+27831234567", "Sending this message", 0);
        assertTrue(msg.checkMessageLength() && msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashPresentInDetails() {
        Message msg = new Message("+27831234567", "Hash me up", 2);
        String details = msg.printMessageDetails();
        assertTrue(details.contains("Message Hash"));
    }
}

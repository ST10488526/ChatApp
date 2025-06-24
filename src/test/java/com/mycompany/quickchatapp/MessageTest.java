/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.quickchatapp;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MessageTest {

    // Checks that a valid phone number passes the validation
    @Test
    public void testValidRecipientNumber() {
        Message msg = new Message("+27831234567", "Hello!", 1);
        assertTrue(msg.checkRecipientCell(), "Expected valid phone number to pass");
    }

    // Checks that an invalid phone number is correctly rejected
    @Test
    public void testInvalidRecipientNumber() {
        Message msg = new Message("0831234567", "Hello!", 1);
        assertFalse(msg.checkRecipientCell(), "Expected number without '+' to fail");
    }

    // Checks that a short message is allowed
    @Test
    public void testValidMessageLength() {
        Message msg = new Message("+27831234567", "Short and sweet.", 2);
        assertTrue(msg.checkMessageLength(), "Expected message under 250 chars to pass");
    }

    // Makes sure a message longer than 250 characters fails validation
    @Test
    public void testTooLongMessage() {
        String longMsg = "a".repeat(300); // Generates a string with 300 'a's
        Message msg = new Message("+27831234567", longMsg, 3);
        assertFalse(msg.checkMessageLength(), "Expected long message to fail length check");
    }

    // Checks that the message details output includes key content
    @Test
    public void testMessageDetailsIncludeAllFields() {
        Message msg = new Message("+27831234567", "Testing details", 4);
        String details = msg.printMessageDetails();
        assertTrue(details.contains("Message ID"));
        assertTrue(details.contains("Message Hash"));
        assertTrue(details.contains("Recipient"));
        assertTrue(details.contains("Testing details"));
    }

    // Checks a message exactly 250 characters long still passes
    @Test
    public void testMessageAtLengthLimit() {
        String msg250 = "x".repeat(250);
        Message msg = new Message("+27831234567", msg250, 5);
        assertTrue(msg.checkMessageLength(), "Expected message of exactly 250 characters to pass");
    }

    // Additional formatting success test
    @Test
    public void testFormattedRecipientPasses() {
        Message msg = new Message("+27838889999", "Yo!", 6);
        assertTrue(msg.checkRecipientCell(), "Expected properly formatted recipient number to pass");
    }

    // Additional formatting failure test
    @Test
    public void testIncorrectRecipientFormatFails() {
        Message msg = new Message("0712345678", "Yo again", 7);
        assertFalse(msg.checkRecipientCell(), "Expected non-international format to fail");
    }

    // Make sure message passes both checks before sending
    @Test
    public void testMessageIsSendable() {
        Message msg = new Message("+27831234567", "Ready to go", 8);
        assertTrue(msg.checkRecipientCell() && msg.checkMessageLength(), "Message should be valid and sendable");
    }

    // Make sure the hash is included in the output
    @Test
    public void testHashAppearsInDetails() {
        Message msg = new Message("+27831234567", "This is hashed", 9);
        String output = msg.printMessageDetails();
        assertTrue(output.contains("Message Hash"), "Message hash should be shown in details");
    }

    // Extra: check that generated ID is always 10 digits
    @Test
    public void testGeneratedIdIsCorrectLength() {
        Message msg = new Message("+27831234567", "Some text", 10);
        String details = msg.printMessageDetails();
        String idLine = details.split("\n")[0]; // "Message ID: XXXXX"
        String id = idLine.split(": ")[1];
        assertEquals(10, id.length(), "Message ID should be 10 digits long");
    }
}
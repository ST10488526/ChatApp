/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.quickchatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuickChatAppTest {

    @Test
    public void testUsernameCorrectFormat() {
        assertTrue(QuickChatApp.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectFormat() {
        assertFalse(QuickChatApp.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsRequirements() {
        assertTrue(QuickChatApp.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordFailsRequirements() {
        assertFalse(QuickChatApp.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellNumberCorrectFormat() {
        assertTrue(QuickChatApp.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellNumberIncorrectFormat() {
        assertFalse(QuickChatApp.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testSuccessfulLogin() {
        QuickChatApp.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(QuickChatApp.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testFailedLogin() {
        QuickChatApp.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(QuickChatApp.loginUser("wrong_user", "wrong_pass"));
    }

    @Test
    public void testRegisterUsernameErrorMessage() {
        String result = QuickChatApp.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is incorrectly formatted, please ensure that your username contains an underscore and is no longer than five characters .", result);
    }

    @Test
    public void testRegisterPasswordErrorMessage() {
        String result = QuickChatApp.registerUser("kyl_1", "password", "+27838968976");
        assertEquals("Password is incorrectly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    @Test
    public void testRegisterCellErrorMessage() {
        String result = QuickChatApp.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals("Cellphone number incorrectly formatted or does not contain international code.", result);
    }

    @Test
    public void testSuccessfulRegistration() {
        String result = QuickChatApp.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("User registered successfully.", result);
    }

    @Test
    public void testReturnLoginStatusSuccess() {
        String message = QuickChatApp.returnLoginStatus("Kyle", "Smith", true);
        assertEquals("Welcome Kyle Smith, it is great to see you again.", message);
    }

    @Test
    public void testReturnLoginStatusFailure() {
        String message = QuickChatApp.returnLoginStatus("Kyle", "Smith", false);
        assertEquals("Username or password incorrect, please try again.", message);
    }
}

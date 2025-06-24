/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.quickchatapp;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class QuickChatAppTest {

    // Checks that a username with underscore and short length is valid
    @Test
    public void testUsernameValidFormat() {
        assertTrue(QuickChatApp.isValidUsername("kyl_1"), "Username with '_' and <= 5 chars should be valid");
    }

    // Checks that an incorrect username (too long or wrong format) is rejected
    @Test
    public void testUsernameInvalidFormat() {
        assertFalse(QuickChatApp.isValidUsername("kyle!!!!!!!"), "Username without underscore or too long should be invalid");
    }

    // Checks that a strong password is accepted
    @Test
    public void testStrongPasswordValid() {
        assertTrue(QuickChatApp.isStrongPassword("Ch&&sec@ke99!"), "Password meeting all criteria should be valid");
    }

    // Checks that a weak password is rejected
    @Test
    public void testWeakPasswordInvalid() {
        assertFalse(QuickChatApp.isStrongPassword("password"), "Simple password should fail complexity requirements");
    }

    // Checks a properly formatted international number is accepted
    @Test
    public void testValidCellNumberFormat() {
        assertTrue(QuickChatApp.isValidPhoneNumber("+27838968976"), "Valid international phone number should pass");
    }

    // Checks that a local number (missing '+') is rejected
    @Test
    public void testInvalidCellNumberFormat() {
        assertFalse(QuickChatApp.isValidPhoneNumber("0831234567"), "Missing '+' should make phone number invalid");
    }

    // Tests full registration flow with correct values
    @Test
    public void testSuccessfulUserRegistration() {
        String result = QuickChatApp.registerNewUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("User registered successfully.", result, "Registration should succeed with valid data");
    }

    // Tests registration fails with invalid username
    @Test
    public void testRegistrationFailsOnUsername() {
        String result = QuickChatApp.registerNewUser("kyle!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is incorrectly formatted, please ensure that your username contains an underscore and is no longer than five characters.", result);
    }

    // Tests registration fails with weak password
    @Test
    public void testRegistrationFailsOnPassword() {
        String result = QuickChatApp.registerNewUser("kyl_1", "password", "+27838968976");
        assertEquals("Password is incorrectly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    // Tests registration fails with badly formatted phone number
    @Test
    public void testRegistrationFailsOnPhoneNumber() {
        String result = QuickChatApp.registerNewUser("kyl_1", "Ch&&sec@ke99!", "0831234567");
        assertEquals("Cellphone number incorrectly formatted or does not contain international code.", result);
    }

    // Tests that login is successful with correct credentials
    @Test
    public void testLoginSuccessful() {
        QuickChatApp.registerNewUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(QuickChatApp.verifyLogin("kyl_1", "Ch&&sec@ke99!"), "Correct login credentials should pass");
    }

    // Tests that login fails if incorrect username/password
    @Test
    public void testLoginFails() {
        QuickChatApp.registerNewUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(QuickChatApp.verifyLogin("wrong_user", "wrong_pass"), "Wrong credentials should fail login");
    }

    // Tests that successful login greeting is generated properly
    @Test
    public void testLoginGreetingSuccess() {
        String greeting = QuickChatApp.getLoginGreeting("Kyle", "Smith", true);
        assertEquals("Welcome Kyle Smith, it is great to see you again.", greeting);
    }

    // Tests that failed login greeting is generated properly
    @Test
    public void testLoginGreetingFailure() {
        String greeting = QuickChatApp.getLoginGreeting("Kyle", "Smith", false);
        assertEquals("Username or password incorrect, please try again.", greeting);
    }
}
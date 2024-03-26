package com.example.sampleandroidapp.testing

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistrationTesting {

    @Test
    fun testUsernameValidation() {

        assertTrue(isValidUsername("User1Name"))
        assertTrue(isValidUsername("AnotherUser2"))


        assertFalse(isValidUsername("username")) // doesn't start with uppercase
        assertFalse(isValidUsername("User")) // less than 8 characters
        assertFalse(isValidUsername("User123")) // does not contain any uppercase letters
        assertFalse(isValidUsername("user1name")) // does not contain any uppercase letters
    }

    @Test
    fun testPasswordValidation() {

        assertTrue(isValidPassword("Password1@")) // includes uppercase, number, and special character
        assertTrue(isValidPassword("AnotherPass3*"))

        assertFalse(isValidPassword("password")) // less than 8 characters
        assertFalse(isValidPassword("Pass1234")) // does not include special character
        assertFalse(isValidPassword("password@")) // does not include uppercase letter and number
        assertFalse(isValidPassword("PASSWORD1")) // does not include lowercase letter and special character
    }

    companion object {
        fun isValidUsername(username: String): Boolean {
            val usernameRegex = """^(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,}$""".toRegex()
            return usernameRegex.matches(username)
        }

        fun isValidPassword(password: String): Boolean {
            val passwordRegex = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$""".toRegex()
            return passwordRegex.matches(password)
        }
    }
}

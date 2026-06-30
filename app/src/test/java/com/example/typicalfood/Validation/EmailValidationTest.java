package com.example.typicalfood.Validation;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Tests the email validation regex pattern used in RegistrarseActivity
 * and ResetPasswordActivity.
 */
public class EmailValidationTest {

    private Pattern emailPattern;

    @Before
    public void setUp() {
        // Same pattern used in RegistrarseActivity and ResetPasswordActivity
        emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        );
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    @Test
    public void testValidSimpleEmail() {
        assertTrue(isValidEmail("user@domain.com"));
    }

    @Test
    public void testValidEmailWithSubdomain() {
        assertTrue(isValidEmail("user@mail.domain.com"));
    }

    @Test
    public void testValidEmailWithPlus() {
        assertTrue(isValidEmail("user+tag@domain.com"));
    }

    @Test
    public void testValidEmailWithDots() {
        assertTrue(isValidEmail("first.last@domain.com"));
    }

    @Test
    public void testValidEmailWithNumbers() {
        assertTrue(isValidEmail("user123@domain456.com"));
    }

    @Test
    public void testValidEmailWithHyphen() {
        assertTrue(isValidEmail("user-name@domain.com"));
    }

    @Test
    public void testValidEmailWithUnderscore() {
        assertTrue(isValidEmail("_user@domain.com"));
    }

    @Test
    public void testInvalidEmailNoAtSymbol() {
        assertFalse(isValidEmail("userdomain.com"));
    }

    @Test
    public void testInvalidEmailNoDomain() {
        assertFalse(isValidEmail("user@"));
    }

    @Test
    public void testInvalidEmailNoUser() {
        assertFalse(isValidEmail("@domain.com"));
    }

    @Test
    public void testInvalidEmailDoubleAt() {
        assertFalse(isValidEmail("user@@domain.com"));
    }

    @Test
    public void testInvalidEmailSpaces() {
        assertFalse(isValidEmail("user @domain.com"));
    }

    @Test
    public void testInvalidEmailEmpty() {
        assertFalse(isValidEmail(""));
    }

    @Test
    public void testInvalidEmailNoTld() {
        assertFalse(isValidEmail("user@domain"));
    }

    @Test
    public void testInvalidEmailSingleCharTld() {
        assertFalse(isValidEmail("user@domain.c"));
    }

    @Test
    public void testValidEmailTwoCharTld() {
        assertTrue(isValidEmail("user@domain.es"));
    }

    @Test
    public void testValidEmailLongTld() {
        assertTrue(isValidEmail("user@domain.museum"));
    }

    @Test
    public void testInvalidEmailDoubleDotInDomain() {
        assertFalse(isValidEmail("user@domain..com"));
    }

    @Test
    public void testInvalidEmailStartsWithDot() {
        assertFalse(isValidEmail(".user@domain.com"));
    }

    @Test
    public void testValidEmailMultipleSubdomains() {
        assertTrue(isValidEmail("user@sub1.sub2.domain.com"));
    }

    @Test
    public void testValidEmailUpperCase() {
        assertTrue(isValidEmail("USER@DOMAIN.COM"));
    }

    @Test
    public void testValidEmailMixedCase() {
        assertTrue(isValidEmail("User@Domain.Com"));
    }
}

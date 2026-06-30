package com.example.typicalfood.Pojo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserPojoTest {

    private UserPojo user;

    @Before
    public void setUp() {
        user = new UserPojo();
    }

    @Test
    public void testDefaultConstructor() {
        UserPojo u = new UserPojo();
        assertNotNull(u);
        assertNull(u.getEmail());
        assertNull(u.getName());
        assertNull(u.getFavorites());
    }

    @Test
    public void testParameterizedConstructor() {
        List favorites = new ArrayList();
        UserPojo u = new UserPojo("test@email.com", "Juan", favorites);
        assertEquals("test@email.com", u.getEmail());
        assertEquals("Juan", u.getName());
        assertNotNull(u.getFavorites());
        assertEquals(0, u.getFavorites().size());
    }

    @Test
    public void testSetAndGetEmail() {
        user.setEmail("user@domain.com");
        assertEquals("user@domain.com", user.getEmail());
    }

    @Test
    public void testSetAndGetName() {
        user.setName("Carlos");
        assertEquals("Carlos", user.getName());
    }

    @Test
    public void testSetAndGetFavorites() {
        List favorites = new ArrayList();
        user.setFavorites(favorites);
        assertNotNull(user.getFavorites());
        assertEquals(0, user.getFavorites().size());
    }

    @Test
    public void testSetEmailNull() {
        user.setEmail(null);
        assertNull(user.getEmail());
    }

    @Test
    public void testSetNameNull() {
        user.setName(null);
        assertNull(user.getName());
    }

    @Test
    public void testSetFavoritesNull() {
        user.setFavorites(null);
        assertNull(user.getFavorites());
    }

    @Test
    public void testSetEmailEmpty() {
        user.setEmail("");
        assertEquals("", user.getEmail());
    }

    @Test
    public void testSetNameEmpty() {
        user.setName("");
        assertEquals("", user.getName());
    }

    @Test
    public void testSettersOverwriteValues() {
        user.setEmail("old@email.com");
        user.setName("OldName");
        user.setEmail("new@email.com");
        user.setName("NewName");
        assertEquals("new@email.com", user.getEmail());
        assertEquals("NewName", user.getName());
    }

    @Test
    public void testSpecialCharactersInEmail() {
        user.setEmail("user+tag@sub.domain.com");
        assertEquals("user+tag@sub.domain.com", user.getEmail());
    }

    @Test
    public void testSpecialCharactersInName() {
        user.setName("José María García-López");
        assertEquals("José María García-López", user.getName());
    }

    @Test
    public void testFieldsAreIndependent() {
        user.setEmail("email@test.com");
        user.setName("Name");
        List favorites = new ArrayList();
        user.setFavorites(favorites);

        user.setEmail("changed@test.com");
        assertEquals("changed@test.com", user.getEmail());
        assertEquals("Name", user.getName());
        assertNotNull(user.getFavorites());
    }
}

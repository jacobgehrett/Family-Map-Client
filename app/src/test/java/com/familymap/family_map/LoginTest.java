package com.familymap.family_map;

import com.familymap.family_map.model.DataCache;
import com.familymap.family_map.model.Person;
import com.familymap.family_map.net.ServerProxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import RequestResult.LoginRequest;
import RequestResult.RegisterRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginTest {

    private ServerProxy proxy;
    private LoginRequest r;
    private LoginRequest s;
    private RegisterRequest t;
    private Person p;

    @Before
    public void setUp() {
        proxy = new ServerProxy();
        r = new LoginRequest("sheila", "parker");
        s = new LoginRequest("Sheila", "parker");
        p = new Person("Sheila", "Parker", "Sheila Parker registered!",
                "f", "Sheila_Parker", "Betty_White", "Blaine_McGary",
                "Davis_Hyer");
        t  = new RegisterRequest("a", "b", "c", "d", "e", "f",
                "g");
    }

    @After
    public void tearDown() {
        DataCache.setUser(null);
        DataCache.clear();
    }

    @Test
    public void loginPass() {
        assertEquals(proxy.login(r).getPersonId(), p.getPersonId());
        assertTrue(DataCache.hasUser());
    }

    @Test
    public void loginFail() {
        assertNotEquals(proxy.login(s).getPersonId(), p.getPersonId());
        assertFalse(DataCache.hasUser());
    }

    @Test
    public void RegisterFail1() {
        assertEquals("Error, register failed!", proxy.register(t).getMessage());
        assertFalse(DataCache.hasUser());
    }

    @Test //Cannot easily call clear methods on backend database'
    public void RegisterFail2() {
        assertNull(proxy.register(t).getPersonId());
        assertFalse(DataCache.hasUser());
    }

    @Test //Cannot easily call clear methods on backend database'
    public void PeoplePass() {
        assertNotNull(proxy.login(r).getPersonId());
        assertTrue(DataCache.hasUser());
    }

    @Test
    public void PeopleFail() {
        assertNull(proxy.register(t).getFirstName());
        assertFalse(DataCache.hasUser());
    }

    @Test
    public void EventsPass() {
        proxy.login(r);
        assertNotNull(DataCache.getAllEvents());
    }

    @Test
    public void EventsFail(){
        DataCache.clear();
        proxy.register(t);
        assertNotNull(DataCache.getAllEvents());
        assertFalse(DataCache.hasUser());
    }
}
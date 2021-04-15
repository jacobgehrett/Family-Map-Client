package com.familymap.family_map;

import com.familymap.family_map.model.DataCache;
import com.familymap.family_map.model.Person;
import com.familymap.family_map.net.ServerProxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import RequestResult.LoginRequest;

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
public class ModelTests {
    private Person p;

    @Before
    public void setUp() {
        ServerProxy proxy = new ServerProxy();
        p = proxy.login(new LoginRequest("sheila", "parker"));
    }

    @After
    public void tearDown() {
        DataCache.setUser(null);
        DataCache.clear();
    }

    @Test
    public void getRelationsPass() {
        assertEquals("Davis_Hyer", DataCache.getPersonById(p.getPersonId()).getSpouse().getPersonId());
        assertEquals("Blaine_McGary", DataCache.getPersonById(p.getPersonId()).getFather().getPersonId());
        assertNotNull(DataCache.getPersonChildren(DataCache.getPersonById("Betty_White")));
    }

    @Test
    public void getRelationsFail() {
        assertTrue(DataCache.getAllPeople().contains(p.getSpouse().getFather()));
        assertNotEquals("Blane_McGary", DataCache.getPersonById(p.getPersonId()).getFather().getPersonId());
        assertNull(DataCache.getPersonById("Betsy_White"));
    }

    @Test
    public void getFilteredEventsPass() {
        DataCache.getSettings().setFemale(false);
        assertFalse(DataCache.getAllFilteredEvents().contains(DataCache.getEventById("Betty_Death")));
        assertTrue(DataCache.getAllFilteredEvents().contains(DataCache.getEventById("BYU_graduation")));
    }

    @Test
    public void getFilteredEventsFail() {
        DataCache.getSettings().setFemale(true);
        DataCache.getSettings().setMother(false);
        assertFalse(DataCache.getAllFilteredEvents().contains(DataCache.getEventById("Betty_Death")));
        assertTrue(DataCache.getAllFilteredEvents().contains(DataCache.getEventById("BYU_graduation")));
    }

    @Test
    public void sortPass() {

        assertNotNull(DataCache.getPersonEvents(p));
        DataCache.getSettings().setMother(true);
        assertNotNull(DataCache.getPersonEvents(p.getFather()));
    }

    @Test
    public void sortFail() {

        assertNotEquals(DataCache.getPersonEvents(p).size(), 1);
        DataCache.getSettings().setMother(true);
        assertFalse(DataCache.getPersonEvents(p.getFather()).isEmpty());
    }

    @Test
    public void searchPass() {
        assertTrue(DataCache.getAllEvents().contains(DataCache.getEventById("Mrs_Jones_Surf")));
        assertEquals(4, DataCache.getAllFilteredEvents().size());
    }

    @Test
    public void searchFail() {
        assertFalse(DataCache.getAllFilteredEvents().contains(DataCache.getEventById("Mrs_Jones_Surf")));
        assertFalse(DataCache.getAllFilteredEvents().contains(DataCache.getEventById("Jones_Marriage")));
    }
}
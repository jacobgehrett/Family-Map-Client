package com.familymap.family_map.model;

import java.util.*;

import RequestResult.EventIDResult;
import RequestResult.EventResult;
import RequestResult.PersonResult;
import RequestResult.PersonIDResult;
import RequestResult.Result;

public class DataCache {

    private static DataCache instance;

    static {
        initialize();
    }

    public static void initialize() { instance = new DataCache(); }

    public static void clear() { instance._clear(); }

    public static void endSync() { instance._endSync(); }

    public static void setUser(Person p) { instance._setUser(p); }

    public static Person getUser(String username) { return instance._getUser(username); }

    public static boolean hasUser() { return instance._hasUser(); }

    public static void addPerson(Person p) { instance._addPerson(p); }

    public static Collection<Person> getAllPeople() { return instance._getAllPeople(); }

    public static Person getPersonById(String id) { return instance._getPersonById(id); }

    public static void addEvent(Event e) { instance._addEvent(e); }

    public static Collection<Event> getAllEvents() { return instance._getAllEvents(); }

    public static Collection<Event> getAllFilteredEvents() {
        return instance._getAllFilteredEvents();
    }

    public static Event getEventById(String id) { return instance._getEventById(id); }

    public static List<String> getEventTypes() { return instance._getEventTypes(); }

    public static Map<String, MapColor> getEventTypeColors() {
        return instance._getEventTypeColors();
    }

    public static boolean isPaternalAncestor(Person p) {
        return instance._isPaternalAncestor(p);
    }

    public static boolean isMaternalAncestor(Person p) {
        return instance._isMaternalAncestor(p);
    }

    public static Settings getSettings() { return instance._getSettings(); }

    public static List<Event> getPersonEvents(Person p) { return instance._getPersonEvents(p); }

    public static List<Event> getPersonFilteredEvents(Person p) {
        return instance._getPersonFilteredEvents(p);
    }

    public static List<Person> getPersonChildren(Person p) {
        return instance._getPersonChildren(p);
    }

    public static void setPeople(PersonIDResult[] r) {
        instance._setPeople(r);
    }
    public static void setEvents(EventIDResult[] r) {
        instance._setEvents(r);
    }

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Settings settings;
    private List<String> eventTypes;
    private Map<String, MapColor> eventTypeColors;
    private Person user;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private Map<String, List<Person>> personChildren;

    private DataCache() {
        people = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        settings = new Settings();
        eventTypes = new ArrayList<>();
        eventTypeColors = new HashMap<>();
        user = null;
        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        personChildren = new HashMap<>();
    }

    private void _clear() {
        people.clear();
        events.clear();
        personEvents.clear();
        eventTypes.clear();
        eventTypeColors.clear();
        user = null;
        paternalAncestors.clear();
        maternalAncestors.clear();
        personChildren.clear();
    }

    private void _endSync() {
        _calcPersonEvents();
        _calcEventTypes();
        _calcEventTypeColors();
        //_calcUser();
        _calcPaternalAncestors();
        _calcMaternalAncestors();
        _calcPersonChildren();
    }

    private void _setUser(Person p) { this.user = p; }

    private Person _getUser(String username) { return user; }

    private boolean _hasUser() { return (user != null); }

    private void _addPerson(Person p) { people.put(p.getPersonId(), p); }

    private void _calcPersonEvents() {
        personEvents.clear();

        for (Event event : events.values()) {
            String personId = event.getPersonId();

            List<Event> eventList = null;
            if (personEvents.containsKey(personId)) {
                eventList = personEvents.get(personId);
            }
            else {
                eventList = new ArrayList<>();
                personEvents.put(personId, eventList);
            }

            eventList.add(event);
        }

        for (List<Event> eventList : personEvents.values()) {
            //Collections.sort(eventList);
        }
    }

    private void _calcEventTypes() {
        Set<String> allTypes = new TreeSet<>();
        for (Event event : _getAllEvents()) {
            allTypes.add(event.getType());
        }

        eventTypes.clear();
        eventTypes.addAll(allTypes);
    }

    private void _calcEventTypeColors() {
        MapColor[] colors = MapColor.values();
        int colorIndex = 0;

        eventTypeColors.clear();
        for (String eventType : eventTypes) {
            eventTypeColors.put(eventType.toLowerCase(), colors[colorIndex]);
            colorIndex = ((colorIndex + 1) % colors.length);
        }
    }

    /*private void _calcUser() {
        Set<String> ancestors = new HashSet<>();
        for (Person p : people.values()) {
            if (p.hasFatherId()) {
                ancestors.add(p.getFatherId());
            }
            if (p.hasMotherId()) {
                ancestors.add(p.getMotherId());
            }
        }

        Set<String> tmp = new HashSet<>(people.keySet());
        tmp.removeAll(ancestors);
        for (String personId : tmp) {
            Person p = _getPersonById(personID);
            if (p.hasFatherId() || p.hasMotherId()) {
                this.user = p;
                break;
            }
        }

        if (this.user == null) {
            throw new illegalStateException("User could not be determined");
        }
    }*/

    private void _calcPaternalAncestors() {
        paternalAncestors.clear();
        _calcPaternalAncestors(user.getFather());
    }

    private void _calcPaternalAncestors(Person p) {
        if (p != null) {
            paternalAncestors.add(p.getPersonId());
            _calcPaternalAncestors(p.getFather());
            _calcPaternalAncestors(p.getMother());
        }
    }

    private void _calcMaternalAncestors() {
        maternalAncestors.clear();
        _calcMaternalAncestors(user.getMother());
    }

    private void _calcMaternalAncestors(Person p) {
        if (p != null) {
            maternalAncestors.add(p.getPersonId());
            _calcMaternalAncestors(p.getFather());
            _calcMaternalAncestors(p.getMother());
        }
    }

    private Collection<Person> _getAllPeople() {
        return null;
    }

    private Person _getPersonById(String id) {
        return null;
    }

    private void _addEvent(Event e) {
    }

    private Collection<Event> _getAllEvents() {
        return new ArrayList<>(events.values());
    }

    private Collection<Event> _getAllFilteredEvents() {
        return null;
    }

    private Event _getEventById(String id) {
        return null;
    }

    private List<String> _getEventTypes() {
        return null;
    }

    private Map<String, MapColor> _getEventTypeColors() {
        return eventTypeColors;
    }

    private boolean _isPaternalAncestor(Person p) {
        return false;
    }

    private boolean _isMaternalAncestor(Person p) {
        return false;
    }

    private Settings _getSettings() {
        return null;
    }

    private List<Event> _getPersonEvents(Person p) {
        return null;
    }

    private List<Event> _getPersonFilteredEvents(Person p) {
        return null;
    }

    private List<Person> _getPersonChildren(Person p) {
        return null;
    }

    private void _calcPersonChildren() {
    }

    private void _setPeople(PersonIDResult[] r) {
        for (PersonIDResult personIDResult : r) {
            people.put(personIDResult.getPersonID(),
                    new Person(personIDResult.getFirstName(), personIDResult.getLastName(), null));
        }
    }

    private void _setEvents(EventIDResult[] r) {
        for (EventIDResult eventIDResult : r) {
            events.put(eventIDResult.getEventID(), new Event(eventIDResult.getEventType(),
                    eventIDResult.getLatitude(), eventIDResult.getLongitude()));
        }
    }
}

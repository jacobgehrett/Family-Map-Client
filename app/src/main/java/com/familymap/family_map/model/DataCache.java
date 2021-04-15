package com.familymap.family_map.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import RequestResult.EventIDResult;
import RequestResult.PersonIDResult;

public class DataCache {

    private static DataCache instance;

    static {
        initialize();
    }

    public static void initialize() { instance = new DataCache(); }

    public static void clear() { instance._clear(); }

    public static void endSync() { instance._endSync(); }

    public static void setUser(Person p) { instance._setUser(p); }

    public static boolean hasUser() { return instance._hasUser(); }

    public static Collection<Person> getAllPeople() { return instance._getAllPeople(); }

    public static Person getPersonById(String id) { return instance._getPersonById(id); }

    public static Collection<Event> getAllEvents() { return instance._getAllEvents(); }

    public static Collection<Event> getAllFilteredEvents() {
        return instance._getAllFilteredEvents();
    }

    public static Event getEventById(String id) { return instance._getEventById(id); }

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

    public static List<Person> getPersonChildren(Person p) {
        return instance._getPersonChildren(p);
    }

    public static void setPeople(PersonIDResult[] r) {
        instance._setPeople(r);
    }

    public static void setEvents(EventIDResult[] r) {
        instance._setEvents(r);
    }

    private final Map<String, Person> people;
    private final Map<String, Event> events;
    private final Map<String, List<Event>> personEvents;
    private final Settings settings;
    private final List<String> eventTypes;
    private final Map<String, MapColor> eventTypeColors;
    private Person user;
    private final Set<String> paternalAncestors;
    private final Set<String> maternalAncestors;
    private final Map<String, List<Person>> personChildren;

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
        _calcPaternalAncestors();
        _calcMaternalAncestors();
        _calcPersonChildren();
    }

    private void _setUser(Person p) { this.user = p; }

    private boolean _hasUser() { return (user != null); }

    private void _calcPersonEvents() {
        personEvents.clear();

        for (Event event : events.values()) {
            String personId = event.getPersonId();

            List<Event> eventList;
            if (personEvents.containsKey(personId)) {
                eventList = personEvents.get(personId);
            }
            else {
                eventList = new ArrayList<>();
                personEvents.put(personId, eventList);
            }

            eventList.add(event);
        }

        personEvents.values();
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
        return new ArrayList<>(people.values());
    }

    private Person _getPersonById(String id) {
        return people.get(id);
    }

    private Collection<Event> _getAllEvents() {
        return new ArrayList<>(events.values());
    }

    private Collection<Event> _getAllFilteredEvents() {
        ArrayList<Event> filtered = (ArrayList<Event>) getAllEvents();
        ArrayList<Event> toRemove = new ArrayList<>();
        if (!this.settings.isFather()) {
            _calcPaternalAncestors();
            for (Event e : filtered) {
                if (isPaternalAncestor(e.getPerson())) {
                    toRemove.add(e);
                }
            }
        }
        if (!this.settings.isMother()) {
            _calcMaternalAncestors();
            for (Event e : filtered) {
                if (isMaternalAncestor(e.getPerson())) {
                    toRemove.add(e);
                }
            }
        }
        if (!this.settings.isMale()) {
            for (Event e : filtered) {
                if (e.getGender().equals("m")) {
                    toRemove.add(e);
                }
            }
        }
        if (!this.settings.isFemale()) {
            for (Event e : filtered) {
                if (e.getGender().equals("f")) {
                    toRemove.add(e);
                }
            }
        }
        filtered.removeAll(toRemove);
        return filtered;
    }

    private Event _getEventById(String id) {
        return events.get(id);
    }

    private Map<String, MapColor> _getEventTypeColors() {
        return eventTypeColors;
    }

    private boolean _isPaternalAncestor(Person p) {
        return paternalAncestors.contains(p.getPersonId());
    }

    private boolean _isMaternalAncestor(Person p) {
        return maternalAncestors.contains(p.getPersonId());
    }

    private Settings _getSettings() {
        return this.settings;
    }

    private List<Event> _getPersonEvents(Person p) {
        List<Event> personEvents = new ArrayList<>();
        for (Event e : this._getAllFilteredEvents()) {
            if (e.getPerson().getPersonId().equals(p.getPersonId())) {
                if (personEvents.size() == 0) {
                    personEvents.add(e);
                }
                else {
                    for (int i = 0; i < personEvents.size(); ++i) {
                        if (personEvents.get(i).getDate() > e.getDate()) {
                            personEvents.add(i, e);
                            break;
                        }
                        else if (i == (personEvents.size() - 1)) {
                            personEvents.add(e);
                            break;
                        }
                    }
                }
            }
        }
        return personEvents;
    }

    private List<Person> _getPersonChildren(Person p) {
        ArrayList<Person> personChildren = new ArrayList<>();
        if (p.getGender().equals("m")) {
            for (Person person : this._getAllPeople()) {
                if (person.getFather() == p) {
                    person.setRelation("Child");
                    personChildren.add(person);
                }
            }
        }
        else {
            for (Person person : this._getAllPeople()) {
                if (person.getMother() == p) {
                    person.setRelation("Child");
                    personChildren.add(person);
                }
            }
        }
        return personChildren;
    }

    private void _calcPersonChildren() {
    }

    private void _setPeople(PersonIDResult[] r) {
        for (PersonIDResult personIDResult : r) {
            people.put(personIDResult.getPersonID(),
                    new Person(personIDResult.getFirstName(), personIDResult.getLastName(),
                            null, personIDResult.getGender(), personIDResult.getPersonID(),
                            personIDResult.getMotherID(), personIDResult.getFatherID(),
                            personIDResult.getSpouseID()));
        }
    }

    private void _setEvents(EventIDResult[] r) {
        for (EventIDResult eventIDResult : r) {
            events.put(eventIDResult.getEventID(), new Event(eventIDResult.getEventType(),
                    eventIDResult.getLatitude(), eventIDResult.getLongitude(),
                    eventIDResult.getYear(), eventIDResult.getCity(), eventIDResult.getCountry(),
                    eventIDResult.getEventID()));
            Person p = people.get(eventIDResult.getPersonID());
            Event e = events.get(eventIDResult.getEventID());
            e.setFirstName(p.getFirstName());
            e.setLastName(p.getLastName());
            e.setGender(p.getGender());
            e.setPerson(p);
        }
    }
}

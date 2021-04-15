package com.familymap.family_map.model;

public class Event {
    private final String type;
    private final double latitude;
    private final double longitude;
    private String gender;
    private String firstName;
    private String lastName;
    private final int date;
    private Person person;
    private final String city;
    private final String country;
    private final String eventID;

    public Event(String type, float latitude, float longitude, int date, String city,
                 String country, String eventID) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.city = city;
        this.country = country;
        this.eventID = eventID;
    }

    public String getPersonId() {
        return null;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Person getPerson() {
        return person;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getDate() {
        return date;
    }

    public void setPerson(Person p) {
        this.person = p;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEventID() {
        return eventID;
    }
}

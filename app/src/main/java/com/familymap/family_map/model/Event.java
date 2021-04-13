package com.familymap.family_map.model;

public class Event {
    private String type;
    private double latitude;
    private double longitude;

    public Event(String type, float latitude, float longitude) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
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
        return null;
    }
}

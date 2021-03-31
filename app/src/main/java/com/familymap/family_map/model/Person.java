package com.familymap.family_map.model;

public class Person {
    private final String firstName;
    private final String lastName;
    private final String message;

    public Person(String firstName, String lastName, String message) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonId() {
        return null;
    }

    public Person getFather() {
        return null;
    }

    public Person getMother() {
        return null;
    }

    public String getMessage() {
        return message;
    }
}

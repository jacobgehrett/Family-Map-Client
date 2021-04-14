package com.familymap.family_map.model;

public class Person {
    private final String firstName;
    private final String lastName;
    private final String message;
    private final String gender;
    private final String personID;
    private final String motherID;
    private final String fatherID;
    private final String spouseID;
    private String relation;

    public Person(String firstName, String lastName, String message, String gender, String personID, String motherID, String fatherID, String spouseID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
        this.gender = gender;
        this.personID = personID;
        this.motherID = motherID;
        this.fatherID = fatherID;
        this.spouseID = spouseID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonId() {
        return personID;
    }

    public Person getFather() {
        return DataCache.getPersonById(fatherID);
    }

    public Person getMother() {
        return DataCache.getPersonById(motherID);
    }

    public Person getSpouse() {
        return DataCache.getPersonById(spouseID);
    }

    public String getMessage() {
        return message;
    }

    public String getGender() {
        return gender;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }
}

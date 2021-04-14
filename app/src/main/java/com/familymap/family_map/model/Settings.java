package com.familymap.family_map.model;

public class Settings {
    private boolean lifeStory;
    private boolean familyTree;
    private boolean spouse;
    private boolean father;
    private boolean mother;
    private boolean male;
    private boolean female;

    public boolean isLifeStory() {
        return lifeStory;
    }

    public void setLifeStory(boolean lifeStory) {
        this.lifeStory = lifeStory;
    }

    public boolean isFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(boolean familyTree) {
        this.familyTree = familyTree;
    }

    public boolean isSpouse() {
        return spouse;
    }

    public void setSpouse(boolean spouse) {
        this.spouse = spouse;
    }

    public boolean isFather() {
        return father;
    }

    public void setFather(boolean father) {
        this.father = father;
    }

    public boolean isMother() {
        return mother;
    }

    public void setMother(boolean mother) {
        this.mother = mother;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }
}

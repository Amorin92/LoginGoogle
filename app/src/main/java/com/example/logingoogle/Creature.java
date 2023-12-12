package com.example.logingoogle;

import java.util.Objects;

public class Creature {

    private String name;
    private String race;
    private String imageUrl;

    private int errorCode;
    private String errorMessage;

    private boolean boosted;  // Novo campo para indicar se a criatura está boosted


    // Construtor vazio
    public Creature() {
    }

    // Construtor com parâmetros
    public Creature(String name, String race, String imageUrl) {
        this.name = name;
        this.race = race;
        this.imageUrl = imageUrl;
        this.boosted = boosted;

    }

    // Getters e Setters

    public boolean isBoosted() {
        return boosted;
    }

    public void setBoosted(boolean boosted) {
        this.boosted = boosted;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Métodos equals, hashCode, e toString para melhor usabilidade

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Creature creature = (Creature) o;
        return Objects.equals(name, creature.name) &&
                Objects.equals(race, creature.race) &&
                Objects.equals(imageUrl, creature.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, race, imageUrl);
    }

    @Override
    public String toString() {
        return "Creature{" +
                "name='" + name + '\'' +
                ", race='" + race + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

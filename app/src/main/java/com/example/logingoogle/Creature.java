package com.example.logingoogle;

public class Creature {

    private String name;
    private String race;
    private String imageUrl;

    // Construtor vazio
    public Creature() {
    }

    // Construtor com parâmetros
    public Creature(String name, String race, String imageUrl) {
        this.name = name;
        this.race = race;
        this.imageUrl = imageUrl;
    }

    // Getters e Setters
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
}

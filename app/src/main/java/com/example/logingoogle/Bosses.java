package com.example.logingoogle;

public class Bosses {

    private String name;
    private String imageUrl;
    private boolean boosted;

    // Construtor vazio
    public Bosses() {
    }

    // Construtor com parâmetros
    public Bosses(String name, String imageUrl, boolean boosted) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.boosted = boosted;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBoosted() {
        return boosted;
    }

    public void setBoosted(boolean boosted) {
        this.boosted = boosted;
    }
}
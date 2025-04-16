package com.example.pincat.model;

import java.util.List;

public class CatImage {
    private String url;
    private List<Breed> breeds;

    // Геттеры и сеттеры
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public List<Breed> getBreeds() { return breeds; }
    public void setBreeds(List<Breed> breeds) { this.breeds = breeds; }

    public static class Breed {
        private String id;
        private String name;

        public String getId() { return id; }
        public String getName() { return name; }
    }
}
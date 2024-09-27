package com.example.myapplication;

import java.util.ArrayList;

public class Recipe {
    private String id;
    private String userId;
    private String name; // Recipe name
    private ArrayList<String> ingredients; // List of ingredients
    private ArrayList<String> instructions; // Cooking instructions
    private String imageUrl; // URL for the recipe image

    // Default constructor for Firebase
    public Recipe() {
    }

    // Constructor for creating Recipe instances
    public Recipe(String id, String userId, String name, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    // Complete constructor including ingredients and instructions
    public Recipe(String id, String userId, String name, ArrayList<String> ingredients, ArrayList<String> instructions, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

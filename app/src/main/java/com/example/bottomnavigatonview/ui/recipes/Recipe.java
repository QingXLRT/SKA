package com.example.bottomnavigatonview.ui.recipes;

public class Recipe {
    private int id;
    private String title;
    private String shortdesc;
    private double preptime;
    private double cooktime;
    private String[] ingredients;
    private String[] amounts;
    private String instructions;
    private int image;

    public Recipe(int id, String title, String shortdesc, double preptime, double cooktime, String[] ingredients, String[] amounts, String instructions, int image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.preptime = preptime;
        this.cooktime = cooktime;
        this.ingredients = ingredients;
        this.amounts = amounts;
        this.instructions = instructions;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public double getPreptime() {
        return preptime;
    }

    public double getCooktime() { return cooktime; }

    public String[] getIngredients() { return ingredients; }

    public String[] getAmounts() { return amounts; }

    public String getInstructions() { return instructions; }

    public int getImage() {
        return image;
    }
}
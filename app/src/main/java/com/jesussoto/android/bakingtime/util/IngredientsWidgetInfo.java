package com.jesussoto.android.bakingtime.util;

import com.google.gson.annotations.SerializedName;

public class IngredientsWidgetInfo {

    @SerializedName("recipeId")
    private long recipeId;

    @SerializedName("recipeName")
    private String recipeName;

    public IngredientsWidgetInfo(long recipeId, String recipeName) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
}

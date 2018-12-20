package com.jesussoto.android.bakingtime.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(
    tableName = "ingredient",
    indices = {
        @Index(value = {"recipeId"})
    },
    foreignKeys = {
        @ForeignKey(
            entity = Recipe.class,
            parentColumns = {"id"},
            childColumns = {"recipeId"},
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    }
)
public class Ingredient implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @SerializedName("quantity")
    @ColumnInfo(name = "quantity")
    private float quantity;

    @NonNull
    @SerializedName("measure")
    @ColumnInfo(name = "measure")
    private String measure;

    @NonNull
    @SerializedName("ingredient")
    @ColumnInfo(name = "ingredient")
    private String ingredient;

    @ColumnInfo(name = "recipeId")
    private long recipeId;

    public Ingredient() {

    }

    protected Ingredient(Parcel in) {
        quantity = in.readFloat();
        measure = Objects.requireNonNull(in.readString());
        ingredient = Objects.requireNonNull(in.readString());
        recipeId = in.readLong();
    }

    public long getId() {
        return id;
    }

    public float getQuantity() {
        return quantity;
    }

    @NonNull
    public String getMeasure() {
        return measure;
    }

    @NonNull
    public String getIngredient() {
        return ingredient;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(@NonNull String measure) {
        this.measure = measure;
    }

    public void setIngredient(@NonNull String ingredient) {
        this.ingredient = ingredient;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
        dest.writeLong(recipeId);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}

package com.jesussoto.android.bakingtime.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "recipe")
public class Recipe implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("servings")
    private int servings;

    @Nullable
    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String image;

    @NonNull
    @Ignore
    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @NonNull
    @Ignore
    @SerializedName("steps")
    private List<BakingStep> steps;

    public Recipe() {

    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = Objects.requireNonNull(in.readString());
        servings = in.readInt();
        image = in.readString();
        ingredients = Objects.requireNonNull(in.createTypedArrayList(Ingredient.CREATOR));
        steps = Objects.requireNonNull(in.createTypedArrayList(BakingStep.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    @NonNull
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @NonNull
    public List<BakingStep> getSteps() {
        return steps;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }
}

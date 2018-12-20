package com.jesussoto.android.bakingtime.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static java.util.Objects.requireNonNull;

@Entity(
    tableName = "baking_step",
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
public class BakingStep implements Parcelable {

    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stepId")
    private long stepId;

    @SerializedName("id")
    @ColumnInfo(name = "stepNumber")
    private int stepNumber;

    @NonNull
    @SerializedName("shortDescription")
    @ColumnInfo(name = "shortDescription")
    private String shortDescription;

    @NonNull
    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;

    @Nullable
    @SerializedName("videoURL")
    @ColumnInfo(name = "videoURL")
    private String videoUrl;

    @Nullable
    @SerializedName("thumbnailURL")
    @ColumnInfo(name = "thumbnailURL")
    private String thumbnailUrl;

    @ColumnInfo(name = "recipeId")
    private long recipeId;

    public BakingStep() {

    }

    protected BakingStep(Parcel in) {
        stepId = in.readLong();
        stepNumber = in.readInt();
        shortDescription = requireNonNull(in.readString());
        description = requireNonNull(in.readString());
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
        recipeId = in.readLong();
    }

    public long getStepId() {
        return stepId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    @NonNull
    public String getShortDescription() {
        return shortDescription;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getVideoUrl() {
        return videoUrl;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setStepId(long id) {
        this.stepId = id;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public void setShortDescription(@NonNull String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setVideoUrl(@Nullable String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setThumbnailUrl(@Nullable String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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
        dest.writeLong(stepId);
        dest.writeInt(stepNumber);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
        dest.writeLong(recipeId);
    }

    public static final Creator<BakingStep> CREATOR = new Creator<BakingStep>() {
        @Override
        public BakingStep createFromParcel(Parcel in) {
            return new BakingStep(in);
        }

        @Override
        public BakingStep[] newArray(int size) {
            return new BakingStep[size];
        }
    };
}

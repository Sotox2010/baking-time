package com.jesussoto.android.bakingtime.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.jesussoto.android.bakingtime.db.entity.BakingStep;

import java.util.List;

@Dao
public interface BakingStepDao {

    @Query("SELECT * FROM baking_step WHERE stepId = :stepId")
    LiveData<BakingStep> getById(long stepId);

    @Query("SELECT * FROM baking_step WHERE recipeId = :recipeId ORDER BY stepNumber")
    LiveData<List<BakingStep>> getAllByRecipeId(long recipeId);

    @Insert
    long insert(BakingStep step);

    @Insert
    List<Long> insert(List<BakingStep> steps);

    @Delete
    void delete(BakingStep step);

    @Query("DELETE FROM baking_step")
    void clearTable();
}

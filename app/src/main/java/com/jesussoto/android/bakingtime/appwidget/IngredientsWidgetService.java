package com.jesussoto.android.bakingtime.appwidget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.AppDatabase;
import com.jesussoto.android.bakingtime.db.entity.Ingredient;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class IngredientsWidgetService extends RemoteViewsService {

    public static final String EXTRA_RECIPE_ID = "extra.RECIPE_ID";

    public static Intent getStartIntent(@NonNull Context context, long recipeId) {
        Intent startIntent = new Intent(context, IngredientsWidgetService.class);
        startIntent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return startIntent;
    }

    @Inject
    AppDatabase mDatabase;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetRemoteViewsFactory(
                getApplicationContext(), mDatabase, intent.getLongExtra(EXTRA_RECIPE_ID, -1L));
    }
}

class IngredientsWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    @NonNull
    private Context mContext;

    @NonNull
    private AppDatabase mDatabase;

    private long mRecipeId;

    @Nullable
    private List<Ingredient> mIngredients;

    public IngredientsWidgetRemoteViewsFactory(@NonNull Context context, @NonNull AppDatabase database, long recipeId) {
        mContext = context;
        mDatabase = database;
        mRecipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mIngredients = mDatabase.ingredientDao().blockingGetAllByRecipeId(mRecipeId);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients != null ? mIngredients.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null) {
            return null;
        }

        Ingredient ingredient = mIngredients.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient_appwidget);
        views.setTextViewText(R.id.ingredient_description, RecipeIngredientsWidgetProvider
                .buildIngredientText(mContext, ingredient));

        views.setOnClickFillInIntent(R.id.item_root, new Intent());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mIngredients != null ? mIngredients.get(position).getId() : 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

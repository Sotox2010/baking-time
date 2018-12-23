package com.jesussoto.android.bakingtime.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.Ingredient;
import com.jesussoto.android.bakingtime.ui.recipedetail.RecipeDetailActivity;
import com.jesussoto.android.bakingtime.util.IngredientsWidgetInfo;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeIngredientsWidgetConfigureActivity RecipeIngredientsWidgetConfigureActivity}
 */
public class RecipeIngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        IngredientsWidgetInfo info = RecipeIngredientsWidgetConfigureActivity
                .loadWidgetInfo(context, appWidgetId);

        if (info == null) {
            return;
        }

        // Sets up the intent that points to the StackViewService that will
        // provide the views for this collection.
        Intent intent = IngredientsWidgetService.getStartIntent(context, info.getRecipeId());
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredients_widget_provider);

        views.setTextViewText(R.id.appwidget_recipe_name, info.getRecipeName());
        views.setRemoteAdapter(R.id.ingredients_list_view, intent);

        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        views.setEmptyView(R.id.ingredients_list_view, R.id.empty_view);

        Intent activityIntent = RecipeDetailActivity.getStartIntent(context, info.getRecipeId());
        PendingIntent operation = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(activityIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.ingredients_list_view, operation);
        views.setOnClickPendingIntent(R.id.root, operation);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static String buildIngredientText(@NonNull Context context, @NonNull Ingredient ingredient) {
        int whole = (int) ingredient.getQuantity();
        float fraction = ingredient.getQuantity() - whole;
        StringBuilder sb = new StringBuilder();

        if (whole > 0) {
            sb.append(whole);
            sb.append(' ');
        }

        if (fraction > 0) {
            sb.append(getMeasurementByFraction(context, fraction));
            sb.append(' ');
        }

        sb.append(ingredient.getMeasure());
        sb.append(context.getString(R.string.ingredient_of));
        sb.append(ingredient.getIngredient());

        return sb.toString();
    }

    private static String getMeasurementByFraction(@NonNull Context context, float fraction) {
        if (fraction == 0.25) {
            return context.getString(R.string.one_quarter);
        } else if (fraction == 0.50) {
            return context.getString(R.string.one_half);
        } else if (fraction == 0.75) {
            return context.getString(R.string.three_quarters);
        } else {
            return "";
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeIngredientsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


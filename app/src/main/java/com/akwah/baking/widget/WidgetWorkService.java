package com.akwah.baking.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;
import com.akwah.baking.details.RecipeActivity;

import java.util.ArrayList;

public class WidgetWorkService extends IntentService {

    public static final String SHOW_INGREDIENTS_ACTION = "show_ingredients";
    public static final String BACK_TO_RECIPES_ACTION = "back_to_recipes";
    public static final String SHOW_RECIPE_ACTION = "show_recipe";
    public static final String APP_WIDGET_ID = "APP_WIDGET_ID";
    private static int randomNumber;

    public WidgetWorkService() {
        super("WidgetWorkService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null)
            return;
        final String action = intent.getAction();
        if (action == null)
            return;
        switch (action) {
            case SHOW_INGREDIENTS_ACTION:
                Recipe recipe = (Recipe) intent.getParcelableExtra(RecipeActivity.RECIPE_KEY);
                int appWidgetId = intent.getIntExtra(APP_WIDGET_ID, -1);
                handleActionShowIngredients(recipe, appWidgetId);
                break;
            case BACK_TO_RECIPES_ACTION:
                handleActionBackToRecipes(intent.getIntExtra(APP_WIDGET_ID, -1));
                break;
            case SHOW_RECIPE_ACTION:
                recipe = (Recipe) intent.getParcelableExtra(RecipeActivity.RECIPE_KEY);
                handleActionShowRecipe(recipe);
                break;
        }
    }

    private void handleActionShowIngredients(Recipe recipe, int appWidgetId) {
        Context context = getApplicationContext();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
        Intent intent = new Intent(context, WidgetIngredientsService.class);
        intent.putExtra(WidgetService.ID_KEY, appWidgetId);
        // Android is stupid! When you put a parcelable object in intent directly, it somehow disappears (in onGetViewFactory)
        // but when you put the parcelable object in a bundle it remains!
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WidgetIngredientsService.INGREDIENTS_KEY, recipe.getIngredients());
        intent.putExtra(WidgetIngredientsService.INGREDIENTS_KEY, bundle);
        // Android is stupid! It doesn't update the intent until you do this!
        intent.putExtra("random", randomNumber);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        randomNumber++;
        // Finally, ListView adapter worked
        views.setRemoteAdapter(R.id.widget_ingredients_listview, intent);
        // Back Button
        Intent backIntent = new Intent(context, WidgetWorkService.class);
        backIntent.setAction(BACK_TO_RECIPES_ACTION);
        backIntent.putExtra(APP_WIDGET_ID, appWidgetId);
        PendingIntent backPendingIntent = PendingIntent.getService(context, 0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.back_to_recipes_button, backPendingIntent);
        // Show Button
        Intent showIntent = new Intent(context, WidgetWorkService.class);
        showIntent.setAction(SHOW_RECIPE_ACTION);
        showIntent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
        PendingIntent showPendingIntent = PendingIntent.getService(context, 0, showIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.show_recipe_button, showPendingIntent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void handleActionBackToRecipes(int appWidgetId) {
        Context context = getApplicationContext();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(WidgetService.ID_KEY, appWidgetId);
        views.setRemoteAdapter(R.id.widget_list, intent);
        views.setEmptyView(R.id.widget_list, R.id.empty_view);
        intent = new Intent(context, WidgetWorkService.class);
        intent.setAction(SHOW_INGREDIENTS_ACTION);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void handleActionShowRecipe(Recipe recipe) {
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
        startActivity(intent);
    }

    public static void startActionBackToRecipes(Context context, int appWidgetId) {
        Intent intent = new Intent(context, WidgetWorkService.class);
        intent.setAction(BACK_TO_RECIPES_ACTION);
        intent.putExtra(APP_WIDGET_ID, appWidgetId);
        context.startService(intent);
    }
}

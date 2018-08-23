package com.akwah.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;
import com.akwah.baking.Utils;
import com.akwah.baking.details.RecipeActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {

    public static final String ID_KEY = "id_key";


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("AYMAN", "onGetViewFactory");
        return new WidgetFactory(getApplicationContext(), intent.getIntExtra(ID_KEY, -1));
    }
}

class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private final int mAppWidgetId;
    private Context mContext;
    private ArrayList<Recipe> mRecipes;

    WidgetFactory(Context context, int appWidgetId) {
        mContext = context;
        mAppWidgetId = appWidgetId;
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        Utils.requestData(
                mContext,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mRecipes = Utils.parseJSON(response);
                        int last = Math.min(8, mRecipes.size());
                        mRecipes = new ArrayList<>(mRecipes.subList(0, last));
                        appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.widget_list);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mRecipes = Utils.parseJSON(Utils.readLocalJsonFile(mContext));
                        int last = Math.min(8, mRecipes.size());
                        mRecipes = new ArrayList<>(mRecipes.subList(0, last));
                        appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.widget_list);
                    }
                });
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Recipe recipe = mRecipes.get(i);
        String imageUrl = recipe.getImage();
        boolean imageSet = false;
        if (imageUrl != null && !imageUrl.equals("")) {
            Bitmap bitmap = Utils.getBitmap(imageUrl);
            if (bitmap != null) {
                views.setImageViewBitmap(R.id.widget_item_image, bitmap);
                imageSet = true;
            }
        }
        if (!imageSet) {
            views.setImageViewResource(R.id.widget_item_image, Utils.getRecipeImage(recipe.getName()));
        }
        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
        Intent intent = new Intent();
        intent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
        intent.putExtra(WidgetWorkService.APP_WIDGET_ID, mAppWidgetId);
        views.setOnClickFillInIntent(R.id.widget_item, intent);
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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}


package com.akwah.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;

import java.util.ArrayList;

public class WidgetIngredientsService extends RemoteViewsService {
    public static final String ID_KEY = "id_key";
    public static final String INGREDIENTS_KEY = "ingredients_key";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("AYMAN", "INGREDIENTS_WIDGET_FACTORY");
        int appWidgetId = intent.getIntExtra(ID_KEY, -1);
        ArrayList<Recipe.Ingredient> ingredients = null;
        if (intent.hasExtra(INGREDIENTS_KEY)) {
            Log.d("AYMAN", "Found");
            ingredients = intent.getBundleExtra(INGREDIENTS_KEY).getParcelableArrayList(INGREDIENTS_KEY);
        } else {
            Log.d("AYMAN", INGREDIENTS_KEY + " Not found");
        }
        return new WidgetIngredientsFactory(getApplicationContext(), appWidgetId, ingredients);
    }
}

class WidgetIngredientsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;
    private ArrayList<Recipe.Ingredient> mIngredients;

    WidgetIngredientsFactory(Context context, int appWidgetId, ArrayList<Recipe.Ingredient> ingredients) {
        mContext = context;
        mAppWidgetId = appWidgetId;
        mIngredients = ingredients;
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
        if (mIngredients == null)
            return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
        views.setTextViewText(R.id.ingredient_item_number, String.valueOf(i + 1));
        views.setTextViewText(R.id.ingredient_content, mIngredients.get(i).toString());
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


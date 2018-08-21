package com.akwah.baking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static void requestData(Context context, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL, responseListener, errorListener);
        queue.add(stringRequest);
    }

    public static ArrayList<Recipe> parseJSON(String response) {
        Type type = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        return new Gson().fromJson(response, type);
    }

    public static Recipe parseRecipe(JSONObject object) throws JSONException {
        int id = object.optInt("id", -1);
        String name = object.optString("name");
        int servings = object.optInt("servings");
        String image = object.optString("image");
        return new Recipe(id, name, parseIngredients(object.getJSONArray("ingredients")), parseSteps(object.getJSONArray("steps")), servings, image);
    }

    private static ArrayList<Recipe.Step> parseSteps(JSONArray array) {
        return null;
    }

    private static ArrayList<Recipe.Ingredient> parseIngredients(JSONArray array) throws JSONException {
        ArrayList<Recipe.Ingredient> ingredients = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            ingredients.add(parseIngredient(array.getJSONObject(i)));
        }
        return ingredients;
    }

    private static Recipe.Ingredient parseIngredient(JSONObject jsonObject) {
        float quantity = (float) jsonObject.optDouble("quantity");
        String measure = jsonObject.optString("measure");
        String ingredient = jsonObject.optString("ingredient");
        return new Recipe.Ingredient(quantity, measure, ingredient);
    }

    public static String readLocalJsonFile(Context context) {
        BufferedReader reader = null;
        StringBuilder text = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("baking.json")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
            }
            return text.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static int getRecipeImage(String name) {
        switch (name) {
            case "Nutella Pie":
                return R.drawable.nutella_pie;
            case "Brownies":
                return R.drawable.brownies;
            case "Yellow Cake":
                return R.drawable.yellow_cake;
            case "Cheesecake":
                return R.drawable.cheese_cake;
            default:
                return R.drawable.default_food_image;
        }
    }

    public static Bitmap getBitmap(String imageURL) {
        try {
            return Picasso.get().load(imageURL).get();
        } catch (IOException e) {
            return null;
        }
    }
}

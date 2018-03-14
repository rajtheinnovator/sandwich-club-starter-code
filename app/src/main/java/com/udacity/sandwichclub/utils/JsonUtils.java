package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static String sandwichMainName;
    private static List<String> alsoKnownAs;
    private static String placeOfOrigin;
    private static String descriptionOfSandwich;
    private static String imageUrl;
    private static List<String> ingredients;

    public static Sandwich parseSandwichJson(String json) {

        try {
            JSONObject sandwichJSONObject = new JSONObject(json);
            alsoKnownAs = new ArrayList<>();
            ingredients = new ArrayList<>();
            if (sandwichJSONObject.has("name")) {
                JSONObject nameObject = sandwichJSONObject.getJSONObject("name");
                sandwichMainName = nameObject.getString("mainName");
                JSONArray alsoKnownArray = nameObject.getJSONArray("alsoKnownAs");
                if (alsoKnownArray.length() > 0) {
                    for (int i = 0; i < alsoKnownArray.length(); i++) {
                        alsoKnownAs.add(alsoKnownArray.getString(i));
                    }
                }

            }
            if (sandwichJSONObject.has("placeOfOrigin")) {
                placeOfOrigin = sandwichJSONObject.getString("placeOfOrigin");
            }
            if (sandwichJSONObject.has("description")) {
                descriptionOfSandwich = sandwichJSONObject.getString("description");
            }
            if (sandwichJSONObject.has("image")) {
                imageUrl = sandwichJSONObject.getString("image");
            }
            if (sandwichJSONObject.has("ingredients")) {
                JSONArray ingredientsJSONArray = sandwichJSONObject.getJSONArray("ingredients");

                if (ingredientsJSONArray.length() > 0) {
                    for (int i = 0; i < ingredientsJSONArray.length(); i++) {
                        ingredients.add(ingredientsJSONArray.getString(i));
                    }
                }
            }
            return new Sandwich(sandwichMainName, alsoKnownAs, placeOfOrigin, descriptionOfSandwich, imageUrl, ingredients);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

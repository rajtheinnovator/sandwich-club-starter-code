package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    Bitmap myBitmap = null;
    /*
    code below referenced from: https://www.bignerdranch.com/blog/extracting-colors-to-a-palette-with-android-lollipop/
    and https://medium.com/david-developer/extracting-colors-from-images-integrating-picasso-and-palette-b9ba45c9c418
    */
    Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
        public void onGenerated(Palette palette) {
            // access palette colors here
            Palette.Swatch palettes = palette.getVibrantSwatch();

            LinearLayout containerLiearLayout = findViewById(R.id.container_linear_layout);
            if (palettes != null) {
                containerLiearLayout.setBackgroundColor(palettes.getRgb());
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palettes.getRgb()));
                //code below referenced from:https://stackoverflow.com/a/26749343/5770629
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(palette.getDarkMutedColor(0x000000));
                }
            }
        }
    };
    // code below referenced from: https://stackoverflow.com/a/34354916/5770629
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //get bitmap
            myBitmap = bitmap;
            Palette.from(myBitmap).generate(paletteListener);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();

        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        changeActionBarColor(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        TextView placeOfOrigin = findViewById(R.id.origin_tv);
        TextView alsoKnownAs = findViewById(R.id.also_known_tv);
        TextView description = findViewById(R.id.description_tv);
        TextView ingredients = findViewById(R.id.ingredients_tv);
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            placeOfOrigin.setText("N/A");
        } else {
            placeOfOrigin.setText(sandwich.getPlaceOfOrigin());
        }
        String alsoKnownAsString = "";
        if (sandwich.getAlsoKnownAs().size() > 0) {
            for (int i = 0; i < sandwich.getAlsoKnownAs().size(); i++) {
                alsoKnownAsString += sandwich.getAlsoKnownAs().get(i) + "\n";
            }
        } else {
            alsoKnownAsString = "N/A";
        }
        alsoKnownAs.setText(alsoKnownAsString);


        String ingredientsString = "";
        if (sandwich.getIngredients().size() > 0) {
            for (int i = 0; i < sandwich.getIngredients().size(); i++) {
                ingredientsString += sandwich.getIngredients().get(i) + "\n";
            }
        } else {
            ingredientsString = "N/A";
        }
        ingredients.setText(ingredientsString);
        if (sandwich.getDescription().isEmpty()) {
            description.setText("N/A");
        } else {
            description.setText(sandwich.getDescription());
        }
    }

    public void changeActionBarColor(Sandwich sandwich) {
        Picasso.with(this).load(sandwich.getImage()).into(target);
    }

    @Override
    public void onDestroy() {  // could be in onPause or onStop
        Picasso.with(this).cancelRequest(target);
        super.onDestroy();
    }
}

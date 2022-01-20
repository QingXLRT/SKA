package com.example.bottomnavigatonview;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.bottomnavigatonview.ui.recipes.RecipesFragment;

public class ShowRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        Intent intent = getIntent();
        String message = intent.getStringExtra(RecipesFragment.EXTRA_MESSAGE);
        String shortdesc = intent.getStringExtra("com.example.bottomnavigatonview.ui.recipes.SHORTDESC");
        double preptime = intent.getDoubleExtra("com.example.bottomnavigatonview.ui.recipes.PREPTIME",0);
        double cooktime = intent.getDoubleExtra("com.example.bottomnavigatonview.ui.recipes.COOKTIME",0);
        String[] ingredients = intent.getStringArrayExtra("com.example.bottomnavigatonview.ui.recipes.INGREDIENTS");
        String[] amounts = intent.getStringArrayExtra("com.example.bottomnavigatonview.ui.recipes.AMOUNTS");
        String instructions = intent.getStringExtra("com.example.bottomnavigatonview.ui.recipes.INSTRUCTIONS");
        int image = intent.getIntExtra("com.example.bottomnavigatonview.ui.recipes.IMAGE",0);

        TextView textView = (TextView) findViewById(R.id.recipe_name);
        textView.setText(message);

        TextView prepView = (TextView) findViewById(R.id.prep2);
        prepView.setText(String.valueOf(preptime));

        TextView cookView = (TextView) findViewById(R.id.cook2);
        cookView.setText(String.valueOf(cooktime));

        ImageView imageView = (ImageView) findViewById(R.id.recipe_image);
        imageView.setImageResource(image);

        TextView ingredientsView = (TextView) findViewById(R.id.ingredients);
        String ingredientString = "Ingredients";
        for(int i = 0; i < ingredients.length; i++){
            ingredientString += "\n - ";
            ingredientString += amounts[i];
            ingredientString += ingredients[i];
        }
        ingredientsView.setText(ingredientString);

        TextView instructionsView = (TextView) findViewById(R.id.instructions);
        instructionsView.setText(instructions);
    }
}

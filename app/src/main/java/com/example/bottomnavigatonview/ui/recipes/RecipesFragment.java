package com.example.bottomnavigatonview.ui.recipes;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ListView;
import android.widget.SearchView;

import android.content.Intent;

import com.example.bottomnavigatonview.R;
import com.example.bottomnavigatonview.ShowRecipeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment implements OnItemClickListener {

    private RecipesViewModel recipesViewModel;

    //a list to store all the products
    List<Recipe> favoritesList;
    List<Recipe> favoritesListSave;

    //the recyclerview
    RecyclerView recyclerView;

    String[] tags = {};
    TextView applied_tags;

//    ListView suggestionList;
//    ListViewAdapter adapter;
//    SearchView search_bar;
    List<String> recipeTitles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recipesViewModel =
                new ViewModelProvider(this).get(RecipesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        final TextView textView = root.findViewById(R.id.text_recipes);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) root.findViewById(R.id.recipe_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initializing the productlist
        favoritesList = new ArrayList<>();

        recipeTitles = new ArrayList<>();

        applied_tags = (TextView) root.findViewById(R.id.applied_tags);


        //adding some items to our list
        favoritesList.add(
                new Recipe(
                        1,
                        "Spinach Rice",
                        "Spinach, Rice, Onions...",
                        10,
                        30,
                        new String[]{"spinach", "rice", "onions"},
                        new String[]{"1/2 lb ", "2 cups uncooked ", "1 chopped "},
                        "Chop and saute onions. Chop spinach and add to pot, cooking until wilted. Add rice and simmer for 20 minutes.",
                        R.drawable.spinachrice));

        favoritesList.add(
                new Recipe(
                        1,
                        "Croissant Tacos",
                        "Crescent rolls, ground beef...",
                        15,
                        25,
                        new String[]{"crescent rolls", "ground beef", "green pepper", "salsa", "taco packet","onions"},
                        new String[]{"2 pkg ", "1 lb ", "1 chopped ", "12 oz ", "1 ", "1 chopped "},
                        "Chop green pepper. Brown grown beef and add taco packet. Mix together with salsa. Layer crescent rolls in a star shape, " +
                                "then add filling around the ring and fold triangle tips back toward the center. Bake for 25 minutes.",
                        R.drawable.croissanttacos));

        favoritesList.add(
                new Recipe(
                        1,
                        "Zucchini Boats",
                        "Zucchini probably",
                        20,
                        20,
                        new String[]{"zucchini", "celery", "other stuff"},
                        new String[]{"2 halved ", "1/2 cup ", "some "},
                        "Chop other stuff and put it in a hollowed out zucchini? Idk this is my roommate's recipe which I have not actually tried yet.",
                        R.drawable.zucchiniboats));

        favoritesList.add(
                new Recipe(
                        1,
                        "Chicken Pot Pie",
                        "Carrots, peas, chicken...",
                        20,
                        20,
                        new String[]{"carrots", "peas", "chicken", "flour", "pie crusts"},
                        new String[]{"2 large ", "1/2 cup ", "1 lb ", "2 Tbsp ", "2 "},
                        "Chop vegetables. Cook chicken. Put in pie crusts and bake. Ta da.",
                        R.drawable.chickenpotpie));

        favoritesList.add(
                new Recipe(
                        1,
                        "Enchiladas",
                        "Ground beef, cheese, tortillas...",
                        20,
                        20,
                        new String[]{"ground beef", "cheese", "tortillas", "enchilada sauce", "black beans"},
                        new String[]{"1 lb ", "1/2 cup ", "10 flour ", "2 cups ", "1 can "},
                        "Brown green beef and add enchilada sauce. Add black beans and cheese. Wrap tortillas and bake.",
                        R.drawable.enchiladas));

        favoritesList.add(
                new Recipe(
                        1,
                        "Chicken and Wild Rice Soup",
                        "Carrots, chicken, celery...",
                        20,
                        20,
                        new String[]{"carrots", "celery", "chicken", "rice"},
                        new String[]{"2 large ", "1/2 cup ", "1 lb ", "2 cups "},
                        "Chop vegetables. Cook chicken and shred it. Put it everything in a pot for a while until it tastes good. Ta da.",
                        R.drawable.chickenwildrice));


        for (int i = 0; i < favoritesList.size(); i++){
            recipeTitles.add(favoritesList.get(i).getTitle());
        }
        //creating recyclerview adapter
        RecipeAdapter adapter = new RecipeAdapter(getActivity(), favoritesList, this);
        favoritesListSave = new ArrayList<>(favoritesList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        TextInputLayout tag_bar = (TextInputLayout) root.findViewById(R.id.outlinedTextField);

        tag_bar.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText edit = (TextInputEditText) root.findViewById(R.id.tag_bar);
                String newtag = String.valueOf(edit.getText());

                TextView view = (TextView) root.findViewById(R.id.applied_tags);
                addTag(newtag, view);
                edit.setText("");
            }
        });

        TextInputEditText editText = (TextInputEditText) root.findViewById(R.id.tag_bar);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    TextInputEditText edit = (TextInputEditText) root.findViewById(R.id.tag_bar);
                    String newtag = String.valueOf(edit.getText());

                    TextView view = (TextView) root.findViewById(R.id.applied_tags);
                    addTag(newtag, view);
                    edit.setText("");
                    handled = true;
                }
                return handled;
            }
        });

        Button clear_tags = (Button) root.findViewById(R.id.clear_tags);
        clear_tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags = new String[]{};
                String tagString = "";
                applied_tags.setText(tagString);
                recyclerView.setAdapter(adapter);
                favoritesList = favoritesListSave;
            }
        });

        SearchView search_bar = (SearchView) root.findViewById(R.id.search_bar);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                find_title(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Change suggestion text
                return false;
            }

        });
        search_bar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                find_title("");
                return false;
            }
        });

//        recipesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }

    public static final String EXTRA_MESSAGE = "com.example.bottomnavigatonview.ui.recipes.MESSAGE";

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ShowRecipeActivity.class);

        intent.putExtra(EXTRA_MESSAGE, favoritesList.get(position).getTitle());
        intent.putExtra("com.example.bottomnavigatonview.ui.recipes.SHORTDESC", favoritesList.get(position).getShortdesc());
        intent.putExtra("com.example.bottomnavigatonview.ui.recipes.PREPTIME", favoritesList.get(position).getPreptime());
        intent.putExtra("com.example.bottomnavigatonview.ui.recipes.COOKTIME", favoritesList.get(position).getCooktime());
        intent.putExtra("com.example.bottomnavigatonview.ui.recipes.INGREDIENTS", favoritesList.get(position).getIngredients());
        intent.putExtra("com.example.bottomnavigatonview.ui.recipes.AMOUNTS", favoritesList.get(position).getAmounts());
        intent.putExtra("com.example.bottomnavigatonview.ui.recipes.INSTRUCTIONS", favoritesList.get(position).getInstructions());
        intent.putExtra("com.example.bottomnavigatonview.ui.recipes.IMAGE", favoritesList.get(position).getImage());


        startActivity(intent);
    }

    public void addTag(String newtag, TextView view){
        String[] new_array = new String[tags.length+1];
        for(int i = 0; i < tags.length; i++){
            new_array[i] = tags[i];
        }
        new_array[tags.length] = newtag;
        tags = new_array;

        String tagString = "";
        for(int i = 0; i < tags.length; i++){
            if(i==0){
                tagString = tags[0];
            }
            else {
                tagString += ", ";
                tagString += tags[i];
            }
        }
        view.setText(tagString);

        updateRecipes();
    }

    public void updateRecipes(){
        List<Recipe> updatedList = new ArrayList<>();

        for(int i = 0; i < favoritesList.size(); i++){
            String[] ingredients = favoritesList.get(i).getIngredients();
            boolean missing_any_tag = false;

            for(int j = 0; j < tags.length; j++){
                String curr_tag = tags[j];
                boolean missing_curr_tag = true;

                for(int k = 0; k < ingredients.length; k++){
                    if (ingredients[k].toLowerCase().equals(curr_tag.toLowerCase())){
                        missing_curr_tag = false;
                        break;
                    }
                }

                if (missing_curr_tag){
                    missing_any_tag = true;
                }

            }
            if (!missing_any_tag){
                updatedList.add(favoritesList.get(i));
            }
        }

        RecipeAdapter adapter = new RecipeAdapter(getActivity(), updatedList, this);
        recyclerView.setAdapter(adapter);

        favoritesList = updatedList;
    }

    public void find_title(String query){
        query = query.trim();
        query = query.toLowerCase();
        List<Recipe> updatedList = new ArrayList<>();
        favoritesList = new ArrayList<>(favoritesListSave);

        if(query.equals("")){
            updatedList = new ArrayList<>(favoritesListSave);
        }

        else {
            for (int i = 0; i < recipeTitles.size(); i++) {
                if (recipeTitles.get(i).trim().toLowerCase().equals(query)) {
                    updatedList.add(favoritesList.get(i));
                }
            }
        }

        RecipeAdapter adapter = new RecipeAdapter(getActivity(), updatedList, this);
        recyclerView.setAdapter(adapter);

        favoritesList = updatedList;
    }
}

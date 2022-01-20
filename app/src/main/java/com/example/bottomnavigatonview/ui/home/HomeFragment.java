package com.example.bottomnavigatonview.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bottomnavigatonview.FoodActivity;
import com.example.bottomnavigatonview.GardenActivity;
import com.example.bottomnavigatonview.LogActivity;
import com.example.bottomnavigatonview.R;
import com.example.bottomnavigatonview.RecipeActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    // Buttons on the Home Fragment page
    private Button foodBtn;
    private Button logBtn;
    private Button recipesBtn;
    private Button gardenBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        // Setting up buttons to appropriate ID's
        foodBtn = (Button) root.findViewById(R.id.available_food_id);
        logBtn = (Button) root.findViewById(R.id.log_food_id);
        recipesBtn = (Button) root.findViewById(R.id.recipes_id);

        /*
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Listens for Food Button click and starts new activity at Food page
        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodActivity.class);
                startActivity(intent);
                // Toast.makeText(getActivity(), "success", 5).show();
            }
        });

        // Listens for recipes Button click and starts new activity at recipes page
        recipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                startActivity(intent);
                // Toast.makeText(getActivity(), "success", 0).show();
            }
        });

        // Listens for log Button click and starts new activity at log page
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogActivity.class);
                startActivity(intent);
                // Toast.makeText(getActivity(), "success", 0).show();
            }
        });
    }
}

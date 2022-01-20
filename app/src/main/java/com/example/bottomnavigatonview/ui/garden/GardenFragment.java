package com.example.bottomnavigatonview.ui.garden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigatonview.R;

public class GardenFragment extends Fragment {

    private GardenViewModel gardenViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gardenViewModel =
                new ViewModelProvider(this).get(GardenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_garden, container, false);
        final TextView textView = root.findViewById(R.id.text_garden);
        //RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
       // recyclerView.setVisibility(View.INVISIBLE);

        /*
        gardenViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        return root;
    }
}
package com.example.bottomnavigatonview.ui.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigatonview.Product;
import com.example.bottomnavigatonview.ProductAdapter;
import com.example.bottomnavigatonview.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FoodViewModel foodViewModel;

    //a list to store all the products
    private List<Product> productList;
    private List<Product> fullProductList;
    //the recyclerview
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private Spinner filter;

    //read food data file
    private static final String FILE_NAME = "food_data.txt";
    private int temp_id;
    private String temp_product;
    private String temp_category;
    private double temp_quantity;
    private String temp_date;
    private String mdrawableName;
    private Button buttonRemove;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodViewModel =
                new ViewModelProvider(this).get(FoodViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food, container, false);
        final TextView textView = root.findViewById(R.id.text_food);

        // Drop down Categories
        filter = (Spinner) root.findViewById(R.id.category);
        filter.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> categories = new ArrayList<String>();
        categories.add(0, "All");
        categories.add("Vegetable");
        categories.add("Fruit");
        categories.add("Dairy");
        categories.add("Protein");
        categories.add("Grains");
        categories.add("Other");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        //Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        filter.setAdapter(dataAdapter);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initializing the productlist
        productList = new ArrayList<>();

        // Adding items into productList from food_data.txt
        try {
            readFoodData("All");

            // Error was caught within readFoodData();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //creating recyclerview adapter
        adapter = new ProductAdapter(getActivity(), productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);


        // gives trash button function to execute
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) throws IOException {
                removeItem(position);

            }
        });

        /*
        // Example of call to delete the product "Sushi" from food_data.txt
        try {
            deleteProductFromFile("Sushi");
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        return root;
    }

    // removes item from recycler viewer
    public void removeItem(int position) throws IOException {
        deleteProductFromFile((productList.get(position)).getTitle());
        productList.remove(position);
        adapter.notifyItemRemoved(position);

        // Toast.makeText(getActivity().getApplicationContext(), productList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String category = (String) parent.getItemAtPosition(position);
        updateProductList(category);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void updateProductList(String filter) {
        productList.clear();
        try {
            readFoodData(filter);

            // Error was caught within readFoodData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reads a file of input foods and adds it to the productList to be displayed
    // File Format
    // *            -- indicates next 4 lines are product, category, quantity, and purchase date
    // Candy        -- food product
    // Other        -- food category
    // 5            -- quantity
    // 3/27/2021    -- purchase date (month, day, year)
    private void readFoodData(String filter) throws IOException {
        FileInputStream fis = getContext().openFileInput(FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            int switchData = 0;
            String line = bufferedReader.readLine();
            // We hit a id or start of new data group, so know the next 4 are product, category, quantity, and date
            while (line != null) {
                while (line != null && switchData < 4) {
                    switch (switchData) {
                        case 0:
                            temp_product = line;
                            break;
                        case 1:
                            temp_category = line;
                            break;
                        case 2:
                            temp_quantity = Double.parseDouble(line);
                            break;
                        case 3:
                            temp_date = line;
                            break;
                        default:
                            break;
                    }
                    switchData += 1;
                    line = bufferedReader.readLine();
                }
                switchData = 0; // Reset SwitchData that indicates product, category, quantity, and date
                mdrawableName = "no_image"; // what image to pull, defaults to no_image unless a hardcoded example

                // Hard Coded examples for bread and ground beef
                if (temp_product.equals("bread") || temp_product.equals("Bread")) {
                    mdrawableName = "bread";
                }
                if (temp_product.equals("ground beef") || temp_product.equals("Ground Beef") || temp_product.equals("Ground beef")) {
                    mdrawableName = "ground_beef";
                }
                if (temp_product.equals("apple") || temp_product.equals("Apple") || temp_product.equals("Apples") || temp_product.equals("apples")) {
                    mdrawableName = "apple";
                }
                if (temp_product.equals("broccoli") || temp_product.equals("Broccoli")) {
                    mdrawableName = "broccoli";
                }
                if (temp_product.equals("carrot") || temp_product.equals("Carrot") || temp_product.equals("Carrots") || temp_product.equals("carrots")) {
                    mdrawableName = "carrots";
                }
                if (temp_product.equals("spinach") || temp_product.equals("Spinach")) {
                    mdrawableName = "spinach";
                }

                // Adds current input data to the productList
                int imageID = getResources().getIdentifier(mdrawableName, "drawable", getContext().getPackageName());
                if (filter.equals("All") || temp_category.equals(filter)) {
                    productList.add(
                            new Product(
                                    1,
                                    temp_product,
                                    temp_category,
                                    temp_quantity,
                                    temp_date,
                                    imageID));
                }
            }
            // Error occurred upon open filing
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close out the file
        if (fis != null) {
            try {
                fis.close();
                // Error occurred during closing of file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // End of readFoodData

    // Method takes in the product name that should be deleted, and removes all its matching
    // fields from the food_data.txt file
    private void deleteProductFromFile(String productDelete) throws IOException {
        FileInputStream fis = getContext().openFileInput(FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(fis);

        // Delete temp_file before starting up again.
        File file = new File("/data/data/com.example.bottomnavigatonview/files/food_data_temp.txt");
        boolean deleted = file.delete();

        FileOutputStream fos = null;
        fos = getContext().openFileOutput("food_data_temp.txt", getContext().MODE_APPEND);

        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line = bufferedReader.readLine();
            while (line != null) {
                // Found product to delete, so we just read the next 4 lines
                // Ignoring the data.
                if (line.equals(productDelete)) {
                    for (int i = 0; i < 4; i++) {
                        // Do nothing, just need next lines
                        line = bufferedReader.readLine();
                    }
                } else {
                    // Want this data so we write it to the temp file.
                    line = line + "\n";
                    fos.write(line.getBytes());
                    line = bufferedReader.readLine();
                }
            }
            // Error occurred upon open filing
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close FileOutputStream file
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Close FileInputStream file
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // change food_data_temp to food_data.txt
        File tempFile = new File("/data/data/com.example.bottomnavigatonview/files/food_data_temp.txt");
        File dataFile = new File("/data/data/com.example.bottomnavigatonview/files/food_data.txt");
        boolean changed = tempFile.renameTo(dataFile);
    }
}

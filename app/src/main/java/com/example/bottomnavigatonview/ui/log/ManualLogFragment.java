package com.example.bottomnavigatonview.ui.log;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bottomnavigatonview.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManualLogFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    LogViewModel logViewModel;
    // Button data
    private Button manualInputBtn;
    private Button barcodeScannerBtn;
    private Button confirmBtn;

    // TextView data
    private EditText product;
    private EditText quantity;
    private String productString;
    private String quantityString;

    // Spinner data
    private Spinner categorySpinner;
    private String categoryItem;

    // Calendar View
    CalendarView calendar;
    private int calendar_month;
    private int calendar_day;
    private int calendar_year;

    // Write to file Data
    private static final String FILE_NAME = "food_data.txt";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logViewModel =
                new ViewModelProvider(this).get(LogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_manual_log, container, false);
        // final TextView textView = root.findViewById(R.id.text_log);

        // Button toggle set up
        barcodeScannerBtn = (Button) root.findViewById(R.id.barcodeScannerBtn);
        barcodeScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });

        // Manual Button set up, on this fragment currently so fires off Toast message.
        manualInputBtn = (Button) root.findViewById(R.id.manualInputBtn);
        manualInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Manual Input -- Displayed Currently", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting up the category spinner / drop down
        // Spinner element
        categorySpinner = (Spinner) root.findViewById(R.id.categorySpinner);
        // Spinner click listener
        categorySpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> categories = new ArrayList<String>();
        categories.add(0, "Select a food category");
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
        categorySpinner.setAdapter(dataAdapter);

        // Method that defaults the date variables to today
        setDateToday();
        // Setting up the calendar
        calendar = (CalendarView) root.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar_month = month+1;
                calendar_day = dayOfMonth;
                calendar_year = year;
                // Toast to check if data saved
                // Toast.makeText(view.getContext(), "Month: " + calendar_month + " Day: " + calendar_day + " Year: " + calendar_year, Toast.LENGTH_LONG).show();
            }
        });

        // Confirmation Button set up and functionality
        confirmBtn = (Button) root.findViewById(R.id.confirmationButton);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resetCheck = 1; // Boolean used to check if we want to reset fragment
                product = (EditText) root.findViewById(R.id.product);
                productString = product.getText().toString();
                quantity = (EditText) root.findViewById(R.id.quantity);
                quantityString = quantity.getText().toString();

                // Check if fields were not updated
                if (productString.equals("Enter Product...") || quantityString.equals("Enter Quantity...") || categoryItem == null){
                    resetCheck = 0;
                }
                if (quantityString.equals("0")){
                    Toast.makeText(v.getContext(), "Please provide quantity greater than 0", Toast.LENGTH_LONG).show();
                    resetCheck = 0;
                    return;
                }
                // Toasts to confirm data is being updated as expected
                // Toast.makeText(v.getContext(), "Product = " + productString, Toast.LENGTH_LONG).show();
                // Toast.makeText(v.getContext(), "Quantity = " + quantity, Toast.LENGTH_LONG).show();
                // Toast.makeText(v.getContext(), "CategoryItem = " + categoryItem, Toast.LENGTH_LONG).show();
                // Toast.makeText(v.getContext(), "Month: " + calendar_month + " Day: " + calendar_day + " Year: " + calendar_year, Toast.LENGTH_LONG).show();

                // All expected data points were adjusted, proceed to add product and inform user
                if (resetCheck == 1){
                    // TODO write the product, quantity, category, and date to file so it can be added to available foods
                    Toast.makeText(v.getContext(), "ADDED Product: "+productString+", Quantity: "+quantityString +
                            ", Category: "+categoryItem+", Purchase Date: "+calendar_month+"/"+calendar_day+"/"+calendar_year, Toast.LENGTH_LONG).show();
                    // write the data to file
                    String date = String.valueOf(calendar_month) +"/"+ String.valueOf(calendar_day) + "/" + String.valueOf(calendar_year);
                    try {
                        // Write the data to file.
                        writeDataToFile(productString, categoryItem, quantityString, date);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // resets the fragment, so that user is set to add next product
                    resetFragment();
                }
                // Not all fields were updated with data, inform the user
                else{
                    Toast.makeText(v.getContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    // Method to swap from fragment_manual_log fragment to fragment_log
    private void swapFragment(){
        LogFragment newLogFragment = new LogFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_log_fragment, newLogFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Method to reset fragment_manual_log
    private void resetFragment(){
        ManualLogFragment newManualLogFragment = new ManualLogFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_log_fragment, newManualLogFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Sets the date to today, for the case that calendarView is never selected.
    private void setDateToday(){
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        calendar_month = cal.get(Calendar.MONTH) + 1;
        calendar_day = cal.get(Calendar.DAY_OF_MONTH);;
        calendar_year = cal.get(Calendar.YEAR);
    }

    private void writeDataToFile(String product, String category, String quantity, String date) throws IOException {
        // FORMAT OF THE FILE
        // 1                id -- indicates start of new item
        // Carrots          product -- the food product
        // Vegetable        category -- the category for the product
        // 5                quantity -- the # of carrots
        // 2/12/2021        date -- month/date/year of purchase
        String text = product + "\n" + category + "\n" + quantity + "\n" + date + "\n";
        FileOutputStream fos = null;

        try {
            fos = getContext().openFileOutput(FILE_NAME, getContext().MODE_APPEND);
            fos.write(text.getBytes());
            // Toast.makeText(getContext(), "Saved to " + getContext().getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    // Handles what to do with selected category from spinner / drop down
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // If item is initial display text, do nothing or inform user to update
        if (parent.getItemAtPosition(position).equals("Select a food category")){
            // Toast.makeText(parent.getContext(), "Please choose a food category", Toast.LENGTH_LONG).show();
        }
        else {
            // Food category selected, update data
            categoryItem = parent.getItemAtPosition(position).toString();
            //Toast.makeText(parent.getContext(), "Selected: " + categoryItem, Toast.LENGTH_LONG).show();
        }
    }
    // Handles what to do if no category as selected from dropdown
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO, Do anything on the case the spinner is never used?
    }
}

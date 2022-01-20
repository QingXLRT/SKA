package com.example.bottomnavigatonview.ui.log;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bottomnavigatonview.R;
import com.example.bottomnavigatonview.ui.home.HomeFragment;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

public class LogFragment extends Fragment {

    LogViewModel logViewModel;

    private SurfaceView surfaceView;
    BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    final Handler handler = new Handler();

    // Scanner and Manual buttons
    private Button manualInputBtn;
    private Button barcodeScannerBtn;

    // Confirmation Button
    private Button confirmButton;

    // Write to file Data
    private static final String FILE_NAME = "food_data.txt";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logViewModel =
                new ViewModelProvider(this).get(LogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log, container, false);
        // final TextView textView = root.findViewById(R.id.text_log);

        // Manual Button toggle set up
        manualInputBtn = (Button) root.findViewById(R.id.manualInputBtn);
        manualInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });

        // Barcode Scanner Button set up, on this fragment currently so fires off Toast message.
        barcodeScannerBtn = (Button) root.findViewById(R.id.barcodeScannerBtn);
        barcodeScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Barcode Scanner -- Displayed Currently", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting up variables for barcode Scanner
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
        surfaceView = root.findViewById(R.id.scannerSurfaceView);
        barcodeText = root.findViewById(R.id.productDescriptortxt);
        barcodeText.setTextColor(Color.parseColor("red"));
        initialiseDetectorsAndSources();

        // CREATED TO FAKE A BARCODE SCANNER THAT WORKS
        barcodeText.setText("Scanning for barcode");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                }
            }, 5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                barcodeText.setTextColor(Color.parseColor("black"));
                barcodeText.setText("Barcode Found: Banana");
            }
        }, 10000);

        // Barcode Scanner Button set up, on this fragment currently so fires off Toast message.
        confirmButton = (Button) root.findViewById(R.id.confirmationBtn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This apparently does not work
                if(barcodeText.equals("Scanning for barcode")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Still Scanning for Barcode", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(v.getContext(), "ADDED Product: Banana, Quantity: 1, " +
                            "Category: Fruit, Purchase Date: 3/30/2021", Toast.LENGTH_LONG).show();

                    try {
                        // Write the data to file.
                        writeDataToFile("Banana", "Fruit", "1", "3/30/2021");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        /*
        logViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        return root;
    }

    // Method to swap fragments
    private void swapFragment(){
        ManualLogFragment manualLogFragment = new ManualLogFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_log_fragment, manualLogFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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



    // Shamelessly stolen from https://medium.com/analytics-vidhya/creating-a-barcode-scanner-using-android-studio-71cff11800a2
    private void initialiseDetectorsAndSources() {

        // Toast.makeText(getActivity().getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        // sets up API to detect barCodes and what type
        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        // Sets up camera Source to search for barcodes
        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // Gets the surfaceView for the camera sets up callback so camera can keep searching synchronously
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // Checking for permissions
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {
                            // Barcode found, plays tone and puts into TextView
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null); // Stops it from running anymore
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                // No valid barcode found, plays tone and will keep looking
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        // getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        // getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }

}
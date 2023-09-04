package com.example.search;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    database products = new database(this);

    public static final int code =1234;
    EditText SearchBar;
    Button TextSearch;
    Button VoiceSearch;
    Button BarcodeSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchBar = findViewById(R.id.SearchBar);
        TextSearch = findViewById(R.id.btn_search);
        VoiceSearch = findViewById(R.id.btn_voice);
        BarcodeSearch = findViewById(R.id.btn_barcode);

        products.AddCategories();
        products.CreateProducts();
        TextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Searched = SearchBar.getText().toString();
                Intent Result = new Intent(SearchActivity.this,SearchResultsActivity.class);
                Result.putExtra("ProductSearched",Searched);
                startActivity(Result);
            }
        });


        VoiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
                if(i.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(i,code);
                }
                else {
                    Toast.makeText(SearchActivity.this,"your Device Doesn't Support Voice Speech Recognition ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        BarcodeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

    }

    private void scanCode() {
        //IntentIntegrator integrator = new IntentIntegrator(this);
        ScanOptions options = new ScanOptions();
//        integrator.setCaptureActivity(CaptureAct.class);
//        integrator.setOrientationLocked(false);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setPrompt("Scanning Code");
//        integrator.initiateScan();
        options.setPrompt("Scanning Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result ->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            SearchBar.setText(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    });




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == code)
        {
            if(resultCode == RESULT_OK&& data != null)
            {
                final ArrayList< String > matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (!matches.isEmpty())
                {
                    String Query = matches.get(0);
                    SearchBar.setText(Query);
                    //speak.setEnabled(false);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
package com.example.dicefinaljoshgrainge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.icu.util.Output;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Key values for shared preferences
    final String SHARE_PREF = "sharedPreferences";
    final String SAVED_DICE = "saved_dice";

    // Populate dice values with popular regular dice
    ArrayList<String> diceValues = new ArrayList<String>(){
        {
            add("4");
            add("6");
            add("8");
            add("10");
            add("12");
            add("20");
        }};

    // Cached component values
    Spinner diceValueSpinner;
    TextView outputTextView;
    EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputTextView = (TextView) findViewById(R.id.output);

        // Add all saved dice values when there is saved dice values in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet(SAVED_DICE, new HashSet<>());
        if(set != null) {
            Log.d("Set values", set.toString());
            diceValues.addAll(set);
            Log.d("Did save?","Did add values!! YAY!");
        }
        else
        {
            Log.d("Did save?","Didn't add values");
        }

        // Populate spinner adapter with dice values
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diceValues);
        diceValueSpinner = (Spinner) findViewById(R.id.dice_values);
        diceValueSpinner.setAdapter(spinnerAdapter);

        // Roll button logic
        Button rollOneButton = (Button) findViewById(R.id.roll_1_die_button);
        Button rollTwoButton = (Button) findViewById(R.id.roll_2_die_button);

        // Set onclick of rolling 1 die button
        rollOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String output = "The die value is: ";
                output += String.valueOf(RollDie());

                OutputResults(output);
            }
        });

        // Set onclick of rolling 2 dice button
        rollTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String output = "The first die value is: ";
                output += String.valueOf(RollDie());
                output += ", The second die value is: ";
                output += String.valueOf(RollDie());

                OutputResults(output);
            }
        });

        // User add custom dice values
        inputField = (EditText) findViewById(R.id.input_field);
        Button addDieValueButton = (Button) findViewById(R.id.add_die_button);
        addDieValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDie(inputField.getText().toString());
                inputField.getText().clear();
            }
        });
    }

    // Rolls die and returns face up value
    int RollDie(){
        int max = Integer.parseInt(diceValueSpinner.getSelectedItem().toString());
        return (int)(Math.random() * max) + 1;
    }

    // Output results to output text view
    void OutputResults(String output){
        outputTextView.setText(output);
    }

    // Add users custom die to dice values
    void AddDie(String value){
        diceValues.add(value);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diceValues);
        diceValueSpinner.setAdapter(spinnerAdapter);
    }

    // Save values to shared preferences when application is paused
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> dice = new HashSet<>();

        // Add all values added after the initial 5 dice values
        for (int i = 6; i < diceValues.size(); i++) {
            dice.add(diceValues.get(i));
        }

        // Store value in shared preferences
        editor.putStringSet(SAVED_DICE, dice);
        editor.apply();
    }
}
package my.edu.utar.individualassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static int numberOfPeople = 0;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find UI elements
        Button equalButton = findViewById(R.id.equal_button);
        Button customButton = findViewById(R.id.custom_button);
        Button combinationButton = findViewById(R.id.combination_button);
        EditText peopleEditText = findViewById(R.id.peopleEditText);

        // Set click listeners for buttons
        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = peopleEditText.getText().toString().trim();
                if (!inputText.isEmpty()) {
                    numberOfPeople = Integer.parseInt(inputText);
                    if (numberOfPeople > 0) {
                        openEqualActivity();
                    } else {
                        showNumberOfPeopleErrorDialog();
                    }
                } else {
                    showNumberOfPeopleErrorDialog();
                }
            }
        });

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = peopleEditText.getText().toString().trim();
                if (!inputText.isEmpty()) {
                    numberOfPeople = Integer.parseInt(inputText);
                    if (numberOfPeople > 0) {
                        openCustomActivity();
                    } else {
                        showNumberOfPeopleErrorDialog();
                    }
                } else {
                    showNumberOfPeopleErrorDialog();
                }
            }
        });

        combinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = peopleEditText.getText().toString().trim();
                if (!inputText.isEmpty()) {
                    numberOfPeople = Integer.parseInt(inputText);
                    if (numberOfPeople > 0) {
                        openCombinationActivity();
                    } else {
                        showNumberOfPeopleErrorDialog();
                    }
                } else {
                    showNumberOfPeopleErrorDialog();
                }
            }
        });
    }

    private void showNumberOfPeopleErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage("Number of people cannot be empty or invalid. Please enter a valid number.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void openEqualActivity() {
        // Redirect to EqualActivity
        Intent intent = new Intent(this, EqualActivity.class);
        startActivity(intent);
    }

    private void openCustomActivity() {
        // Redirect to CustomActivity
        Intent intent = new Intent(this, CustomActivity.class);
        startActivity(intent);
    }

    private void openCombinationActivity() {
        // Redirect to CombinationActivity
        Intent intent = new Intent(this, CombinationActivity.class);
        startActivity(intent);
    }

}
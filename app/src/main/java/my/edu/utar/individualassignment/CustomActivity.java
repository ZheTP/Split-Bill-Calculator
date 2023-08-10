package my.edu.utar.individualassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    EditText totalAmountEditText;
    private LinearLayout containerLayout;
    private Button nextButton;
    private TextView resultTextView;
    private boolean isPercentage;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        radioGroup = findViewById(R.id.selection_radioGroup);
        totalAmountEditText = findViewById(R.id.total_custom_amount_edit_text);
        containerLayout = findViewById(R.id.container_layout);
        nextButton = findViewById(R.id.custom_next_button);
        Button homeButton = findViewById(R.id.custom_home_button);
        resultTextView = findViewById(R.id.custom_result_text_view);
        containerLayout.setVisibility(View.GONE);
        resultTextView.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                createEditTextFields();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextButtonClick();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isClicked) {
            getMenuInflater().inflate(R.menu.menu_action, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                saveResult(resultTextView.getText().toString().trim());
                return true;
            case R.id.action_share:
                shareResult(resultTextView.getText().toString().trim());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createEditTextFields() {
        containerLayout.removeAllViews(); // Clear previous views

        int numberOfPeople = MainActivity.numberOfPeople;

        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(checkedRadioButtonId);

        String radioButtonText = selectedRadioButton.getText().toString();
        isPercentage = radioButtonText.equals("Percentage/Ratio");

        if (numberOfPeople > 0 && !radioButtonText.equals("")) {
            for (int i = 0; i < numberOfPeople; i++) {
                LinearLayout horizontalLayout = new LinearLayout(this, null, 0, R.style.LayoutStyle);
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Set LayoutParams for the LinearLayout
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                int marginBottomInDp = 10;
                float scale = getResources().getDisplayMetrics().density;
                int marginBottomInPx = (int) (marginBottomInDp * scale + 0.5f);
                layoutParams.setMargins(0, 0, 0, marginBottomInPx);

                horizontalLayout.setLayoutParams(layoutParams);

                Typeface typeface = ResourcesCompat.getFont(this, R.font.handjet);

                EditText nameText = new EditText(this);
                nameText.setHint("Person " + (i + 1));
                nameText.setTextSize(20f);
                nameText.setTypeface(typeface);
                nameText.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1.5f
                        )
                );

                EditText amountText = new EditText(this);
                amountText.setHint("Amount for person " + (i + 1));
                amountText.setTextSize(20f);
                amountText.setTypeface(typeface);
                amountText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                amountText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        ContextCompat.getDrawable(this, R.drawable.dollar_icon),
                        null, null, null
                );
                amountText.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1.0f
                        )
                );

                horizontalLayout.addView(nameText);
                horizontalLayout.addView(amountText);
                containerLayout.addView(horizontalLayout);
            }
        }

        containerLayout.setVisibility(View.VISIBLE);
    }

    private void handleNextButtonClick() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        // Validate RadioButton (if not found radioButton)
        if (checkedRadioButtonId == -1) {
            // Show a Toast message
            Toast.makeText(this, "One of the radio buttons must be selected", Toast.LENGTH_SHORT).show();
            return; // Return early to prevent further processing
        }

        // Validate totalAmount
        float totalAmount;
        try {
            totalAmount = Float.parseFloat(totalAmountEditText.getText().toString().trim());
        } catch (NumberFormatException e){
            // Handle invalid input
            // Show a Toast message
            Toast.makeText(this, "Total Amount must not be empty.", Toast.LENGTH_SHORT).show();
            return; // Return early to prevent further processing
        }

        if (totalAmount == 0){
            // Show a Toast message
            Toast.makeText(this, "Total Amount must larger than 0.", Toast.LENGTH_SHORT).show();
            return; // Return early to prevent further processing
        }

        int childCount = containerLayout.getChildCount();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Float> amountList = new ArrayList<>();

        // Traverse through EditText and store name and amount
        for (int i = 0; i < childCount; i++) {
            View view = containerLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout horizontalLayout = (LinearLayout) view;

                // Get person name
                EditText nameText = (EditText) horizontalLayout.getChildAt(0);
                String nameStr = nameText.getText().toString().trim();

                // Validate person name
                if (nameStr.equals("")) {
                    nameStr = "Person " + (i + 1);
                }

                nameList.add(nameStr);

                // Get person amount
                EditText amountText = (EditText) horizontalLayout.getChildAt(1);
                String amountStr = amountText.getText().toString().trim();

                // Validate amount
                try {
                    float value = Float.parseFloat(amountStr);
                    amountList.add(value);
                } catch (NumberFormatException e){
                    // Handle invalid input
                    // Show a Toast message
                    String toast = (isPercentage ? "Percentage" : "Amount") + " must be a number.";
                    Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
                    return; // Return early to prevent further processing
                }
            }
        }

        // Calculate and display result
        // Calculate sum
        float totalValues = 0;
        for (int i = 0; i < amountList.size(); i++){
            totalValues = totalValues + amountList.get(i);
        }

        // Create a result string
        String result = "";
        for (int i = 0; i < amountList.size(); i++){
            float amount = (amountList.get(i)/totalValues) * totalAmount;
            // Append the result for each person to the result string
            result = result + nameList.get(i) + " pay for $" + String.format("%.2f", amount) + "\n";
        }
        resultTextView.setText(result);
        containerLayout.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        isClicked = true;
        invalidateOptionsMenu();
    }

    private void saveResult(String result) {
        // Save the result in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Bill", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Result", result);
        editor.apply();

        // Show a Toast message indicating the result has been saved
        Toast.makeText(CustomActivity.this, "Result saved!", Toast.LENGTH_SHORT).show();
    }

    private void shareResult(String result){
        // Share the result via WhatsApp
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, result);

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share Result via...");

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserIntent);
        } else {
            // Handle the case where no suitable app is available to handle the share action.
            Toast.makeText(CustomActivity.this, "No suitable app available to share the result.", Toast.LENGTH_SHORT).show();
        }
    }
}
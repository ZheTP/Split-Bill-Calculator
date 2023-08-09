package my.edu.utar.individualassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

public class CombinationActivity extends AppCompatActivity {

    EditText totalAmountEditText;
    private LinearLayout containerLayout;
    private Button nextButton;
    private TextView resultTextView;
    private boolean isClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination);

        totalAmountEditText = findViewById(R.id.total_combination_amount_edit_text);
        containerLayout = findViewById(R.id.combination_layout);
        nextButton = findViewById(R.id.combination_next_button);
        Button homeButton = findViewById(R.id.combination_home_button);
        resultTextView = findViewById(R.id.combination_result_text_view);
        containerLayout.setVisibility(View.GONE);
        resultTextView.setVisibility(View.GONE);

        createLayout();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextButtonClick();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CombinationActivity.this, MainActivity.class);
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
        switch (item.getItemId()) {
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

    private void createLayout() {
        containerLayout.removeAllViews(); // Clear previous views

        int numberOfPeople = MainActivity.numberOfPeople;

        for (int i = 0; i < numberOfPeople; i++) {
            // Create a vertical LinearLayout to hold each person's view
            LinearLayout personLayout = new LinearLayout(this, null, 0, R.style.LayoutStyle);
            personLayout.setOrientation(LinearLayout.VERTICAL);

            // Set LayoutParams for the LinearLayout
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            int marginBottomInDp = 10;
            float scale = getResources().getDisplayMetrics().density;
            int marginBottomInPx = (int) (marginBottomInDp * scale + 0.5f);
            layoutParams.setMargins(0, 0, 0, marginBottomInPx);

            personLayout.setLayoutParams(layoutParams);

            Typeface typeface = ResourcesCompat.getFont(this, R.font.handjet);

            // Create a RadioGroup to hold the two radio buttons (Percentage/Ratio and Amount)
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(LinearLayout.HORIZONTAL);
            radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            // Create and add the radio buttons to the RadioGroup
            RadioButton percentageRadio = new RadioButton(this);
            percentageRadio.setText(getString(R.string.percentage_ratio));
            percentageRadio.setTypeface(typeface);

            RadioButton amountRadio = new RadioButton(this);
            amountRadio.setText(getString(R.string.amount));
            amountRadio.setTypeface(typeface);

            // Create a horizontal LinearLayout to hold the TextView and EditText
            LinearLayout detailsLayout = new LinearLayout(this);
            detailsLayout.setOrientation(LinearLayout.HORIZONTAL);
            detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            ;

            // Create and add the TextView to the details layout
            TextView textView = new TextView(this);
            textView.setText("Percentage/Amount for Person " + (i + 1) + ":");
            textView.setTypeface(typeface);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.5f
            ));

            // Create and add the EditText to the details layout
            EditText editText = new EditText(this);
            editText.setTypeface(typeface);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, R.drawable.dollar_icon),
                    null, null, null
            );
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            // Add textBox into detailsLayout
            detailsLayout.addView(textView);
            detailsLayout.addView(editText);

            // Add the percentageRadio and amountRadio into RadioGroup
            radioGroup.addView(percentageRadio);
            radioGroup.addView(amountRadio);

            // Add the Views to the person's layout
            personLayout.addView(radioGroup);
            personLayout.addView(detailsLayout);

            // Add the person's layout to the container layout
            containerLayout.addView(personLayout);
        }

        containerLayout.setVisibility(View.VISIBLE);
    }

    private void handleNextButtonClick() {
        int childCount = containerLayout.getChildCount();
        Log.d("Debug", "childCount: " + childCount);
        ArrayList<Integer> radioValues = new ArrayList<>();
        ArrayList<Float> editTextValues = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            View view = containerLayout.getChildAt(i);
            Log.d("Debug", "view get");
            if (view instanceof LinearLayout) {
                LinearLayout personLayout = (LinearLayout) view;
                Log.d("Debug", "linear layout get");
                // Get the RadioGroup for the person
                RadioGroup radioGroup = (RadioGroup) personLayout.getChildAt(0);

                // Get the selected radio button's text
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if (checkedRadioButtonId == -1) {
                    // Show a Toast message
                    Toast.makeText(this, "One of the radio buttons must be selected", Toast.LENGTH_SHORT).show();
                    return; // Return early to prevent further processing
                }

                RadioButton selectedRadioButton = personLayout.findViewById(checkedRadioButtonId);
                String radioButtonText = selectedRadioButton.getText().toString().trim();
                // Determine if the selected radio button is "Percentage/Ratio"
                boolean isPercentage = radioButtonText.equals(getString(R.string.percentage_ratio));
                Log.d("Debug", "Person " + i + " Radio Button Text: " + radioButtonText);
                Log.d("Debug", "Person " + i + " Radio Button ID: " + checkedRadioButtonId);
                Log.d("Debug", "Person " + i + " isPercentage: " + isPercentage);

                // Get the EditText value for the person
                LinearLayout detailLayout = (LinearLayout) personLayout.getChildAt(1);
                for (int j = 0; j < detailLayout.getChildCount(); j++) {
                    View childView = detailLayout.getChildAt(j);
                    Log.d("Debug", "childView get");
                    if (childView instanceof EditText) {
                        EditText editText = (EditText) childView;
                        Log.d("Debug", "editText get");
                        String editTextValueStr = editText.getText().toString().trim();
                        if (editTextValueStr.isEmpty()) {
                            // Show a Toast message
                            Toast.makeText(this, "Percentage/Amount must be a number.", Toast.LENGTH_SHORT).show();
                            return; // Return early to prevent further processing
                        }

                        float editTextValue;
                        try {
                            editTextValue = Float.parseFloat(editTextValueStr);
                            Log.d("Debug", "value Stored");
                        } catch (NumberFormatException e) {
                            // Handle invalid input (e.g., non-float values)
                            // Show a Toast message
                            Toast.makeText(this, "Percentage/Amount must be a number.", Toast.LENGTH_SHORT).show();
                            return; // Return early to prevent further processing
                        }

                        // Store 1 for Percentage/Ratio, 0 for Amount
                        if (isPercentage) {
                            radioValues.add(1);
                            Log.d("Debug", "radioValue add 1");
                        } else {
                            radioValues.add(0);
                            Log.d("Debug", "radioValue add 0");
                        }
                        editTextValues.add(editTextValue);
                        Log.d("Debug", "editTextValue: " + editTextValue);
                    }
                }
            }
        }

        // Calculate and display result
        ArrayList<Integer> percentIndex = new ArrayList<>();
        ArrayList<Integer> valueIndex = new ArrayList<>();
        float totalPercent = 0;
        float totalAmount;

        try {
            totalAmount = Float.parseFloat(totalAmountEditText.getText().toString());
        } catch (NumberFormatException e) {
            // Handle invalid input (e.g., non-float values)
            // Show a Toast message
            Toast.makeText(this, "Total Amount must be a number.", Toast.LENGTH_SHORT).show();
            return; // Return early to prevent further processing
        }

        for (int i = 0; i < radioValues.size(); i++) {
            if (radioValues.get(i) == 1) {
                // isPercentage
                percentIndex.add(i);
                totalPercent = totalPercent + editTextValues.get(i);
            } else {
                valueIndex.add(i);
            }
        }

        Log.d("Debug", "radioValues: " + radioValues.toString());
        Log.d("Debug", "editTextValues: " + editTextValues.toString());
        Log.d("Debug", "totalPercent: " + totalPercent);
        Log.d("Debug", "totalAmount: " + totalAmount);
        Log.d("Debug", "percentIndex: " + percentIndex.toString());
        Log.d("Debug", "valueIndex: " + valueIndex.toString());

        // Create a result string
        StringBuilder result = new StringBuilder();
        int personNumber;
        float amount;
        String formattedAmount;
        // Find person is paying by amount first
        for (int i = 0; i < valueIndex.size(); i++) {
            personNumber = valueIndex.get(i);
            amount = editTextValues.get(personNumber);
            formattedAmount = String.format("%.2f", amount);
            totalAmount = totalAmount - amount;
            result.append("Person ").append(personNumber).append(" needs to pay $").append(formattedAmount).append("\n");
        }
        // Find person is paying by percent
        for (int i = 0; i < percentIndex.size(); i++) {
            personNumber = percentIndex.get(i);
            amount = (editTextValues.get(personNumber) / totalPercent) * totalAmount;
            formattedAmount = String.format("%.2f", amount);
            result.append("Person ").append(personNumber).append(" needs to pay $").append(formattedAmount).append("\n");
        }

        Log.d("Debug", "result: " + result);
        resultTextView.setText(result);
        containerLayout.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
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
        Toast.makeText(CombinationActivity.this, "Result saved!", Toast.LENGTH_SHORT).show();
    }

    private void shareResult(String result) {
        // Share the result via WhatsApp
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, result);

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share Result via...");

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserIntent);
        } else {
            // Handle the case where no suitable app is available to handle the share action.
            Toast.makeText(CombinationActivity.this, "No suitable app available to share the result.", Toast.LENGTH_SHORT).show();
        }
    }
}
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

            // Create the radio buttons for RadioGroup
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
            // Create the TextView for the details layout
            EditText nameText = new EditText(this);
            nameText.setHint("Person " + (i + 1));
            nameText.setTextSize(20f);
            nameText.setTypeface(typeface);
            nameText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.5f
            ));
            // Create the EditText for the details layout
            EditText amountText = new EditText(this);
            amountText.setHint("Percentage/Amount of Person " + (i + 1));
            amountText.setTypeface(typeface);
            amountText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            amountText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, R.drawable.dollar_icon),
                    null, null, null
            );
            amountText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            // Add Views into detailsLayout
            detailsLayout.addView(nameText);
            detailsLayout.addView(amountText);

            // Add percent and amount RadioButton into RadioGroup
            radioGroup.addView(percentageRadio);
            radioGroup.addView(amountRadio);

            // Add Views into the person's layout
            personLayout.addView(radioGroup);
            personLayout.addView(detailsLayout);

            // Add the whole person's layout to the container layout
            containerLayout.addView(personLayout);
        }

        // After finished for all people, set Visible
        containerLayout.setVisibility(View.VISIBLE);
    }

    private void handleNextButtonClick() {
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
        ArrayList<Integer> radioValues = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Float> amountList = new ArrayList<>();

        // Traverse through containerLayout (For each person)
        for (int i = 0; i < childCount; i++) {
            // One person detail (name, amount in percent or amount)
            View view = containerLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout personLayout = (LinearLayout) view;
                // Get the RadioGroup for the person
                RadioGroup radioGroup = (RadioGroup) personLayout.getChildAt(0);
                // Get the selected radio button's text
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                // Validate RadioButton (if not found radioButton)
                if (checkedRadioButtonId == -1) {
                    // Show a Toast message
                    Toast.makeText(this, "One of the radio buttons must be selected", Toast.LENGTH_SHORT).show();
                    return; // Return early to prevent further processing
                }

                // Validate this person is in percentage or amount value
                RadioButton selectedRadioButton = personLayout.findViewById(checkedRadioButtonId);
                String radioButtonText = selectedRadioButton.getText().toString().trim();
                // Determine if the selected radio button is "Percentage/Ratio"
                boolean isPercentage = radioButtonText.equals(getString(R.string.percentage_ratio));

                // Store 1 for Percentage/Ratio, 0 for Amount
                if (isPercentage) {
                    radioValues.add(1);
                } else {
                    radioValues.add(0);
                }

                // Get the EditText value for the person
                LinearLayout detailLayout = (LinearLayout) personLayout.getChildAt(1);
                // Get person name
                EditText nameText = (EditText) detailLayout.getChildAt(0);
                String nameStr = nameText.getText().toString().trim();
                // Validate person name
                if (nameStr.equals("")) {
                    nameStr = "Person " + (i + 1);
                }
                nameList.add(nameStr);

                // Get person amount
                EditText amountText = (EditText) detailLayout.getChildAt(1);
                String amountStr = amountText.getText().toString().trim();
                // Validate amount
                try {
                    float value = Float.parseFloat(amountStr);
                    amountList.add(value);
                } catch (NumberFormatException e){
                    // Handle invalid input
                    // Show a Toast message
                    String toast = (isPercentage ? "Percentage" : "Amount") + " for Person " + i +
                            " must be a number.";
                    Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
                    return; // Return early to prevent further processing
                }
            }
        }

        // Calculate and display result
        // See which person is in amount
        ArrayList<Integer> percentIndex = new ArrayList<>();
        ArrayList<Integer> valueIndex = new ArrayList<>();
        float totalPercent = 0;
        for (int i = 0; i < radioValues.size(); i++) {
            if (radioValues.get(i) == 1) {
                // isPercentage
                percentIndex.add(i);
                totalPercent = totalPercent + amountList.get(i);
            } else {
                valueIndex.add(i);
            }
        }

        // Create a result string
        String result = "";
        int personNumber;
        float amount;
        // Find person is paying by amount first
        for (int i = 0; i < valueIndex.size(); i++) {
            personNumber = valueIndex.get(i);
            amount = amountList.get(personNumber);
            totalAmount = totalAmount - amount;
            result = result + nameList.get(personNumber) + " pay for $" + String.format("%.2f", amount) + "\n";
        }
        // Find person is paying by percent
        for (int i = 0; i < percentIndex.size(); i++) {
            personNumber = percentIndex.get(i);
            amount = (amountList.get(personNumber) / totalPercent) * totalAmount;
            result = result + nameList.get(personNumber) + " pay for $" + String.format("%.2f", amount) + "\n";
        }

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
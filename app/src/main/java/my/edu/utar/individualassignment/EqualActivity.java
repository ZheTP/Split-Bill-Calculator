package my.edu.utar.individualassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EqualActivity extends AppCompatActivity {

    private TextView resultTextView;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equal);

        EditText totalAmountEditText = findViewById(R.id.total_equal_amount_edit_text);
        Button calculateButton = findViewById(R.id.calculate_button);
        Button homeButton = findViewById(R.id.equal_home_button);
        resultTextView = findViewById(R.id.equal_result_text_view);
        resultTextView.setVisibility(View.GONE);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calculate and display result
                float totalAmount = 0;
                try {
                    totalAmount = Float.parseFloat(totalAmountEditText.getText().toString());
                } catch (NumberFormatException e){
                    // Handle invalid input (e.g., non-float values)
                    // Show a Toast message
                    Toast.makeText(EqualActivity.this, "Total Amount must be a number.", Toast.LENGTH_SHORT).show();
                    return; // Return early to prevent further processing
                }
                float result = totalAmount / MainActivity.numberOfPeople;
                resultTextView.setText(String.format("Each person need to pay $%.2f", result));
                resultTextView.setTextSize(30);
                resultTextView.setVisibility(View.VISIBLE);
                calculateButton.setVisibility(View.GONE);
                isClicked = true;
                invalidateOptionsMenu();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EqualActivity.this, MainActivity.class);
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

    private void saveResult(String result) {
        // Save the result in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Bill", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Result", result);
        editor.apply();

        // Show a Toast message indicating the result has been saved
        Toast.makeText(EqualActivity.this, "Result saved!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(EqualActivity.this, "No suitable app available to share the result.", Toast.LENGTH_SHORT).show();
        }
    }
}
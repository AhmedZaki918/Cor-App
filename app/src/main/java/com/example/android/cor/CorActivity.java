package com.example.android.cor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

// Main Activity class
public class CorActivity extends AppCompatActivity {

    // Initializations for variables
    @BindView(R.id.edit_text_totalWeight)
    EditText totalWeight;
    @BindView(R.id.edit_text_corWeight)
    EditText corWeight;
    @BindView(R.id.edit_text_stretchPrice)
    EditText stretchPrice;
    @BindView(R.id.edit_text_cor_price)
    EditText corPrice;
    @BindView(R.id.text_view_result_net)
    TextView netCost;
    @BindView(R.id.text_view_result_total)
    TextView totalCost;
    @BindView(R.id.button_run)
    Button run;
    @BindView(R.id.button_clear)
    Button clear;
    @BindView(R.id.button_plus)
    Button plus;
    @BindView(R.id.button_subtract)
    Button subtract;
    @BindView(R.id.button_50)
    Button fifty;
    @BindView(R.id.button_150)
    Button oneHundredFifty;
    @BindView(R.id.image_button_lock)
    ImageButton lock;
    @BindView(R.id.button_clear_lock)
    Button clearLock;


    // Variable to format the result
    private double formatted;

    // Initializations for static variables
    private static final double FIFTY = 0.050;
    private static final double ONE_HUNDRED_FIFTY = 0.150;

    // String Variables to store the data passed from each EditText
    String getValueCorWeight;
    String getValueTotalWeight;
    String getValueStretchPrice;
    String getValueCorPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cor);
        ButterKnife.bind(this);

        // Displays the price of stretch and cor once activity is opened
        load();

        // This button Calculate the total and net price
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcNet();
                calcTotal();
            }
        });

        // This button clear all data the user insert it
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll();
            }
        });

        // This button add one on the number
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String object to store the given text from EditText
                String weight = totalWeight.getText().toString().trim();
                // Double object to assign it 0 value if weight object is empty
                double weightField;
                // If the weight object is empty, assign 0 then add 0.5 to it
                if (weight.isEmpty()) {
                    weightField = 0;
                    weightField += 0.5;
                    totalWeight.setText(weightField + "");
                } else {
                    // Convert weight object to double and store it in converted object
                    double converted = Double.parseDouble(weight);
                    converted += 0.5;
                    totalWeight.setText(converted + "");
                }
            }
        });

        // This button subtract one from the number
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String object to store the given text from EditText
                String weight = totalWeight.getText().toString().trim();
                // Double object to assign it 0 value if weight object is empty
                double weightField;
                // If the weight object is empty, assign 0
                if (weight.isEmpty()) {
                    weightField = 0;
                    totalWeight.setText(weightField + "");
                } else {
                    // Convert weight object to double and store it in converted object
                    double converted = Double.parseDouble(weight);
                    // If the converted object is equal to zero or less than zero, assign zero to it
                    if (converted <= 0) {
                        converted = 0;
                    } else {
                        converted -= 0.5;
                    }
                    totalWeight.setText(converted + "");
                }
            }
        });

        // This button displays core weight (0.050 kg) in EditText
        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCorWeight(FIFTY);
            }
        });

        //  This button displays core weight (0.150 kg) in EditText
        oneHundredFifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCorWeight(ONE_HUNDRED_FIFTY);
            }
        });

        // Saves the stretch price
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(stretchPrice, corPrice);
            }
        });

        // Clear the stretch price
        clearLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stretchPrice.setText("");
                corPrice.setText("");
            }
        });
    }

    // This method saves the price of stretch and cor in shared preference
    private void save(EditText corPrice, EditText stretchPrice) {
        // String object to store the given price from the user
        String priceOfCor = corPrice.getText().toString();
        String priceOfStretch = stretchPrice.getText().toString();
        // Check if the price is empty or not to change the toast message
        if (TextUtils.isEmpty(priceOfCor) || TextUtils.isEmpty(priceOfStretch)) {
            // Toast message to tell the user to enter the price
            Toast.makeText(this, R.string.enter_price, Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences shrd = getSharedPreferences("file", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shrd.edit();
            editor.putString("cor", corPrice.getText().toString());
            editor.putString("stretch", stretchPrice.getText().toString());
            editor.apply();
            // Toast message to confirm the price was saved
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        }
    }

    // This method retrieve the stretch price in shared preference
    private void load() {
        SharedPreferences shrd = getSharedPreferences("file", Context.MODE_PRIVATE);
        String cor = shrd.getString("cor", "0");
        String stretch = shrd.getString("stretch", "0");
        stretchPrice.setText(cor);
        corPrice.setText(stretch);
    }

    /**
     * This method take the given parameter and display it on the EditText
     *
     * @param coreWeight both core weight (0.050 kg, 0.150 kg)
     */
    private void displayCorWeight(double coreWeight) {
        corWeight.setText(coreWeight + "");
    }

    // The whole operation of net price
    private void calcNet() {
        // Extract the values from each EditText
        getAllEditTexts();

        if (TextUtils.isEmpty(getValueCorWeight) || TextUtils.isEmpty(getValueTotalWeight) || TextUtils.isEmpty(getValueStretchPrice)) {
            Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_SHORT).show();

        } else {
            // Calculate the cost of cor
            double calcCostCor = Double.parseDouble(getValueCorWeight) * Double.parseDouble(getValueCorPrice);
            // Calculate the net weight
            double netWeight = Double.parseDouble(getValueTotalWeight) - Double.parseDouble(getValueCorWeight);
            // Calculate the final net weight
            double cost = Double.parseDouble(getValueTotalWeight) - Double.parseDouble(getValueCorWeight);
            cost = cost * Double.parseDouble(getValueStretchPrice);
            cost = cost + calcCostCor;
            double result = cost / netWeight;
            // format the double to 2 digit
            formatter(result);
            // Display the final cost
            netCost.setText(formatted + "");
        }
    }

    // The whole operation of total price
    private void calcTotal() {
        // Extract the values from each EditText
        getAllEditTexts();

        if (TextUtils.isEmpty(getValueCorWeight) || TextUtils.isEmpty(getValueTotalWeight) || TextUtils.isEmpty(getValueStretchPrice)) {
            //Log.e(LOG_TAG, "The values are: " + getValueCorWeight );
            Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_SHORT).show();

        } else {
            // Calculate the cost of cor
            double calcCostCor = Double.parseDouble(getValueCorWeight) * Double.parseDouble(getValueCorPrice);
            // Calculate the final total weight
            double cost = Double.parseDouble(getValueTotalWeight) - Double.parseDouble(getValueCorWeight);
            cost = cost * Double.parseDouble(getValueStretchPrice);
            cost = cost + calcCostCor;
            double result = cost / Double.parseDouble(getValueTotalWeight);
            // format the double to 2 digit
            formatter(result);
            // Display the final cost
            totalCost.setText(formatted + "");
        }
    }

    // Extract all values from each EditText and store it in string variables
    private void getAllEditTexts() {
        // Get the data from all EditTexts
        getValueCorWeight = corWeight.getText().toString();
        getValueTotalWeight = totalWeight.getText().toString();
        getValueStretchPrice = stretchPrice.getText().toString();
        getValueCorPrice = corPrice.getText().toString();
    }

    // Clear all data in all views
    private void clearAll() {
        totalWeight.setText("");
        corWeight.setText("");
        netCost.setText(0 + "");
        totalCost.setText(0 + "");
    }

    // format the double number
    private void formatter(double num) {
        DecimalFormat dFormatter = new DecimalFormat("#.##");
        formatted = Double.parseDouble(dFormatter.format(num));
    }
}
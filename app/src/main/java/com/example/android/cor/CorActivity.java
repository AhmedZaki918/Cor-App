package com.example.android.cor;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

// Main Activity class
public class CorActivity extends AppCompatActivity {


    // Constants for cor weight
    private static final double FIFTY = 0.05;
    private static final double ONE_HUNDRED_FIFTY = 0.15;

    // Initializations for variables
    @BindView(R.id.tv_totalWeight)
    TextView tvTotalWeight;
    @BindView(R.id.btn_plus)
    Button btnPlus;
    @BindView(R.id.btn_subtract)
    Button btnSubtract;
    @BindView(R.id.tv_cor_weight)
    TextView tvCorWeight;
    @BindView(R.id.btn_plus2)
    Button btnPlus2;
    @BindView(R.id.btn_subtract2)
    Button btnSubtract2;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    @BindView(R.id.et_plastic_price)
    EditText etPlasticPrice;
    @BindView(R.id.et_cor_price)
    EditText etCorPrice;
    @BindView(R.id.tv_net_cost)
    TextView tvNetCost;
    @BindView(R.id.tv_total_cost)
    TextView tvTotalCost;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_save)
    Button btnSave;

    // String Variables to store the data passed from each EditText
    private String corWeight;
    private String totalWeight;
    private String stretchPrice;
    private String corPrice;
    public String netCost;
    public String totalCost;

    // Index variables to use it as a reference
    private double plasticWeight = 0;
    private int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cor);
        ButterKnife.bind(this);

        // Displays the saved prices of Plastic and Cor once an activity is opened
        load();

        // Click listener on calculate button
        btnCalculate.setOnClickListener(view -> {
            calcNet();
            calcTotal();
        });

        // Click listener on clear button
        btnClear.setOnClickListener(view -> clearAll());

        // Click listener on plus button
        btnPlus.setOnClickListener(v -> {
            // If the plastic weight >= 5kg assign it to 5kg
            if (plasticWeight >= 5) {
                plasticWeight = 5;
                // Toast message to notify the user this's maximum value
                Toast.makeText(this, R.string.maximum_value, Toast.LENGTH_SHORT).show();

                // Increase the weight by 0.5kg
            } else {
                plasticWeight += 0.5;
                tvTotalWeight.setText(String.format("%s", plasticWeight));
            }
        });

        // Click listener on subtract button
        btnSubtract.setOnClickListener(v -> {
            // If the plastic weight <= 0kg assign it to 0kg
            if (plasticWeight <= 0) {
                plasticWeight = 0;

                // Decrease the weight by 0.5kg
            } else {
                plasticWeight -= 0.5;
                tvTotalWeight.setText(String.format("%s", plasticWeight));
            }
        });

        // Click listener on plus button2
        btnPlus2.setOnClickListener(v -> displayCorWeight());

        // Click listener on subtract button2
        btnSubtract2.setOnClickListener(view -> checkCorWeight());

        // Click listener on save button
        btnSave.setOnClickListener(v -> save(etPlasticPrice, etCorPrice));

        // To retrieve all values if savedInstanceState object is not null
        if (savedInstanceState != null) {
            // Store all values in corresponding variables by "key"
            totalWeight = savedInstanceState.getString("totalWeight");
            corWeight = savedInstanceState.getString("corWeight");
            netCost = savedInstanceState.getString("netCost");
            totalCost = savedInstanceState.getString("totalCost");

            // Set those values on corresponding views
            tvTotalWeight.setText(totalWeight);
            tvCorWeight.setText(corWeight);
            tvNetCost.setText(netCost);
            tvTotalCost.setText(totalCost);
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Get the values from views and store them
        String netCost = tvNetCost.getText().toString();
        String totalCost = tvTotalCost.getText().toString();

        // Save the values in outState object
        outState.putString("totalWeight", totalWeight);
        outState.putString("corWeight", corWeight);
        outState.putString("netCost", netCost);
        outState.putString("totalCost", totalCost);
    }


    /**
     * Store the price of plastic and cor in shared preference
     *
     * @param corPrice     cor price on screen to save it
     * @param plasticPrice plastic price on screen to save it
     */
    private void save(EditText corPrice, EditText plasticPrice) {
        // Get the values from views and store them
        String returnedCor = corPrice.getText().toString();
        String returnedPlastic = plasticPrice.getText().toString();

        // Check if the prices is empty or not before saving operation
        if (TextUtils.isEmpty(returnedCor) || TextUtils.isEmpty(returnedPlastic)) {
            // Toast message to notify the user to enter the prices
            Toast.makeText(this, R.string.enter_price, Toast.LENGTH_SHORT).show();

            // Save the prices to shared preferences
        } else {
            SharedPreferences shrd = getSharedPreferences("file", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shrd.edit();
            editor.putString("cor", returnedCor);
            editor.putString("stretch", returnedPlastic);
            editor.apply();
            // Toast message to confirm the price was saved
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Retrieve the prices by shared preference
     */
    private void load() {
        SharedPreferences shrd = getSharedPreferences("file", Context.MODE_PRIVATE);
        String cor = shrd.getString("cor", "0");
        String stretch = shrd.getString("stretch", "0");
        // Displays the prices on screen
        etPlasticPrice.setText(cor);
        etCorPrice.setText(stretch);
    }


    /**
     * Displays cor weight on screen
     */
    private void displayCorWeight() {
        // We created index variable to do 2 operations only.
        // If index = 0 displays 50g on screen
        if (index == 0) {
            tvCorWeight.setText(String.format("%s", FIFTY));

            index++;
            // Displays 150g on screen
        } else {
            tvCorWeight.setText(String.format("%s", ONE_HUNDRED_FIFTY));
            index = 0;
        }
    }


    /**
     * Check cor weight before decrease the weight in subtract button
     */
    private void checkCorWeight() {
        // Store the cor weight in variable
        String value = tvCorWeight.getText().toString();
        // Convert the value variable to double
        double converted = Double.valueOf(value);
        // If the cor weight (converted) = 150g assign it to 50g
        if (converted == ONE_HUNDRED_FIFTY) {
            tvCorWeight.setText(String.format("%s", FIFTY));
        }
    }


    /**
     * The whole operation of calculating net price cost
     */
    private void calcNet() {
        // Extract the values from each EditText and TextView
        getAllEditTexts();
        // Check the views if it's null or not before calculate the net price
        if (TextUtils.isEmpty(corWeight) || TextUtils.isEmpty(totalWeight) || TextUtils.isEmpty(stretchPrice)) {
            Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_SHORT).show();

            // Calculate the net price cost
        } else {
            // Calculate the cost of cor
            double calcCostCor = Double.parseDouble(corWeight) * Double.parseDouble(corPrice);
            // Calculate the net weight
            double netWeight = Double.parseDouble(totalWeight) - Double.parseDouble(corWeight);

            // Calculate the final net weight
            double cost = Double.parseDouble(totalWeight) - Double.parseDouble(corWeight);
            cost *= Double.parseDouble(stretchPrice);
            cost += calcCostCor;
            double result = cost / netWeight;

            // Store returned value from formatter method
            double formattedValue = formatter(result);
            // Display the final cost
            tvNetCost.setText(String.format("%s", formattedValue));
        }
    }


    /**
     * The whole operation of calculating total price cost
     */
    private void calcTotal() {
        // Extract the values from each EditText and TextView
        getAllEditTexts();
        // Check the views if it's null or not before calculate the total price
        if (TextUtils.isEmpty(corWeight) || TextUtils.isEmpty(totalWeight) || TextUtils.isEmpty(stretchPrice)) {
            Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_SHORT).show();

            // Calculate the total price cost
        } else {
            // Calculate the cost of cor
            double calcCostCor = Double.parseDouble(corWeight) * Double.parseDouble(corPrice);
            // Calculate the final total weight
            double cost = Double.parseDouble(totalWeight) - Double.parseDouble(corWeight);
            cost *= Double.parseDouble(stretchPrice);
            cost += calcCostCor;
            double result = cost / Double.parseDouble(totalWeight);

            // Store returned value from formatter method
            double formattedValue = formatter(result);
            // Display the final cost
            tvTotalCost.setText(String.format("%s", formattedValue));
        }
    }


    /**
     * Extract the values from all views to store it in variables
     */
    private void getAllEditTexts() {
        corWeight = tvCorWeight.getText().toString();
        totalWeight = tvTotalWeight.getText().toString();
        stretchPrice = etPlasticPrice.getText().toString();
        corPrice = etCorPrice.getText().toString();
    }


    /**
     * Clear all values from all views if the user click on Clear Button
     */
    private void clearAll() {
        tvTotalWeight.setText("0");
        tvCorWeight.setText("0");
        tvNetCost.setText(R.string.zero);
        tvTotalCost.setText(R.string.zero);
        plasticWeight = 0;
    }


    /**
     * format the cost price of Plastic and Cor
     *
     * @param costPrice of cor and plastic
     * @return the price after formatted
     */
    private double formatter(double costPrice) {
        DecimalFormat value = new DecimalFormat("#.##");
        return Double.parseDouble(value.format(costPrice));
    }
}
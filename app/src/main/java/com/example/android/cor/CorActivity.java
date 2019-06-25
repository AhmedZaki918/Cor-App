package com.example.android.cor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

// Main Activity class
public class CorActivity extends AppCompatActivity {


    // Initializations for variables
    private final static double COR_PRICE = 12;
    @BindView(R.id.edit_text_totalWeight)
    EditText totalWeight;
    @BindView(R.id.edit_text_corWeight)
    EditText corWeight;
    @BindView(R.id.edit_text_stretchPrice)
    EditText stretchPrice;
    @BindView(R.id.text_view_result_net)
    TextView netCost;
    @BindView(R.id.text_view_result_total)
    TextView totalCost;
    @BindView(R.id.button_run)
    Button run;
    @BindView(R.id.button_clear)
    Button clear;

    // Variable to format the result
    private double formatted;

    // String Variables to store the data passed from each EditText
    String getValueCorWeight;
    String getValueTotalWeight;
    String getValueStretchPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cor);
        ButterKnife.bind(this);

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
    }


    // The whole operation of run price
    private void calcNet() {
        // Extract the values from each EditText
        getAllEditTexts();

        if (TextUtils.isEmpty(getValueCorWeight) || TextUtils.isEmpty(getValueTotalWeight) || TextUtils.isEmpty(getValueStretchPrice)) {
            //Log.e(LOG_TAG, "The values are: " + getValueCorWeight );
            Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_SHORT).show();

        } else {
            // Calculate the cost of cor
            double calcCostCor = Double.parseDouble(getValueCorWeight) * COR_PRICE;

            // Calculate the run weight
            double netWeight = Double.parseDouble(getValueTotalWeight) - Double.parseDouble(getValueCorWeight);

            // Calculate the final run weight
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
            double calcCostCor = Double.parseDouble(getValueCorWeight) * COR_PRICE;

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
    }

    // Clear all data in all views
    private void clearAll() {
        totalWeight.setText("");
        corWeight.setText("");
        stretchPrice.setText("");
        netCost.setText(0 + "");
        totalCost.setText(0 + "");
    }

    // format the double number
    private void formatter(double num) {
        DecimalFormat dFormatter = new DecimalFormat("#.##");
        formatted = Double.parseDouble(dFormatter.format(num));
    }
}
package com.example.android.cor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    @BindView(R.id.text_view_result)
    TextView realCost;
    @BindView(R.id.button_net)
    Button net;
    @BindView(R.id.button_total)
    Button total;
    @BindView(R.id.button_clear)
    Button clear;

    // Variable to format the result
    private double formatted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cor);
        ButterKnife.bind(this);

        // This button Calculate the net price
        net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcNet();
            }
        });

        // This Button Calculate the Total price
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    // The whole operation of net price
    private void calcNet() {
        // Get data form all EditText and convert it to double
        double getValueCorWeight = Double.parseDouble(corWeight.getText().toString());
        double getValueTotalWeight = Double.parseDouble(totalWeight.getText().toString());
        double getValueStretchPrice = Double.parseDouble(stretchPrice.getText().toString());

        // Calculate the cost of cor
        double calcCostCor = getValueCorWeight * COR_PRICE;

        // Calculate the net weight
        double netWeight = getValueTotalWeight - getValueCorWeight;

        // Calculate the final net weight
        double cost = (getValueTotalWeight - getValueCorWeight);
        cost = cost * getValueStretchPrice;
        cost = cost + calcCostCor;
        double result = cost / netWeight;

        // format the double to 2 digit
        formatter(result);

        // Display the final cost
        realCost.setText(formatted + "");
    }

    // The whole operation of total price
    private void calcTotal() {
        // Get data form all EditText and convert it to double
        double getValueCorWeight = Double.parseDouble(corWeight.getText().toString());
        double getValueTotalWeight = Double.parseDouble(totalWeight.getText().toString());
        double getValueStretchPrice = Double.parseDouble(stretchPrice.getText().toString());

        // Calculate the cost of cor
        double calcCostCor = getValueCorWeight * COR_PRICE;

        // Calculate the final total weight
        double cost = (getValueTotalWeight - getValueCorWeight);
        cost = cost * getValueStretchPrice;
        cost = cost + calcCostCor;
        double result = cost / getValueTotalWeight;

        // format the double to 2 digit
        formatter(result);

        // Display the final cost
        realCost.setText(formatted + "");
    }

    // Clear all data in all views
    private void clearAll() {
        totalWeight.setText("");
        corWeight.setText("");
        stretchPrice.setText("");
        realCost.setText(0 + "");
    }

    // format the double number
    private void formatter(double num) {
        DecimalFormat dFormatter = new DecimalFormat("#.##");
        formatted = Double.parseDouble(dFormatter.format(num));
    }
}
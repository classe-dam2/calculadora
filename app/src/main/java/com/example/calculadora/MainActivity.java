package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Botons dels números (0-9): comparteixen el primer OnClickListener
    private final int[] numberButtons = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
    };

    // Resta de botons: comparteixen el segon OnClickListener
    private final int[] otherButtons = {
            R.id.btnSum, R.id.btnSub, R.id.btnMult, R.id.btnDiv, R.id.btnDecimal,
            R.id.btnAc, R.id.btnDel, R.id.btnEq,
            R.id.btnMc, R.id.btnMr, R.id.btnMplus, R.id.btnMsub
    };

    private Calculator calculator = new Calculator();
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.txtResult);

        setupButtons();
    }

    private void setupButtons() {
        // Primer listener: només els números
        View.OnClickListener numberListener = v -> {
            calculator.input(((Button) v).getText().toString());
            showResult();
        };

        // Segon listener: operadors, punt, AC, DEL, = i memòria
        View.OnClickListener actionListener = v -> {
            int id = v.getId();
            try {
                if (id == R.id.btnAc) {
                    calculator.clear();
                } else if (id == R.id.btnDel) {
                    calculator.delete();
                } else if (id == R.id.btnEq) {
                    calculator.calculate();
                } else if (id == R.id.btnMplus) {
                    calculator.memoryAdd();
                } else if (id == R.id.btnMsub) {
                    calculator.memorySubtract();
                } else if (id == R.id.btnMr) {
                    calculator.memoryRecall();
                } else if (id == R.id.btnMc) {
                    calculator.memoryClear();
                } else {
                    // Operadors i punt
                    calculator.input(((Button) v).getText().toString());
                }
                showResult();
            } catch (ArithmeticException e) {
                result.setText("Error");
            }
        };

        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(numberListener);
        }
        for (int id : otherButtons) {
            findViewById(id).setOnClickListener(actionListener);
        }
    }

    private void showResult() {
        result.setText(calculator.getDisplay());
    }
}

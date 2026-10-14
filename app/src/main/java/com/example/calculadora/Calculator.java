package com.example.calculadora;

import java.math.BigDecimal;

// Aquí hi ha tota la lògica de la calculadora. No fa servir res d'Android.
// Les operacions es fan d'esquerra a dreta (2+3x4 = 20).
public class Calculator {

    private String expression = "";
    private double memory = 0;
    private boolean hasMemory = false;

    // Text que es veu per pantalla
    public String getDisplay() {
        if (expression.equals("")) {
            return "0";
        }
        return expression;
    }

    public double getMemory() {
        return memory;
    }

    public boolean hasMemory() {
        return hasMemory;
    }

    // Afegeix un número, un operador o el punt
    public void input(String symbol) {
        if (isOperator(symbol)) {
            // No es pot començar amb un operador ni posar-ne dos seguits
            if (expression.equals("") || endsWithOperator()) {
                return;
            }
        }

        // Un número només pot tenir un punt
        if (symbol.equals(".") && currentNumber().contains(".")) {
            return;
        }

        expression = expression + symbol;
    }

    public void clear() {
        expression = "";
    }

    public void delete() {
        if (!expression.equals("")) {
            expression = expression.substring(0, expression.length() - 1);
        }
    }

    // Calcula el resultat. Si l'operació no està acabada (per exemple "5+") no fa res.
    // Si es divideix entre zero llança ArithmeticException.
    public void calculate() {
        try {
            double result = getValue();
            expression = format(result);
        } catch (NumberFormatException e) {
            // operació incompleta, no fem res
        }
    }

    public void memoryAdd() {
        try {
            memory = memory + getValue();
            hasMemory = true;
            expression = "";
        } catch (NumberFormatException e) {
            // operació incompleta, no fem res
        }
    }

    public void memorySubtract() {
        try {
            memory = memory - getValue();
            hasMemory = true;
            expression = "";
        } catch (NumberFormatException e) {
            // operació incompleta, no fem res
        }
    }

    public void memoryRecall() {
        // Si ja hi ha un número escrit, se substitueix pel valor de la memòria
        String withoutNumber = expression.substring(0, expression.length() - currentNumber().length());
        expression = withoutNumber + format(memory);
    }

    public void memoryClear() {
        memory = 0;
        hasMemory = false;
    }

    // Resultat de l'operació que hi ha escrita
    private double getValue() {
        try {
            return evaluate(expression);
        } catch (ArithmeticException e) {
            expression = "";
            throw e;
        }
    }

    // Recorre el text lletra a lletra, guardant el número actual i l'operador pendent
    private double evaluate(String text) {
        double result = 0;
        String operator = "+";
        String number = "";

        for (int i = 0; i < text.length(); i++) {
            String letter = text.substring(i, i + 1);

            // Un "-" abans d'un número és el signe (-5), no una resta
            boolean isSign = letter.equals("-") && number.equals("");

            if (isOperator(letter) && !isSign) {
                result = apply(result, operator, Double.parseDouble(number));
                operator = letter;
                number = "";
            } else {
                number = number + letter;
            }
        }

        // L'últim número
        return apply(result, operator, Double.parseDouble(number));
    }

    private double apply(double left, String operator, double right) {
        if (operator.equals("+")) {
            return left + right;
        } else if (operator.equals("-")) {
            return left - right;
        } else if (operator.equals("x")) {
            return left * right;
        } else {
            if (right == 0) {
                throw new ArithmeticException("Divisió entre zero");
            }
            return left / right;
        }
    }

    // Número que s'està escrivint (el que hi ha després de l'últim operador)
    private String currentNumber() {
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (isOperator(expression.substring(i, i + 1))) {
                return expression.substring(i + 1);
            }
        }
        return expression;
    }

    private boolean endsWithOperator() {
        String last = expression.substring(expression.length() - 1);
        return isOperator(last);
    }

    private boolean isOperator(String symbol) {
        return symbol.equals("+") || symbol.equals("-") || symbol.equals("x") || symbol.equals("/");
    }

    // Treu els zeros sobrants: 12.0 passa a ser "12"
    private String format(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}

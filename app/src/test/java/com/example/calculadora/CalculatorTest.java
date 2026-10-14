package com.example.calculadora;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    private void type(String... symbols) {
        for (String symbol : symbols) calculator.input(symbol);
    }

    @Test
    public void emptyShowsZero() {
        assertEquals("0", calculator.getDisplay());
    }

    @Test
    public void evaluatesLeftToRight() {
        type("2", "+", "3", "x", "4");
        calculator.calculate();
        assertEquals("20", calculator.getDisplay());
    }

    @Test
    public void supportsDecimalsAndIgnoresSecondSeparator() {
        type("1", ".", "5", ".", "5", "+", "1");
        calculator.calculate();
        assertEquals("2.55", calculator.getDisplay());
    }

    @Test
    public void ignoresLeadingAndRepeatedOperators() {
        type("+", "5", "+", "-", "x", "2");
        assertEquals("5+2", calculator.getDisplay());
    }

    @Test
    public void incompleteExpressionIsKept() {
        type("5", "+");
        calculator.calculate();
        assertEquals("5+", calculator.getDisplay());
    }

    @Test
    public void divisionByZeroThrowsAndClears() {
        type("5", "/", "0");
        try {
            calculator.calculate();
        } catch (ArithmeticException expected) {
            assertEquals("0", calculator.getDisplay());
            return;
        }
        throw new AssertionError("Expected ArithmeticException");
    }

    @Test
    public void deleteOnEmptyDoesNotCrash() {
        calculator.delete();
        assertEquals("0", calculator.getDisplay());
    }

    @Test
    public void negativeResultCanBeContinued() {
        type("2", "-", "5");
        calculator.calculate();
        type("+", "1");
        calculator.calculate();
        assertEquals("-2", calculator.getDisplay());
    }

    @Test
    public void memoryAddRecallSubtractClear() {
        type("5");
        calculator.memoryAdd();
        assertEquals("0", calculator.getDisplay());

        calculator.memoryRecall();
        assertEquals("5", calculator.getDisplay());

        type("+", "1");
        calculator.memorySubtract();
        assertEquals(-1.0, calculator.getMemory(), 0.0); // 5 - (5+1)

        calculator.memoryClear();
        assertTrue(!calculator.hasMemory());
    }

    @Test
    public void recallReplacesCurrentNumber() {
        type("5");
        calculator.memoryAdd();
        type("3");
        calculator.memoryRecall();
        assertEquals("5", calculator.getDisplay());
    }
}

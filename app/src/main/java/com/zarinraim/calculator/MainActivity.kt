package com.zarinraim.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.zarinraim.calculator.databinding.ActivityMainBinding
import kotlin.ArithmeticException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var lastNumeric: Boolean = false
    private var lastPoint: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setting up listeners for all buttons
        // numeric buttons
        binding.btnOne.setOnClickListener { view -> onDigit(view) }
        binding.btnTwo.setOnClickListener { view -> onDigit(view) }
        binding.btnThree.setOnClickListener { view -> onDigit(view) }
        binding.btnFour.setOnClickListener { view -> onDigit(view) }
        binding.btnFive.setOnClickListener { view -> onDigit(view) }
        binding.btnSix.setOnClickListener { view -> onDigit(view) }
        binding.btnSeven.setOnClickListener { view -> onDigit(view) }
        binding.btnEight.setOnClickListener { view -> onDigit(view) }
        binding.btnNine.setOnClickListener { view -> onDigit(view) }
        binding.btnZero.setOnClickListener { view -> onDigit(view) }

        // decimal point button
        binding.btnDecimal.setOnClickListener { view -> onDecimalPoint(view) }

        // operators
        binding.btnDivide.setOnClickListener { view -> onOperator(view) }
        binding.btnMultiply.setOnClickListener { view -> onOperator(view) }
        binding.btnPlus.setOnClickListener { view -> onOperator(view) }
        binding.btnMinus.setOnClickListener { view -> onOperator(view) }

        // clear button
        binding.btnClr.setOnClickListener { onClear() }

        //equals button
        binding.btnEquals.setOnClickListener { view -> onEquals(view) }
    }

    /**
     * On equals button clicked
     */
    private fun onEquals(view: View) {
        if (lastNumeric){
            var statement = binding.tvInput.text
            var prefix = ""
            try {
                if (statement.startsWith("-")) {
                    prefix = "-"
                    statement = statement.substring(1)
                }

                if (statement.contains(Regex("[-+*/]"))) {
                    val splitValue = statement.split("+", "-", "/", "*")

                    val operandOne = splitValue[0]
                    val operandTwo = splitValue[1]

                    var result = when {
                        statement.contains("-") ->
                            (prefix + operandOne).toDouble() - operandTwo.toDouble()
                        statement.contains("+") ->
                            (prefix + operandOne).toDouble() + operandTwo.toDouble()
                        statement.contains("*") ->
                            (prefix + operandOne).toDouble() * operandTwo.toDouble()
                        else ->
                            (prefix + operandOne).toDouble() / operandTwo.toDouble()
                    }

                    binding.tvInput.text = removeZeroAfterDot(result.toString())
                }


            } catch (e: ArithmeticException){
                binding.tvInput.text = "ERR"

                e.printStackTrace()
            }
        }
    }

    /**
     * On operator button clicked, if conditions are met adds an operator symbol
     */
    private fun onOperator(view: View) {
        // if last symbol is numeric and there is not already an operator
        if (lastNumeric && !isOperatorAdded(binding.tvInput.text.toString())){
            // convert flags
            lastNumeric = false
            lastPoint = false
            // adds an operator
            binding.tvInput.append((view as Button).text)
        }
    }

    /**
     * Checks if there is an operator in input
     */
    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("/") || value.contains("*")
                    || value.contains("+") || value.contains("-")
        }
    }

    /**
     * Adds a decimal point if there is not already one
     */
    private fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastPoint) {
            lastPoint = true
            lastNumeric = false
            binding.tvInput.append((view as Button).text)
        }
    }

    /**
     * Adds a digit to the text view
     */
    private fun onDigit(view: View) {
        // get the value
        val value = (view as Button).text

        // get the input
        val input = binding.tvInput.text
        // if input is blank or last symbol isn't numeric (an operator) and digit is zero
        // do not add
        if ((input.isBlank() || !lastNumeric) && value == "0") return

        // set the numeric flag
        lastNumeric = true
        // add a digit to the textview
        binding.tvInput.append(value)
    }

    /**
     * Clears the text view on button
     * resets flags
     */
    private fun onClear() {
        binding.tvInput.text = ""
        lastNumeric = false
        lastPoint = false
    }

    /**
     * Removes zeros after dot
     */
    private fun removeZeroAfterDot(result: String): String{
        var value = result
        if (value.contains(".0")) {
            value = result.substring(0, result.length - 2)
        }
        return value
    }
}
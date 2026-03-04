package com.example.frragmentosasvr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class Calculadora : Fragment() {

    private lateinit var pantalla: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_calculadora, container, false)

        pantalla = view.findViewById(R.id.pantalla)

        view.findViewById<Button>(R.id.suma).setOnClickListener { sum() }
        view.findViewById<Button>(R.id.menos).setOnClickListener { sub() }
        view.findViewById<Button>(R.id.mult).setOnClickListener { mult() }
        view.findViewById<Button>(R.id.div).setOnClickListener { div() }
        view.findViewById<Button>(R.id.buttone).setOnClickListener { calculate() }

        view.findViewById<Button>(R.id.button1).setOnClickListener { addExpresion("1") }
        view.findViewById<Button>(R.id.button2).setOnClickListener { addExpresion("2") }
        view.findViewById<Button>(R.id.button3).setOnClickListener { addExpresion("3") }
        view.findViewById<Button>(R.id.button4).setOnClickListener { addExpresion("4") }
        view.findViewById<Button>(R.id.button5).setOnClickListener { addExpresion("5") }
        view.findViewById<Button>(R.id.button6).setOnClickListener { addExpresion("6") }
        view.findViewById<Button>(R.id.button7).setOnClickListener { addExpresion("7") }
        view.findViewById<Button>(R.id.button8).setOnClickListener { addExpresion("8") }
        view.findViewById<Button>(R.id.button9).setOnClickListener { addExpresion("9") }
        view.findViewById<Button>(R.id.button0).setOnClickListener { addExpresion("0") }
        view.findViewById<Button>(R.id.buttonpoint).setOnClickListener { addExpresion(".") }

        return view
    }

    fun sum() {
        limpiarError()
        pantalla.text = pantalla.text.toString() + "+"
    }

    fun sub() {
        limpiarError()
        pantalla.text = pantalla.text.toString() + "-"
    }

    fun div() {
        limpiarError()
        pantalla.text = pantalla.text.toString() + "/"
    }

    fun mult() {
        limpiarError()
        pantalla.text = pantalla.text.toString() + "*"
    }


    fun addExpresion(value: String) {
        limpiarError()
        pantalla.append(value)
    }

    fun calculate() {

        val currentCalculation = pantalla.text.toString()
        val regex = Regex("""^\d+(\.\d+)?([+\-*/]\d+(\.\d+)?)*$""")

        if (!regex.matches(currentCalculation)) {
            pantalla.text = "ERROR: Invalid operation"
            return
        }

        val tokenRegex = Regex("""(\d+(\.\d+)?)|[+\-*/]""")
        val tokens = tokenRegex.findAll(currentCalculation)
            .map { it.value }
            .toMutableList()

        var i = 0
        while (i < tokens.size) {
            if (tokens[i] == "*" || tokens[i] == "/") {
                val left = tokens[i - 1].toDouble()
                val right = tokens[i + 1].toDouble()

                val result =
                    if (tokens[i] == "*") left * right
                    else left / right

                tokens[i - 1] = result.toString()
                tokens.removeAt(i)
                tokens.removeAt(i)
                i = 0
            } else i++
        }

        var result = tokens[0].toDouble()
        i = 1

        while (i < tokens.size) {
            val op = tokens[i]
            val value = tokens[i + 1].toDouble()

            result = if (op == "+") result + value else result - value
            i += 2
        }

        pantalla.text = result.toString()
    }

    private fun limpiarError() {
        if (pantalla.text.toString() == "ERROR: Invalid operation") {
            pantalla.text = ""
        }
    }
}
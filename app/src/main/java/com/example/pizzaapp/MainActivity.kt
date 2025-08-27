package com.example.pizzaapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    // Prices
    private val priceSmall = 5.0
    private val priceMedium = 10.0
    private val priceBig = 15.0
    private val priceCheese = 1.5
    private val priceHam = 2.5
    private val pricePepperoni = 2.5
    private val pricePineapple = 1.75

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inputs
        val inputName = findViewById<TextInputEditText>(R.id.txtEditName)

        // Sizes
        val cbSmall = findViewById<CheckBox>(R.id.cbSmall)
        val cbMedium = findViewById<CheckBox>(R.id.cbMedium)
        val cbBig = findViewById<CheckBox>(R.id.cbBigPizza)

        val spSmall = findViewById<Spinner>(R.id.spinnerQuantitySmall)
        val spMedium = findViewById<Spinner>(R.id.spinnerQuantityMedium)
        val spBig = findViewById<Spinner>(R.id.spinnerQuantityBig)

        val tvSubtotalSmall = findViewById<TextView>(R.id.tvSubtotalSmall)
        val tvSubtotalMedium = findViewById<TextView>(R.id.tvSubtotalMedium)
        val tvSubtotalBig = findViewById<TextView>(R.id.tvSubtotalBig)

        // Extras
        val cbCheese = findViewById<CheckBox>(R.id.cbCheese)
        val cbHam = findViewById<CheckBox>(R.id.cbHam)
        val cbPepperoni = findViewById<CheckBox>(R.id.cbPepperoni)
        val cbPineapple = findViewById<CheckBox>(R.id.cbPineapple)

        val spCheese = findViewById<Spinner>(R.id.spinnerQuantityCheese)
        val spHam = findViewById<Spinner>(R.id.spinnerQuantityHam)
        val spPepperoni = findViewById<Spinner>(R.id.spinnerQuantityPepperoni)
        val spPineapple = findViewById<Spinner>(R.id.spinnerQuantityPineapple)

        val tvSubtotalCheese = findViewById<TextView>(R.id.tvSubtotalCheese)
        val tvSubtotalHam = findViewById<TextView>(R.id.tvSubtotalHam)
        val tvSubtotalPepperoni = findViewById<TextView>(R.id.tvSubtotalPepperoni)
        val tvSubtotalPineapple = findViewById<TextView>(R.id.tvSubtotalPineapple)

        // Button
        val btnConfirm = findViewById<MaterialButton>(R.id.btnConfirmarPedido)

        // Function to calculate subtotal
        fun calculateSubTotals(cb: CheckBox, spinner: Spinner, price: Double, tvSubtotal: TextView): Double {
            return if (cb.isChecked) {
                val quantity = spinner.selectedItem.toString().toInt()
                val subtotal = quantity * price
                tvSubtotal.text = "$${String.format("%.2f", subtotal)}"
                subtotal
            } else {
                tvSubtotal.text = "$0.00"
                0.0
            }
        }

        // Function to update all subtotals and return total order
        fun calculateTotalOrder(): Double {
            val totalSmall = calculateSubTotals(cbSmall, spSmall, priceSmall, tvSubtotalSmall)
            val totalMedium = calculateSubTotals(cbMedium, spMedium, priceMedium, tvSubtotalMedium)
            val totalBig = calculateSubTotals(cbBig, spBig, priceBig, tvSubtotalBig)

            val totalCheese = calculateSubTotals(cbCheese, spCheese, priceCheese, tvSubtotalCheese)
            val totalHam = calculateSubTotals(cbHam, spHam, priceHam, tvSubtotalHam)
            val totalPepperoni = calculateSubTotals(cbPepperoni, spPepperoni, pricePepperoni, tvSubtotalPepperoni)
            val totalPineapple = calculateSubTotals(cbPineapple, spPineapple, pricePineapple, tvSubtotalPineapple)

            return totalSmall + totalMedium + totalBig + totalCheese + totalHam + totalPepperoni + totalPineapple
        }

        // Set up dynamic listeners
        fun setupListeners(cb: CheckBox, spinner: Spinner) {
            cb.setOnCheckedChangeListener { _, _ ->
                calculateTotalOrder()
            }
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    calculateTotalOrder()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        // Attach listeners
        val allCheckboxes = listOf(cbSmall, cbMedium, cbBig, cbCheese, cbHam, cbPepperoni, cbPineapple)
        val allSpinners = listOf(spSmall, spMedium, spBig, spCheese, spHam, spPepperoni, spPineapple)

        for (i in allCheckboxes.indices) {
            setupListeners(allCheckboxes[i], allSpinners[i])
        }

        // Confirm button - show summary
        btnConfirm.setOnClickListener {
            val name = inputName?.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!(cbSmall.isChecked || cbMedium.isChecked || cbBig.isChecked)) {
                Toast.makeText(this, "Selecciona al menos un tamaño de pizza", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resumen = StringBuilder("Pedido de $name:\n\n")
            if (cbSmall.isChecked) resumen.append("🍕 Pizza Chica x${spSmall.selectedItem} → ${tvSubtotalSmall.text}\n")
            if (cbMedium.isChecked) resumen.append("🍕 Pizza Mediana x${spMedium.selectedItem} → ${tvSubtotalMedium.text}\n")
            if (cbBig.isChecked) resumen.append("🍕 Pizza Grande x${spBig.selectedItem} → ${tvSubtotalBig.text}\n")

            if (cbCheese.isChecked) resumen.append("🧀 Extra Queso x${spCheese.selectedItem} → ${tvSubtotalCheese.text}\n")
            if (cbHam.isChecked) resumen.append("🥓 Extra Jamón x${spHam.selectedItem} → ${tvSubtotalHam.text}\n")
            if (cbPepperoni.isChecked) resumen.append("🍖 Extra Pepperoni x${spPepperoni.selectedItem} → ${tvSubtotalPepperoni.text}\n")
            if (cbPineapple.isChecked) resumen.append("🍍 Extra Piña x${spPineapple.selectedItem} → ${tvSubtotalPineapple.text}\n")

            // Final Total
            val totalOrder = calculateTotalOrder()
            resumen.append("\n💵 Total: $${String.format("%.2f", totalOrder)}")

            AlertDialog.Builder(this)
                .setTitle("Confirmar Pedido")
                .setMessage(resumen.toString())
                .setPositiveButton("Enviar") { _, _ ->
                    Toast.makeText(this, "Pedido enviado", Toast.LENGTH_SHORT).show()
                    // Limpiar campos
                    inputName.setText("")
                    allCheckboxes.forEach { it.isChecked = false }
                    allSpinners.forEach { it.setSelection(0) }
                    calculateTotalOrder() // actualizar subtotales
                }
                .setNegativeButton("Cancelar") { _, _ ->
                    Toast.makeText(this, "Pedido cancelado", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Initialize subtotals
        calculateTotalOrder()
    }
}

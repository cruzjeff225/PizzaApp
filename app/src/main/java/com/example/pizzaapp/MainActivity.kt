package com.example.pizzaapp

import android.os.Bundle
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    // Price base for pizzas
    private val priceSmall = 5.0
    private val priceMedium = 10.0
    private val priceBig = 15.0
    private val priceCheese = 1.5
    private val priceHam = 2.5
    private val pricePepperoni = 2.5
    private val pricePineapple = 1.75

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Input Name
        val inputName = findViewById<TextInputEditText>(R.id.txtEditName)

        // Sizes Checkboxes
        val cbSmall = findViewById<CheckBox>(R.id.cbSmall)
        val cbMedium = findViewById<CheckBox>(R.id.cbMedium)
        val cbBig = findViewById<CheckBox>(R.id.cbBigPizza)

        // Spinners for sizes
        val spSmall = findViewById<Spinner>(R.id.spinnerQuantitySmall)
        val spMedium = findViewById<Spinner>(R.id.spinnerQuantityMedium)
        val spBig = findViewById<Spinner>(R.id.spinnerQuantityBig)

        // Subtotals sizes
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


        // Calculate subtotals
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

        //Event in button confirm order
        btnConfirm.setOnClickListener {
            val name = inputName?.text.toString().trim()

            // Validate input name
            if (name.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!(cbSmall.isChecked || cbMedium.isChecked || cbBig.isChecked)) {
                Toast.makeText(this, "Selecciona al menos un tamaño de pizza", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Calculate subtotals
            val totalSmall = calculateSubTotals(cbSmall, spSmall, priceSmall, tvSubtotalSmall)
            val totalMedium = calculateSubTotals(cbMedium, spMedium, priceMedium, tvSubtotalMedium)
            val totalBig = calculateSubTotals(cbBig, spBig, priceBig, tvSubtotalBig)

            val totalCheese = calculateSubTotals(cbCheese, spCheese, priceCheese, tvSubtotalCheese)
            val totalHam = calculateSubTotals(cbHam, spHam, priceHam, tvSubtotalHam)
            val totalPepperoni = calculateSubTotals(cbPepperoni, spPepperoni, pricePepperoni, tvSubtotalPepperoni)
            val totalPineapple = calculateSubTotals(cbPineapple, spPineapple, pricePineapple, tvSubtotalPineapple)

            val totalOrder = totalSmall + totalMedium + totalBig + totalCheese + totalHam + totalPepperoni + totalPineapple

            // Order review
            val resumen = StringBuilder("Pedido de $name:\n\n")
            if (cbSmall.isChecked) resumen.append("🍕 Pizza Chica x${spSmall.selectedItem} → $${String.format("%.2f", totalSmall)}\n")
            if (cbMedium.isChecked) resumen.append("🍕 Pizza Mediana x${spMedium.selectedItem} → $${String.format("%.2f", totalMedium)}\n")
            if (cbBig.isChecked) resumen.append("🍕 Pizza Grande x${spBig.selectedItem} → $${String.format("%.2f", totalBig)}\n")

            if (cbCheese.isChecked) resumen.append("🧀 Extra Queso x${spCheese.selectedItem} → $${String.format("%.2f", totalCheese)}\n")
            if (cbHam.isChecked) resumen.append("🥓 Extra Jamón x${spHam.selectedItem} → $${String.format("%.2f", totalHam)}\n")
            if (cbPepperoni.isChecked) resumen.append("🍖 Extra Pepperoni x${spPepperoni.selectedItem} → $${String.format("%.2f", totalPepperoni)}\n")
            if (cbPineapple.isChecked) resumen.append("🍍 Extra Piña x${spPineapple.selectedItem} → $${String.format("%.2f", totalPineapple)}\n")

            resumen.append("\n💵 Total: $${String.format("%.2f", totalOrder)}")

            // Show dialog order confirmation
            AlertDialog.Builder(this).setTitle("Confirmar Pedido").setMessage(resumen.toString()).setPositiveButton("Enviar") {_, _ ->
                Toast.makeText(applicationContext, "✅ Pedido enviado", Toast.LENGTH_SHORT).show()

            // Clear components
                inputName?.setText("")
                cbSmall.isChecked = false
                cbMedium.isChecked = false
                cbBig.isChecked = false
                cbCheese.isChecked = false
                cbHam.isChecked = false
                cbPepperoni.isChecked = false
                cbPineapple.isChecked = false

                tvSubtotalSmall.text = "$0.00"
                tvSubtotalMedium.text = "$0.00"
                tvSubtotalBig.text = "$0.00"
                tvSubtotalCheese.text = "$0.00"
                tvSubtotalHam.text = "$0.00"
                tvSubtotalPepperoni.text = "$0.00"
                tvSubtotalPineapple.text = "$0.00"
            }
                .setNegativeButton("Cancelar") {_,_ ->
                    Toast.makeText(applicationContext, "❌ Pedido cancelado", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }
}
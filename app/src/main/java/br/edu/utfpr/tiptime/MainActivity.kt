package br.edu.utfpr.tiptime

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.calculateButton.setOnClickListener {
            calculate()
        }

        if ( savedInstanceState == null ) {
            val formattedTip = NumberFormat.getCurrencyInstance().format(0)
            binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        } else {
            binding.tipResult.text = savedInstanceState.getString("result")
        }


    } //fim do onCreate()

    fun calculate() {

        //entrada de dados
        val stringInTextField = binding.costOfService.text.toString()

        val cost = stringInTextField.toDoubleOrNull() ?: return

        val selectedId = binding.tipOption.checkedRadioButtonId

        val roundUp = binding.roundUpSwitch.isChecked

        //processamento
        val tipPercentage = when (selectedId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercentage

        if (roundUp) {
            tip = kotlin.math.ceil(tip)
        }

        //saída
        val formattedTip = NumberFormat.getCurrencyInstance().format( tip )

        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString( "result", binding.tipResult.text.toString() )
    }
}
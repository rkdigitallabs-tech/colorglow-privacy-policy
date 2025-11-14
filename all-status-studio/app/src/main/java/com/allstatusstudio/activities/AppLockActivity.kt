package com.allstatusstudio.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.allstatusstudio.databinding.ActivityAppLockBinding
import com.allstatusstudio.utils.AppLockUtils
import com.google.android.material.snackbar.Snackbar

class AppLockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppLockBinding
    private var enteredPin = ""
    private var mode = "unlock" // unlock, set, verify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getStringExtra("mode") ?: "unlock"

        setupUI()
        setupPinPad()
    }

    private fun setupUI() {
        when (mode) {
            "unlock", "unlock_vault" -> {
                binding.tvTitle.text = "Enter PIN"
            }
            "set" -> {
                binding.tvTitle.text = "Set New PIN"
            }
            "verify" -> {
                binding.tvTitle.text = "Confirm PIN"
            }
        }
    }

    private fun setupPinPad() {
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                addDigit(index.toString())
            }
        }

        binding.btnBackspace.setOnClickListener {
            removeDigit()
        }
    }

    private fun addDigit(digit: String) {
        if (enteredPin.length < 4) {
            enteredPin += digit
            updatePinDots()

            if (enteredPin.length == 4) {
                verifyPin()
            }
        }
    }

    private fun removeDigit() {
        if (enteredPin.isNotEmpty()) {
            enteredPin = enteredPin.dropLast(1)
            updatePinDots()
        }
    }

    private fun updatePinDots() {
        binding.dot1.isActivated = enteredPin.length >= 1
        binding.dot2.isActivated = enteredPin.length >= 2
        binding.dot3.isActivated = enteredPin.length >= 3
        binding.dot4.isActivated = enteredPin.length >= 4
    }

    private fun verifyPin() {
        when (mode) {
            "unlock", "unlock_vault" -> {
                if (AppLockUtils.verifyPin(this, enteredPin)) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    showError("Incorrect PIN")
                    resetPin()
                }
            }
            "set" -> {
                // Save for confirmation
                val intent = intent
                intent.putExtra("mode", "verify")
                intent.putExtra("pin", enteredPin)
                finish()
                startActivity(intent)
            }
            "verify" -> {
                val originalPin = intent.getStringExtra("pin")
                if (enteredPin == originalPin) {
                    AppLockUtils.savePin(this, enteredPin)
                    setResult(RESULT_OK)
                    finish()
                } else {
                    showError("PINs don't match")
                    resetPin()
                }
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun resetPin() {
        enteredPin = ""
        updatePinDots()
    }
}

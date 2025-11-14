package com.allstatusstudio.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.allstatusstudio.R
import com.allstatusstudio.databinding.ActivityVaultBinding
import com.allstatusstudio.utils.AppLockUtils
import com.allstatusstudio.viewmodels.VaultViewModel
import com.google.android.material.snackbar.Snackbar

class VaultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVaultBinding
    private lateinit var viewModel: VaultViewModel
    private var isUnlocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[VaultViewModel::class.java]

        setupUI()
        authenticateUser()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.fabAddToVault.setOnClickListener {
            if (isUnlocked) {
                pickFilesToVault()
            }
        }
    }

    private fun authenticateUser() {
        if (AppLockUtils.isBiometricAvailable(this)) {
            showBiometricPrompt()
        } else {
            showPinPrompt()
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    unlockVault()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Snackbar.make(binding.root, "Authentication failed", Snackbar.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showPinPrompt()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Vault")
            .setSubtitle("Use your fingerprint or face to unlock")
            .setNegativeButtonText("Use PIN")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showPinPrompt() {
        val intent = Intent(this, AppLockActivity::class.java)
        intent.putExtra("mode", "unlock_vault")
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            unlockVault()
        } else {
            finish()
        }
    }

    private fun unlockVault() {
        isUnlocked = true
        binding.layoutLocked.visibility = View.GONE
        binding.layoutUnlocked.visibility = View.VISIBLE
        loadVaultContents()
    }

    private fun loadVaultContents() {
        viewModel.loadVaultFiles()
        viewModel.vaultFiles.observe(this) { files ->
            if (files.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
                // Setup RecyclerView with files
            }
        }
    }

    private fun pickFilesToVault() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, 200)
    }
}

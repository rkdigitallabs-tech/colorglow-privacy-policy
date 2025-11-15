package com.allstatusstudio.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.allstatusstudio.R
import com.allstatusstudio.databinding.ActivityOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val PERMISSION_REQUEST_CODE = 100

    // Launcher for Android 11+ storage permission settings
    private val manageStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // Recheck and complete onboarding
        completeOnboarding()
    }

    private val slides = listOf(
        OnboardingSlide(
            "Save Any Status",
            "Download WhatsApp & WhatsApp Business statuses instantly",
            R.raw.onboarding_1
        ),
        OnboardingSlide(
            "Pro Editor Suite",
            "Edit videos & photos with powerful tools, filters & effects",
            R.raw.onboarding_2
        ),
        OnboardingSlide(
            "Premium Templates",
            "Create stunning content with 1000+ templates & captions",
            R.raw.onboarding_3
        ),
        OnboardingSlide(
            "Secure Vault",
            "Hide private media with AES encryption & biometric lock",
            R.raw.onboarding_4
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupButtons()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = OnboardingAdapter(slides)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateUI(position)
            }
        })
    }

    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < slides.size - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                requestPermissions()
            }
        }

        binding.btnSkip.setOnClickListener {
            requestPermissions()
        }
    }

    private fun updateUI(position: Int) {
        if (position == slides.size - 1) {
            binding.btnNext.text = "Get Started"
            binding.btnSkip.text = ""
        } else {
            binding.btnNext.text = "Next"
            binding.btnSkip.text = "Skip"
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ requires MANAGE_EXTERNAL_STORAGE permission
            if (!Environment.isExternalStorageManager()) {
                showManageStoragePermissionDialog()
            } else {
                completeOnboarding()
            }
        } else {
            // Android 10 and below use regular runtime permissions
            val permissions = mutableListOf<String>()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.POST_NOTIFICATIONS)
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }

            if (permissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
            } else {
                completeOnboarding()
            }
        }
    }

    private fun showManageStoragePermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Storage Permission Required")
            .setMessage("To access WhatsApp statuses, this app needs 'All files access' permission.\n\nPlease enable it in the next screen.")
            .setPositiveButton("Grant Permission") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    manageStoragePermissionLauncher.launch(intent)
                } catch (e: Exception) {
                    // Fallback to general settings
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    manageStoragePermissionLauncher.launch(intent)
                }
            }
            .setNegativeButton("Skip") { dialog, _ ->
                dialog.dismiss()
                completeOnboarding()
            }
            .setCancelable(false)
            .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            completeOnboarding()
        }
    }

    private fun completeOnboarding() {
        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_complete", true)
            .apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    data class OnboardingSlide(
        val title: String,
        val description: String,
        val lottieRes: Int
    )

    inner class OnboardingAdapter(private val slides: List<OnboardingSlide>) :
        RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val lottieView: LottieAnimationView = view.findViewById(R.id.lottieAnimation)
            val title: TextView = view.findViewById(R.id.tvTitle)
            val description: TextView = view.findViewById(R.id.tvDescription)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_onboarding_slide, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val slide = slides[position]
            holder.title.text = slide.title
            holder.description.text = slide.description
            holder.lottieView.setAnimation(slide.lottieRes)
            holder.lottieView.playAnimation()
        }

        override fun getItemCount() = slides.size
    }
}

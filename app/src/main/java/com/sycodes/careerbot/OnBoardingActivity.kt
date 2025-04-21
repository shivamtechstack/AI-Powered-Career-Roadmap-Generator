package com.sycodes.careerbot

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sycodes.careerbot.data.SharedPreferencesHelper
import com.sycodes.careerbot.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        binding.btnContinue.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val profession = binding.etProfession.text.toString()
            val age = binding.etAge.text.toString().toIntOrNull() ?: 0

            if (name.isNotEmpty() && email.isNotEmpty()) {
                sharedPreferencesHelper.saveUserData(name, email, profession, age)
                sharedPreferencesHelper.saveFirstTimeUser()
                startActivity(Intent(this, AddNewRoadmapActivity::class.java)).apply {
                    finish()
                }

            }else{
                if (name.isEmpty()) {
                    binding.tilName.error = "Name is required"
                }
                if (email.isEmpty()) {
                    binding.tilEmail.error = "Email is required"
                }
            }
        }
    }
}
package com.sycodes.careerbot

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sycodes.careerbot.data.SharedPreferencesHelper
import com.sycodes.careerbot.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        val isFirstLaunch = sharedPreferencesHelper.isFirstTimeUser()

        Handler().postDelayed({

            if (isFirstLaunch) {
                var intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        },1000)
    }
}
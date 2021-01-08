package com.example.triviaapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.triviaapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private  val SPLASH_DISPLAY_LENGTH:Long = 3000
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },SPLASH_DISPLAY_LENGTH)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacksAndMessages(null)

    }
    override fun onDestroy() {
        super.onDestroy()
    }
}

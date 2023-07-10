package com.example.kotlin_heatmap

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.view.animation.Animation as Animation1

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val logo = findViewById<ImageView>(R.id.logo)

        var logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        logo.animation = logoAnim
        logo.animate().setDuration(2500).alpha(1f).withEndAction() {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }


    }
}
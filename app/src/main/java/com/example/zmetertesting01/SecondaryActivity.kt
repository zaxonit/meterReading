package com.example.zmetertesting01

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)
        val mLayer = findViewById<ImageView>(R.id.pointer0104)
        mLayer.rotation = 30F




        btnTestHelp.setOnClickListener {
            var intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)

        }
    }
}
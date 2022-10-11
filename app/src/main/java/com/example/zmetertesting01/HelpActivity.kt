package com.example.zmetertesting01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

    }
    fun displayReadingMeters(view: View){
        answers.text="Read the dials from left to right." +
                "Keep in mind that the first and third (and fifth) are counter-clockwise." +
                "Read them like a clock and record the number that is smaller between" +
                "the two numbers that the dial is between."
    }
    fun displayPreferencesHowTo(view: View){
        answers.text="This is where the preferences will be explained" +
                "more more more" +
                "more more more"
    }
    fun backHome(view: View) {
        onBackPressed()
    }
}
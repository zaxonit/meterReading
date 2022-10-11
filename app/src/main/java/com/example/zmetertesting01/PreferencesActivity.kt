package com.example.zmetertesting01

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity : AppCompatActivity() {

    lateinit var numQuesEditText: EditText
    lateinit var numDiffQuesEditText: EditText
    lateinit var testTypeRadioButton: RadioButton
    lateinit var sharedPrefs: SharedPreferences

    // created variables for shared pref keys
    var PREFS_KEY = "prefs"

    var NUM_QUES_KEY = "number_questions"
    var NUM_DIFF_QUES_KEY = "number_diff_questions"
    var TEST_TYPE_KEY = "test_type"
    var IS_TIMED_KEY = "is_timed"
    var IS_INPROG_KEY = "is_inprog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        var radioGroup: RadioGroup = findViewById(R.id.testTypeRadioGroup)

        sharedPrefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

//        var prefNumQuestions: Int = sharedPrefs.getInt(NUM_QUES_KEY, 0)!!
//        numQuesEditText = findViewById(R.id.numQuestions)
//        numQuesEditText.setText(prefNumQuestions)

        btnSave.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("numberOfQuestions", numQuestions.text.toString())
            intent.putExtra("numberOfDiffQuestions", numDiffQuestions.text.toString())
            startActivity(intent)

        }



        btnHelp.setOnClickListener {
            var intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)

        }
    }
    fun backHome(view: View) {
        onBackPressed()
    }
}
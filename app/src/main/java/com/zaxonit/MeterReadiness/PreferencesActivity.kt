package com.zaxonit.MeterReadiness

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity : AppCompatActivity() {

    lateinit var numQuesEditText: EditText
    lateinit var numDiffQuesEditText: EditText
    lateinit var testTypeRadioButton: RadioButton
    lateinit var testTypeRadioGroup: RadioGroup
    lateinit var testIsTimedSwitch: Switch
    lateinit var sharedPrefs: SharedPreferences

    // created variables for shared pref keys
    var PREFS_KEY = "prefs"

    var NUM_QUES_KEY = "number_questions"
    var NUM_DIFF_QUES_KEY = "number_diff_questions"
    var TEST_TYPE_KEY = "test_type"
    var IS_TIMED_KEY = "is_timed"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        var radioGroup: RadioGroup = findViewById(R.id.testTypeRadioGroup)

        // collect all Shared Preferences
        sharedPrefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        var PrefNumQuestions: Int = sharedPrefs.getInt(NUM_QUES_KEY, 0)
        var PrefNumDiffQues: Int = sharedPrefs.getInt(NUM_DIFF_QUES_KEY, 0)
        var WhichTestTypeRadioButton: String = sharedPrefs.getString(TEST_TYPE_KEY, "")!!
        var IsTimedSwitch: Int = sharedPrefs.getInt(IS_TIMED_KEY, 0)

        // assure all settings match shared prefs (or default)
        numQuesEditText = findViewById(R.id.numQuestions)
        numDiffQuesEditText = findViewById(R.id.numDiffQuestions)
        testTypeRadioGroup = findViewById(R.id.testTypeRadioGroup)
        testIsTimedSwitch = findViewById(R.id.TimedSwitch)

        numQuesEditText.setText(PrefNumQuestions.toString())
        numDiffQuesEditText.setText(PrefNumDiffQues.toString())
        when (WhichTestTypeRadioButton) {
            "4" -> testTypeRadioGroup.check(R.id.radio4Dial)
            "5" -> testTypeRadioGroup.check(R.id.radio5Dial)
            "M" -> {
                testTypeRadioGroup.check(R.id.radioMixed)
                //snackByView(WhichTestTypeRadioButton, findViewById(testTypeRadioGroup.id))
            }
            else -> testTypeRadioGroup.check(R.id.radio4Dial)
        }
        testIsTimedSwitch.isChecked = IsTimedSwitch == 1

        btnSave.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("numberOfQuestions", numQuestions.text.toString())
            intent.putExtra("numberOfDiffQuestions", numDiffQuestions.text.toString())
            startActivity(intent)

        }

        btnSave.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)

            if (TextUtils.isEmpty(numQuesEditText.text.toString()) || TextUtils.isEmpty(numDiffQuesEditText.text.toString())) {
                if (TextUtils.isEmpty(numQuesEditText.text.toString())) {
                    snackByView("The number of questions must be filled out.", findViewById(numQuesEditText.id))
                } else if (TextUtils.isEmpty(numDiffQuesEditText.text.toString())) {
                    snackByView("The number of difficult questions must be filled out.", findViewById(numDiffQuesEditText.id))
                }
            } else {
                val editor: SharedPreferences.Editor = sharedPrefs.edit()

                editor.putInt(NUM_QUES_KEY, numQuesEditText.text.toString().toInt())
                editor.putInt(NUM_DIFF_QUES_KEY, numDiffQuesEditText.text.toString().toInt())
                var whichTestTypeRadioId: Int = testTypeRadioGroup.checkedRadioButtonId
                if (whichTestTypeRadioId >= 0) {
                    val SelectedTestType: RadioButton = findViewById(whichTestTypeRadioId)
                    //snackByView("Test type is set to ${SelectedTestType.text}", findViewById(testIsTimedSwitch.id))
                    when(SelectedTestType.text) {
                       "4-Dial Only" -> editor.putString(TEST_TYPE_KEY, "4")
                       "5-Dial Only" -> editor.putString(TEST_TYPE_KEY, "5")
                       "Mixed" -> editor.putString(TEST_TYPE_KEY, "M")
                       else -> editor.putString(TEST_TYPE_KEY, "4")
                    }
                }
                if (testIsTimedSwitch.isChecked) {
                    editor.putInt(IS_TIMED_KEY, 1)
                } else {
                    editor.putInt(IS_TIMED_KEY, 0)
                }

                editor.commit()
                startActivity(intent)
            }
    }

    btnHelp.setOnClickListener {
        var intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }
    fun backHome(view: View) {
        onBackPressed()
    }

    private fun snackByView(message: String, whichView: View) {
        val snackView = Snackbar.make(whichView, message, Snackbar.LENGTH_LONG)
        snackView.anchorView = whichView
        snackView.show()
    }
}
package com.zaxonit.MeterReadiness

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.*
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnHelp
import kotlin.math.floor
import kotlin.math.pow


class MainActivity : AppCompatActivity() {

    // create variables for shared-pref-related
    // activity components
    lateinit var nameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var empNumEditText: EditText
    lateinit var loginBtn: Button
    lateinit var startBtn: Button
    lateinit var sharedPrefs: SharedPreferences

    // created variables for shared pref keys
    var PREFS_KEY = "prefs"
    var NAME_KEY = "name"
    var EMAIL_KEY = "email"
    var EMPNUM_KEY = "employee_num"

    var NUM_QUES_KEY = "number_questions"
    var NUM_DIFF_QUES_KEY = "number_diff_questions"
    var TEST_TYPE_KEY = "test_type"
    var IS_TIMED_KEY = "is_timed"
    var QUES_INPROG_KEY = "ques_inprog"
    var TEST_ID_KEY = "test_id"
    var IS_REVIEW_KEY = "is_review"

    // created variables for shared pref values
    var name=""
    var email=""
    var empNum=""

    // preferences defaults
    val NUM_QUESTIONS = 10
    val NUM_DIFF_QUES = 3
    val TEST_TYPE = "M"
    val IS_TIMED = -1

    // defaults is not in prog when setting shared preferences
    val QUES_INPROG = -1
    val TEST_ID = -1
    val IS_REVIEW = -1

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // collect all fields by view
        nameEditText = findViewById(R.id.textFullName)
        emailEditText = findViewById(R.id.textEmail)
        empNumEditText = findViewById(R.id.textEmployeeNo)

        loginBtn = findViewById(R.id.btnLogin)
        startBtn = findViewById(R.id.btnStart)
        sharedPrefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        // evaluate shared preferences
        if (sharedPrefs.contains(NAME_KEY) && sharedPrefs.contains(EMAIL_KEY) && sharedPrefs.contains(EMPNUM_KEY)) {
            nameEditText.setText(sharedPrefs.getString(NAME_KEY, "").toString())
            emailEditText.setText(sharedPrefs.getString(EMAIL_KEY, "").toString())
            empNumEditText.setText(sharedPrefs.getString(EMPNUM_KEY, "").toString())
            setLoggedIn()
        } else {
            setLoggedOut()
        }

        var testWhatSet: String = ""
        if (!sharedPrefs.contains(NUM_QUES_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(NUM_QUES_KEY, NUM_QUESTIONS)
            editor.commit()
        } else {
            testWhatSet += sharedPrefs.getInt(NUM_QUES_KEY, 0).toString() + "-"
        }
        if (!sharedPrefs.contains(NUM_DIFF_QUES_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(NUM_DIFF_QUES_KEY, NUM_DIFF_QUES)
            editor.commit()
        } else {
            testWhatSet += sharedPrefs.getInt(NUM_DIFF_QUES_KEY, 0).toString() + "-"
        }

        if (!sharedPrefs.contains(TEST_TYPE_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putString(TEST_TYPE_KEY, TEST_TYPE)
            editor.commit()
        } else {
            testWhatSet += sharedPrefs.getString(TEST_TYPE_KEY, "").toString() + "-"
        }

        if (!sharedPrefs.contains(IS_TIMED_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(IS_TIMED_KEY, IS_TIMED)
            editor.commit()
        } else {
            testWhatSet += sharedPrefs.getInt(IS_TIMED_KEY, 0).toString() + "-"
        }

//        snackByView(testWhatSet, findViewById(btnHelp.id))

    if (!sharedPrefs.contains(QUES_INPROG_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(QUES_INPROG_KEY, QUES_INPROG)
            editor.commit()
        } else {
            var QuestionInProgress: Int = sharedPrefs.getInt(QUES_INPROG_KEY, 0)
            if (QuestionInProgress >= 0) {
                btnStart.text = "Resume Test"
                btnPreferences.isEnabled = false
                btnPreferences.setTextColor(Color.LTGRAY)
            }
    }

        if (!sharedPrefs.contains(TEST_ID_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(TEST_ID_KEY, TEST_ID)
            editor.commit()
        }

        if (!sharedPrefs.contains(IS_REVIEW_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(IS_REVIEW_KEY, IS_REVIEW)
            editor.commit()
        }

        btnLogin.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            if (btnLogin.text == "Login") {
                val editor: SharedPreferences.Editor = sharedPrefs.edit()

                if (isEmpty(nameEditText.text.toString()) || isEmpty(emailEditText.text.toString()) || isEmpty(empNumEditText.text.toString()
                    )
                ) {
                    if (isEmpty(nameEditText.text.toString())) {
                        snackByView("The full name field must be filled out.", findViewById(nameEditText.id))
                    } else if (isEmpty(emailEditText.text.toString())) {
                        snackByView("The email address field must be filled out.", findViewById(emailEditText.id))
                    } else if (isEmpty(empNumEditText.text.toString())) {
                        snackByView("The employee number field must be filled out.", findViewById(empNumEditText.id))
                    }
                } else {
                    editor.putString(NAME_KEY, nameEditText.text.toString())
                    editor.putString(EMAIL_KEY, emailEditText.text.toString())
                    editor.putString(EMPNUM_KEY, empNumEditText.text.toString())
                    editor.commit()
                    setLoggedIn()
                }
            } else if (btnLogin.text == "Logout"){
//                this.getSharedPreferences("prefs", 0).edit().clear().apply()
                if (btnStart.text != "Resume Test") {
                    sharedPrefs.edit().remove("name").commit()
                    sharedPrefs.edit().remove("email").commit()
                    sharedPrefs.edit().remove("employee_num").commit()
                    setLoggedOut()
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    snackByView("Make sure you have finished the test before logging off.",
                        findViewById(loginBtn.id))
                }
            }

//            startActivity(intent)

        }

        btnStart.setOnClickListener {
            var intent = Intent(this, SecondaryActivity::class.java)
            val dbInstance = DBHelper(this, null)

            // get shared preferences for this test instance
            var PrefNumQuestions: Int = sharedPrefs.getInt(NUM_QUES_KEY, 0)
            var PrefNumDiffQues: Int = sharedPrefs.getInt(NUM_DIFF_QUES_KEY, 0)
            var WhichTestTypeRadioButton: String = sharedPrefs.getString(TEST_TYPE_KEY, "")!!
            var IsTimedSwitch: Int = sharedPrefs.getInt(IS_TIMED_KEY, 0)

            if (btnStart.text != "Resume Test") {
                // add test instance to tests table
                var thisTestId: Int = dbInstance.addTest(
                    sharedPrefs.getString(NAME_KEY, "").toString(),
                    sharedPrefs.getString(EMAIL_KEY, "").toString(),
                    sharedPrefs.getString(EMPNUM_KEY, "")!!.toInt(),
                    PrefNumQuestions, PrefNumDiffQues, WhichTestTypeRadioButton, IsTimedSwitch
                )
                // generate questions for questions table
                var thisTest =
                    QuestionGenerator(PrefNumQuestions, PrefNumDiffQues, WhichTestTypeRadioButton)
                var testFirstQues = thisTest.questions[0]
                var testFirstDialCount = thisTest.questionDialCount[0]
//                test string for debug
//                var checkTestString: String = "$thisTestId - $testFirstQues - $testFirstDialCount"
//                snackByView(checkTestString, findViewById(empNumEditText.id))
                // add questions to questions table with test instance id
                for ((idx, ques) in thisTest.questions.withIndex()) {
                    // calculate and prepare variables for entry into questions tables
                    var thisQuestion: Double = thisTest.questions[idx]
                    var thisCorrectAnswer: Int = thisQuestion.toInt()
                    var thisNumDials = thisTest.questionDialCount[idx]
//                var thisDivisor: Double = (10).toDouble().pow(thisNumDials-1)
                    var thisDivisor: Double = 10.0
                    var thisDial5: Double? = null
                    if (thisNumDials == 5) {
                        thisDial5 = thisQuestion-(floor(thisQuestion/thisDivisor)*thisDivisor)
                        thisDivisor *= 10
                    }
                    var thisDial4: Double = (thisQuestion-(floor(thisQuestion/thisDivisor)*thisDivisor))/(thisDivisor/10)
                    thisDivisor *= 10
                    var thisDial3: Double = (thisQuestion-(floor(thisQuestion/thisDivisor)*thisDivisor))/(thisDivisor/10)
                    thisDivisor *= 10
                    var thisDial2: Double = (thisQuestion-(floor(thisQuestion/thisDivisor)*thisDivisor))/(thisDivisor/10)
                    thisDivisor *= 10
                    var thisDial1: Double = (thisQuestion-(floor(thisQuestion/thisDivisor)*thisDivisor))/(thisDivisor/10)
                    thisDivisor *= 10

                    dbInstance.addQuestion(thisTestId, (idx+1), thisNumDials, thisCorrectAnswer,
                        thisDial1, thisDial2, thisDial3, thisDial4, thisDial5, 0
                    )
                    val editor: SharedPreferences.Editor = sharedPrefs.edit()
                    editor.putInt(QUES_INPROG_KEY, 1)
                    editor.putInt(TEST_ID_KEY, thisTestId)
                    editor.commit()
                }
            }
            finish()
            startActivity(intent)
        }

        btnPreferences.setOnClickListener {
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_out_bottom)
            var intent = Intent(this, PreferencesActivity::class.java)
            finish()
            startActivity(intent)

        }

        btnHelp.setOnClickListener {
            var intent = Intent(this, HelpActivity::class.java)
            finish()
            startActivity(intent)

        }
    }

    private fun setLoggedIn() {
        setETDisabled(nameEditText)
        setETDisabled(emailEditText)
        setETDisabled(empNumEditText)
        btnLogin.text = "Logout"
        btnStart.isEnabled = true
        btnStart.setTextColor(Color.WHITE)
    }

    private fun setLoggedOut() {
        setETEnabled(nameEditText)
        setETEnabled(emailEditText)
        setETEnabled(empNumEditText)

        nameEditText.text.clear()
        emailEditText.text.clear()
        empNumEditText.text.clear()

        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putInt(QUES_INPROG_KEY, QUES_INPROG)
        editor.putInt(TEST_ID_KEY, TEST_ID)
        editor.putInt(IS_REVIEW_KEY, IS_REVIEW)
        editor.commit()

        btnLogin.text = "Login"
        btnStart.isEnabled = false
        btnStart.text = "Start"
        btnPreferences.isEnabled = true
        btnPreferences.setTextColor(Color.WHITE)
        btnStart.setTextColor(Color.GRAY)
    }
    private fun setETEnabled(editText: EditText) {
        editText.focusable = View.FOCUSABLE
        editText.setTextColor(Color.BLACK)
    }

    private fun setETDisabled(editText: EditText) {
        editText.focusable = View.NOT_FOCUSABLE
        editText.setTextColor(Color.GRAY)
    }

    private fun snackByView(message: String, whichView: View) {
        val snackView = Snackbar.make(whichView, message, Snackbar.LENGTH_LONG)
        snackView.anchorView = whichView
        snackView.show()
    }

}

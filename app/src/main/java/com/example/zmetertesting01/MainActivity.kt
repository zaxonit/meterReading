package com.example.zmetertesting01

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.*
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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
    var IS_INPROG_KEY = "is_inprog"

    // created variables for shared pref values
    var name=""
    var email=""
    var empNum=""

    // preferences defaults
    val NUM_QUESTIONS = 10
    val NUM_DIFF_QUES = "3"
    val TEST_TYPE = "4"
    val IS_TIMED = "0"

    // defaults is not in prog when setting shared preferences
    val IS_INPROG = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.textFullName)
        emailEditText = findViewById(R.id.textEmail)
        empNumEditText = findViewById(R.id.textEmployeeNo)

        loginBtn = findViewById(R.id.btnLogin)
        startBtn = findViewById(R.id.btnStart)
        sharedPrefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        if (sharedPrefs.contains(NAME_KEY) && sharedPrefs.contains(EMAIL_KEY) && sharedPrefs.contains(EMPNUM_KEY)) {
            setLoggedIn()
        } else {
            setLoggedOut()
        }

        if (!sharedPrefs.contains(NUM_QUES_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(NUM_QUES_KEY, NUM_QUESTIONS)
        }

        if (!sharedPrefs.contains(NUM_DIFF_QUES_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putString(NUM_DIFF_QUES_KEY, NUM_DIFF_QUES)
        }

        if (!sharedPrefs.contains(TEST_TYPE_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putString(TEST_TYPE_KEY, TEST_TYPE)
        }

        if (!sharedPrefs.contains(IS_TIMED_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putString(IS_TIMED_KEY, IS_TIMED)
        }

        if (!sharedPrefs.contains(IS_INPROG_KEY)) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putString(IS_INPROG_KEY, IS_INPROG)
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

                    setLoggedIn()
                }
            } else {
//                this.getSharedPreferences("prefs", 0).edit().clear().apply()
                sharedPrefs.edit().remove("name").commit()
                sharedPrefs.edit().remove("email").commit()
                sharedPrefs.edit().remove("employee_num").commit()
                setLoggedOut()
            }

//            startActivity(intent)

        }

        btnStart.setOnClickListener {
            var intent = Intent(this, SecondaryActivity::class.java)
            startActivity(intent)

        }

        btnPreferences.setOnClickListener {
            var intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)

        }

        btnHelp.setOnClickListener {
            var intent = Intent(this, HelpActivity::class.java)
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

        btnLogin.text = "Login"
        btnStart.isEnabled = false
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

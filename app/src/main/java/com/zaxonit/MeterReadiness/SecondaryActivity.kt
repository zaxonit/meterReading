package com.zaxonit.MeterReadiness

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : AppCompatActivity() {

    lateinit var sharedPrefs: SharedPreferences
    lateinit var testTitleTextView: TextView
    lateinit var dial4LinearLayout: LinearLayout
    lateinit var dial5LinearLayout: LinearLayout
    lateinit var pointer4LinearLayout: LinearLayout
    lateinit var pointer5LinearLayout: LinearLayout
    lateinit var bottomImageView: ImageView

    // created variables for shared pref keys
    var PREFS_KEY = "prefs"

    var IS_TIMED_KEY = "is_timed"
    var QUES_INPROG_KEY = "ques_inprog"
    var TEST_ID_KEY = "test_id"
    var IS_REVIEW_KEY = "is_review"
    var NUM_QUES_KEY = "number_questions"

    // question variable from DB
    var thisNumDials: Int = -1
    var thisAnswer: Int = -1
    var thisDial1: Double = -1.0
    var thisDial2: Double = -1.0
    var thisDial3: Double = -1.0
    var thisDial4: Double = -1.0
    var thisDial5: Double = -1.0


    @SuppressLint("Range", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        testTitleTextView = findViewById(R.id.testTitle)
        dial4LinearLayout = findViewById(R.id.dialLayout)
        dial5LinearLayout = findViewById(R.id.dial5Layout)
        pointer4LinearLayout = findViewById(R.id.pointerLayout)
        pointer5LinearLayout = findViewById(R.id.pointer5Layout)
        bottomImageView = findViewById(R.id.bottomImage)


        // evaluate question Shared Preferences
        sharedPrefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        var IsTimedTest: Int = sharedPrefs.getInt(IS_TIMED_KEY, 0)
        var QuestionInProgress: Int = sharedPrefs.getInt(QUES_INPROG_KEY, 0)
        var PrefNumQuestions: Int = sharedPrefs.getInt(NUM_QUES_KEY, 0)
        var TestId: Int = sharedPrefs.getInt(TEST_ID_KEY, 0)
        var IsReview: Int = sharedPrefs.getInt(IS_REVIEW_KEY, 0)

        val thisDB = DBHelper(this, null)
        val thisCursor = thisDB.getQuestion(TestId, QuestionInProgress)

        testTitleTextView.text = "Question #${QuestionInProgress.toString()}"

        if(thisCursor!!.moveToFirst()) {
            thisAnswer = thisCursor.getInt(thisCursor.getColumnIndex(DBHelper.ANSWER_COL))
            thisNumDials = thisCursor.getInt(thisCursor.getColumnIndex(DBHelper.NUM_DIALS_COL))
            thisDial1 = thisCursor.getDouble(thisCursor.getColumnIndex(DBHelper.DIAL_1_COL))
            thisDial2 = thisCursor.getDouble(thisCursor.getColumnIndex(DBHelper.DIAL_2_COL))
            thisDial3 = thisCursor.getDouble(thisCursor.getColumnIndex(DBHelper.DIAL_3_COL))
            thisDial4 = thisCursor.getDouble(thisCursor.getColumnIndex(DBHelper.DIAL_4_COL))
            thisDial5 = thisCursor.getDouble(thisCursor.getColumnIndex(DBHelper.DIAL_5_COL))

        }
        var testMessage = "$thisAnswer - $thisNumDials - $thisDial1 - $thisDial2 - $thisDial3 - $thisDial4 - $thisDial5"
        snackByView(testMessage, findViewById(bottomImageView.id))

        if (QuestionInProgress == PrefNumQuestions) {
            btnNext.text = "Finish"
        }

        if (QuestionInProgress == 1) {
            btnPrevious.isEnabled = false
            btnPrevious.setTextColor(Color.GRAY)
        }

        if (thisNumDials == 5) {
            dial4LinearLayout.visibility = View.INVISIBLE
            pointer4LinearLayout.visibility = View.INVISIBLE
            dial5LinearLayout.visibility = View.VISIBLE
            pointer5LinearLayout.visibility = View.VISIBLE

            rotateImageClockwise(thisDial1, findViewById(R.id.pointer0105))
            rotateImageCounterClockwise(thisDial2, findViewById(R.id.pointer0205))
            rotateImageClockwise(thisDial3, findViewById(R.id.pointer0305))
            rotateImageCounterClockwise(thisDial4, findViewById(R.id.pointer0405))
            rotateImageClockwise(thisDial5, findViewById(R.id.pointer0505))

        } else {
            dial4LinearLayout.visibility = View.VISIBLE
            pointer4LinearLayout.visibility = View.VISIBLE
            dial5LinearLayout.visibility = View.INVISIBLE
            pointer5LinearLayout.visibility = View.INVISIBLE

            rotateImageCounterClockwise(thisDial1, findViewById(R.id.pointer0104))
            rotateImageClockwise(thisDial2, findViewById(R.id.pointer0204))
            rotateImageCounterClockwise(thisDial3, findViewById(R.id.pointer0304))
            rotateImageClockwise(thisDial4, findViewById(R.id.pointer0404))
        }

        btnNext.setOnClickListener {
            if (btnNext.text != "Finish") {
                var intent = Intent(this, SecondaryActivity::class.java)

                val editor: SharedPreferences.Editor = sharedPrefs.edit()
                editor.putInt(QUES_INPROG_KEY, (QuestionInProgress+1))
                editor.commit()

                startActivity(intent)
            } else {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        btnPrevious.setOnClickListener {
            var intent = Intent(this, SecondaryActivity::class.java)

            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt(QUES_INPROG_KEY, (QuestionInProgress-1))
            editor.commit()

            startActivity(intent)
        }

        btnTestHelp.setOnClickListener {
            var intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun snackByView(message: String, whichView: View) {
        val snackView = Snackbar.make(whichView, message, Snackbar.LENGTH_LONG)
        snackView.anchorView = whichView
        snackView.show()
    }

    private fun rotateImageClockwise(dial_read: Double, whichView: View) {
        var rotationInDegrees: Float = (360.0F*dial_read/10).toFloat()
        whichView.rotation = rotationInDegrees
    }

    private fun rotateImageCounterClockwise(dial_read: Double, whichView: View) {
        var rotationInDegrees: Float = 360.0F - (360*dial_read/10).toFloat()
        whichView.rotation = rotationInDegrees
    }


}
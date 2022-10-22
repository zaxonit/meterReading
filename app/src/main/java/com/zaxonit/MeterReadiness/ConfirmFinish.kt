package com.zaxonit.MeterReadiness

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_confirm_finish.*
import kotlinx.android.synthetic.main.activity_preferences.*
import kotlinx.android.synthetic.main.activity_secondary.*
import org.w3c.dom.Text
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

class ConfirmFinish : AppCompatActivity() {
    lateinit var sharedPrefs: SharedPreferences
    lateinit var testTitleTextView: TextView
    lateinit var resultHeaderTextView: TextView
    lateinit var resultMessageTextView: TextView
    lateinit var testReviewTextView: TextView

    // created variables for shared pref keys
    var PREFS_KEY = "prefs"

    var IS_TIMED_KEY = "is_timed"
    var TEST_ID_KEY = "test_id"
    var NUM_QUES_KEY = "number_questions"
    var QUES_INPROG_KEY = "ques_inprog"
    var QUES_INPROG = -1


    override fun onBackPressed() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_finish)

        // evaluate question Shared Preferences
        sharedPrefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        var IsTimedTest: Int = sharedPrefs.getInt(IS_TIMED_KEY, 0)
        var PrefNumQuestions: Int = sharedPrefs.getInt(NUM_QUES_KEY, 0)
        var TestId: Int = sharedPrefs.getInt(TEST_ID_KEY, 0)


        val thisDB = DBHelper(this, null)
        val thisCursor = thisDB.getAllQuestionsData(TestId)

        var thisNumDials = IntArray(PrefNumQuestions)
        var thisAnswer = IntArray(PrefNumQuestions)
        var thisDial1 = DoubleArray(PrefNumQuestions)
        var thisUserAnswerInt = IntArray(PrefNumQuestions)
        var thisUserAnswer = arrayOfNulls<String?>(PrefNumQuestions)
        var thisDblAnswer = DoubleArray(PrefNumQuestions)
        var thisQuestionNum = IntArray(PrefNumQuestions)
        var x = 0
        var unanswered = 0
        var reviewString = ""
        var reviewStringWithAnswer = ""
        var currentCorrect = 0
        var currentScoreString = ""

        if(thisCursor!!.moveToFirst()) {
            do {
                thisAnswer[x] = thisCursor.getInt(thisCursor.getColumnIndex(DBHelper.ANSWER_COL))

                thisNumDials[x] =
                    thisCursor.getInt(thisCursor.getColumnIndex(DBHelper.NUM_DIALS_COL))
                thisDial1[x] = thisCursor.getDouble(thisCursor.getColumnIndex(DBHelper.DIAL_1_COL))

                thisUserAnswerInt[x] =
                    thisCursor.getInt(thisCursor.getColumnIndex(DBHelper.USER_ANSWER_COL))
                if (thisUserAnswerInt[x] == 0 && thisAnswer[x] != 0) {
                    unanswered++
                    thisUserAnswer[x] = "(no answer)"
                } else {
                    thisUserAnswer[x] = thisUserAnswerInt[x].toString()
                }
                thisDblAnswer[x] = thisDial1[x]*(10.0).pow(thisNumDials[x]-1)

                var isCorrect = "unknown"
                if ((thisUserAnswerInt[x].toDouble() == floor(thisDblAnswer[x]))
                        || (thisUserAnswerInt[x].toDouble() == floor(thisDblAnswer[x]+0.1))
                        || (thisUserAnswerInt[x].toDouble() == ceil(thisDblAnswer[x]-0.1))) {
                    isCorrect = "correct"
                    currentCorrect++
                } else {
                    isCorrect = "incorrect"
                }

                thisQuestionNum[x] = thisCursor.getInt(thisCursor.getColumnIndex(DBHelper.QUES_NUM_COL))

                reviewString += "Meter ${thisQuestionNum[x]}: ${thisNumDials[x].toString()} dials; answer given: ${thisUserAnswer[x]} \r\n"
                reviewStringWithAnswer += "Question ${thisQuestionNum[x]}: $isCorrect (The answer was ${thisAnswer[x].toString()}; you answered ${thisUserAnswer[x]}) \r\n"

                x++
            } while (thisCursor.moveToNext())
        }

        testReviewTextView = findViewById(R.id.testReview)
        resultHeaderTextView = findViewById(R.id.txtFinishedHeader)
        resultMessageTextView = findViewById(R.id.txtFinishedMessage)

        reviewString = "There are ${unanswered.toString()} incomplete reads.\r\n\r\n" + reviewString
        testReviewTextView.text = reviewString

        btnCancelConfirm.setOnClickListener {
            finish()
            startActivity(Intent(this,SecondaryActivity::class.java))
        }

        btnConfirm.setOnClickListener {
            if (btnConfirm.text != "Home") {
                currentScoreString = "You got ${currentCorrect.toString()} correct out of ${PrefNumQuestions.toString()}."
                resultHeaderTextView.text = "Results"
                resultMessageTextView.text = currentScoreString
                testReviewTextView.text = reviewStringWithAnswer

                val editor: SharedPreferences.Editor = sharedPrefs.edit()
                editor.putInt(QUES_INPROG_KEY, QUES_INPROG)
                editor.commit()

                btnCancelConfirm.isEnabled = false
                btnCancelConfirm.setTextColor(Color.GRAY)
                btnConfirm.text = "Home"
            } else {
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }

        }

    }
}
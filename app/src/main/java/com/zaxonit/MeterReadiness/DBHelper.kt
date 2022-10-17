package com.zaxonit.MeterReadiness

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, factory, DB_VER) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
       val testTableQuery = ("CREATE TABLE " + TABLE_TEST_NAME + " (" +
               ID_COL + " INTEGER PRIMARY KEY, " +
               EMP_NUM_COL + " INTEGER," +
               NAME_COL + " TEXT," +
               EMAIL_COL + " TEXT," +
               NUM_QUES_COL + " INTEGER, " +
               NUM_DIFF_QUES_COL + " INTEGER, " +
               TEST_TYPE_COL + " TEXT," +
               TIMED_BOOL_COL + " INTEGER" +
               ")"
               )
        db.execSQL(testTableQuery)
       val questionsTableQuery = ("CREATE TABLE " + TABLE_QUES_NAME + " (" +
               ID_COL + " INTEGER PRIMARY KEY, " +
               TEST_ID_COL + " INTEGER," +
               QUES_NUM_COL + " INTEGER," +
               NUM_DIALS_COL + " INTEGER," +
               ANSWER_COL + " INTEGER," +
               DIAL_1_COL + " REAL," +
               DIAL_2_COL + " REAL," +
               DIAL_3_COL + " REAL," +
               DIAL_4_COL + " REAL," +
               DIAL_5_COL + " REAL," +
               IS_DIFF_COL + " INTEGER, " +
               USER_ANSWER_COL + " INTEGER, " +
               "FOREIGN KEY(" + TEST_ID_COL + ") REFERENCES " + TABLE_TEST_NAME + "(" + ID_COL + ") ON DELETE SET NULL" +
               ")"
               )
        db.execSQL(questionsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TEST_NAME")
        onCreate(db)
    }

    fun addTest(name: String, email: String, employeeNumber: Int, numberQues: Int, numberDiffQues: Int, testType: String, isTimed: Int) : Int {
        val values = ContentValues()

        values.put(NAME_COL, name)
        values.put(EMAIL_COL, email)
        values.put(EMP_NUM_COL, employeeNumber)
        values.put(NUM_QUES_COL, numberQues)
        values.put(NUM_DIFF_QUES_COL, numberDiffQues)
        values.put(TEST_TYPE_COL, testType)
        values.put(TIMED_BOOL_COL, isTimed)

        val db = this.readableDatabase
        db.insert(TABLE_TEST_NAME, null, values)
        db.close()

        return getCurrentTestId()

    }

    fun addQuestion(
        test_id: Int, question_number: Int, number_dials: Int, correct_answer: Int,
        dial_1: Double, dial_2: Double, dial_3: Double, dial_4: Double, dial_5: Double? = null,
        is_diff: Int) {
        val values = ContentValues()

        values.put(TEST_ID_COL, test_id)
        values.put(QUES_NUM_COL, question_number)
        values.put(NUM_DIALS_COL, number_dials)
        values.put(ANSWER_COL, correct_answer)
        values.put(DIAL_1_COL, dial_1)
        values.put(DIAL_2_COL, dial_2)
        values.put(DIAL_3_COL, dial_3)
        values.put(DIAL_4_COL, dial_4)
        values.put(DIAL_5_COL, dial_5)
        values.put(IS_DIFF_COL, is_diff)

        val db = this.readableDatabase
        db.insert(TABLE_QUES_NAME, null, values)
        db.close()
    }

    fun getQuestion(test_id: Int, question_number: Int): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery(
            "SELECT * FROM $TABLE_QUES_NAME WHERE $TEST_ID_COL = $test_id AND $QUES_NUM_COL = $question_number",
            null
        )
    }

    @SuppressLint("Range")
    fun getCurrentTestId(): Int {
        val db = this.readableDatabase
        var currentTestId: Int = 0
        var thisCursor: Cursor? = db.rawQuery("SELECT id FROM $TABLE_TEST_NAME WHERE $ID_COL = (SELECT MAX($ID_COL) FROM $TABLE_TEST_NAME)", null)

        if (thisCursor?.moveToFirst() == true) {
            currentTestId = thisCursor.getInt(thisCursor.getColumnIndex("id"))
        }
        db.close()
        return currentTestId
    }

    fun getAllQuestionsData(test_id: Int): Cursor? {
        val db = this.readableDatabase

        var returnRawQuery: Cursor? = if (test_id != -1) {
            db.rawQuery(
                "SELECT * FROM $TABLE_QUES_NAME WHERE $TEST_ID_COL = $test_id",
                null)
        } else {
            db.rawQuery(
                "SELECT * FROM $TABLE_QUES_NAME",
                null)
        }
        return returnRawQuery
    }

    fun getCurrentTestCorrect() {
        null
    }

    fun answerQuestion(testId: Int, questionNumber: Int, answer: Int) {
        val db = this.readableDatabase
        val values = ContentValues()
        var whereClause = "$TEST_ID_COL = $testId AND $QUES_NUM_COL = $questionNumber"
        values.put(USER_ANSWER_COL, answer)
        db.update(TABLE_QUES_NAME, values, whereClause,arrayOf() )
        db.close()
    }


    companion object{
        private val DB_NAME = "METER_READ_TESTING"
        private val DB_VER = 1
        val ID_COL = "id"

        val TABLE_TEST_NAME = "tests"
        val TABLE_QUES_NAME = "questions"
        val EMP_NUM_COL = "employee_number"
        val NAME_COL = "name"
        val EMAIL_COL = "email"
        val NUM_QUES_COL = "number_of_questions"
        val NUM_DIFF_QUES_COL = "no_difficult_questions"
        val TEST_TYPE_COL = "test_type"
        val TIMED_BOOL_COL = "is_timed"

        val TEST_ID_COL = "test_id"
        val QUES_NUM_COL = "question_number"
        val NUM_DIALS_COL = "number_of_dials"
        val ANSWER_COL = "correct_answer"
        val DIAL_1_COL = "dial01"
        val DIAL_2_COL = "dial02"
        val DIAL_3_COL = "dial03"
        val DIAL_4_COL = "dial04"
        val DIAL_5_COL = "dial05"
        val IS_DIFF_COL = "is_difficult"
        val USER_ANSWER_COL = "user_answer"

    }
}
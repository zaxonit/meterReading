package com.example.zmetertesting01

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
               ANSWER_COL + " REAL," +
               DIFF_QUES_BOOL_COL + " INTEGER, " +
               DIFF_DIAL1_COL + " INTEGER, " +
               USER_ANSWER_COL + " INTEGER," +
               "FOREIGN KEY(" + TEST_ID_COL + ") REFERENCES " + TABLE_TEST_NAME + "(" + ID_COL + ") ON DELETE SET NULL" +
               ")"
               )
        db.execSQL(questionsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TEST_NAME")
        onCreate(db)
    }

    fun addTest(name: String, email: String, employeeNumber: Int, numberQues: Int, numberDiffQues: Int, testType: String, isTimed: Int) {
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
    }

    fun getName(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $TABLE_TEST_NAME", null)
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
        val DIFF_QUES_BOOL_COL = "is_difficult"
        val DIFF_DIAL1_COL = "difficult_dial01"
        val USER_ANSWER_COL = "user_answer"

    }
}
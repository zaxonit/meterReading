package com.zaxonit.MeterReadiness

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

class QuestionGenerator {
    var numQuestions: Int = 0
    var numDiffQuestions: Int = 0
    var numDblDiffQuestions: Int = 0
    var testType: String = ""
    var questions = mutableListOf<Double>()
    var questionDialCount = mutableListOf<Int>()
    var dial01 = mutableListOf<Double?>()
    var dial02 = mutableListOf<Double?>()
    var dial03 = mutableListOf<Double?>()
    var dial04 = mutableListOf<Double?>()
    var dial05 = mutableListOf<Double?>()
    var difficultDial01 = mutableListOf<Int>()
    var difficultDial02 = mutableListOf<Int>()

    private var orderedQuestions = mutableListOf<Double>()
    private var orderedQuestionDialCount = mutableListOf<Int>()
    private var randomQuestionIndex = mutableListOf<Int>()
    private var orderedDial01 = mutableListOf<Double?>()
    private var orderedDial02 = mutableListOf<Double?>()
    private var orderedDial03 = mutableListOf<Double?>()
    private var orderedDial04 = mutableListOf<Double?>()
    private var orderedDial05 = mutableListOf<Double?>()


    private var num4DialQuestions = 0
    private var num5DialQuestions = 0

    constructor(numQues: Int, numDiffQues: Int, testTyp: String) {
        numQuestions = numQues
        numDiffQuestions = numDiffQues
        num4DialQuestions = when(testTyp) {
            "4" -> numQuestions
            "5" -> 0
            "M" -> {
                val halfQues: Double = numQuestions/2.0
                ceil(halfQues).toInt()
            }
            else -> numQuestions
        }
        num5DialQuestions = numQuestions - num4DialQuestions

        for (q in 0 until numQuestions){
            var thisQuestion = 0.00
            var thisNumDials = 0
            if ((testTyp == "5") || ((q%2) != 0 && testTyp == "M")) {
                thisQuestion = get5Dial()
                thisNumDials = 5
            } else {
                thisQuestion = get4Dial()
                thisNumDials = 4
            }
            orderedQuestions.add(thisQuestion)
            orderedQuestionDialCount.add(thisNumDials)
            getDials(q, thisQuestion, thisNumDials)
        }


        for (r  in (numQuestions-1) downTo 0 step 1) {
            randomQuestionIndex.add(r)
        }
        randomQuestionIndex.shuffle()

        for (idx in randomQuestionIndex) {
            questions.add(orderedQuestions[idx])
            questionDialCount.add(orderedQuestionDialCount[idx])
            dial01.add(orderedDial01[idx])
            dial02.add(orderedDial02[idx])
            dial03.add(orderedDial03[idx])
            dial04.add(orderedDial04[idx])
            dial05.add(orderedDial05[idx])
        }

        testType = testTyp
    }

    private fun get4Dial(): Double {
        return Random.nextDouble(0.0, 9999.9)
    }

    private fun get5Dial(): Double {
        return Random.nextDouble(0.0, 99999.9)
    }

    private fun getDials(idx: Int, thisQuestion: Double, thisNumDials: Int) {
        // calculate and prepare variables for entry into questions tables
//            var thisCorrectAnswer: Int = thisQuestion.toInt()
//                var thisDivisor: Double = (10).toDouble().pow(thisNumDials-1)
        var thisDivisor: Double = 10.0
        if (thisNumDials == 5) {
            orderedDial05.add(thisQuestion-(floor(thisQuestion/thisDivisor) *thisDivisor))
            thisDivisor *= 10
        } else {
            orderedDial05.add(null)
        }
        orderedDial04.add((thisQuestion-(floor(thisQuestion/thisDivisor) *thisDivisor))/(thisDivisor/10))
        thisDivisor *= 10
        orderedDial03.add((thisQuestion-(floor(thisQuestion/thisDivisor) *thisDivisor))/(thisDivisor/10))
        thisDivisor *= 10
        orderedDial02.add((thisQuestion-(floor(thisQuestion/thisDivisor) *thisDivisor))/(thisDivisor/10))
        thisDivisor *= 10
        orderedDial01.add((thisQuestion-(floor(thisQuestion/thisDivisor) *thisDivisor))/(thisDivisor/10))
        thisDivisor *= 10

    }

}

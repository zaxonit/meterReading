package com.zaxonit.MeterReadiness

import kotlin.math.ceil
import kotlin.random.Random

class QuestionGenerator {
    var numQuestions: Int = 0
    var numDiffQuestions: Int = 0
    var testType: String = ""
    var questions = mutableListOf<Double>()
    var questionDialCount = mutableListOf<Int>()
    private var orderedQuestions = mutableListOf<Double>()
    private var orderedQuestionDialCount = mutableListOf<Int>()
    private var randomQuestionIndex = mutableListOf<Int>()
    var num4DialQuestions = 0
    var num5DialQuestions = 0

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

        for (q in 1.rangeTo(numQuestions)) {
            if ((testTyp == "5") || ((q%2) == 0 && testTyp == "M")) {
               orderedQuestions.add(get5Dial())
               orderedQuestionDialCount.add(5)
            } else {
               orderedQuestions.add(get4Dial())
               orderedQuestionDialCount.add(4)
            }
        }

        for (r  in (numQuestions-1) downTo 0 step 1) {
           randomQuestionIndex.add(r)
        }
        randomQuestionIndex.shuffle()

        for (idx in randomQuestionIndex) {
            questions.add(orderedQuestions[idx])
            questionDialCount.add(orderedQuestionDialCount[idx])
        }

        testType = testTyp
    }

    private fun get4Dial(): Double {
        return Random.nextDouble(0.0, 9999.0)
    }

    private fun get5Dial(): Double {
        return Random.nextDouble(0.0, 99999.0)
    }

}
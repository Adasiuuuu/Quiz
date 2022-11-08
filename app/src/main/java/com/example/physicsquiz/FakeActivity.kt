package com.example.physicsquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class FakeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fraud)
        val answer: TextView by lazy { findViewById(R.id.fraud_answer_text) }
        val quizData = intent.getParcelableExtra<QuestionsActivity.QuizState>("quiz_data")
        if(quizData != null){
            if (!quizData.correct_answer)
                answer.text = "YES"
            else
                answer.text = "NO"
        }
    }

    fun goBackToQuiz(view: View) {
        val quizData = intent.getParcelableExtra<QuestionsActivity.QuizState>("quiz_data")
        val overviewIntent = Intent(this, QuestionsActivity::class.java)
            .putExtra("quiz_data", QuestionsActivity.QuizState(
                quizData!!.points,
                quizData!!.correct,
                quizData!!.frauds,
                quizData!!.correct_answer,
                quizData!!.question_number
            )
            )
        startActivity(overviewIntent)
    }
}


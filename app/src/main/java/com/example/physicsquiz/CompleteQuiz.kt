package com.example.physicsquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class CompleteQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        val points: TextView by lazy { findViewById(R.id.points_text) }
        val correct: TextView by lazy { findViewById(R.id.correct_answers_text) }
        val frauds: TextView by lazy { findViewById(R.id.frauds_text) }
        val quizData = intent.getParcelableExtra<QuestionsActivity.QuizState>("quiz_data")
        if(quizData != null){
            points.text = "Points: ${quizData.points}"
            correct.text = "Correct answers: ${quizData.correct}"
            frauds.text = "Frauds: ${quizData.frauds}"
        }
    }

    fun backToMenu(view: View) {
        val mainMenuIntent = Intent(this, MainActivity::class.java)
        startActivity(mainMenuIntent)
    }
}
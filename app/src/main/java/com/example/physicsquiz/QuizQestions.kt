package com.example.physicsquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.CATEGORY_BROWSABLE
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.os.Parcelable




class QuestionsActivity() : AppCompatActivity(), Parcelable {
    class Question(
        val question: String,
        val correctAnswer: Boolean,
        val url: String?,
    )


class QuizState (val points: Int, val correct: Int, val frauds: Int, val correct_answer: Boolean, val question_number: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(points)
        parcel.writeInt(correct)
        parcel.writeInt(frauds)
        parcel.writeByte(if (correct_answer) 1 else 0)
        parcel.writeInt(question_number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuizState> {
        override fun createFromParcel(parcel: Parcel): QuizState {
            return QuizState(parcel)
        }

        override fun newArray(size: Int): Array<QuizState?> {
            return arrayOfNulls(size)
        }
    }
}

    val listOfQuestions = listOf(
    Question(
        "Is Wyspa Słodowa located in Wrocław?",
        true,
        "https://pl.wikipedia.org/wiki/Wyspa_Słodowa_we_Wrocławiu_(ulica)"
    ),
    Question(
        "Do you hear a lightning before it strikes?",
        false,
        "https://www.hko.gov.hk/en/education/weather/thunderstorm-and-lightning/00021-why-does-lightning-always-come-before-thunder.html"
    ),
    Question(
        "Is the black box in the plane is black?",
        false,
        "https://aviation.stackexchange.com/questions/2723/why-are-fdrs-called-black-boxes-when-they-are-actually-orange"
    ),
    Question(
        "Is coconut a nut?",
        false,
        "https://www.healthline.com/nutrition/is-a-coconut-a-fruit"
    ),
    Question(
        "Is there a cafeteria on WFiA?",
        true,
        "https://wfa.uni.wroc.pl/pl/co-robimy/"
    ),
    Question(
        "Was there ever a monkey in space?",
        true,
        "https://en.wikipedia.org/wiki/Monkeys_and_apes_in_space"
    ),
    Question(
        "Is unicorn a national animal of Scotland?",
        true,
        "https://www.nts.org.uk/stories/the-unicorn-scotlands-national-animal"
    ),
    Question(
        "Are you having a good time? (you get 10 points if you answer yes :) )",
        true,
        "https://media.makeameme.org/created/hey-good-choice.jpg"
    ),
)


    class Statistics (var correct: Int, var frauds: Int, var points: Int)
    private val gameStats = Statistics(0, 0, 0)
    private val questionNumberText: TextView by lazy { findViewById(R.id.question_number_text) }
    private val questionContentText: TextView by lazy { findViewById(R.id.question_content_text) }

    fun answerCheat(view: View) {
        this.gameStats.frauds += 1
        this.gameStats.points -= 15  // odejmowanie punktów za cheating
        Intent(this, FakeActivity::class.java)
            .putExtra(/* name = */ "quiz_data", /* value = */ QuizState(
                gameStats.points,
                gameStats.correct, gameStats.frauds,
                listOfQuestions[questionNumber - 1].correctAnswer,
                questionNumber,
            )
            ).also {
                startActivity(it)
            }
    }


    private fun answerQuestion(answer: Boolean){
        if (currentQuestion.correctAnswer == answer){
            gameStats.correct += 1
            gameStats.points += 10
        }
        questionNumber += 1
        if(questionNumber <= listOfQuestions.size){
            setQuestion()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion(){
        listOfQuestions[questionNumber-1].also { this.currentQuestion = it }
        questionNumberText.text = "Question $questionNumber"
        questionContentText.text = currentQuestion.question
    }
    private var questionNumber: Int = 1
    private var currentQuestion: Question = listOfQuestions[questionNumber-1]

    constructor(parcel: Parcel) : this() {
        questionNumber = parcel.readInt()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        findViewById<Button>(R.id.button_find_answer).setOnClickListener{
            val redirectIntent = Intent(Intent.ACTION_VIEW, Uri.parse(listOfQuestions[questionNumber - 1].url)).apply {
                addCategory(CATEGORY_BROWSABLE)
            }
            if (redirectIntent.resolveActivity(packageManager) != null) {
                startActivity(redirectIntent)
            }
        }
        val quizMemory = intent.getParcelableExtra<QuizState>("quiz_data")
        if(quizMemory != null){
            this.questionNumber = quizMemory.question_number
            this.currentQuestion = listOfQuestions[questionNumber - 1]
            this.gameStats.points = quizMemory.points
            this.gameStats.frauds = quizMemory.frauds
            this.gameStats.correct = quizMemory.correct
        }
        setQuestion()
    }

    fun answerYes(view: View) {
        answerQuestion(true)
        if(questionNumber > listOfQuestions.size){
            val overviewIntent = Intent(this, CompleteQuiz::class.java)
                .putExtra("quiz_data", QuizState(gameStats.points, gameStats.correct, gameStats.frauds, listOfQuestions[questionNumber - 2].correctAnswer, questionNumber))
            startActivity(overviewIntent)
        }
    }

    fun answerNo(view: View) {
        answerQuestion(false)
        if(questionNumber > listOfQuestions.size){
            val overviewIntent = Intent(this, CompleteQuiz::class.java)
                .putExtra("quiz_data", QuizState(gameStats.points, gameStats.correct, gameStats.frauds, listOfQuestions[questionNumber - 2].correctAnswer, questionNumber))
            startActivity(overviewIntent)
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(questionNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionsActivity> {
        override fun createFromParcel(parcel: Parcel): QuestionsActivity {
            return QuestionsActivity(parcel)
        }

        override fun newArray(size: Int): Array<QuestionsActivity?> {
            return arrayOfNulls(size)
        }
    }
}
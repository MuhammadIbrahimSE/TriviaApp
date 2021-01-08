package com.example.triviaapp.ui

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.triviaapp.R
import com.example.triviaapp.model.QuestionAndAnswers
import com.example.triviaapp.utils.ViewUtils
import com.example.triviaapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_question.*
import java.util.*

@AndroidEntryPoint
class QuestionActivity : AppCompatActivity(), View.OnClickListener {


    private var isQuestionEnd = false
    private var totalPoint: Int = 0
    private var levelName: String? = ""
    private var type: String? = ""
    private var categoryId: Int = 0
    private lateinit var context: Context
    private lateinit var viewUtils: ViewUtils

    private lateinit var mainViewModel: MainViewModel
    private var questionIndex = 1
    private var amount = 10
    private lateinit var mQuestionList: ArrayList<QuestionAndAnswers>
    private val QUICK_MODE = "quick"
    private val SIMPLE_MODE = "simple"
    private var mode = SIMPLE_MODE
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        initialization()
        getIntentData()
        setOnClickListener()
    }



    private fun getIntentData() {
        if (intent != null) {
            mode = intent.getStringExtra("mode").toString()
            if (mode == SIMPLE_MODE) {
                categoryId = intent.getIntExtra("id", 0)
                levelName = intent.getStringExtra("level")
                type = intent.getStringExtra("type")
            } else {
                amount = 100
                tv_question_timer.visibility = View.VISIBLE
            }
            fetchQuestions()
        }
    }

    private fun fetchQuestions() {
        levelName = levelName?.toLowerCase(Locale.getDefault())
        Log.d("TAG_BODY", "categoryId: $categoryId level:  $levelName  type:  $type")
        mainViewModel.callQuestionApi(amount, categoryId, levelName!!, type!!)
        progress.visibility = View.VISIBLE
        cl_detail.visibility = View.GONE

        mainViewModel.getAllQuestions().observe(this, { response ->
            progress.visibility = View.GONE
            cl_detail.visibility = View.VISIBLE
            if (response != null) {
                mQuestionList = response as ArrayList<QuestionAndAnswers>

                if (mQuestionList.size > 0) {
                    val question = mQuestionList[0]
                    tv_question.text = question.question

                    if (question.type.equals("multiple")) {

                        val options = question.incorrect_answers
                        options.add(question.correct_answer)

                        option_1.text = options[0]
                        option_2.text = options[1]
                        option_3.text = options[2]
                        option_4.text = options[3]
                    } else {
                        option_1.text = "True"
                        option_2.text = "False"
                        option_3.visibility = View.GONE
                        option_4.visibility = View.GONE
                    }
                    countDownTimer()

                } else {
                    viewUtils.showToast("Sorry! No question found")
                    finish()
                }
            } else
                viewUtils.showToast(ViewUtils.API_ERROR_MESSAGE)
        })
    }

    private fun initialization() {
        context = this
        viewUtils = ViewUtils(context)
        mQuestionList = ArrayList()


        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun setOnClickListener() {
        btn_next.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_next -> {
                if (questionIndex <= mQuestionList.size) {


                    val selectedId: Int = radio.checkedRadioButtonId

                    val radioButton = findViewById<View>(selectedId) as RadioButton
                    val currentQuestion = mQuestionList[questionIndex - 1]
                    var isCorrect = false
                    val markedAnswer = radioButton.text.toString()
//                        if(type.equals("multiple")) {
                    if (markedAnswer.toLowerCase() == currentQuestion.correct_answer.toLowerCase())
                        isCorrect = true
//                        }
//                        else{
//                            if(markedAnswer.toLowerCase() == "True".toLowerCase())
//                                isCorrect = true
//                        }
                    //constant.showToast("" + radioButton.text)

                    option_1.isChecked = true
                    option_2.isChecked = false
                    option_3.isChecked = false
                    option_4.isChecked = false



                    totalPoint += when (currentQuestion.difficulty) {
                        "hard" -> {
                            1
                        }
                        "medium" -> {
                            2
                        }
                        else -> {
                            3
                        }
                    }

//                        localViewModel.getAllRecordFromLocal().observe(this, androidx.lifecycle.Observer { data ->
//                            Toast.makeText(this, "${data.size}", Toast.LENGTH_LONG).show()
//                        })

                    if (questionIndex < mQuestionList.size) {
                        val nextQuestion = mQuestionList[questionIndex]

                        tv_question.text = nextQuestion.question
                        if (nextQuestion.type.equals("multiple")) {
                            option_3.visibility = View.VISIBLE
                            option_4.visibility = View.VISIBLE

                            val options = nextQuestion.incorrect_answers
                            options.add(nextQuestion.correct_answer)

                            option_1.text = options[0]
                            option_2.text = options[1]
                            option_3.text = options[2]
                            option_4.text = options[3]
                        } else {
                            option_1.text = "True"
                            option_2.text = "False"
                            option_3.visibility = View.GONE
                            option_4.visibility = View.GONE
                        }

                        if (mode == QUICK_MODE)
                            countDownTimer()
                    }
                    questionIndex++

                    if (questionIndex <= mQuestionList.size)
                        tv_question_number.text = "Question: " + (questionIndex)
                    else
                        showDialogForPlayAgain()

                    tv_point.text = "Score: " + totalPoint

                } else {
                    showDialogForPlayAgain()
                    isQuestionEnd = true
                    viewUtils.showToast("Quiz ended!")
                }
            }


        }
    }

    private fun showDialogForPlayAgain() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(
            context,
            R.style.CustomAlertDialog
        )
        alertDialogBuilder.setTitle("Over!")
        alertDialogBuilder.setMessage("Do you want to solve more questions?")
        alertDialogBuilder.setPositiveButton("Yes") { dialogInterface, _ ->
            tv_question_number.text = "Question: " + (questionIndex)

            amount += 10
            fetchQuestions()
            dialogInterface.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialogInterface, _ ->
            if (countDownTimer != null)
                countDownTimer?.cancel()
            finish()
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        if (isQuestionEnd) {
            if (countDownTimer != null)
                countDownTimer?.cancel()
            super.onBackPressed()
        } else
            viewUtils.showToast("Please complete questionnaire")
    }

    private fun countDownTimer() {
        if (countDownTimer != null)
            countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_question_timer.text = "Seconds remaining: " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                tv_question_timer.text = "Seconds remaining: 5"
                btn_next.performClick()

            }
        }.start()

    }

}
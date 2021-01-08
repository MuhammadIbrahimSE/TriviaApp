package com.example.triviaapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.triviaapp.R
import com.example.triviaapp.utils.ViewUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var viewUtils: ViewUtils
    private var type: String=""
    private var levelName: String=""
    private var categoryId: Int=0
    private lateinit var context:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialization()
        setOnCLickListener()
        setCategoryViews()
        setLevelViews()
        setTypeViews()
    }

    private fun setCategoryViews() {
        val categoryList = arrayOf("Select Category","General Knowledge","Entertainment: Books","Entertainment: Film","Entertainment: Music",
            "Entertainment: Musicals & Theatres","Entertainment: Television","Entertainment: Video Games",
            "Entertainment: Board Games","Science & Nature","Science: Computers" )

        val categoryAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, categoryList)

        with(spinner_category){
            adapter = categoryAdapter
            prompt = getString(R.string.select_category)
            onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(position>0) {
                        var category = categoryList[position]
                        categoryId = position+8
                    }
                    else
                        categoryId=0
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            })
        }
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    }
    private fun setLevelViews() {
        val difficultyLevelList = arrayOf("Select Level","Easy","Medium","Hard")

        val questionLevelAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, difficultyLevelList)

        with(spinner_level){
            adapter = questionLevelAdapter
            prompt = getString(R.string.select_category)
            onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(position>0) {
                        levelName = difficultyLevelList[position]
                    }
                    else
                        levelName = ""
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            })
        }
        questionLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    }
    private fun setTypeViews() {
        val typeList = arrayOf("Select type","Multiple Choice","True / False" )

        val questionTypeAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, typeList)

        with(spinner_type){
            adapter = questionTypeAdapter
            prompt = getString(R.string.select_type)
            onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    type = if(position>0) {
                        if(position == 1)
                            "multiple"
                        else
                            "boolean"
                    } else
                        ""
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            })
        }
        questionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    }

    private fun initialization() {
        context = this
        viewUtils = ViewUtils(context)
    }

    private fun setOnCLickListener() {
        submit.setOnClickListener(this)
        btn_quick_mode.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.submit->{
                if(categoryId==0)
                    viewUtils.showToast("please select category")
                else if (levelName.isEmpty())
                    viewUtils.showToast("please select level")
                else if(type.isEmpty())
                    viewUtils.showToast("please select type")
                else{
                     val intent:Intent = Intent(context,QuestionActivity::class.java)
                    intent.putExtra("id",categoryId)
                    intent.putExtra("level",levelName)
                    intent.putExtra("type",type)
                    intent.putExtra("mode","simple")

                    startActivity(intent)
                }
            }
            R.id.btn_quick_mode->{
               showAlertDialog()
            }
        }
    }
    private fun showAlertDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(
            context,
            R.style.CustomAlertDialog
        )
        alertDialogBuilder.setTitle("Alert!")
        alertDialogBuilder.setMessage("Remember you have only 5 seconds to answer each question")
        alertDialogBuilder.setPositiveButton("Start"){ dialogInterface, _ ->
            val intent:Intent = Intent(context,QuestionActivity::class.java)
            intent.putExtra("mode","quick")
            startActivity(intent)
            dialogInterface.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No"){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
package com.example.calc2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
//string where library counts the result of operations
    var currentOperation: String = ""

    fun inputButtonNum(num: String):Unit{
        val str = curNumber.text.toString()

       if(currentOperation.length > 0 && (currentOperation[currentOperation.lastIndex] == '-' || currentOperation[currentOperation.lastIndex] == '*' || currentOperation[currentOperation.lastIndex] == '/' || currentOperation[currentOperation.lastIndex] == '+')){
           curNumber.text = ""
       }

        if(curNumber.text == "ERROR"){
            curNumber.text = ""
        }

        if(str.length < 10) {
            if (str == "-" || str == "+" || str == "/" || str == "X") {
                curNumber.text = ""
            }
            if (str == "0") {
                if(num != ".") {
                    curNumber.text = num
                    if(currentOperation.isEmpty()) {
                        currentOperation = num
                    }
                    return
                }
                curNumber.text = curNumber.text.toString() + num
                currentOperation += num
                return
            }
            if(num != ".") {
                currentOperation += num
                curNumber.text = curNumber.text.toString() + num
            }
            else{
                if(str.length > 0 && str[str.lastIndex] == '.'){
                    return
                }
                curNumber.text = curNumber.text.toString() + num
                currentOperation += num
            }
        }
    }


    fun inputButtonOperator(op: Char){
        buttonEquals.callOnClick()

        if(currentOperation.length > 0) {
            val last: Char = currentOperation[currentOperation.length - 1]
            if (last == '-' || last == '+' || last == '/' || last == '*') {
                currentOperation = currentOperation.substring(0, currentOperation.length - 1) + op
            } else {
                currentOperation += op
            }
        }
        else
        {
            currentOperation = "0" + op
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        setContentView(R.layout.activity_main)


        button0.setOnClickListener{ inputButtonNum("0") }
        button1.setOnClickListener{ inputButtonNum("1") }
        button2.setOnClickListener{ inputButtonNum("2") }
        button3.setOnClickListener{ inputButtonNum("3") }
        button4.setOnClickListener{ inputButtonNum("4") }
        button5.setOnClickListener{ inputButtonNum("5") }
        button6.setOnClickListener{ inputButtonNum("6") }
        button7.setOnClickListener{ inputButtonNum("7") }
        button8.setOnClickListener{ inputButtonNum("8") }
        button9.setOnClickListener{ inputButtonNum("9") }
        buttonComma.setOnClickListener{ inputButtonNum(".") }

        buttonDivide.setOnClickListener{ inputButtonOperator('/') }
        buttonPlus.setOnClickListener{ inputButtonOperator('+') }
        buttonMultiply.setOnClickListener{ inputButtonOperator('*') }
        buttonMinus.setOnClickListener{ inputButtonOperator('-') }


        buttonPercent.setOnClickListener{
            if(curNumber.text != "0") {
                val recentLen: Int = curNumber.text.length
                var temp: Double = curNumber.text.toString().toDouble() * 0.01
                curNumber.text = temp.toString()
                currentOperation = currentOperation.substring(0,
                    currentOperation.length - recentLen
                ) + temp.toString()
            }
        }


        buttonAC.setOnClickListener{
            curNumber.text = "0"
            currentOperation = ""
        }


        buttonEquals.setOnClickListener{
            if(currentOperation.length > 1){
                if(currentOperation[currentOperation.length - 2] == '/' && currentOperation[currentOperation.length - 1] == '0'){
                    curNumber.text = "ERROR"
                    currentOperation = ""
                }
            }
            try {
                val ex = ExpressionBuilder(currentOperation).build()
                val result = ex.evaluate()

                val longResult = result.toLong()
                if(result == longResult.toDouble()){
                    currentOperation = longResult.toString()
                    curNumber.text = currentOperation
                }
                else{
                    currentOperation = result.toString()
                    curNumber.text = currentOperation
                }
            } catch (e:Exception) {
                Log.d("ERROR", "message:${e.message}")
            }
        }


        buttonSign.setOnClickListener{
            val temp: String = curNumber.text.toString()
            if(temp[0] == '-'){
                curNumber.text = temp.substring(1, temp.length)
                currentOperation = currentOperation.substring(0, currentOperation.length - temp.length) + curNumber.text.toString()
            }
            else{
                curNumber.text = "-" + temp
                currentOperation = currentOperation.substring(0, currentOperation.length - temp.length) + curNumber.text.toString()
            }
        }
    }
}
package com.startproject.startkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var linearLayout = LinearLayout(this)
        var layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        linearLayout.layoutParams = layoutParams

        var textView = TextView(this)
        textView.text = "Text"

        var textLayoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        textView.layoutParams = textLayoutParams
        linearLayout.addView(textView)

        setContentView(linearLayout)
    }

}

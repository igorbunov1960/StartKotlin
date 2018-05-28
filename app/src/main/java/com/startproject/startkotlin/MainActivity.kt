package com.startproject.startkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val btnView by lazy { btnActTwo }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnView.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ActivityTwo::class.java)
            startActivity(intent)
        })
    }

}

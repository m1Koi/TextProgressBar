package com.m1ku.textprogressbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.setBottomText(listOf("0", "20", "40"))
        progressBar.setTopText(listOf("2.0%", "2.5%"))
        progressBar.setMaxProgress(40f)
        progressBar.setCurrentProgress(35f)

        progressBar2.setBottomText(listOf("0", "20", "40", "60", "80"))
        progressBar2.setTopText(listOf("2.0%", "2.5%", "3.0%", "3.5%"))
        progressBar2.setMaxProgress(80f)
        progressBar2.setCurrentProgress(55f)

        progressBar3.setBottomText(listOf("0", "20", "40", "60", "80"))
        progressBar3.setTopText(listOf("2.0%", "2.5%", "3.0%", "3.5%"))
        progressBar3.setMaxProgress(80f)
        progressBar3.setCurrentProgress(28f)
    }
}

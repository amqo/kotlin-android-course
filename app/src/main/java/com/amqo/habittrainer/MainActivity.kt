package com.amqo.habittrainer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // lateinit means this variable does not need to be init at this point
    private lateinit var tvDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDescription = findViewById(R.id.tv_description)
        tvDescription.text = getString(R.string.water_description)
    }
}

package com.app.farm.appUI.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.app.farm.R

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.main_content) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }
}
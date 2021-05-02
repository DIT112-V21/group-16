package com.example.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.inflate
import com.example.firstapp.databinding.ActivityCleaningOptionBinding
import com.example.firstapp.databinding.ActivityCleaningOptionBinding.inflate
import com.example.firstapp.databinding.ActivityMainBinding

class CleaningOptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCleaningOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_cleaning_option)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        binding = inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.manual.setOnClickListener{
            binding.manual.text = ""
            startActivity(Intent(this,ManualOptionActivity::class.java))
        }
    }
}
package com.example.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firstapp.databinding.ActivityCleaningOptionBinding
import com.example.firstapp.databinding.ActivityCleaningOptionBinding.inflate

class CleaningOptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCleaningOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaning_option)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        binding = inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.manual.setOnClickListener{
            binding.manual.text = ""
            startActivity(Intent(this,ManualDrivingActivity::class.java))
        }

        binding.auto.setOnClickListener{
            binding.auto.text = ""
            startActivity(Intent(this,AutonomousActivity::class.java))
        }
    }
}
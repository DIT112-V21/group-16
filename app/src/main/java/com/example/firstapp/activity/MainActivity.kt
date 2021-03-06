package com.example.firstapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firstapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar!!.title = ""

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startCleaning.setOnClickListener {
            binding.startCleaning.text = "start"
            startActivity(Intent(this, CleaningOptionActivity::class.java))
        }
    }
}
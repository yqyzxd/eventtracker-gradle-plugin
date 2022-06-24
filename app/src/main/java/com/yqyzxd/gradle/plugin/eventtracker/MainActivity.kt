package com.yqyzxd.gradle.plugin.eventtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yqyzxd.event.Event

import com.yqyzxd.gradle.plugin.eventtracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)


            event()

        }

    @Event("001")
    private fun event() {
        println("MainAct event")
    }
}



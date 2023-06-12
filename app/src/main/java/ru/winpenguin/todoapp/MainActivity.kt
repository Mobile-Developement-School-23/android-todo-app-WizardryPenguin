package ru.winpenguin.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.winpenguin.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
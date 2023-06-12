package ru.winpenguin.todoapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.databinding.ActivityMainBinding
import ru.winpenguin.todoapp.main_screen.ui.MainScreenViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainScreenViewModel> {
        MainScreenViewModel.Factory
    }

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter = TodoAdapter { id, isChecked ->
            viewModel.changeCheckedState(id, isChecked)
        }
        binding.todoList.adapter = todoAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collect { screenState ->
                        todoAdapter.submitList(screenState.todoItems)
                    }
            }
        }
    }
}
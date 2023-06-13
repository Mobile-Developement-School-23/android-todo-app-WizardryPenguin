package ru.winpenguin.todoapp.main_screen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel by viewModels<MainScreenViewModel> {
        MainScreenViewModel.Factory
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoAdapter = TodoAdapter { id, isChecked ->
            viewModel.changeCheckedState(id, isChecked)
        }
        binding.todoList.adapter = todoAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collect { screenState ->
                        todoAdapter.submitList(screenState.todoItems)
                        binding.doneItemsSubtitle.text =
                            getString(R.string.done_count, screenState.doneItemsCount)
                    }
            }
        }

        binding.addTodoButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
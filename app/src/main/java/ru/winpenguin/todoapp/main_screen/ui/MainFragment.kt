package ru.winpenguin.todoapp.main_screen.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.MainActivity
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.databinding.FragmentMainBinding
import ru.winpenguin.todoapp.main_screen.ui.MainScreenEvent.ShowMessage

class MainFragment : Fragment() {

    private lateinit var viewModel: MainScreenViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoAdapter: TodoAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelFactory =
            (requireActivity() as MainActivity).activityComponent.viewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory)[MainScreenViewModel::class.java]
    }

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

        todoAdapter = TodoAdapter(
            onItemChecked = { id, isChecked ->
                viewModel.changeCheckedState(id, isChecked)
            },
            onItemClicked = { id ->
                openDetailsScreen(id)
            }
        )
        binding.todoList.adapter = todoAdapter
        initItemTouchHelper()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collect { screenState ->
                        todoAdapter.submitList(screenState.todoItems)
                        binding.doneItemsSubtitle.text =
                            getString(R.string.done_count, screenState.doneItemsCount)
                        binding.ivVisibility.setImageResource(screenState.visibilityImageRes)
                    }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events
                    .collect { event ->
                        when (event) {
                            is ShowMessage -> showSnackbar(getString(event.message))
                        }
                    }
            }
        }

        binding.ivVisibility.setOnClickListener {
            viewModel.changeItemsVisibility()
        }
        binding.addTodoButton.setOnClickListener {
            openDetailsScreen()
        }
    }

    private fun openDetailsScreen(id: String? = null) {
        val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(id)
        findNavController().navigate(action)
    }

    private fun initItemTouchHelper() {
        val swipeCallback = object : ItemSwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    LEFT -> {
                        val item = todoAdapter.getItem(viewHolder.adapterPosition)
                        viewModel.removeItem(item.id)
                    }

                    RIGHT -> {
                        val item = todoAdapter.getItem(viewHolder.adapterPosition)
                        viewModel.invertCheckedState(item.id)
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.todoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackbar.anchorView = binding.addTodoButton
        snackbar.show()
        viewModel.onMessageShown()
    }
}
package ru.winpenguin.todoapp.details_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.winpenguin.todoapp.MainActivity
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.databinding.FragmentDetailsBinding
import ru.winpenguin.todoapp.domain.models.Importance

class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsScreenViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val itemId: String?
        get() = arguments?.getString("id")

    private var isTextSet = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelFactory =
            (requireActivity() as MainActivity).activityComponent.viewModelFactory
        viewModel =
            ViewModelProvider(this, viewModelFactory)[DetailsScreenViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (savedInstanceState == null) {
            viewModel.updateCurrentItemId(itemId)
        }
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            viewModel.saveTodoItem(binding.todoText.text.toString())
            findNavController().popBackStack()
        }
        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.removeButton.setOnClickListener {
            viewModel.removeTodoItem()
            findNavController().popBackStack()
        }

        initSpinner()

        binding.todoText.doOnTextChanged { text, _, _, _ ->
            viewModel.textChanged(text)
        }

        binding.deadlineSwitch.setOnClickListener {
            when (viewModel.deadline) {
                null -> findNavController()
                    .navigate(DetailsFragmentDirections.actionDetailsFragmentToDatePickerFragment())

                else -> viewModel.clearDeadline()
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deadlineFlow
                    .collect { deadline ->
                        binding.deadlineSwitch.isChecked = deadline != null
                        binding.deadlineDate.isVisible = deadline != null
                        binding.deadlineDate.text = deadline
                    }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (!isTextSet) {
                        isTextSet = true
                        binding.todoText.setText(state.text)
                    }

                    val selectedPosition = when (state.importance) {
                        Importance.NORMAL -> 0
                        Importance.LOW -> 1
                        Importance.HIGH -> 2
                    }
                    binding.importanceSpinner.setSelection(selectedPosition)

                    binding.removeButton.isEnabled = state.isRemoveEnabled
                }
            }
        }
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.importances, R.layout.importances_initial_item
        )

        arrayAdapter.setDropDownViewResource(R.layout.importances_dropdown_item)
        binding.importanceSpinner.adapter = arrayAdapter
        binding.importanceSpinner.onItemSelectedListener = ImportanceSpinnerSelectionListener(
            requireContext()
        ) { position -> viewModel.changeImportance(position) }
    }
}
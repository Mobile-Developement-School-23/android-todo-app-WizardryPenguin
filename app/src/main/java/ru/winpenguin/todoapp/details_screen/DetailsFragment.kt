package ru.winpenguin.todoapp.details_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private val viewModel by viewModels<DetailsScreenViewModel> {
        DetailsScreenViewModel.Factory
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            viewModel.saveTodoItem(binding.todoText.text.toString())
            findNavController().popBackStack()
        }

        initSpinner()
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.importances,
            R.layout.importances_initial_item
        )
        arrayAdapter.setDropDownViewResource(R.layout.importances_dropdown_item)
        binding.importanceSpinner.adapter = arrayAdapter
        binding.importanceSpinner.onItemSelectedListener = ImportanceSpinnerSelectionListener(
            requireContext()
        ) { position -> viewModel.changeImportance(position) }
    }
}
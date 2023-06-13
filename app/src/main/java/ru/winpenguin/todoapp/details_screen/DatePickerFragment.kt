package ru.winpenguin.todoapp.details_screen

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ru.winpenguin.todoapp.domain.models.Deadline
import java.time.LocalDate

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val detailsViewModel by activityViewModels<DetailsScreenViewModel> {
        DetailsScreenViewModel.Factory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = when (val deadline = detailsViewModel.deadline) {
            is Deadline.NotSelected -> LocalDate.now()
            is Deadline.Selected -> deadline.date
        }

        val dialog = DatePickerDialog(
            requireContext(),
            this,
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )
        dialog.datePicker.minDate = System.currentTimeMillis()
        return dialog
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val deadline = LocalDate.of(year, month + 1, dayOfMonth)
        detailsViewModel.selectDeadline(Deadline.Selected(deadline))
    }

    override fun onCancel(dialog: DialogInterface) {
        detailsViewModel.cancelDeadlineSelection()
    }
}
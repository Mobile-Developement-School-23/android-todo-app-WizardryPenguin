package ru.winpenguin.todoapp.details_screen

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import java.time.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val detailsViewModel by activityViewModels<DetailsScreenViewModel> {
        DetailsScreenViewModel.Factory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val localDate = when (val deadline = detailsViewModel.deadline) {
            null -> LocalDate.now()
            else -> deadline.atZone(ZoneId.systemDefault()).toLocalDate()
        }

        val dialog = DatePickerDialog(
            requireContext(),
            this,
            localDate.year,
            localDate.monthValue - 1,
            localDate.dayOfMonth
        )
        dialog.datePicker.minDate = System.currentTimeMillis()
        return dialog
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val deadline = createInstant(year, month, dayOfMonth)
        detailsViewModel.selectDeadline(deadline)
    }

    private fun createInstant(
        year: Int,
        month: Int,
        dayOfMonth: Int
    ): Instant {
        val localDate = LocalDate.of(year, month + 1, dayOfMonth)
        val localTime = LocalTime.of(0, 0, 0)
        val zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault())
        return Instant.from(zonedDateTime)
    }

    override fun onCancel(dialog: DialogInterface) {
        detailsViewModel.cancelDeadlineSelection()
    }
}


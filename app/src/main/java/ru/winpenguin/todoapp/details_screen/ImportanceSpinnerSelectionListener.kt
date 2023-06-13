package ru.winpenguin.todoapp.details_screen

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.winpenguin.todoapp.R

class ImportanceSpinnerSelectionListener(
    private val context: Context,
    private val onSelectionChanged: (Int) -> Unit,
) : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val color = when (position) {
            0 -> ContextCompat.getColor(context, R.color.label_tertiary)
            1 -> ContextCompat.getColor(context, R.color.label_primary)
            2 -> ContextCompat.getColor(context, R.color.red)
            else -> ContextCompat.getColor(context, R.color.label_primary)
        }
        (view as? TextView)?.setTextColor(color)
        onSelectionChanged(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
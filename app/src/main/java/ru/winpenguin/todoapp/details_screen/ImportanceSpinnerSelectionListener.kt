package ru.winpenguin.todoapp.details_screen

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.utils.getColorFromAttr

class ImportanceSpinnerSelectionListener(
    private val context: Context,
    private val onSelectionChanged: (Int) -> Unit,
) : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val color = when (position) {
            0 -> context.getColorFromAttr(R.attr.labelTertiary)
            1 -> context.getColorFromAttr(R.attr.labelPrimary)
            2 -> context.getColorFromAttr(R.attr.red)
            else -> context.getColorFromAttr(R.attr.labelPrimary)
        }
        (view as? TextView)?.setTextColor(color)
        onSelectionChanged(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
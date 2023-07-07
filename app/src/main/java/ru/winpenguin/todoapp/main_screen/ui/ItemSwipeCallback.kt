package ru.winpenguin.todoapp.main_screen.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.winpenguin.todoapp.R
import ru.winpenguin.todoapp.utils.getColorFromAttr

/**
 * Реализация свайпа удаления и отметки "выполнено"
 */
abstract class ItemSwipeCallback(
    context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete)!!
    private val deleteIntrinsicWidth = deleteIcon.intrinsicWidth
    private val deleteIntrinsicHeight = deleteIcon.intrinsicHeight
    private val redColor = context.getColorFromAttr(R.attr.red)

    private val checkIcon = ContextCompat.getDrawable(context, R.drawable.check)!!
    private val checkIntrinsicWidth = checkIcon.intrinsicWidth
    private val checkIntrinsicHeight = checkIcon.intrinsicHeight
    private val greenColor = context.getColorFromAttr(R.attr.green)

    private val background = ColorDrawable()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val view = viewHolder.itemView
        val height = view.bottom - view.top
        val isCanceled = dX.compareTo(0f) == 0 && !isCurrentlyActive

        if (isCanceled) {
            return super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }

        if (dX < 0) {
            drawDeleteState(view, dX, c, height)
        } else {
            drawChangeCheckedState(view, dX, c, height)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawChangeCheckedState(
        view: View,
        dX: Float,
        c: Canvas,
        height: Int
    ) {
        background.color = greenColor
        val rightBound = view.right.coerceAtMost(view.left + dX.toInt())
        background.setBounds(view.left, view.top, rightBound, view.bottom)
        background.draw(c)

        val iconMargin = (height - checkIntrinsicHeight) / 2
        val iconTop = view.top + iconMargin
        val iconBottom = iconTop + deleteIntrinsicHeight
        val checkIconLeft1 = view.left + iconMargin
        val checkIconLeft2 = dX.toInt() - iconMargin - checkIntrinsicWidth
        val iconLeft = checkIconLeft1.coerceAtLeast(checkIconLeft2)
        val iconRight = iconLeft + checkIntrinsicWidth

        checkIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        checkIcon.draw(c)
    }

    private fun drawDeleteState(
        view: View,
        dX: Float,
        c: Canvas,
        height: Int
    ) {
        background.color = redColor
        val leftBound = (view.right + dX.toInt()).coerceAtLeast(view.left)
        background.setBounds(leftBound, view.top, view.right, view.bottom)
        background.draw(c)

        val iconMargin = (height - deleteIntrinsicHeight) / 2
        val iconTop = view.top + iconMargin
        val iconBottom = iconTop + deleteIntrinsicHeight
        val iconLeft1 = view.right - iconMargin - deleteIntrinsicWidth
        val iconLeft2 = leftBound + iconMargin
        val iconLeft = iconLeft1.coerceAtMost(iconLeft2)
        val iconRight = iconLeft + deleteIntrinsicWidth

        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        deleteIcon.draw(c)
    }
}
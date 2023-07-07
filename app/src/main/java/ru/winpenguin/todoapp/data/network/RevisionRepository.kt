package ru.winpenguin.todoapp.data.network

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class RevisionRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun setRevision(revision: Int) {
        sharedPreferences.edit {
            putInt(REVISION_KEY, revision)
        }
    }

    fun getRevision(): Int {
        return sharedPreferences.getInt(REVISION_KEY, 0)
    }

    companion object {
        private const val REVISION_KEY = "REVISION_KEY"
    }
}
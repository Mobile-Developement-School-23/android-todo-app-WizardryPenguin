package ru.winpenguin.todoapp.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class DeviceIdRepository(
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getDeviceId(): String {
        return withContext(ioDispatcher) {
            val deviceId = sharedPreferences.getString(DEVICE_ID_KEY, "")
            if (deviceId.isNullOrBlank()) {
                val newDeviceId = UUID.randomUUID().toString()
                sharedPreferences.edit {
                    putString(DEVICE_ID_KEY, newDeviceId)
                }
                newDeviceId
            } else {
                deviceId
            }
        }
    }

    companion object {
        private const val DEVICE_ID_KEY = "DeviceId"
    }
}
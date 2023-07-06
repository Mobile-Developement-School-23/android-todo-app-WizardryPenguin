package ru.winpenguin.todoapp.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.winpenguin.todoapp.di.IoDispatcher
import java.util.UUID
import javax.inject.Inject

class DeviceIdRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
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
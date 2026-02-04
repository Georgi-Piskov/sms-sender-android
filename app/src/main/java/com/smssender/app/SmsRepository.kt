package com.smssender.app

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SmsLogEntry(
    val phoneNumber: String,
    val message: String,
    val success: Boolean,
    val error: String? = null,
    val timestamp: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
)

object SmsRepository {
    private val _smsLogs = MutableStateFlow<List<SmsLogEntry>>(emptyList())
    val smsLogs: StateFlow<List<SmsLogEntry>> = _smsLogs.asStateFlow()

    private val _fcmToken = MutableStateFlow<String?>(null)
    val fcmToken: StateFlow<String?> = _fcmToken.asStateFlow()

    fun addSmsLog(phoneNumber: String, message: String, success: Boolean, error: String? = null) {
        val entry = SmsLogEntry(phoneNumber, message, success, error)
        _smsLogs.value = listOf(entry) + _smsLogs.value.take(49)
    }

    fun setFcmToken(token: String) {
        _fcmToken.value = token
    }
}

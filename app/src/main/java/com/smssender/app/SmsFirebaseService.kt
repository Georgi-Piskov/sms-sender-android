package com.smssender.app

import android.telephony.SmsManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SmsFirebaseService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "SmsFirebaseService"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "Message received from: ${message.from}")

        val phoneNumber = message.data["phone"]
        val smsText = message.data["message"]

        if (phoneNumber.isNullOrEmpty() || smsText.isNullOrEmpty()) {
            Log.e(TAG, "Missing phone or message in FCM data")
            return
        }

        sendSms(phoneNumber, smsText)
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager = getSystemService(SmsManager::class.java)
            val parts = smsManager.divideMessage(message)
            
            if (parts.size == 1) {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            } else {
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
            }
            
            Log.d(TAG, "SMS sent successfully to $phoneNumber")
            SmsRepository.addSmsLog(phoneNumber, message, true)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send SMS: ${e.message}")
            SmsRepository.addSmsLog(phoneNumber, message, false, e.message)
        }
    }
}

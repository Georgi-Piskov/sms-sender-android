# 📱 SMS Sender - Android App with n8n Integration

A complete solution for sending SMS messages from your Android phone via n8n workflow automation. Perfect for businesses that want to send automated SMS notifications without paying for expensive SMS gateway services.

## 🎯 Use Case

This project was built for **Rodopi Dent** dental clinic to send automated SMS notifications to patients:
- Booking confirmations
- Appointment reminders
- Booking rejections
- Custom messages

## 🏗️ Architecture

```
┌─────────────┐     ┌─────────────┐     ┌─────────────────┐     ┌──────────────┐
│   Website   │────▶│    n8n      │────▶│  Firebase FCM   │────▶│ Android App  │
│  (Booking)  │     │  Workflow   │     │  Push Service   │     │  (SMS Send)  │
└─────────────┘     └─────────────┘     └─────────────────┘     └──────────────┘
```

1. **Website** sends booking data to n8n webhook
2. **n8n** processes the request and sends FCM push notification
3. **Firebase FCM** delivers the notification to the Android app
4. **Android App** receives notification and sends SMS via phone's SIM card

## 📦 Components

### 1. Android App
- Built with Kotlin & Jetpack Compose
- Receives Firebase Cloud Messaging (FCM) notifications
- Sends SMS using Android's SmsManager API
- Displays FCM token for configuration
- Shows SMS history log

### 2. n8n Workflow (`n8n/workflow.json`)
- Webhook endpoint for receiving SMS requests
- Bulgarian phone number formatting (+359)
- Pre-built message templates
- FCM v1 API integration with Google Service Account authentication

## 🚀 Quick Start

### Prerequisites
- Android phone with SIM card
- Self-hosted n8n instance
- Firebase project with Cloud Messaging enabled
- GitHub account (for CI/CD)

### Step 1: Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project (e.g., `sms-sender`)
3. Add an Android app with package name: `com.rodopident.smssender`
4. Download `google-services.json`
5. Enable Cloud Messaging API
6. Create a Service Account:
   - Go to Project Settings → Service Accounts
   - Generate new private key
   - Save the JSON file

### Step 2: Build Android App

#### Option A: GitHub Actions (Recommended)
1. Fork/clone the repository
2. Add secret `GOOGLE_SERVICES_JSON` with contents of `google-services.json`
3. Push to trigger build
4. Download APK from Actions artifacts or Releases

#### Option B: Android Studio
1. Clone the repository
2. Open in Android Studio
3. Place `google-services.json` in `app/`
4. Build → Build Bundle(s) / APK(s) → Build APK(s)

### Step 3: Install & Configure App

1. Install APK on your Android phone
2. Grant permissions:
   - SMS sending permission
   - Notification permission
3. Copy the FCM token from the app

### Step 4: n8n Setup

1. Import `n8n/workflow.json` into n8n
2. Create Google Service Account API credential:
   - Service Account Email: `your-service-account@project.iam.gserviceaccount.com`
   - Private Key: (from Firebase service account JSON)
   - Enable "Set up for use in HTTP Request node"
   - Allowed HTTP Request Domains: All
3. Update FCM token in the workflow (Send FCM node → JSON body → token)
4. Activate the workflow

### Step 5: Test

Send a POST request to your webhook:

```bash
curl -X POST https://your-n8n-instance/webhook/send-sms-android \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "0888123456",
    "template": "booking_confirmed",
    "date": "15.02.2026",
    "time": "14:00"
  }'
```

## 📝 API Reference

### Webhook Endpoint

**POST** `/webhook/send-sms-android`

#### Request Body

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `phone` | string | ✅ | Phone number (any format) |
| `message` | string | ❌ | Custom message text |
| `template` | string | ❌ | Template name (see below) |
| `date` | string | ❌ | Date for template |
| `time` | string | ❌ | Time for template |

#### Available Templates

| Template | Message (Bulgarian) |
|----------|---------------------|
| `booking_received` | Родопи Дент: Заявката ви за час на {date} в {time} е получена и чака одобрение. |
| `booking_confirmed` | Родопи Дент: Часът ви е ПОТВЪРДЕН! {date} в {time}. Очакваме ви! |
| `booking_rejected` | Родопи Дент: За съжаление не можем да потвърдим часа ви за {date} в {time}. |
| `reminder` | Родопи Дент: Напомняне - имате час утре ({date}) в {time}. Очакваме ви! |

#### Response

```json
{
  "success": true,
  "message": "SMS изпратен успешно",
  "phone": "+359888123456"
}
```

## 📁 Project Structure

```
sms-sender-android/
├── README.md                          # This file
├── n8n/
│   └── workflow.json                  # n8n workflow (import this)
├── app/
│   ├── src/main/
│   │   ├── java/com/rodopident/smssender/
│   │   │   ├── MainActivity.kt        # Main UI
│   │   │   ├── SmsFirebaseService.kt  # FCM handler
│   │   │   └── SmsRepository.kt       # State management
│   │   ├── AndroidManifest.xml        # Permissions
│   │   └── res/values/                # Resources
│   └── build.gradle.kts               # App config
├── build.gradle.kts                   # Project config
├── settings.gradle.kts
└── .github/
    └── workflows/
        └── build.yml                  # CI/CD pipeline
```

## 🔒 Permissions

The Android app requires:
- `SEND_SMS` - To send SMS messages
- `POST_NOTIFICATIONS` - To receive FCM notifications
- `INTERNET` - For FCM communication
- `FOREGROUND_SERVICE` - For reliable SMS delivery

## ⚙️ Configuration

### Updating FCM Token

If you reinstall the app or the token changes:
1. Open the app and copy the new FCM token
2. In n8n, edit the "Send FCM" node
3. Update the `token` field in the JSON body
4. Save and test

### Adding New Templates

Edit the "Prepare SMS" code node in n8n:

```javascript
const templates = {
  'your_template': `Your message with {placeholder}`,
  // ... existing templates
};
```

## 🐛 Troubleshooting

### App doesn't receive notifications
- Check if the FCM token is correct in n8n
- Verify Google Service Account credentials
- Ensure the app has notification permissions
- Check if battery optimization is disabled for the app

### SMS not sending
- Verify SMS permission is granted
- Check if the phone has active SIM card
- Look at the app's SMS log for errors

### n8n credential errors
- Ensure Private Key has proper line breaks
- Enable "Set up for use in HTTP Request node"
- Set "Allowed HTTP Request Domains" to "All"

## 📄 License

This project is provided as-is for educational and personal use.

## 🙏 Credits

Built with:
- [Kotlin](https://kotlinlang.org/) & [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)
- [n8n](https://n8n.io/) workflow automation
- [GitHub Actions](https://github.com/features/actions) for CI/CD

---

**Made for Rodopi Dent 🦷**
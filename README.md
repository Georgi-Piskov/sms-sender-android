# SMS Sender Android App

Android app that receives FCM push notifications from n8n and sends SMS messages.

## Features

- Receives Firebase Cloud Messaging (FCM) push notifications
- Automatically sends SMS to specified phone number
- Shows FCM token for n8n configuration
- Displays SMS sending history

## Setup

### 1. Firebase Setup

1. Create a Firebase project at https://console.firebase.google.com/
2. Add an Android app with package name `com.smssender.app`
3. Download `google-services.json` and place it in `app/` folder
4. Get your Firebase Server Key from Project Settings → Cloud Messaging

### 2. Build APK

APK is automatically built on every push via GitHub Actions.
Download from [Releases](../../releases).

### 3. Install on Phone

1. Download APK from Releases
2. Enable "Install from unknown sources" on your phone
3. Install the APK
4. Grant SMS and Notification permissions
5. Copy the FCM token displayed in the app

### 4. n8n Workflow

Use HTTP Request node to send FCM message:

```
POST https://fcm.googleapis.com/fcm/send

Headers:
  Authorization: key=YOUR_SERVER_KEY
  Content-Type: application/json

Body:
{
  "to": "FCM_TOKEN_FROM_APP",
  "data": {
    "phone": "+359888123456",
    "message": "Your SMS text here"
  }
}
```

## License

MIT

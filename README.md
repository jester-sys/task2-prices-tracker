Android App – Real-Time Prices Tracker 
This Android application demonstrates a real-time price tracking system with secure login, biometric authentication, and live data visualization. The app is built in Java using MVVM architecture, Retrofit for networking, and integrates advanced Android features like EncryptedSharedPreferences, Room DB, and Firebase Cloud Messaging (FCM) for push notifications.

    ✅ Note: App name and icon are AI-generated placeholders for demo purposes.

✅ Features Implemented

    🔐 Login API Integration (POST https://api.prepstripe.com/login)

    📊 Real-Time Price API Integration (GET https://api.prepstripe.com/prices)

    🧬 Biometric Authentication for Secure Subsequent Access

    🔑 EncryptedSharedPreferences for Secure Token Storage

    🔔 Firebase Cloud Messaging (FCM) for Real-time Price Notifications

    📈 MPAndroidChart for Price Trend Graphs

    🔄 Live Price Updates in RecyclerView

    🚪 Logout Functionality with Token Clearance

    🌙 Dark Mode + Light Mode Support ✅

    🔍 Search and Category Filtering via Spinner ✅

    📶 Offline Mode using Room Database ✅

🌟 Bonus Features (Implemented)

    Dark Mode Support

        App automatically adapts to system theme. Enhances visual comfort during night use.

    Offline Mode with Room Database

        App caches previously fetched prices using Room. Works smoothly even without internet.

    Search/Filter Functionality

        Allows users to filter or search prices using categories via Spinner dropdown. Improves data usability.

🔗 API Endpoints Used

    Login
    POST https://api.prepstripe.com/login
    Payload: { "username": "user", "password": "pass" }

    Prices
    GET https://api.prepstripe.com/prices
    Requires: Authorization: Bearer <token>

🔐 Biometric Authentication Code

BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
    .setTitle("Biometric Login")
    .setSubtitle("Log in using your fingerprint")
    .setNegativeButtonText("Use Account Password")
    .build();

BiometricPrompt biometricPrompt = new BiometricPrompt(
    this, ContextCompat.getMainExecutor(this),
    new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(AuthenticationResult result) {
            // Proceed to Home
        }
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            // Handle error
        }
    }
);

biometricPrompt.authenticate(promptInfo);

🔐 EncryptedSharedPreferences Code

SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
    "secure_prefs",
    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
    context,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
);

// Store token
sharedPreferences.edit().putString("auth_token", token).apply();

// Retrieve token
String token = sharedPreferences.getString("auth_token", null);

🚪 Logout Functionality

new MaterialAlertDialogBuilder(context)
    .setTitle("Logout")
    .setMessage("Are you sure you want to logout?")
    .setIcon(R.drawable.ic_logout)
    .setPositiveButton("Yes", (dialog, which) -> {
        sharedPreferences.edit().clear().apply();
        // Navigate to Login
    })
    .setNegativeButton("No", null)
    .show();

🎨 UI Previews
🌙 Dark Theme

<img src="https://github.com/user-attachments/assets/47f11981-a720-4ec4-9776-add711e6cded" width="100"/> <img src="https://github.com/user-attachments/assets/c628dc53-f72c-4c53-9e8c-f59c24e4ec6f" width="100"/> <img src="https://github.com/user-attachments/assets/1a4646f7-8b75-4602-b68e-3813aba5c5c5" width="100"/> <img src="https://github.com/user-attachments/assets/ab0b64c9-b015-498f-9e13-c397e1b53e7d" width="100"/> <img src="https://github.com/user-attachments/assets/9b4088bc-0c62-4f9c-94b9-61c6196aabaa" width="100"/> <img src="https://github.com/user-attachments/assets/a3e08e9f-8699-4530-9a86-9b2bc8cff51c" width="100"/> <img src="https://github.com/user-attachments/assets/09c6812b-e697-4fc6-86c4-4edbe834c8dc" width="100"/>
☀️ Light Theme

<img src="https://github.com/user-attachments/assets/08fc17d7-03ce-4241-8eef-d672ba2bb0f2" width="100"/> <img src="https://github.com/user-attachments/assets/8ce05ed2-6c15-40af-be7c-1c915a24e340" width="100"/> <img src="https://github.com/user-attachments/assets/3caaa725-5223-4776-aacd-5cd3f712d07a" width="100"/> <img src="https://github.com/user-attachments/assets/6f217a21-090c-4816-b4fd-2be54e1d4a23" width="100"/> <img src="https://github.com/user-attachments/assets/2c01ac91-1689-42fc-962c-52cd44af795f" width="100"/> <img src="https://github.com/user-attachments/assets/a633cfc6-3ad0-48a6-9599-ac33d5d4ec28" width="100"/> <img src="https://github.com/user-attachments/assets/52c63100-a085-47c4-b917-3db7e3f2ee64" width="100"/>
🧰 Tech Stack

    Java

    MVVM Architecture

    Retrofit (API Calls)

    EncryptedSharedPreferences

    BiometricPrompt API

    MPAndroidChart (Price Visualization)

    Room DB (for Offline Mode)

    Firebase Cloud Messaging (FCM)

    Material Design Components

📂 Folder Structure

├── ui/
│   ├── LoginFragment.java
│   ├── HomeFragment.java
│   ├── BiometricActivity.java
├── viewmodel/
│   └── PriceViewModel.java
├── repository/
│   └── PriceRepository.java
├── model/
│   └── Price.java
├── network/
│   └── ApiService.java
└── utils/
    └── AuthManager.java

📦 APK & Build Instructions

    Clone the repo

    Open in Android Studio

    Sync Gradle & Run on Emulator or Device

    Test with dummy credentials provided by backend

    Login once and biometric will handle future access

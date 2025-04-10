 Android App – Real-Time Prices Tracker

This Android application is a complete solution for tracking live prices with secure access and dynamic data updates. It is designed using Java and follows the MVVM (Model-View-ViewModel) architectural pattern, ensuring clean separation of concerns and easy maintainability.

The app authenticates users securely using a login API with token-based authentication. Once logged in, the token is safely stored using EncryptedSharedPreferences to prevent unauthorized access. For enhanced security on subsequent launches, the app implements Biometric Authentication (Fingerprint) using the BiometricPrompt API, providing a seamless yet secure login experience.

Live price data is fetched from a remote server using Retrofit, and displayed in a RecyclerView that updates in real time. The app also visualizes historical and live price trends using MPAndroidChart, giving users an intuitive understanding of market behavior.

To ensure users are instantly notified of price changes, Firebase Cloud Messaging (FCM) is integrated for sending push notifications. The app also supports Dark Mode and Offline Mode using Room Database, so users can access previous data even without internet connectivity. Additionally, search and filter functionality allows users to find specific price items quickly.

Overall, this project is an ideal reference for developers looking to implement secure login flows, real-time data handling, biometric authentication, and modern Android UI/UX best practices in financial, crypto, or e-commerce related apps.

    ✅ Note: App name and icon are AI-generated placeholders for demo purposes.

✅ Features Implemented

     Login API Integration (POST https://api.prepstripe.com/login)

     Real-Time Price API Integration (GET https://api.prepstripe.com/prices)

     Biometric Authentication for Secure Subsequent Access

     EncryptedSharedPreferences for Secure Token Storage

     Firebase Cloud Messaging (FCM) for Real-time Price Notifications

     MPAndroidChart for Price Trend Graphs

     Live Price Updates in RecyclerView

     Logout Functionality with Token Clearance

     Dark Mode + Light Mode Support ✅

     Search and Category Filtering via Spinner ✅

     Offline Mode using Room Database ✅

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
    

##  Biometric Authentication Code

```java
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
```

## EncryptedSharedPreferences Code

```java
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
```

## Logout Functionality

```java
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
```

🎨 UI Previews

🌙 Dark Theme

<img src="https://github.com/user-attachments/assets/c4d468d6-88fa-4974-96cd-fe78c0e106aa" width="100"/> <img src="https://github.com/user-attachments/assets/bdc2e206-4b5a-4b9c-93de-ae0e995df2d5" width="100"/> 
<img src="https://github.com/user-attachments/assets/ce464a42-ca65-4643-a293-b2f8c4c7ec33" width="100"/>
<img src="https://github.com/user-attachments/assets/9d96966c-6280-436e-a5f1-73d5214ff779" width="100"/> <img src="https://github.com/user-attachments/assets/7ce46f71-d3ac-4e2c-bed0-a54eaec96716" width="100"/> <img src="https://github.com/user-attachments/assets/f1fc478a-7ce4-4088-9b7c-1811f99c6d90" width="100"/> <img src="https://github.com/user-attachments/assets/cdbb45e1-4886-419d-99b2-229583af8138" width="100"/> <img src="https://github.com/user-attachments/assets/2099bc8a-397a-46fb-9a96-a6442a2c1a09" width="100"/>  <img src="https://github.com/user-attachments/assets/51f3f532-cb0f-453a-920f-d55c5ba589d2" width="100"/> 


Light Theme

<img src="https://github.com/user-attachments/assets/e37e07bb-56cc-4336-b58f-a007b38c8534" width="100"/> <img src="https://github.com/user-attachments/assets/ad3771bc-d53d-48a1-8173-59ce5b560752" width="100"/> <img src="https://github.com/user-attachments/assets/07bb135f-6319-4413-b75a-d1cfebddbe4d" width="100"/> <img src="https://github.com/user-attachments/assets/922862f2-a5d2-4884-8835-b5db459bc712" width="100"/> <img src="https://github.com/user-attachments/assets/26a949d5-44b4-4075-b4ad-dba49faf9795" width="100"/> <img src="https://github.com/user-attachments/assets/621526f1-dbbc-40c1-abcc-06b5e96703a7" width="100"/> 
<img src="https://github.com/user-attachments/assets/a24aea5f-7682-47ff-a906-3167f69dd4af" width="100"/>
<img src="https://github.com/user-attachments/assets/fbfd9195-bf3f-4002-9a04-3bdbf0f72ca6" width="100"/> <img src="https://github.com/user-attachments/assets/68f1b87c-f1d2-43ba-8147-c0a86f53610c" width="100"/> 


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
```

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
```

 APK & Build Instructions

    Clone the repo

    Open in Android Studio

    Sync Gradle & Run on Emulator or Device

    Test with dummy credentials provided by backend

    Login once and biometric will handle future access

# 📈 Price Tracker App

**Price Tracker** is a full-featured Android app that helps users securely track live price trends for various items or products. Built with modern Android components, it includes biometric login, real-time updates, dark mode, and cloud push notifications for a seamless and secure experience.

---

## 🚀 Project Overview

The app enables users to:

- Authenticate securely via API.
- Fetch and display live prices.
- View price changes over time via interactive charts.
- Receive push notifications when prices change.
- Visualize data trends and maintain offline access.
- Enjoy a modern, responsive UI in both Light and Dark modes.

---

## ✅ Features Implemented

### 🔐 **Authentication**
- Secure user login via `POST https://api.prepstripe.com/login`.
- Biometric Authentication (Fingerprint/Face Unlock) on app restart.

### 💹 **Price Display**
- Fetches prices from `GET https://api.prepstripe.com/prices`.
- Displays real-time price changes in a `RecyclerView`.
- Logout option to clear token and return to login.

### 📈 **Analytics**
- MPAndroidChart integration to visualize price change trends.
- Intuitive and clean graph representation.

### 🔔 **Notifications**
- Firebase Cloud Messaging (FCM) for push alerts on price updates.

### 🌓 **Bonus Features**
- ✅ Dark Mode: Auto-switch based on system theme.
- ✅ Offline Mode: Local storage of prices using Room Database.
- ✅ Search & Filter: Filter by name, category, price, and change percentage.

---

## 📦 APK Build Instructions

To build the APK:

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/price-tracker.git
   cd price-tracker
Open the project in Android Studio.

Build the APK:
Build > Build Bundle(s)/APK(s) > Build APK(s)

Find the APK at:
/app/build/outputs/apk/release/
🧪 Setup Instructions

    Open the project in Android Studio.

    Add your Firebase project (google-services.json in app/ folder).

    Run the app on a real/emulated Android device (API 23+).

    App auto-fetches and caches price data via API.

📂 Project Structure

📦 price-tracker
├── fragment/
├── viewmodel/
├── repository/
├── model/
├── adapter/
├── network/
├── utils/
├── res/
│   ├── layout/
│   ├── drawable/
│   ├── values/
│   ├── values-night/
└── AndroidManifest.xml

📥 APK Download

📌 Download Latest APK
🧠 Future Enhancements

    Add price alerts and user-configurable thresholds.

    In-app charts with historical data analysis.

    Multi-user support with user profiles.

    Export data as PDF or Excel.


# 🌍 Travel Companion Converter

A clean, modern, and crash-proof Android application designed for travelers to quickly convert **Currency**, **Fuel Efficiency/Distance**, and **Temperature** on the go. Built with Java and Material Design components.

## 🚀 Features

* **Real-time Category Selection**: Toggle between Currency, Fuel, and Temperature categories with a single tap.
* **Dynamic Unit Loading**: Spinners automatically update their options based on the selected category.
* **2026 Conversion Rates**: Uses fixed conversion factors optimized for 2026 travel scenarios.
* **Smart Validation**: 
    * Prevents crashes from empty or non-numeric inputs.
    * Blocks negative values for Fuel and Currency.
    * Allows negative values for Temperature (Climate Check).
    * Handles "Identity Conversions" (e.g., USD to USD) gracefully.
* **Responsive UI**: Built using `ConstraintLayout` to ensure a consistent look across various screen sizes.

---

## 🛠️ Technical Stack

* **Language**: Java
* **UI Framework**: XML (ConstraintLayout & Material Components)
* **Minimum SDK**: API 24 (Android 7.0)
* **Architecture**: Activity-based logic with modular conversion methods.

---

## 📸 UI Components

The app follows a single-screen layout for rapid access:
* **Category Buttons**: Material Outlined Buttons for high visibility.
* **Spinners**: Standard Android dropdowns for "From" and "To" selection.
* **TextInputEditText**: Modern Material design input with built-in error handling.
* **Result Display**: High-contrast, large-font `TextView` for easy reading.

---

## 📊 Supported Conversions

### 1. Currency (Fixed 2026 Rates)
| From | To | Rate |
| :--- | :--- | :--- |
| USD | AUD | 1.55 |
| USD | EUR | 0.92 |
| USD | JPY | 148.50 |
| USD | GBP | 0.78 |

### 2. Fuel Efficiency & Distance
* **mpg ↔ km/L**: 1 mpg = 0.425 km/L
* **Gallon ↔ Liter**: 1 Gallon = 3.785 Liters
* **Nautical Mile ↔ Kilometers**: 1 NM = 1.852 km

### 3. Temperature
* **Celsius ↔ Fahrenheit**
* **Celsius ↔ Kelvin**
* **Fahrenheit ↔ Kelvin**

---

## ⚙️ Installation

1.  Clone this repository or download the source code.
2.  Open the project in **Android Studio**.
3.  Sync Project with Gradle Files.
4.  Run on an Emulator or physical Android device.

---

## 📝 How to Use

1.  Select a **Category** (Currency, Fuel, or Temp) at the top.
2.  Choose your **Source Unit** from the "From" dropdown.
3.  Choose your **Target Unit** from the "To" dropdown.
4.  Enter the numeric value in the "Enter value" field.
5.  Tap **Convert** to see the result formatted to 2 decimal places.

---

## 🛡️ Error Handling
* **Empty Input**: Shows an "Enter a value" error on the text field.
* **Negative Values**: Blocks negative input for non-temperature categories via a `Toast` notification.
* **Invalid Format**: Uses a `try-catch` block to prevent crashes if unexpected characters are entered.

  

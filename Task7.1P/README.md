# Lost & Found App

## Features
- Create Lost / Found adverts with full details
- Upload an image from the device gallery
- View all items in a scrollable RecyclerView list
- Filter items by category (Electronics, Pets, Wallets, etc.)
- View full item details by tapping any row
- Delete adverts with a single tap
- Data persisted locally using SQLite

## Tech Stack
| Technology         | Purpose                            |
|--------------------|-------------------------------------|
| Java               | Primary programming language        |
| Android Studio     | IDE                                 |
| SQLite             | Local database for persisting data  |
| RecyclerView       | Efficient scrollable list display   |
| Glide              | Image loading and caching library   |
| Material Design    | UI components (TextInputLayout etc) |

## Project Structure
```
app/src/main/java/com.yourname.lostandfound/
├── MainActivity.java
├── CreateAdvertActivity.java
├── ListActivity.java
├── DetailActivity.java
├── DatabaseHelper.java
├── LostFoundItem.java
└── ItemAdapter.java
```

## How to Run
1. Clone this repository
2. Open in Android Studio
3. Let Gradle sync automatically
4. Run on emulator (API 21+) or physical Android device
5. Grant gallery permission when prompted

## Minimum Requirements
- Android SDK 21 (Android 5.0 Lollipop)
- Android Studio Hedgehog or later
- Gradle 8+

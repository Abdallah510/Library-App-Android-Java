# Library App — Android (Java)

An Android library management app built in Java with two separate user roles: students and librarians. Students can browse and reserve books while librarians manage the catalog, reservations, and announcements.

## How It Was Made

Built as a native Android app in Java using the Fragment-based navigation pattern with separate activity flows for students and librarians. Data is stored locally using SQLite via a custom `SQLiteOpenHelper`. The app also fetches external data (new arrivals/products) over HTTP using an `AsyncTask` with a JSON parser. Session persistence across app restarts is handled with `SharedPreferences`.

## Features

**Student side**
- Book catalog with search and filtering
- New arrivals feed
- Reserve books and view current reservations
- Borrowed books history
- Personal reading list
- Library info and announcements
- Profile management

**Librarian side**
- Add, edit, and manage books
- Manage student accounts
- Handle and approve reservations
- Post and manage announcements
- View reports
- Library info management
- Librarian registration

## Requirements

- Android Studio
- Android SDK (minSdk as configured in `build.gradle`)

## Running the Project

Open the project in Android Studio, sync Gradle, and run on an emulator or physical device.

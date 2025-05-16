#   Exam1_Frontend

Mobile application developed in Android using Jetpack Compose for the first exam.

------------

##   Description

This project is an Android mobile application built with Jetpack Compose. It consumes a REST API developed in .NET to manage **courses** and **students**. The app allows for the management of courses and the enrollment of students in a single course. It also features offline functionality using Room persistence and synchronization with the remote API. Firebase Cloud Messaging (FCM) is integrated for push notifications.

---

##   📱 Features

-   User interface built with **Jetpack Compose**.
-   Implements the **MVVM** architecture pattern.
-   Consumes a **REST API** using Retrofit and OkHttp.
-   Local data persistence using **Room** database.
-   **Firebase Cloud Messaging (FCM)** integration for push notifications.
-   **Offline functionality** allowing the app to work without an internet connection.
-   Data **synchronization** with the API upon reconnection.
-   Displays alerts when loading data from cache or the server.
-   Full **CRUD** operations for courses.
-   Displays a list of students enrolled in each course, including their name and email.
-   Detailed view for each student, showing their enrolled course.

---

##   💻 Technologies Used

-   [Android SDK](https://developer.android.com/studio)
-   [Kotlin](https://kotlinlang.org/)
-   [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   [Android Studio](https://developer.android.com/studio) or similar IDE.
-   **MVVM** Architecture
-   [Retrofit](https://square.github.io/retrofit/) for network requests.
-   [OkHttp](https://square.github.io/okhttp/) as the HTTP client for Retrofit.
-   [Room Persistence Library](https://developer.android.com/training/data-storage/room) for local database.
-   [Firebase Cloud Messaging (FCM)](https://firebase.google.com/docs/cloud-messaging) for push notifications.

---

##   🚀 Getting Started

Follow these steps to set up and run the project on your local machine:

1.  **Install Android Studio**:
    -   Download and install [Android Studio](https://developer.android.com/studio).

2.  **Clone the repository:**

    ```bash
    git clone https://github.com/AaronMatarrita/Exam1_Frontend.git
    ```

3.  **Open the project** in Android Studio:
    -   Launch Android Studio.
    -   Select "Open an existing project" and navigate to the cloned repository.

4.  **Verify the API Base URL (Conditional)**:
    -   The API base URL is set to `http://10.0.2.2:8080/` by default, which is suitable for running against a local server from an Android emulator.
    -   If you need to connect to a different server (e.g., a remote server or a local server on a physical device), locate the file where the base URL for the API is defined (e.g., in a `Constants.kt` file, a network service class, or `build.gradle` if using BuildConfig) and update it accordingly.

5.  **Firebase Configuration**:
    -   The necessary Firebase configuration files are already included in the project. No additional setup is required unless you intend to use a different Firebase project.

6.  **Build and Run the application**:
    -   Connect an Android device or start an emulator in Android Studio.
    -   Click the "Run" button (the green triangle) in the Android Studio toolbar.
    -   Select your connected device or emulator and click "OK".

---

##   📁 Project Structure

```none
Exam1_Frontend/
│
├── app/
│   ├── manifests/
│   │   └── AndroidManifest.xml
│   ├── kotlin+java/
│   │   └── com/
│   │       └── moviles/
│   │           └── exam/
│   │               ├── common/         #   Utility classes, extensions
│   │               ├── components/     #   Reusable UI components
│   │               ├── dao/            #   Data Access Objects for Room
│   │               ├── data/           #   Data sources (local and remote)
│   │               ├── models/         #   Data classes
│   │               ├── network/        #   Retrofit API service and related classes
│   │               ├── pages/          #   Screen composables
│   │               ├── services/       #   Application-level services (e.g., FCM)
│   │               ├── sync/           #   Data synchronization logic
│   │               ├── ui/             #   UI-related packages (theme)
│   │               │   └── theme/
│   │               ├── utils/          #   Utility functions
│   │               └── viewmodel/      #   ViewModels for the MVVM architecture
│   │               └── MainActivity.kt #   Main application activity
│   ├── res/
│   │   ├── ...         #   Resources (layouts, drawables, strings, etc.)
│   └── build.gradle    #   App-level Gradle build file
├── build.gradle        #   Project-level Gradle build file
├── gradle/
│   └── ...
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md           #   Project documentation
```

---

## 🤝 Authors

- **Yeiler Montes Rojas**  
  **GitHub:** [YeilerMR](https://github.com/YeilerMR)

- **Aaron Matarrita Portuguez**  
  **GitHub:** [AaronMatarrita](https://github.com/AaronMatarrita)

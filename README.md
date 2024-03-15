WeatherInfoApp
Welcome to WeatherInfoApp! This is a simple Android application built using Kotlin and following the MVVM architecture pattern. It utilizes Retrofit for network calls, Hilt for dependency injection, and GSON for JSON parsing. The app fetches weather information from the Open Meteo API.

Design Decisions
MVVM Architecture: The app follows the Model-View-ViewModel architecture pattern to separate concerns and make the codebase more maintainable and testable.
Retrofit: Retrofit is used for making network requests to fetch weather data from the Open Meteo API. It provides a clean and type-safe way to interact with RESTful APIs.
Hilt: Hilt is used for dependency injection, making it easier to manage dependencies and facilitate testing.
GSON: GSON is utilized for JSON parsing to deserialize the API responses into Kotlin objects.
Single Screen Design: The app has a simple design with only one screen containing a search bar at the top and current time and location details below it. This minimalist approach keeps the focus on the weather information.
Libraries Used
Retrofit: Link
Hilt: Link
GSON: Link
Instructions to Run the Application
Clone the repository: git clone <repository-url>
Open the project in Android Studio.
Build the project.
Run the application on an Android device or emulator.

Screenshots
<img src="https://github.com/Shashank02051997/WeatherApp-Android/blob/main/Screenshots/weather_app_screen_1.png" height="600" width="300" hspace="40">

Demo Video
Demo Video Link

Feel free to explore the code and reach out for any questions or suggestions!

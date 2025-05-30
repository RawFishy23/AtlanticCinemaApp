# AtlanticCinemaApp

This is a JavaFX application built as part of Assignment 2 for the IPRO002 course at UTS.

## ✅ Features

- JavaFX UI using `javafx.controls` and `javafx.fxml`
- Built and tested with Java 21 on macOS (Apple Silicon/M1)
- Simple movie and concessions ordering interface

## 🛠️ Setup Instructions

1. **Install Java 21**  
   Download from [Adoptium](https://adoptium.net/) or any preferred JDK 21 distribution.

2. **Download JavaFX SDK 23**  
   From the [Gluon website](https://gluonhq.com/products/javafx/).

3. **Extract the JavaFX SDK**  
   Place it somewhere on your system, e.g.,  
   `/Users/yourname/javafx-sdk-23/`

4. **Compile the project**  
   Run from the project root:

   ```bash
   javac --module-path /path/to/javafx-sdk-23/lib --add-modules javafx.controls,javafx.fxml -d bin src/*.java
   ```

```
AtlanticCinemaApp/
├── src/ # Source code (all .java files have 'package src;')
│ ├── AtlanticCinemaApp.java # Main application entry point
│ ├── AppController.java # Controller class for handling UI logic
│ ├── AppModel.java # Contains nested model classes (Movie, Order, etc.)
│ └── AppView.java # UI layout and JavaFX components
│
├── bin/ # Compiled .class files after building
│ └── src/ # Mirrors src/ with compiled .class files
│
├── .gitignore # Git ignore rules
├── README.md # Project documentation (this file)
```

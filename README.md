WordWizz: Guess the Word
An interactive and educational word puzzle game inspired by Wordle.

Overview
WordWizz is a Java-based word-guessing game designed to enhance vocabulary and cognitive skills. Players must guess a five-letter word within six attempts, receiving real-time, color-coded feedback that guides them toward the correct answer. The game offers a fun and mentally stimulating experience for players of all ages, featuring theme-based word categories and responsive gameplay.

Features
Word-Guessing Challenge: Guess a five-letter word with color-coded feedback (green: correct letter and position, yellow: correct letter but wrong position).
User-Friendly Interface: Simple, responsive design built using Java Swing.
Themed Categories: Choose from different themes like Animals, Fruits, and Vegetables.
Secure Login System: User authentication with a MySQL database.
API Integration: Fetches random five-letter words using an external dictionary API.
Game Modes: Random word generation or themed sections.
Score Tracking: Visual display of attempts and scores.
Restart Options: Play again or return to the main menu after the game ends.
Technology Stack
Programming Language: Java
IDE: IntelliJ IDEA
Framework: Java Swing (for GUI development)
Database: MySQL (via JDBC)
API: Random Word API
Version Control: Git
Installation
Clone the Repository:

bash
Copy code
git clone https://github.com/Om-Babar/WordWizz.git
cd WordWizz
Set Up the Database:

Create a MySQL database named wordwizz_db.
Import the wordwizz.sql script to set up the user authentication table.
Configure the Database Connection: Update the DatabaseConfig.java file with your MySQL username, password, and database details.

Run the Game:

Open the project in IntelliJ IDEA.
Run the WordWizzGUI.java file to start the application.
Usage
Login/Register: Create an account or log in to access the game.
Choose a Theme: Select a word category from the main menu.
Guess the Word: Enter your guesses and use color-coded hints to find the correct word.
Track Your Progress: View your remaining attempts and score during gameplay.
Screenshots

Login interface for WordWizz.


Gameplay with color-coded feedback.

Future Enhancements
AI-Driven Difficulty Levels: Adjust puzzle complexity based on player performance.
Gamification Features: Add achievements, badges, and rewards.
Multi-Language Support: Expand to support multiple languages.
Educational Collaboration: Partner with educational platforms for language learning integration.

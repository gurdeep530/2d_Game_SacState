# 🚁 Firefighting Helicopter Game

A single-player 2D simulation game developed in Java using the **Codename One** framework. The player controls a helicopter tasked with extinguishing fires in buildings while managing water and fuel resources. The game introduces a unique AI-controlled helicopter that supports the player by responding to user-marked fires.

## 🎮 Game Objective

The main goal is to put out all the fires represented by pink circles on the map. Fires break out in random buildings, and the player must fly the helicopter to collect water from the river and extinguish them. The game ends in one of two ways:
- **Victory**: All fires are put out.
- **Defeat**: The player runs out of helicopter fuel before completing the mission.

## 🧠 Highlight Feature: AI Helicopter
An AI-controlled helicopter spawns during gameplay. When the player clicks on a fire, the AI helicopter calculates the shortest route to the river, collects water, and flies to the selected fire to assist in putting it out. This demonstrates basic pathfinding and AI decision-making in a game setting.

## 🔧 Technologies Used
- Java
- Codename One framework
- Object-Oriented Programming
- Simple game physics and state management

## 🕹️ Controls

- Use arrow keys or on-screen buttons (Codename One simulator) to move the helicopter.
- Click on a fire to command the AI helicopter to assist.
- Monitor water and fuel levels during gameplay (displayed on screen).

## 📦 How to Run the Game

1. Make sure you have Java and [Codename One plugin](https://www.codenameone.com/) installed (e.g., in IntelliJ or NetBeans).
2. Clone or unzip the project folder.
3. Open the project in your IDE of choice with Codename One support.
4. Run the `Main` class to start the game on the simulator.

## 📝 Game Features

- Helicopter flight with limited fuel
- Water collection from river source
- Fire extinguishing mechanics
- AI helicopter pathfinding
- Win/loss condition logic
- Clean, modular object-oriented code


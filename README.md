# 🏝️ Island Ecosystem Simulator

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Multithreading](https://img.shields.io/badge/Concurrency-ReentrantLock-blue?style=flat-square)
![YAML](https://img.shields.io/badge/Config-YAML-green?style=flat-square)
![OOP](https://img.shields.io/badge/Architecture-Clean_Code-red?style=flat-square)

> A highly concurrent, data-driven simulation of an island ecosystem where plants grow, and animals move, eat,
> reproduce, and die in real-time.

## 📋 About The Project

This project is a multithreaded Java application that simulates the life cycle of a biological ecosystem. The island is
represented as a 2D grid of cells, where various species interact with each other. The core focus of this project is to
demonstrate advanced Java features, including **Multithreading**, **Reflection API**, and **Design Patterns**.

### ✨ Key Features

* **Concurrency & Thread Safety:** The simulation runs on a `ScheduledExecutorService`. Cell interactions are strictly
  thread-safe, utilizing `ReentrantLock` and `tryLock()` to prevent deadlocks and race conditions during cross-cell
  movements.
* **Data-Driven Design (YAML):** Animal characteristics (weight, speed, food probabilities) are not hardcoded. They are
  loaded dynamically from `.yaml` configuration files, making balancing and extending the game incredibly easy.
* **Design Patterns Implemented:**
    * **Factory Method & Prototype:** Used for the dynamic creation of organisms via reflection and
      custom `@GameObjectEntity` annotations.
    * **Facade (`DeathManager`):** Centralizes the logic for organism removal and statistics updating.
    * **Singleton:** Ensures thread-safe, global access to core managers (`CellManager`, `StatisticMonitor`).
* **Smart Statistics:** Real-time tracking of population changes, births, and deaths, outputted at the end of each
  simulation cycle.

## 🛠️ Built With

* **Java 17**
* **Multithreading** (`java.util.concurrent`)
* **Jackson** (for YAML parsing)
* **Lombok** (to reduce boilerplate code)
* **Maven**

## 🚀 Getting Started

Follow these steps to run the simulation locally.

### Prerequisites

* Java JDK 17 or higher
* Maven installed

### Installation & Execution

1. Clone the repository:
   ```sh
   git clone [https://github.com/your_username/island-simulation.git](https://github.com/your_username/island-simulation.git)
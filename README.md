# Moondream Container Test Project

This is a Maven project that demonstrates the use of Testcontainers and Ollama for testing a Docker container running the Moondream model. The project includes a single test class, `MoondreamContainerTest`, which performs an image recognition test by interacting with the Moondream model inside a Docker container.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 21 or higher installed.
- Apache Maven 3.6.0 or higher installed.
- Docker installed and running on your local machine.

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/moondream-container-test.git
    cd moondream-container-test
    ```

2. Build the project using Maven:

    ```bash
    mvn clean install
    ```

## Running Tests

To run the tests, use the following Maven command:

```bash
mvn test

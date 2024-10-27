# Codex Naturalis

### Project Overview

This university project is a digital adaptation of the Codex Naturalis board game, originally designed by Cranio Creations. It is a distributed game system where players can join through either a Graphical User Interface (GUI) or a Command Line Interface (CLI).

Our system supports multiple concurrent games, with each game capable of hosting 2 to 4 players. The software is designed using the MVC (Model-View-Controller) architecture, ensuring clean separation of game logic, user interaction, and network communication.

### Team
- [Mariano Christian](https://github.com/ChristianMariano4)
- [Meacci Alessandro](https://github.com/AlessandroMeacci)
- [Paganelli Andrea](https://github.com/andreeapaganelli)
- [Martini Simone](https://github.com/Simone1Martini)

### Project Evaluation

- **Grade**: 30L/30
- **Presentation Date**: 03/07/2024

### Functionalities
In addition to the core game mechanics of Codex Naturalis, the project incorporates several advanced features to enhance both functionality and user experience.
| Functionality         | Done |
|-----------------------|------|
| Simplified rules      | ✅    |
| Full rules            | ✅    |
| Socket                | ✅    |
| RMI                   | ✅    |
| TUI                   | ✅    |
| GUI                   | ✅    |
| Multiple games        | ✅    |
| Client disconnections | ✅    |
| Server disconnections | ❌    |
| Chat                  | ✅    | 

### Execution of the game
- Make sure the jars are downloaded with git LFS or from github directly
- Open a terminal tab in the ```/deliverables/jar/``` folder
- To execute the server paste this command in the terminal ```java -jar PSP45-1.0-SNAPSHOT-server.jar```
- To execute the client paste this command in the terminal ```java -jar PSP45-1.0-SNAPSHOT-client.jar```

### Project Documentation

All relevant documentation can be found within the `/deliverables` folder, including:

- **Cranio Website**: http://www.craniocreations.it/prodotto/codex-naturalis
- **UML Diagrams**: Under `/deliverables/uml`, you will find the Class and Sequence Diagrams detailing the network architecture and game flow.
- **JavaDoc**: JavaDoc documentation of the key classes and methods is located in `/docs/javadoc`.

### Disclaimer

**Codex Naturalis** is a trademarked board game developed by **Cranio Creations Srl**. All artwork, logos, and content used in this project were obtained with approval for educational purposes only. Any distribution, reproduction, or commercial use of these materials outside the context of this project is strictly prohibited.

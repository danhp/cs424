# Hus Player

> Final class project for [COMP 424](http://cs.mcgill.ca/~jpineau/comp424/) - Artificial Intelligence.

## Ant commands

* `clean`          - Delete the bin directory.
* `compile`        - Compile the code in release mode. Stores the class files in bin.
* `debug`          - Compile the code in debug mode. Stores the class files in bin.
* `student`        - Launch a client with the student player.
* `gui`            - Launch the server with the GUI enabled.
* `server`         - Launch the Server with no GUI, and the -k switch provided so the server restarts after each game.
* `autoplay`       - Run the Autoplay script. By default, runs 2 games. To run a different number of games, supply an argument like -Dn games=10 to the ant command.

---

## Playing a game

Launch a server with `ant gui` then 2 players with `ant student`.

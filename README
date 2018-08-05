# Robot Localisation

- Mini java project as part of a larger team project to enable a lego NXT robot to navigate a maze
- This code allows robot to determine where it is currently located, based on a previously given map

The map file was written by a team mate but is necessary to perform the localisation
There are two type of models that are used to calculate the current position of the robot on the map, an **action model** and a **sensor model**
The action model calculates the probability that the robot is in a current position X, given it's previous position Y, and the move that the robot just took
The sensor model calculates the probability that the robot is in a current position X, given the objects and walls that the robot can sense around it
The localiser uses both models whenever it needs to localise - it will create a map of probabilities for each position and then select the point with the highest probability to be the assumed position of the robot

# ShootingStar
Using C# Windows Form interface we give an option to the user to choose one of three colors (Red, Blue, Green). After that we send the choice to the Java server. Then in the Java app using OpenCV we take a photo of the balls and the robot sitting on the table in front of the laptop's camera. Then with OpenCV we process the image and find the position of the ball with the chosen color. After that we calculate the angle from the base of the robot to the ball and send this value to the C# app. Then the C# app converts the angles to robotic units (which are not equal to standard angle units) and send instructions to the robotic arm for how many units each motor should rotate.

Team members:
Viki Peeva,
Goce Cvetanov,
Goran Iliev.

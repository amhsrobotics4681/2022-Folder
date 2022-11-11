[FRC 2017 Game & Season Manual](https://firstfrc.blob.core.windows.net/frc2017/Manual/2017FRCGameSeasonManual.pdf)  
[FRC 2017 Game Animation Video](https://www.youtube.com/watch?v=EMiNmJW7enI)

Although we didn't get around to creating a code framework for the 2017 bot, I'll still write up a good approach for a general bot. Recall that our strategy was based on the following data:

* A robot may control only one gear at a time.
* A robot may control unlimited fuel at a time.
* Successive rotors require 1, 2, 4, and 6 gears to activate.
* A single rotor awards 40 points when activated. Activating all four awards 1 RP.
* 1/9 kPa per fuel in the lower boiler, and 1/3 kPa per fuel in the upper boiler.
* 1 kPa is equivalent to 1 point. Generating 40 kPa awards 1 RP.
* There is 600 fuel available on the field. There are 22 gears availble to each alliance.
* Climbing scores 50 points per robot.

Although we can handle multiple fuel at once, the point equivalent of activating a rotor is scoring 120 fuel in the upper boiler, or 360 in the lower boiler. This does have an attractive ranking point. If one robot focused solely on fuel, and were incredibly accurate, this might be possible. However, gears are more lucrative, and there are opportunities to play defense if all three alliance robots are gear-centered. That being said, the ability to score fuel can't be discarded because other teams are expected to follow this strategy, so to gain the upper hand in points, there should be some mechanism to score fuel. A mechanism to climb goes without saying.

The code then has the following framework:
```
src\main\java\frc\robot\
|-- autonomous
    |-- RightGear.java
    |-- CenterGear.java
    |-- LeftGear.java
|-- Robot.java
|-- FuelSystem.java
|-- Climber.java
```

Also part of kickoff is updating all the software. Luckily, all the links are provided for us, and we just need to know where to find them. Here are examples for 2022:
* [WPILib Installation](https://github.com/wpilibsuite/allwpilib/releases)
* [FRC Game Tools/NI Suite](https://www.ni.com/en-us/support/downloads/drivers/download.frc-game-tools.html#440024)
* [FRC Radio Configuration Utility](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-3/radio-programming.html)

A guide for offline installation can be found [here](https://docs.wpilib.org/en/latest/docs/zero-to-robot/step-2/offline-installation-preparations.html). You will not need to download anything at the moment.

We continue next week with control structures and logic, and if we have time, moving on to components.

Autonomous programming is not as scary as it might sound, especially with how we implement it. It helps to know what we need to do during autonomous of course, so consider the following before we write some code:

* 2017: Reach the base line (93.25 inches from alliance wall). Score fuel (wiffle balls) and gears.
* 2018: Reach the auto line (120 inches from alliance wall). Score power cubes (milk crates) on the scale and switch.
* 2019: Reach the hab line (94.25 inches from alliance wall). Score cargo (playground balls) and hatch panels (velcro plates).
* 2020: Drive off the initiation line (120 inches from alliance wall). Score power cells (dodgeballs) in the bottom, outer, or inner port.

I hope you see a pattern. The easiest way to score points in autonomous is get the robot to move. Once the robot moves around the field, it can start scoring for us. Each year has had a unique autonomous style, and I'm hoping to standardize it this year.

* In 2017, we selected our own autonomous based on where the robot started.
* In 2018, our autonomous was further selected by the field state at the start of the match. That is, the field was randomized.
* In 2019, autonomous was optional. There was a "sandstorm" instead, and blinds were lowered over the alliance station during autonomous. You could drive blind, or pre-program an autonomous.
* In 2020, we had a single automous for the majority of the competition, but developed it into four similar routines.

It is good to have multiple autonomous routines, but it takes a lot of testing. I'm thinking we have an `auto` folder so we don't clutter up the main folder, and we call the preference from our dashboard. I'm still fuzzy on how to implement it, and once the season starts, I don't want to write it for you. So, if you have ideas, let me know and we can work it out at a meeting!

For now, let's write some simple code. To get the robot to move, apply power to the motors. So, would this work?

```java
public void autonomousPeriodic() {
    m_drive.driveCartesian(0, 1, 0);
}
```

Technically yes, it will drive the robot at full speed into whatever its facing, be it a wall, a friendly robot, or an enemy robot (that's a penalty!). Yikes. Can we refine it?

```java
int counter;

public void autonomousInit() {
    counter = 0;
}

public void autonomousPeriodic() {
    counter++;
    while (counter < 100) {
        m_drive.driveCartesian(0, 1, 0);
    }
}
```

Alright, it now drives the robot at full speed for .... how many seconds? Well, 'periodic' runs at 50 Hz, and we run the counter under 100, so 100/50 = 2 seconds. That oughta be enough.... until the robot falls over from accelerating and decelerating suddenly. At this point, you might be frustrated with me for letting you plug a fork into an outlet twice. But this is the thought path you need to follow. People don't learn from conclusions, they learn by explanation.

Okay, well, that's the gist of it. Let's do some more advanced stuff now!

```java
Counter m_counter;

public void robotInit() {
    m_counter = new Counter(0); // DIO port
    counter = new Counter(Constants.kDIOLifterHeightSensor);
    counter.setMaxPeriod(1.0);
    counter.setSemiPeriodMode(true);
    counter.reset();
}

public double getDistance() {
    // getPeriod() returns 10 us/cm, so convert to inches
    return counter.getPeriod() * 100000 / 2.54;
}

public void autonomousPeriodic() {
    while (getDistance() > 12) {
        m_drive.driveCartesian(0, 0.7, 0);
    }
}
```

Now we're driving forward at half speed until we're 12 inches in front of an object. We can do something similar with a gyroscope or IMU: rotate until we hit an angle, then drive forward. An IMU can keep track of velocity too, but in our experience, it's been over-sensitive to noise. It would be better to use encoders on the drive train, but that is a bit beyond us right now.
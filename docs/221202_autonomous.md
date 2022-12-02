The only new gadget we used was a gyroscope, which had a function `getAngle()`. This does not make for accurate timing or movement because our cascading if-else statements are prone to two different variables. However, if we allow enough error to occur, this is not a large issue.

If we get better, we can explore more advanced autonomous routines. Our choice of TimedRobot restricts us as well. Had we used a CommandBased framework, we could schedule paths to run one after another, but the syntax and organization are much more difficult to grasp, and require time and effort to learn.

The example code today will be short and sweet, just a demonstration of how to add a gyro to autonomous.

```java
// Basic set-up statements
ADIS16470_IMU m_gyro = new ADIS16470_IMU();
PWMVictorSPX m_right = new PWMVictorSPX(0);
PWMVictorSPX m_left = new PWMVictorSPX(1);
int timer;
m_right.setInverted(true);

public void autonomousInit() {
    timer = 0;
    m_gyro.reset();
}

public void autonomousPeriodic() {
    timer++;
    if (timer < 50) {
        m_right.set(1);
        m_left.set(1);
    } else if (timer < 150 && m_gyro.getAngle() > -90) {
        m_left.set(0.5);
        m_right.set(-0.5)
    } else {
        m_left.set(0);
        m_right.set(0);
    }
}
```

---

To expand further on Kellen's idea of nested ArrayLists to create a series of autonomous paths, there's two things I will change after our conversation:
* Use a Queue of ArrayList\<Float\> instead. That way we don't need to loop through all the entries, just peek the first one and if the time is past, remove it from the queue.
* Implement a basic proportional response loop to turn the robot. That way we avoid a second angle/direction entry in the ArrayList that would describe the prior state of the robot. It may also be more precise.

What you could also do is define the angle as a condition in the Queue such that you remove it only after reaching the angle. However, there will need to be a way to correct overshooting before the robot continues to the next autonomous routine. I don't have a good idea for this.
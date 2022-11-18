Today mainly served as a review of pneumatics and cameras. If you haven't seen from [the previous document](/docs/221112_components1.md), please check it out. This document will cover how to effectively use solenoids within conditional structures, as well as limit switches.

There is code in the previous document concerning extending and retracting pistons. A piston is the visual output of the solenoid, which itself is just a magnetic switch. We primarily use solenoids to control the flow of air into pistons, so they extend and retract. Returning to the previous code, writing two buttons to control a single solenoid is wasteful, especially if we only need to switch between them. (An exception to this was our 2018 game, where dropping a game piece was a major disadvantage. To avoid accidental deployment, the extend/retract commands were separated.) So, how do we write this?

Recall first that the exclamation symbol (!) negates a boolean in Java. Is there a way to set a solenoid to the opposite of what it is? Indeed, the Solenoid class provides `.get()` and `.set()` methods. So,

```java
public void robotInit() {
    m_compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
    s_left = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
    m_compressor.enableDigital()
}

public void teleopPeriodic() {
    if (c_driver.getRawButton(5)) {
        s_left.set(!s_left.get());
    }
}
```
However, this actually won't work! Can you see why? What if I modify the code in `teleopPeriodic()` to be the following?

```java
if (c_driver.getRawButtonPressed(5)) {
    s_left.set(!s_left.get());
}
```
Now you can see the difference. Remember that we are in a periodic function. Had we kept the old code, we would have reversed the solenoid every 20 ms while button 5 (left bumper) was held down.

---
Next up, limit switches. Here we introduce a new import, `*.DigitalInput`. The limit switch is a simple on-off state that we read. We can plug it into any DIO port, instead of into the PWM ports. The limit switch is unique in having three prongs instead of two. This allows us to choose the switch to be 'normally on' or 'normally off'. That is, when the switch is unpressed, should it return `true` or `false`? Designer's choice.

It does not have the fancy capabilities of our Joystick buttons, but sometimes, we do need a `getPressed()` method. How can we do this? We take advantage of the periodic nature of our code:

```java
boolean flag = false;
DigitalInput dio = new DigitalInput(0);

public void teleopPeriodic() {
    if (dio.get && !flag) {
        flag = true;
    } else {
        flag = false;
    }

    if (flag) {
        // do something once when limit switch is pressed
    }
}
```

We could abstract this away in a custom class, and if we have many limit switches, we might do that just to keep the code clean. Our 2020 robot had two switches that both required this flag, but we didn't abstract it. Only because we lacked experience. In the future, we will be very competent programmers :sunglasses:.

Next meeting, we'll work with a gyroscope and start diving into autonomous programming. The heavier components will have to wait until we gain more familiarity with Java.

For now, you also have some "homework"! Please see [robot specs](docs/221117_challenge.md) for information.
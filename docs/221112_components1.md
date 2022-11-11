You may have a chance to be creative programmers in my absence! To my knowledge there's a leak in the pneumatics that prevents the air tanks from filling up. I can get that fixed next Thursday, as well as trying to sync the robot radio with my computer. In the mean time, I'll try to supply you with all the tools you need to experiment. Today would have covered working with pneumatics and a camera. This requires a few new imports:

```java
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor; 
import edu.wpi.first.wpilibj.Solenoid;
```

Cameras are plugged directly into the roboRIO's USB ports. Inside `robotInit()` add this line:
```java
CameraServer.startAutomaticCapture("camera name", 0);
```

You declare and initialize the compressor and solenoid just like the motor controllers and joysticks. Pay attention to if you need to add a parameter or not, as well as what the parameter means in the real world. You'll have to write the declarations yourself, but here's how to initialize them because it's slightly more complicated. Learn by reading this time.
```java
m_compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
s_right = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
s_left = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
m_compressor.enableDigital();
```

The prefixes are mostly arbitrary, but my reasoning is using 'm' with compressor to represent a unique member variable, and 's' to represent the solenoids. You could also use 'p' if we have a naming conflict, though I don't immediately see one. To experiment with objects, I recommend binding button presses to solenoids. You can set them to `true` or `false` to extend and retract them. For example,
```java
public void teleopPeriodic() {
    if (c_driver.getRawButton(1)) {
        s_right.set(true);
    }
    if (c_driver.getRawButton(2)) {
        s_right.set(false);
    }
}
```

There's a lot more I'm gonna be putting into this document, so here's a brief table of contents:

* [SendableChooser](#sendablechooser)
* [Primitives and Binary](#primitives-and-binary)
* [PID Loops](#pid-loops)
* [Vendor Libraries](#vendor-libraries)
* [DifferentialDrive](#differentialdrive)
* [Bonus: PID Math](#pid-math)

### SendableChooser
You might have noticed that the field is large. It is in fact 27' x 54' (though the actual playfield may be smaller because that's just the carpet size according to the game manual). Our robot can definitely start in different positions on the field then! How can we manage multiple autonomous routines? Enter `SendableChooser`. This allows you to map strings to objects, and retrieve your choice from the dashboard at the start of autonomous. Let's do a bit of example code:

```java
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot() {
    SendableChooser<String> m_chooser;
    String auto_choice;
    int timer;
    // Assume we have an `m_robot` object that controls the drivetrain.

    public void robotInit() {
        m_chooser = new SendableChooser<>();
        m_chooser.setDefaultOption("default", "Go forward.");
        m_chooser.addOption("right", "Turn right.");
        m_chooser.addOption("left", "Turn left.");
        SmartDashboard.put(m_chooser);
    }

    public void autonomousInit() {
        auto_choice = m_chooser.getSelected();
        timer = 0;
    }

    public void autonomousPeriodic() {
        timer++;
        switch (auto_choice) {
            case "right":
                if (timer < 50)
                    m_robot.drive(0.5, 0);
                else if (timer < 150)
                    m_robot.drive(0, 0.5);
                else
                    m_robot.drive(0, 0);
                break;
            case "left":
                if (timer < 100)
                    m_robot.drive(0.5, 0);
                else if (timer < 200)
                    m_robot.drive(0, -0.5);
                else
                    m_robot.drive(0, 0);
                break;
            case "default":
            default:
                if (timer < 100)
                    m_robot.drive(0.5, 0);
                else
                    m_robot.drive(0, 0);
                break;
        }
    }
}
```

Okay, that's a lot more than "a bit of example code." However, we can always shove the autonomous routines in separate files and call them individually, rather than stack them in a disturbing switch-case block. What's happening above? We create options and put them on the computer dashboard, and we can select one of the options before the game starts. Then, in `autonomousInit()`, we see what we chose, and `autonomousPeriodic()` runs the appropriate autonomous routine. It'll look like this on the dashboard:
```
 ------------------------------
|                              |
|   〇 Go forward.             |
|   ⦿ Turn right.             |
|   〇 Turn left.              |
|                              |
 ------------------------------
 ```
Okay, that wasn't too hard eh? I'll also point out something new: why did we enclose a type `String` in angle brackets when declaring `m_chooser`? It tells the compiler what type we're mapping strings to. We could just have easily said `SendableChooser<Integer> m_chooser`, and this would require something like `m_chooser.addOption("my favorite number", 42);`, and then 42 would be on the dashboard. But that's not quite as useful for telling what we're about to run.

### Primitives and Binary
Java has eight primitive types, and they have different sizes depending on what they need to hold. But first! Let's cover some simple binary.

Computer store numbers using only 1's and 0's, and the root of it is that capacitors have two states: charged and discharged. Unlike how we're used to seeing numbers, which is in base 10 (i.e. using ten digits), computers have to work in base 2.

For example, 352 is a base 10 number that means 3 hundreds, 5 tens, 2 ones. If we need to work in base two instead, a number like 1011 would be 1 eight, 0 fours, 1 two, 1 one. Each place value becomes a power of two instead of ten. Counting in binary is ridiculously easy too. Here's 0 through 15: 0000, 0001, 0010, 0011, 0100, 0101, 0110, 0111, 1000, 1001, 1010, 1011, 1100, 1101, 1110, 1111. For computers, each digit is a single bit. One bit stores either a 0 or a 1. Eight bits is equal to a byte. That means a byte stores 2^8 possible values!

| type | size |
|:----:|:----:|
| boolean | 1 bit |
| byte | 1 byte | 
| char | 2 bytes |
| short | 2 bytes |
| int | 4 bytes |
| long | 8 bytes |
| float | 4 bytes |
| double | 8 bytes |

If an `int` can store 32 bits, then shouldn't its maximum value be 2^32 - 1? (We subtract 1 because zero is a number). Well, what about the poor negative numbers? So then, we have to divide our range in half, yielding `int = [-2^31, 2^31-1] = [-2147483648, 2147483647]`. Other programming languages have something called **unsigned** integers, where the range is `[0, 2^32-1] = [0, 4294967295]` instead, but Java isn't special enough for that. Some C languages even implement a 128-bit unsigned integer (`max = 340282366920938463463374607431768211455`)!

A natural question you might have is, how do we represent negative numbers? The most common method is something called two's complement. This means that if you add a number and its negative, the binary representations of `N` bits will sum to `2^(N+1)`. For example, 3 = 0011, and -3 = 1101. Add them together and the binary will equal 10000 = 2^5 = 16. Naturally this doesn't happen because computers won't create the extra bit out of nowhere, so we have (1)0000 = 0 instead. Thus, math works!

### PID Loops

Let's say we have a target, and the robot needs to turn to face the target. Also, our robot has a vision system that can determine target offsets. Define our current heading as 0, and the target as 40 degrees to our left. That is, `a = -40`. How do we turn towards it until we face it, and then stop? Do we run a timer? No, because what if we're facing +20 instead? Then we'd land on -20.

Maybe, we turn based on our offset, so `m_robot.turn(target - current)`. Okay, so the robot turns at full power towards the target and completely overshoots it, so it turns the other way at full power, and completely overshoots it.

Maybe, we just do a fraction of our offset? If we plan on being simple, this will be good enough actually! Just requires some testing. However, we're not tame; we're really really brave! PID loops are a way to align a robot to a target efficiently. It stands for Proportional-Integral-Derivative. The proportional part, we've just discussed: a fraction of our current offset. The other two are a bit more fun. The integral is simply a running total of our error over time, and the derivative is the change in error between measurements. There's a good bit of mathematical theory behind this, but it requires calculus that's not for the faint of heart. It's at the very end if you want though. Here's code:

```java
double P = 0.05, I = 0.01, D = 0.2;
double prev_err, err;
double integral;
double target = -40;

public void PIDLoop() {
    prev_err = err;
    err = target - m_robot.getHeading();
    integral += err
    m_robot.set(P*err + I*integral + D*(err - prev_err));
}
```

The integral part is to keep nudging the robot towards the center when the proportional part doesn't provide enough '*oomph*'. The derivative part is to slow down the robot if we're turning too quickly. It can be mathematically shown that the ideal P value is `P = 2√(I(1+D))`. In physics speak, it's called critical damping.

### Vendor Libraries
Sometimes we use parts from third-party vendors, like our Talon motor controllers. They have their own libraries that would take advantage of fancy tools like encoders. I'm just going to redirect you to the [FRC documentation](https://docs.wpilib.org/en/stable/docs/software/vscode-overview/3rd-party-libraries.html), because it's better than me retyping instructions.

### DifferentialDrive
Say, driving our motors using one joystick per side makes for really choppy driving. What if we used a more intuitive control style? Enter arcade drive, where pushing the stick forward moves the robot forward, and pushing the stick sideways turns the robot. A differential drive just means one "wheel" on each side of the robot. You can think of tank treads as an infinite number of wheels if you want to. Here is [documentation](https://github.wpilib.org/allwpilib/docs/release/java/edu/wpi/first/wpilibj/drive/DifferentialDrive.html), and here is outline code:

```java
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;

public class Robot extends TimedRobot {
    PWMVictorSPX m_left, m_right;
    Joystick m_controller;
    DifferentialDrive m_drive;

    public void robotInit() {
        // Initialize m_left, m_right, m_controller
        m_drive = new DifferentialDrive(m_left, m_right)
    }

    public void teleopPeriodic() {
        m_drive.arcadeDrive(m_controller.getRawAxis(1), m_controller.getRawAxis(0));
    }
}
```

It's nothing you haven't really seen before. Not complicated at all. Go ahead and try implementing it!

### PID Math

Okay, the brave of you that want to see calculus...

Let $x(t)$ represent the current position/heading of the robot, and let $a$ represent the target value. Define a function $e(t) = a - x(t)$ that equals the error in our current position. We want to turn the robot to face the heading, but we can't just set its position. That'd be trivial. Instead, we have to set its velocity.

$x'(t) = P*e(t) + I\cdot\int e(\tau)\ d\tau + D\cdot\frac{d}{dt}e(t)$

The P, I, and D are parameters that we control depending on how quickly we want the robot to respond. Recall what each component is:
- Proportional is the current error
- Integral is the sum of all error over time
- Derivative is the change in error between position measurements (over time)

We want to solve for the robot's position over time though, just to see how our choice of parameters would affect the movement. To do this we have to get rid of that integral and replace all the $e(t)$. Let's sub in...

$x'(t) = P(a - x(t)) + I\cdot\int (a - x(\tau))\ d\tau + D\cdot\frac{d}{dt}(a - x(t))$

... and take the derivative...

$x''(t) = -Px'(t) + Ia - Ix(t) - Dx''(t)$

... and rearrange terms...

$(1+D)x''(t) + Px'(t) + Ix(t) = Ia$

... and use the characteristic equation (and quadratic formula) to find the solutions.

$r = \frac{-P\ \pm\ \sqrt{P^2 - 4I(1+D)}}{2(1+D)}$

Let the two roots be $r_1, r_2$.
* If they're both real, then $x(t) = Ae^{r_1t} + Be^{r_2t}$
* If they're equal (that is, $P = 2\sqrt{I(1+D)}$), then $x(t) = (At+B)e^{rt}$. **Note that this is the mathematically ideal path to reach the target fastest while preventing oscillation.**
* If they're complex, then $x(t) = e^{at}(\cos(\omega t) + \sin(\omega t))$, where $r = a \pm \omega i$.

To see the parameters in action, check out [this graph](https://www.desmos.com/calculator/9mdf6j8b99).
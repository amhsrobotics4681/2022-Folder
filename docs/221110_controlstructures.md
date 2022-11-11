Today we covered loops and control structures, which also required logic, and Java's primitive types. Here is a summary of the syntaxes:

**While** - While the condition is true, execute the code inside the curly braces. This can be dangerous in FRC's periodic loops!
```java
while (condition) {
  code;
}
```

**For** - Execute the code block a specific number of times.
```java
for (int i=0; i<10; i++) {
  System.out.println(i*i);
}
```

**Do While** - Execute the code block at least once, and while the condition holds true at the end, execute the code block.
```java
do {
  code;
} while (condition);
```

**For Each** - We didn't cover this at robotics, but Java has a special for loop which is mostly used with arrays.
```java
int[] myArr = new int[10];
for(int n : myArr) {
  code pertaining to n;
}
```

**If-Else** - Execute code depending on the first condition met. The final else statement is not strictly necessary.
```java
if (condition 1) {
  code 1;
} else if (condition 2) {
  code 2;
} else {
  code default;
}
```

**Switch** - Execute code on a case by case basis, as well as combining multiple conditions. More efficient than if-else statements. Also note in the code below that for `i == 2` or `i == 4`, the code under `case 3:` will execute as well because there's no break statement. This can be advantageous.
```java
int i = 5;
switch (i) {
  case 1:
    code 1;
    break;
  case 2:
  case 4:
    code 2;
  case 3:
    code 3;
    break;
  default:
    code default;
    break;
}
```

**Logic Statements** - Java has three logical operators: and (`&&`), or (`||`), and not (`!`). The example code below will evaluate to `true`.
```java
boolean a = true;
boolean b = false;
boolean c = false;

boolean myBool = ((a && b) || (!c || b)) && (!a || !c)
```

As Kellen noted, `teleopPeriodic()` operates periodically, so omitting an `else` branch may cause the robot to hold at a specific value. As long as you know what you're doing, this may not be an issue.

A good methodd to use with control structures are `boolean Joystick.getRawButton(int button)`. You can also use similar methods to define a toggle:
```java
boolean flag;

if (c_driver.getRawButtonPressed(2)) {
    flag = !flag; // when the button is pressed (holding doesn't matter), invert the state of the flag
}
```

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.Joystick;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot { 
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  Victor motorL;
  Victor motorR;
  Joystick controller;

  @Override
  public void robotInit() {
    motorL = new Victor(0);
    motorR = new Victor(1);
    controller = new Joystick(0);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {r motorL;
    Victor motorR;
    Joystick controller;
  
    @Override
    public void robotInit() {
      motorL = new Victor(0);
      motorR = new Victor(1);
      controller = new Joystick(0);
    }
  
    @Override
    public void robotPeriodic() {}
  
    @Override}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    motorL.set(controller.getRawAxis(1));
    motorR.set(controller.getRawAxis(3));
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}

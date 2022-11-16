// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
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
  PWMVictorSPX m_left,m_right;
  Joystick c_driver,c_shooter;
  


  @Override
  public void robotInit() {
    m_left = new PWMVictorSPX(0);
    m_right= new PWMVictorSPX(1);
    c_driver = new Joystick(0);
    c_shooter = new Joystick(1);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {

  m_left.set(0.5);
  }

  @Override
  public void teleopPeriodic() {
    if(c_driver.getRawButton(2)){
      m_right.set(0.5);
    }
    else{
      m_right.set(0);
    }
    
    m_left.set(c_driver.getRawAxis(0));
    m_right.set(c_driver.getRawAxis(2));
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
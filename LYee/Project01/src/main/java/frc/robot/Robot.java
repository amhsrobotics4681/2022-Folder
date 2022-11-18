// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  PWMVictorSPX m_left;
  PWMVictorSPX m_right;
  Joystick c_driver; 
  Joystick c_shooter;
  Compressor m_Compressor;
  Solenoid s_left;
  Solenoid s_right;
  DigitalInput m_switch;
 

  @Override
  public void robotInit() {
    PWMVictorSPX m_left = new PWMVictorSPX(0);
    PWMVictorSPX m_right = new PWMVictorSPX(1);
    Joystick c_driver = new Joystick(0);
    Joystick c_shooter = new Joystick(1);
    CameraServer.startAutomaticCapture("camera name", 0);
    m_Compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
    s_left = new Solenoid(PneumaticsModuleType.CTREPCM,0);
    s_right = new Solenoid(PneumaticsModuleType.CTREPCM,1);
    m_Compressor.enableDigital();
    m_switch = new DigitalInput(4);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    m_left.set(c_driver.getRawAxis(0));
    if (c_driver.getRawButton(2)){
       m_right.set(c_driver.getRawAxis(2));
      }
    if (c_driver.getRawButton(1)){
      s_left.set(true);
    } else if (c_driver.getRawButton(2)){
        s_left.set(false);
      }
    if (c_driver.getRawButtonPressed(3)){
      s_right.set(!s_right.get());
    }
    if (m_switch.get()){
      s_right.set(true);
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

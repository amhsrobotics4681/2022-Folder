// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CameraServerCvJNI;
import edu.wpi.first.wpilibj.DigitalInput;

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

  PWMVictorSPX m_left;
  PWMVictorSPX m_right;
  Joystick control;
  Compressor compressor;
  Solenoid solenoidl;
  Solenoid solenoidr;
  DigitalInput swich;
  
  
  @Override
  public void robotInit() {
    m_left = new PWMVictorSPX(0);
    m_right = new PWMVictorSPX(1);
    control = new Joystick(0);
    compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
    solenoidl = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
    solenoidr = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
    compressor.enableDigital();
    CameraServer.startAutomaticCapture("camera", 0);
    swich = new DigitalInput(2);
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

    
    m_left.set(control.getRawAxis(0)); // can go from -1 to 1
    m_left.set(control.getRawAxis(1));
    //m_right.set();
    
    if (control.getRawButtonPressed(1)) {
      solenoidr.set(control.getRawButtonPressed(1));
      solenoidl.set(control.getRawButtonPressed(1));
    }

    if (swich.get()) {
      solenoidl.set(swich.get());
      solenoidr.set(swich.get());
    }
    
    


    
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

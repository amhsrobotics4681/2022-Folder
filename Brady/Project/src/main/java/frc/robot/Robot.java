// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

 package frc.robot;
import edu.wpi.first.wpilibj.Joystick;
 import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
 import edu.wpi.first.wpilibj.Compressor;
 import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.cameraserver.CameraServer;
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
  PWMVictorSPX m_left,m_right;
  Joystick c_driver,c_shooter;
  Compressor m_compressor;
  Solenoid s_left;
  Solenoid s_right;
  DigitalInput m_switch;

  @Override
  public void robotInit() {
    m_left = new PWMVictorSPX(0);
    m_right= new PWMVictorSPX(1);
    c_driver = new Joystick(0);
    c_shooter = new Joystick(1);
    m_right.setInverted(true);
//--------------------------------------------------------------
    m_compressor=new Compressor(0,PneumaticsModuleType.CTREPCM);
    s_left=new Solenoid(PneumaticsModuleType.CTREPCM,0);
    s_right=new Solenoid(PneumaticsModuleType.CTREPCM,1);
    m_compressor.enableDigital();
//---------------------------------------------------------------
    CameraServer.startAutomaticCapture("camera name",0);
//---------------------------------------------------------------
    DigitalInput m_switch= new DigitalInput(4);
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
  //TeleopPeriodic checks conditions every 20 milliseconds. 
    // if(c_driver.getRawButton(2)){
    //   m_right.set(0.5);
    //   m_left.set(0.5);
    // }
    // ------------------------------------------------------
    // else{
    //   m_left.set(c_driver.getRawAxis(0));
    //   m_right.set(c_driver.getRawAxis(2)); 
      //This is literally all you need to get the motors to work with the joystick. Sets motors (m_left or m_right) to whatever the joystick (c_driver or c_shooter) is.  

      // if (c_driver.getRawButton(1)){
      //   s_right.set(true);
      // }
      // else if(c_driver.getRawButton(2)){
      //   s_right.set(false);
      // }
      // getRawButtonPressed checks whether the button is PRESSED not HELD DOWN
      // if (c_driver.getRawButtonPressed(3)){
      //   s_right.set(!s_right.get());
      // ! means NOT
      // So !s_right.get means whatever s_right is NOT
      // }
    //}
    if (m_switch.get()) {
      s_right.set(true);
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
  public void simulationPeriodic(){}
}


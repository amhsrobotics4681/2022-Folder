package frc.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser; // <--------
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
  PWMVictorSPX m_left, m_right;
  Joystick m_controller;
  ADIS16470_IMU m_gyro;
  int timer;
  SendableChooser<Double> m_chooser;

  @Override
  public void robotInit() {
    m_right = new PWMVictorSPX(0);
    m_left = new PWMVictorSPX(1);
    m_controller = new Joystick(0);
    m_gyro = new ADIS16470_IMU();
    m_chooser = new SendableChooser<>();

    m_chooser.setDefaultOption("default", 0.2);
    m_chooser.addOption("option 1", 0.3);
    m_chooser.addOption("option 2", 0.4);
    m_chooser.addOption("option 3", -0.3);
    m_chooser.addOption("option 4", -0.2);

    m_right.setInverted(true);
    SmartDashboard.putData(m_chooser);
  }

  @Override
  public void autonomousInit() {
    m_gyro.reset();
    timer = 0;

  }

  @Override
  public void autonomousPeriodic() {
    timer++;
    double data = m_chooser.getSelected();
    System.out.println(data);
    m_left.set(data);
    m_right.set(data);
  }

  @Override
  public void teleopPeriodic() {
    m_right.set(-m_controller.getRawAxis(3));
    m_left.set(-m_controller.getRawAxis(1));
    System.out.println(m_gyro.getAngle());
  }
}
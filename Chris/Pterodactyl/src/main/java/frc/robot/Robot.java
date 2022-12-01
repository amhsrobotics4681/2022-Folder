package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
  PWMVictorSPX m_left, m_right;
  ADIS16470_IMU m_gyro;
  int timer;

  @Override
  public void robotInit() {
    m_left = new PWMVictorSPX(0);
    m_right = new PWMVictorSPX(1);
    m_gyro = new ADIS16470_IMU();
  }

  @Override
  public void autonomousInit() {
    timer = 0;
  }

  @Override
  public void autonomousPeriodic() {
    if (timer < 50) {
      m_left.set(1);
      timer++;
    } else if (timer < 100) {
      m_left.set(0);
      m_right.set(1);
      timer++;
    } else {
      m_left.set(0);
      m_right.set(0);
    }
  }

  @Override
  public void teleopPeriodic() {
    System.out.println(m_gyro.getAngle());
  }
}
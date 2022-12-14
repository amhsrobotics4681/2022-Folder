package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  VictorSPX m_frontLeft, m_rearLeft;
  PWMVictorSPX m_frontRight, m_rearRight;
  
  MotorControllerGroup m_right;
  CustomDrive m_drive;
  Joystick m_controller;

  @Override
  public void robotInit() {
    m_frontLeft = new VictorSPX(0);
    m_rearLeft = new VictorSPX(1);
    m_frontRight = new PWMVictorSPX(0);
    m_rearRight = new PWMVictorSPX(1);

    m_rearLeft.follow(m_frontLeft);
    m_right = new MotorControllerGroup(m_frontRight, m_rearRight);
    m_right.setInverted(true);

    m_drive = new CustomDrive(m_frontLeft, m_right);

    m_controller = new Joystick(0);
  }

  @Override
  public void teleopPeriodic() {
    m_drive.arcadeDrive(-m_controller.getRawAxis(1), -m_controller.getRawAxis(0));
  }
}

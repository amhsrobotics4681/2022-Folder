package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
// If the code uses the wrong motor controller type, update appropriately.
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  PWMSparkMax m_frontLeft, m_rearLeft, m_frontRight, m_rearRight;
  MotorControllerGroup m_left, m_right;
  DifferentialDrive m_drive;
  Joystick m_controller;

  @Override
  public void robotInit() {
    m_frontLeft = new PWMSparkMax(0);
    m_rearLeft = new PWMSparkMax(0);
    m_frontRight = new PWMSparkMax(0);
    m_rearRight = new PWMSparkMax(0);

    m_left = new MotorControllerGroup(m_frontLeft, m_rearLeft);
    m_right = new MotorControllerGroup(m_frontRight, m_rearRight);
    m_right.setInverted(true);

    m_drive = new DifferentialDrive(m_left, m_right);

    m_controller = new Joystick(0);
  }

  @Override
  public void teleopPeriodic() {
    m_drive.arcadeDrive(-m_controller.getRawAxis(1), -m_controller.getRawAxis(0));
  }
}

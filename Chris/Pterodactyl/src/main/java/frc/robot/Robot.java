package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  PWMVictorSPX m_left, m_right;
  Joystick c_driver, c_shooter;

  @Override
  public void robotInit() {
    m_left = new PWMVictorSPX(0);
    m_right = new PWMVictorSPX(1);
    c_driver = new Joystick(0);
    c_shooter = new Joystick(1);
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    m_left.set(c_driver.getRawAxis(0));
    if (c_driver.getRawButton(2))
      m_right.set(c_driver.getRawAxis(2));
  }
}

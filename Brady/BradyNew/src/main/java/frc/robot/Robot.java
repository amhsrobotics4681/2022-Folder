

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;





public class Robot extends TimedRobot {
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
public void teleopPeriodic() {}
  
}

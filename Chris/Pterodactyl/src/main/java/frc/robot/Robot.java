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
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
  PWMVictorSPX m_left, m_right;
  Joystick m_controller;
  ADIS16470_IMU m_gyro;
  int timer;

  ArrayList<ArrayList<Float>> arr2 = new ArrayList<ArrayList<Float>>();
  

  @Override
  public void robotInit() {
    m_right = new PWMVictorSPX(0);
    m_left = new PWMVictorSPX(1);
    m_controller = new Joystick(0);
    m_gyro = new ADIS16470_IMU();

    m_right.setInverted(true);

    ArrayList<Float> arr = new ArrayList<>();
    arr.add((float) 0); arr.add((float) 50.0);arr.add((float) 0.5);arr.add((float) 0);
    arr2.add(arr);
    ArrayList<Float> ars = new ArrayList<>();
    ars.add((float) 50); ars.add((float) 100.0);ars.add((float) 0);ars.add((float) 90);
    arr2.add(arr);
    ArrayList<Float> art = new ArrayList<>();
    art.add((float) 100); art.add((float) 200.0);art.add((float) 0);art.add((float) -180);
    arr2.add(arr);
    ArrayList<Float> aru = new ArrayList<>();
    aru.add((float) 200); aru.add((float) 400.0);aru.add((float) -0.3);aru.add((float) 0);
    arr2.add(arr);
  }

  @Override
  public void autonomousInit() {
    m_gyro.reset();
    timer = 0;
  }

  @Override
  public void autonomousPeriodic() {
    timer++;
    for (ArrayList<Float> arrf : arr2) {
      if (timer < arrf.get(1) && timer > arrf.get(0)) {
        if (arrf.get(2) != 0) {
          m_left.set(arrf.get(2));
          m_right.set(arrf.get(2));
        } else {
          if (m_gyro.getAngle() < arrf.get(3)) {
            m_left.set(1);
          }
        }
      }
      System.out.print(arrf);
    }
  }

  @Override
  public void teleopPeriodic() {
    m_right.set(-m_controller.getRawAxis(3));
    m_left.set(-m_controller.getRawAxis(1));
    System.out.println(m_gyro.getAngle());
  }
}
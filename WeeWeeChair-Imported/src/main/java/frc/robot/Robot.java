// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Compressor; 
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  PWMVictorSPX m1 = new PWMVictorSPX(0);
  PWMVictorSPX m2 = new PWMVictorSPX(1);
  Joystick controller = new Joystick(3);
  SendableChooser<String> chooser = new SendableChooser<>();
  SendableChooser<String> timeChooser = new SendableChooser<>();

  boolean locked = false;
  double speed = .6;
  double multiplier = 0.8;

  double rodeoSpeedLeft = 0.6;
  double rodeoSpeedRight = 0.6;
  int rodeoTimerLeft = 0;
  int rodeoTimerRight = 0;
  int timeLimit = 100;

  Compressor compressor;
  Solenoid left, right; 
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    /*compressor = new Compressor();
    right = new Solenoid(0);
    left = new Solenoid(1);
    compressor.start(); */

    chooser.setDefaultOption("Para Cobardes", "lento");
    chooser.addOption("Para Desesperados", "rapido");
    chooser.addOption("Para Locos", "rodeo");
    chooser.addOption("Para Broncos", "hardRodeo");
    timeChooser.addOption("0.5 secs", "0.5");
    timeChooser.addOption("1 sec", "1");
    timeChooser.setDefaultOption("2 secs", "2");
    timeChooser.addOption("4 secs", "4");
    SmartDashboard.putData(chooser);
    SmartDashboard.putData(timeChooser);
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
    switch(timeChooser.getSelected()){
      case("0.5"):
        timeLimit = 25;
        break;
      case("1"):
        timeLimit = 50;
        break;
      case("4"):
        timeLimit = 200;
        break;
      default:
        timeLimit = 100;
    }
    if(chooser.getSelected().equals("rodeo")){
      double currSpeed = rodeoSpeedChooserLeft();
      m2.set(currSpeed * multiplier * -controller.getRawAxis(1));
      m1.set(currSpeed * controller.getRawAxis(5));
    } else if (chooser.getSelected().equals("hardRodeo")){
      m2.set(rodeoSpeedChooserLeft() * multiplier * -controller.getRawAxis(1));
      m1.set(rodeoSpeedChooserRight() * controller.getRawAxis(5));
    } else if(!locked){
      boolean mode = chooser.getSelected().equals("rapido");
      speed = mode ? 1 : speed;
      m2.set(controller.getRawAxis(1) * -speed * multiplier);
      m1.set(controller.getRawAxis(5) * speed);
    } else {
      m2.set(0.0);
      m1.set(0.0);
    }
    //if A is pressd locked = false;
    if(controller.getRawButtonPressed(1)){
      locked = false; 
    }
    //if B is pressed locked = true;
    if(controller.getRawButtonPressed(2)){
      locked = true;
    }
    //if Y is pressed speed = cringle (aka slow lmao)
    if(controller.getRawButtonPressed(4)){
      speed = .6;
    }
    //if X is pressed speed = FAAAAAAAAAAAAST
    if(controller.getRawButtonPressed(3)){
      speed = 1;
    }
    //if LB is pressed = left solenoid
    if(controller.getRawButtonPressed(5))
      left.set(!left.get());
    
    //if RB is pressed = right solenoid
    if(controller.getRawButtonPressed(6))
      right.set(!right.get());
  }

  double rodeoSpeedChooserLeft(){
    rodeoTimerLeft++;
    if(rodeoTimerLeft > timeLimit){
      rodeoSpeedLeft = Math.random() * 2 + 0.6;
      rodeoTimerLeft = 0;
      if(Math.random() - 0.5 < 0){
        rodeoSpeedLeft *= -1;
      }
    }
    return rodeoSpeedLeft;
  }

  double rodeoSpeedChooserRight(){
    rodeoTimerRight++;
    if(rodeoTimerRight > timeLimit){
      rodeoSpeedRight = Math.random() * 2 + 0.6;
      rodeoTimerRight = 0;
      if(Math.random() - 0.5 < 0){
        rodeoSpeedRight *= -1;
      }
    }
    return rodeoSpeedRight;
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}

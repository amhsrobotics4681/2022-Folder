/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    private Joystick controllerDriver, controllerShooter;
    private Victor m_left, m_right;
    private DifferentialDrive m_drive;
    private double vTranslational, vRotational = 0;
    private String drivingStatus;
    private SendableChooser<String> m_chooser = new SendableChooser<>();
    private double integral, error,setpoint, derivative, previousError = 0;
    private boolean aligned, currentlyShooting;

    private Index m_index;
    private Intake m_intake;
    private Shooter m_shooter;
    private Screw m_screw;
    private Climber m_climber;
    private Wheel m_wheel;
    private Counter counter;
    private ADIS16470_IMU m_gyro;
    private Limelight m_limelight;

    @Override
    public void robotInit() {
        controllerDriver = new Joystick(0);
        controllerShooter = new Joystick(1);
        m_left = new Victor(Constants.PWM_TreadsLeft);
        m_right = new Victor(Constants.PWM_TreadsRight);
        m_right.setInverted(false);
        m_left.setInverted(true);
        m_drive = new DifferentialDrive(m_left, m_right);

        m_climber = new Climber();
        m_wheel = new Wheel();
        m_index = new Index();
        m_intake = new Intake();
        m_shooter = new Shooter();
        m_screw = new Screw();
        m_gyro = new ADIS16470_IMU();
        m_limelight = new Limelight();
        
        counter = new Counter(Constants.DIO_LIDAR);
        counter.setMaxPeriod(1.0);
        counter.setSemiPeriodMode(true);
        counter.reset();
        
        CameraServer.startAutomaticCapture("Shooting Camera", 0);
        CameraServer.startAutomaticCapture("Collecting Camera", 1);
    }

    @Override
    public void autonomousInit() {
        setStatus("Auto", false);
        m_gyro.setYawAxis(IMUAxis.kY);
        m_gyro.reset();
        m_drive.setSafetyEnabled(false);
    }

    @Override
    public void autonomousPeriodic() {}
    
    @Override
    public void teleopInit() {
        setStatus("Driving", false); // driving mode and turn off LL
    }

    @Override
    public void teleopPeriodic() {
        //System.out.println(m_limelight.hasValidTarget());
        m_index.mainMethod();
        m_wheel.mainMethod();
        m_climber.mainMethod();

        // CONTROLS
        if (drivingStatus.equals("Driving")) {//Incramental Acceleration to prevent falling over
            driveCurve(-controllerDriver.getRawAxis(2), -controllerDriver.getRawAxis(1));
        } else if (drivingStatus.equals("Manual Shooting")) {
            vRotational = controllerShooter.getRawAxis(0);
            m_screw.setSpeed(-controllerShooter.getRawAxis(1));
            if(controllerShooter.getRawButtonPressed(1))
                aligned = true;
            if (aligned) {
                m_shooter.startShooter(false);
                m_index.setEjecting(m_shooter.getEjecting());
            }
        // auto-drives and auto-screws
        } else if (drivingStatus.equals("Limelight Shooting") || drivingStatus.equals("Full Shooting")) {
            /*potential issue: if screwAtElevation is too sensitive this condition could be toggled a couple hundred times a second
            will not cause problems for the time being, but we'll have to keep an eye out if we make screwAtElevation automatic*/
            limelight_Shooting();
            m_screw.setSpeed(-controllerShooter.getRawAxis(1)); // until numbers testing & regression formula

            /*for some reason the robot thinks we are always aligned
            will control manually until fixed
            if (aligned && m_screw.screwAtElevation) 
                currentlyShooting = true;*/
            if(controllerShooter.getRawButtonPressed(1))
                currentlyShooting = true;
            if(currentlyShooting){
                m_shooter.startShooter(drivingStatus.equals("Full Shooting"));
                m_index.setEjecting(m_shooter.getEjecting());
            }
        }
        else if (drivingStatus.equals("Climbing")) {//Controls shift to other remote and everything is dialed down to half power
            vTranslational = (controllerShooter.getRawAxis(1)/2);
            vRotational = (controllerShooter.getRawAxis(0)/2);
            if (controllerShooter.getRawButton(6)) {
                m_climber.extending(); // Brings arms up
            } else if (controllerShooter.getRawButton(7)) {
                m_climber.contracting(); // Pulls arms back down, which pulls the robot up if it is hooked upon the bar
            } else {
                m_climber.stop();
            }
        }
        else if (drivingStatus.equals("Loading")) {
            System.out.println("You're fired from the drive team");
            // limelight_Loading();
        }

        // DRIVE SPEED LOGIC
        if (drivingStatus.equals("Driving") || drivingStatus.equals("Climbing"))
            m_drive.arcadeDrive(-vRotational*0.8 + 0.04, vTranslational, false);
        else if (drivingStatus.equals("Manual Shooting"))
            m_drive.arcadeDrive(-vRotational*0.5, 0, false);
        
        // BUTTONS
        if (controllerDriver.getRawButtonPressed(Constants.bResetScrew))
            m_screw.resetScrew(); // set current screw position as 0 on encoder (for testing purposes only)

        if (controllerDriver.getRawButtonPressed(Constants.bIntakeToggle)) {
            m_intake.setIntake(!m_intake.getIntake());
            m_index.setSpitting(false);
        }   
        if (controllerDriver.getRawButtonPressed(Constants.bSpitToggle)) {
            m_index.setSpitting(!m_index.getSpitting());
            m_intake.setSpitting(!m_intake.getSpitting());
        }
        if (controllerShooter.getRawButtonPressed(2)) {
            m_shooter.killShooter();//Stops shooter regardless of status
        }
        
        //FOR TESTING- limelight toggles
        if(controllerShooter.getRawButton(8))
            m_limelight.setLED(!m_limelight.getLED());

        // MODE SWITCHING - logic is in teleop-CONTROLS
        if(controllerShooter.getRawButtonPressed(3)) {
            setStatus("Manual Shooting", true);
        }
        /* Removed for saftey, will re-implement later
        else if (controllerShooter.getRawButtonPressed(5)) {
            setStatus("Full Shooting", true);
        }*/
        else if (controllerShooter.getRawButtonPressed(4)) {
            setStatus("Limelight Shooting", true);
        }
        else if (controllerShooter.getRawButton(6) || controllerShooter.getRawButton(7)) {
            setStatus("Climbing", false);
        }
        else if (controllerDriver.getRawButtonPressed(Constants.bDriving)) {
            setStatus("Driving", false);
        }
    }

    private double getDistance() {//Returns distance from front of the robot to wall
        return (counter.getPeriod() * 100000 / 2.54)-Constants.LidarError;
        // getPeriod returns cm / µs, then --> sec --> in
    }

    public void limelight_Shooting() {
        if (m_limelight.hasValidTarget()) {
            m_drive.arcadeDrive(PID()*0.03, 0);
        } else {
            m_drive.arcadeDrive(.5,0); // when in doubt, aim right
        }
        aligned = aligned || m_limelight.isAligned(); // if we've aligned, we're aligned til status reset
    }

    public void limelight_Loading() { // will align us in 2 ft semicircle around vision target
        m_drive.arcadeDrive(PID()*0.03, (getDistance()-24)/24);
    }

    private void driveCurve(double speed, double rotation){
        if(vTranslational < speed)
            vTranslational += Constants.kSpeedCurve;
        else if (vTranslational > speed)
            vTranslational -= Constants.kSpeedCurve;
        if(vRotational < rotation)
            vRotational += Constants.kSpeedCurve;
        else if(vRotational > rotation)
            vRotational -= Constants.kSpeedCurve;
    }

    private void setStatus(String status, boolean limelightLED) {
        drivingStatus = status;
        m_limelight.setLED(limelightLED);
        aligned = currentlyShooting = false;
        m_shooter.resetTimer();
        switch (status) {
            case "Full Shooting": case "Limelight Shooting":
                m_limelight.setPipeline(0);
            case "Manual Shooting":
                m_intake.setIntake(false);
                break;
            case "Loading":
                m_limelight.setPipeline(1);
            case "Driving":
                m_intake.setIntake(false); //turned off to not damage intake
            case "Climbing": case "Auto":
                m_index.setEjecting(false);
                break;
        }
        if (status.equals("Climbing") || status.equals("Auto")) // to prevent intake motor jerking
            m_intake.setIntake(false);
    }

    public double PID(){
        error = (setpoint - m_limelight.getX())*.4;
        integral += error*.02;
        derivative = (previousError - error)/.1;
        previousError = error;
        return (error + integral + derivative);
    }

    @Override
    public void testPeriodic() {
        m_index.mainMethod(); // allows indexer to assume different values

        m_drive.arcadeDrive(-controllerDriver.getRawAxis(1), controllerDriver.getRawAxis(2));
        m_screw.setSpeed(-controllerShooter.getRawAxis(1));

        if (controllerShooter.getRawButton(1)) {
            m_shooter.resetTimer();
            currentlyShooting = true;
        }
        if (currentlyShooting) {
            m_shooter.startShooter(false);
            m_index.setEjecting(m_shooter.getEjecting());
        }

        if (controllerShooter.getRawButton(2)) {
            m_shooter.killShooter();
            currentlyShooting = true;
        }
    }

    @Override
    public void disabledInit(){
        m_limelight.setLED(false);
    }
}
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class CustomDrive {
    VictorSPX m_left;
    MotorControllerGroup m_right;
    double k_defaultDeadband = 0.02;

    public CustomDrive(VictorSPX m_left, MotorControllerGroup m_right) {
        this.m_left = m_left;
        this.m_right = m_right;
    }

    public void arcadeDrive(double lat, double rot) {
        if (Math.abs(lat) < k_defaultDeadband)
            lat = 0;
        if (Math.abs(rot) < k_defaultDeadband)
            rot = 0;
        
        lat = Math.copySign(lat*lat, lat);
        rot = Math.copySign(rot*rot, rot);

        double leftSpeed = lat - rot;
        double rightSpeed = lat + rot;
        double k_normalization = Math.max(1, Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed)));

        leftSpeed /= k_normalization;
        rightSpeed /= k_normalization;

        m_left.set(ControlMode.PercentOutput, leftSpeed);
        m_right.set(rightSpeed);

    }
}

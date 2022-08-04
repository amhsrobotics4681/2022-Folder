package frc.robot;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Screw {
    TalonSRX m_screw;
    DigitalInput m_screwStop;
    boolean screwAtElevation = true;
    int encoderTarget;

    public Screw(){
        m_screw = new TalonSRX(Constants.CAN_Screw);
        m_screwStop = new DigitalInput(Constants.DIO_ScrewSwitch); // prevents locking up screw
        encoderTarget = 0;
        m_screw.setSelectedSensorPosition(0);
    }
    public double getPosition() {
        return m_screw.getSelectedSensorPosition();
    }

    public void setSpeed(double speed) {
        if (m_screwStop.get()) {
            m_screw.set(ControlMode.PercentOutput, 0);
        } else {
            m_screw.set(ControlMode.PercentOutput, speed);
        }
    }

    public void resetScrew() {
        m_screw.setSelectedSensorPosition(0);
    }
}
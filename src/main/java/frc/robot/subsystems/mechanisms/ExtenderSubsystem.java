package frc.robot.subsystems.mechanisms;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ExtenderSubsystem extends SubsystemBase {
    private final SparkMax extenderMotor;
    private final RelativeEncoder extenderEncoder;
    private final DigitalInput extenderLimitSwitch;

    private double speed;

    public ExtenderSubsystem() {
        this.extenderMotor = new SparkMax(Constants.Extender.motorChannel, MotorType.kBrushed);
        this.extenderEncoder = extenderMotor.getEncoder();
        this.extenderLimitSwitch = new DigitalInput(Constants.Extender.limitSwitchChannel);
    }

    public void resetEncoder() {
        extenderEncoder.setPosition(0);
    }

    public double getEncoderPosition() {
        return extenderEncoder.getPosition();//value is in encoder counts (does not return common units like degrees)
    }

    public boolean getLimitSwitch() {
        return !extenderLimitSwitch.get();
    }

    public void setMotorSpeed(double speed) {
        this.speed = speed;
    }

    //the periodic method always runs over and over when robot is enabled
    @Override
    public void periodic() {
        //sets voltage from -1 to 1 not actual rpm
        extenderMotor.set(speed);

        if (getLimitSwitch()) {
            resetEncoder();
        }
    }
}

package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class TroughSubsystem extends SubsystemBase{

    private final SparkMax outputMotor = new SparkMax(Constants.Trough.motorChannel, MotorType.kBrushless);
    private final DigitalInput limitSwitch = new DigitalInput(Constants.Trough.limitSwitchChannel);

    private double speed;

    public void setSpeed (double speed) {
        this.speed = speed;
    }
    public boolean getLimitSwitch(){
        return limitSwitch.get();
    }

    @Override
    public void periodic() {
        outputMotor.set(speed);
    }
}

package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ChuteSubsystem extends SubsystemBase{

    private final SparkMax outputMotor = new SparkMax(Constants.Chute.motorChannel, MotorType.kBrushless);
    private final DigitalInput limitSwitch = new DigitalInput(Constants.Chute.limitSwitchChannel);

    private double speed;

    public void setSpeed (double speed) {
        this.speed = speed;
    }
    public boolean getLimitSwitch(){
        return limitSwitch.get();
    }

    @Override
    public void periodic() {
        if (getLimitSwitch() && speed == 0){
            outputMotor.set(Constants.Chute.holdSpeed);
        } else {
            outputMotor.set(speed);
        }
    }
}

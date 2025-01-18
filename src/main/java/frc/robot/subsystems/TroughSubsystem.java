package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class TroughSubsystem extends SubsystemBase{

    private double speed;
    SparkMax outputMotor = new SparkMax(Constants.Trough.outputMotorChannel, MotorType.kBrushless);

    public void setSpeed (double speed) {
        this.speed = speed;
    }

    @Override
    public void periodic() {
        outputMotor.set(speed);
    }
}

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class TroughSubsystem extends SubsystemBase{
    private double speed = 0.0;
    SparkMax outputMotor = new SparkMax(Constants.Trough.outputMotorChannel, MotorType.kBrushless);

    public void loadZeMissle () {
        speed = -0.1;
    }

    public void fireZeMissle(){
        speed = 1;
    }


    @Override
    public void periodic() {
        outputMotor.set(speed);
    }
}

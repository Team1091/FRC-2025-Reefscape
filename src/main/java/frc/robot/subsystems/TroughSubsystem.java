package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class TroughSubsystem extends SubsystemBase{
    private double speed = 0.0;
    SparkMax outputMotor = new SparkMax(Constants.Trough.outputMotorChannel, MotorType.kBrushless);
//keep coral in
    public void loadZeMissle () {
        speed = -0.1;
    }
//shoot the coral out
    public void fireZeMissle(){
        speed = 1;
    }


    @Override
    public void periodic() {
        outputMotor.set(speed);
    }
}

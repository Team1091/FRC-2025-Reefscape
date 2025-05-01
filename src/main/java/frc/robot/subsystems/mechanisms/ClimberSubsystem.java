package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
    private final SparkMax climberMotor;
    private final RelativeEncoder climberEncoder;
    private double speed;

    public ClimberSubsystem(){
        climberMotor = new SparkMax(Constants.Climber.motorChannel, SparkLowLevel.MotorType.kBrushless);
        climberEncoder =
    }
    public void setMotorSpeed(double speed){
        this.speed = speed
    }

    public getEncoderValue(){

    }
}

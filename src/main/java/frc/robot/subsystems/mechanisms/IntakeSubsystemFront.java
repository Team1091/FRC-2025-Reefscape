package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystemFront extends SubsystemBase {
    private final SparkMax intakemotorFront = new SparkMax(Constants.Intake.frontMotorChannel, MotorType.kBrushless);
    
    private double speed;

    public void setSpeed(double speed){
        this.speed = -speed;
    }
    @Override
    public void periodic() {
        intakemotorFront.set(speed);
    }
}

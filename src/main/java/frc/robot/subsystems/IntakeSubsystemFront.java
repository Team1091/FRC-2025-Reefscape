package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystemFront extends SubsystemBase {
    private double speed;
    SparkMax intakemotorFront = new SparkMax(Constants.Intake.intakeMotorChannel, MotorType.kBrushless);
   
    public void setSpeed(double speed){
        this.speed = speed;
    }
    @Override
    public void periodic() {
        intakemotorFront.set(speed);
    }
}

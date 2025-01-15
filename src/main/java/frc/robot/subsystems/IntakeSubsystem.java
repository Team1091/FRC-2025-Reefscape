package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
private double speed = 0.0;
    SparkMax intakemotor1 = new SparkMax(Constants.Intake.intakeMotorChannel, MotorType.kBrushless);
    SparkMax intakemotor2 = new SparkMax(Constants.Intake.intakeMotorChannel, MotorType.kBrushless);
   
    public void intake(){
        speed = 1;
    }
    @Override
    public void periodic() {
        intakemotor1.set(speed);
        intakemotor2.set(speed);
    }
}

package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystemFront extends SubsystemBase {
    private final SparkMax intakeMotorFront = new SparkMax(Constants.Intake.frontMotorChannel, MotorType.kBrushless);

    private double speed;

    public void setSpeed(double speed) {
        this.speed = -speed;
    }

    @Override
    public void periodic() {
        intakeMotorFront.set(speed);
    }
}
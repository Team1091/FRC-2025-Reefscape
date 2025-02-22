package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystemBack extends SubsystemBase {
    private final SparkMax intakeMotorBack = new SparkMax(Constants.Intake.backMotorChannel, MotorType.kBrushless);

    private double speed;

    public void setSpeed(double speed) {
        this.speed = -speed;
    }

    @Override
    public void periodic() {
        intakeMotorBack.set(speed);
    }
}

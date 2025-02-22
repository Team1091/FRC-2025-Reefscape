package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
    private final SparkMax climberMotor;
    private final RelativeEncoder climberEncoder;

    private double speed;

    public ClimberSubsystem() {
        climberMotor = new SparkMax(Constants.Climber.motorChannel, MotorType.kBrushless);
        climberEncoder = climberMotor.getEncoder();
    }

    public void setSpeed(double speed) {
        this.speed = -speed;
    }

    public void resetEncoder() {
        climberEncoder.setPosition(0);
    }

    public double getEncoderPosition() {
        return climberEncoder.getPosition();//value is in encoder counts (does not return common units like degrees)
    }

    @Override
    public void periodic() {
        climberMotor.set(speed);
    }
}

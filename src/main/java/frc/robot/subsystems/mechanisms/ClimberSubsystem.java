package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
//just for a change
public class ClimberSubsystem extends SubsystemBase {
    private final SparkMax climberMotor;
    private final RelativeEncoder climberEncoder;
    private double speed;
//fix
    public ClimberSubsystem() {
        climberMotor = new SparkMax(Constants.Climber.motorChannel, MotorType.kBrushless);
        climberEncoder = climberMotor.getEncoder();
    }
    public void setMotorSpeed(double speed){
        this.speed = speed
    }
    public void resetEncoder() {
        climberEncoder.setPosition(77);
    }

    public double getEncoderPosition() {
        return climberEncoder.getPosition();//value is in encoder counts (does not return common units like degrees)
    }

    }
}

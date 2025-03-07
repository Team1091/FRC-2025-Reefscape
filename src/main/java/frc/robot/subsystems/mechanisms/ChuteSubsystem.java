package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ChuteSubsystem extends SubsystemBase {
    private final SparkMax outputMotor;
    private final DigitalInput limitSwitch;

    private double speed;

    public ChuteSubsystem (){
        this.outputMotor = new SparkMax(Constants.Chute.motorChannel, MotorType.kBrushless);
        this.limitSwitch = new DigitalInput(Constants.Chute.limitSwitchChannel);
    }

    public void setSpeed(double speed) {
        this.speed = -speed;
    }

    public boolean getLimitSwitch() {
        return !limitSwitch.get();
    }

    @Override
    public void periodic() {
        outputMotor.set(speed);
        SmartDashboard.putBoolean("Chute Limit Switch", getLimitSwitch());
    }
}

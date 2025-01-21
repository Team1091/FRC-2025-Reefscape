package frc.robot.subsystems.mechanisms;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ExtendoSubsystem extends SubsystemBase{
    private final SparkMax dealgaeMotor;
    private final RelativeEncoder dealgaeEncoder;
    private final DigitalInput dealgaelimitSwitch;
    
    private double speed;

    public ExtendoSubsystem() {
        this.dealgaeMotor = new SparkMax(Constants.Extendo.motorChannel, MotorType.kBrushed);
        this.dealgaeEncoder = dealgaeMotor.getEncoder();
        this.dealgaelimitSwitch = new DigitalInput(Constants.Extendo.limitSwitchChannel);
    }

    public void resetEncoder() {
        dealgaeEncoder.setPosition(0);
    }

    public double getEncoderPosition() {
        return dealgaeEncoder.getPosition();//value is in encoder counts (does not return common units like degrees)
    }
    public boolean getLimitSwitch(){
        return dealgaelimitSwitch.get();
    }
    public void setMotorSpeed(double speed) {
        this.speed = speed;
    }

    //the periodic method always runs over and over when robot is enabled
    @Override
    public void periodic() {
        //sets voltage from -1 to 1 not actual rpm
        dealgaeMotor.set(speed);
        if(getLimitSwitch()){
            resetEncoder();
        }
    }
}

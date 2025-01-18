package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DealgaeSubsystem extends SubsystemBase{
    private final SparkMax dealgaeMotor;
    private final RelativeEncoder dealgaeEncoder;
    private final DigitalInput dealgaelimitSwitch;
    private double speed;

    public DealgaeSubsystem() {
        this.dealgaeMotor = new SparkMax(Constants.Dealgae.dealgaemotor, MotorType.kBrushed);
        this.dealgaeEncoder = dealgaeMotor.getEncoder();
        this.dealgaelimitSwitch = new DigitalInput(Constants.Dealgae.dealgaelimitSwitch);
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

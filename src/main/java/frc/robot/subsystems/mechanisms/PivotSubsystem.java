
package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/*
    subsystems contain methods used to interact with the robot hardware
    each hardware component can only be accessed in one subsystem
    methods in subsystems are run by commands
 */

public class PivotSubsystem extends SubsystemBase {

    //motors are defined by the type of their motor controller (ask electrical)
    private final SparkMax pivotMotor;
    private final RelativeEncoder pivotEncoder;
    private final DigitalInput limitSwitch;
    
    private double speed;

    public PivotSubsystem() {
        this.pivotMotor = new SparkMax(Constants.Pivot.motorChannel, MotorType.kBrushed);
        this.limitSwitch = new DigitalInput(Constants.Pivot.limitSwitchChannel);
        this.pivotEncoder = pivotMotor.getEncoder();
    }

    public void resetEncoder() {
        pivotEncoder.setPosition(0);
    }

    public double getEncoderPosition() {
        return pivotEncoder.getPosition();//value is in encoder counts (does not return common units like degrees)
    }
    public boolean getLimitSwitch(){
        return limitSwitch.get();
    }
    public void setMotorSpeed(double speed) {
        this.speed = speed;
    }

    //the periodic method always runs over and over when robot is enabled
    @Override
    public void periodic() {
        //sets voltage from -1 to 1 not actual rpm
        pivotMotor.set(speed);
        
        if(getLimitSwitch()){
            resetEncoder();
        }
    }
}



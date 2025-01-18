
package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
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
    private final Encoder pivotEncoder;
    private final DigitalInput limitSwitch;
    private double speed;

    public PivotSubsystem() {
        this.pivotMotor = new SparkMax(Constants.Pivot.pivotMotor, MotorType.kBrushed);
        this.pivotEncoder = new Encoder(Constants.Pivot.pivotEncoder1, Constants.Pivot.pivotEncoder2, Constants.Pivot.pivotEncoder3);
        this.limitSwitch = new DigitalInput(Constants.Pivot.limitSwitch);
    }

    public void resetEncoder() {
                pivotEncoder.reset();
    }

    public int getEncoderPosition() {
        return pivotEncoder.get();//value is in encoder counts (does not return common units like degrees)
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



package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import java.lang.annotation.ElementType;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevatorSubsystem extends SubsystemBase{
    //motors are defined by the type of their motor controller (ask electrical)
    private final SparkMax elevatorMotor;
    private final Encoder elevatorEncoder;
    private final DigitalInput elevatorlimitSwitch;
    private double speed;

    public ElevatorSubsystem() {
        this.elevatorMotor = new SparkMax(Constants.Elevator.elevatormotor, MotorType.kBrushed);
        this.elevatorEncoder = new Encoder(Constants.Elevator.elevatorEncoder1, Constants.Elevator.elevatorEncoder2, Constants.Elevator.elevatorEncoder3);
        this.elevatorlimitSwitch = new DigitalInput(Constants.Elevator.elevatorlimitSwitch);
    }

    public void resetEncoder() {
                elevatorEncoder.reset();
    }

    public int getEncoderPosition() {
        return elevatorEncoder.get();//value is in encoder counts (does not return common units like degrees)
    }
    public boolean getLimitSwitch(){
        return elevatorlimitSwitch.get();
    }
    public void setMotorSpeed(double speed) {
        this.speed = speed;
    }

    //the periodic method always runs over and over when robot is enabled
    @Override
    public void periodic() {
        //sets voltage from -1 to 1 not actual rpm
        elevatorMotor.set(speed);
        if(getLimitSwitch()){
            resetEncoder();
        }
    }
}

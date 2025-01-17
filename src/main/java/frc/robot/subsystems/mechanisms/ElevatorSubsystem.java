package frc.robot.subsystems.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevatorSubsystem extends SubsystemBase{
    //motors are defined by the type of their motor controller (ask electrical)
    private final SparkMax elevatorMotor;
    private final RelativeEncoder elevatorEncoder;
    private final DigitalInput elevatorlimitSwitch;
    private double speed;

    public ElevatorSubsystem() {
        this.elevatorMotor = new SparkMax(Constants.Elevator.elevatormotor, MotorType.kBrushed);
        this.elevatorEncoder = elevatorMotor.getEncoder();
        this.elevatorlimitSwitch = new DigitalInput(Constants.Elevator.elevatorlimitSwitch);
    }

    public void resetEncoder() {
        elevatorEncoder.setPosition(0);
    }

    public double getEncoderPosition() {
        return elevatorEncoder.getPosition();//value is in encoder counts (does not return common units like degrees)
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

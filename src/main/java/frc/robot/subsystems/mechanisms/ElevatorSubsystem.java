package frc.robot.subsystems.mechanisms;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.enums.ElevatorPosition;

public class ElevatorSubsystem extends SubsystemBase {
    //motors are defined by the type of their motor controller (ask electrical)
    private final SparkMax elevatorMotor;
    private final RelativeEncoder elevatorEncoder;
    private final DigitalInput elevatorLimitSwitchTop;
    private final DigitalInput elevatorLimitSwitchBottom;

    private double selectedLevel = Constants.Elevator.l4Position;
    private double speed;

    public ElevatorSubsystem() {
        this.elevatorMotor = new SparkMax(Constants.Elevator.motorChannel, MotorType.kBrushless);
        this.elevatorEncoder = elevatorMotor.getEncoder();
        this.elevatorLimitSwitchTop = new DigitalInput(Constants.Elevator.limitSwitchChannelTop);
        this.elevatorLimitSwitchBottom = new DigitalInput(Constants.Elevator.limitSwitchChannelBottom);
    }

    public void resetEncoder() {
        elevatorEncoder.setPosition(0);
    }

    public double getEncoderPosition() {
        return elevatorEncoder.getPosition();//value is in encoder counts (does not return common units like degrees)
    }

    public boolean getLimitSwitchTop() {
        return !elevatorLimitSwitchTop.get();
    }

    public boolean getLimitSwitchBottom() {
        return !elevatorLimitSwitchBottom.get();
    }

    public double getSelectedLevel(){
        return selectedLevel;
    }

    public void setMotorSpeed(double speed) {
        this.speed = speed;
    }

    public void setScoreLevelL4(){
        setScoreLevel(ElevatorPosition.l4);
    }

    public void setScoreLevelL3(){
        setScoreLevel(ElevatorPosition.l3);
    }

    public void setScoreLevelL2(){
        setScoreLevel(ElevatorPosition.l2);
    }

    public void setScoreLevel(ElevatorPosition scoreLevel) {
        selectedLevel = switch (scoreLevel) {
            case l2 -> Constants.Elevator.l2Position;
            case l3 -> Constants.Elevator.l3Position;
            case l4 -> Constants.Elevator.l4Position;
            case algae1 -> Constants.Elevator.algae1Position;
            case algae2 -> Constants.Elevator.algae2Position;
            default -> 0;
        };
        SmartDashboard.putString("Score Level", scoreLevel.toString());
    }

    //the periodic method always runs over and over when robot is enabled
    @Override
    public void periodic() {
        //sets voltage from -1 to 1 not actual rpm
        elevatorMotor.set(speed);

        if (getLimitSwitchBottom()) {
            resetEncoder();
        }

        SmartDashboard.putNumber("Elevator Encoder", getEncoderPosition());
        SmartDashboard.putBoolean("Elevator Top Limit Switch", getLimitSwitchTop());
        SmartDashboard.putBoolean("Elevator Bottom Limit Switch", getLimitSwitchBottom());

    }
}

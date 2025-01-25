package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.enums.PivotPosition;
import frc.robot.subsystems.mechanisms.PivotSubsystem;

public class PivotCommandAutomatic extends Command {
    private final PivotSubsystem pivotSubsystem;

    private PivotPosition pivotPosition;
    private double endPosition = 0;
    private int motorDirection = 1;

    public PivotCommandAutomatic(PivotSubsystem pivotSubsystem, PivotPosition pivotPosition) {
        this.pivotSubsystem = pivotSubsystem;
        this.pivotPosition = pivotPosition;
        addRequirements(pivotSubsystem);
    }

    @Override
    public void initialize(){
        switch (pivotPosition) {
            case out:
                endPosition = Constants.Pivot.outPosition;
                break;
        
            case score:
                endPosition = Constants.Pivot.scorePosition;
                break;
        
            default:
                endPosition = 0;
                break;
        }
        
        if (pivotSubsystem.getEncoderPosition() > endPosition){
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        pivotSubsystem.setMotorSpeed(Constants.Pivot.speed * motorDirection);

    }
    @Override
    public void end(boolean interrupted){
        pivotSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if (pivotSubsystem.getLimitSwitch() && motorDirection == -1){
            return true;
        }
        if (endPosition == 0){
            if (pivotSubsystem.getLimitSwitch()){
                return true;
            }
        } else {
            if (pivotSubsystem.getEncoderPosition() < endPosition && motorDirection == -1){
                return true;
            }
            if (pivotSubsystem.getEncoderPosition() > endPosition && motorDirection == 1){
                return true;
            }
        }
        return false;
    }
}

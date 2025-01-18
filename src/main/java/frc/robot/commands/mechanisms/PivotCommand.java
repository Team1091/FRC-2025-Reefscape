package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.enums.PivotPosition;
import frc.robot.subsystems.mechanisms.PivotSubsystem;

public class PivotCommand extends Command {
    private final PivotSubsystem pivotSubsystem;
    private PivotPosition pivotPosition;

    private double endPosition = 0;
    private int motorDirection = 1;
    //because Ben says it is cool

    public PivotCommand(PivotSubsystem pivotSubsystem, PivotPosition pivotPosition) {
        this.pivotSubsystem = pivotSubsystem;
        this.pivotPosition = pivotPosition;
        addRequirements(pivotSubsystem);
    }

    @Override
    public void initialize(){
        if (pivotPosition == PivotPosition.out){
            endPosition = Constants.Pivot.outEncoderPosition;
        }else if(pivotPosition == PivotPosition.score){
            endPosition = Constants.Pivot.scorePosition;
        } else{
            endPosition = 0;
        }

        if (pivotSubsystem.getEncoderPosition() > endPosition){
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        pivotSubsystem.setMotorSpeed(Constants.Pivot.pivotSpeed * motorDirection);

    }
    @Override
    public void end(boolean interrupted){
        pivotSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if (pivotSubsystem.getEncoderPosition() < endPosition && motorDirection == -1){
            return true;
        }
        if (pivotSubsystem.getEncoderPosition() > endPosition && motorDirection == 1){
            return true;
        }
        return false;
    }
}

package frc.robot.commands;

import java.util.concurrent.locks.Condition;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.PivotSubsystem.PivotPosition;

public class PivotCommand extends Command {
    private final PivotSubsystem pivotSubsystem;
    private PivotPosition pivotPosition;

    public PivotCommand(PivotSubsystem pivotSubsystem, PivotPosition pivotPosition) {
        this.pivotSubsystem = pivotSubsystem;
        this.pivotPosition = pivotPosition;
        addRequirements(pivotSubsystem);
    }

    @Override
    public void initialize(){
        pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.in);
    }

    @Override
    public void execute() {
        if (pivotPosition == PivotPosition.out){
            pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.out);
        } else if (pivotPosition == PivotPosition.in) {
            pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.in);
        }else if(pivotPosition == PivotPosition.score){
            pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.score);
        } else{
            pivotSubsystem.setPivotPosition(0);
        }

    }
    @Override
    public void end(boolean interrupted){
        pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.in);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}

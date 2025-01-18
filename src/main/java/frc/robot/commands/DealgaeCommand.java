package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;

import frc.robot.subsystems.DealgaeSubsystem;

public class DealgaeCommand extends Command{
    private final DealgaeSubsystem dealgaeSubsystem;
    private boolean dealgaePosition ;
    private double endPosition = 0;
    private int motorDirection = 1;
    //because Ben says it is cool

    public DealgaeCommand(DealgaeSubsystem dealgaeSubsystem) {
        this.dealgaeSubsystem = dealgaeSubsystem;
        this.dealgaePosition = dealgaePosition;
        addRequirements(dealgaeSubsystem);
    }

    @Override
    public void initialize(){
        if (dealgaePosition == true){
            endPosition= Constants.Dealgae.outEncoderPosition;
        } else{
            endPosition= Constants.Dealgae.inEncoderPosition;
        }

        if (dealgaeSubsystem.getEncoderPosition()> endPosition){
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        dealgaeSubsystem.setMotorSpeed(Constants.Dealgae.dealgaeSpeed * motorDirection);

    }
    @Override
    public void end(boolean interrupted){
        dealgaeSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if (dealgaeSubsystem.getEncoderPosition() < endPosition && motorDirection == -1){
            return true;
        }
        if (dealgaeSubsystem.getEncoderPosition() > endPosition && motorDirection == 1){
            return true;
        }
        return false;
    }
}

package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ExtendoSubsystem;

public class ExtendoCommand extends Command{
    private final ExtendoSubsystem extendoSubsystem;
    
    private boolean isOut;
    private double endPosition = 0;
    private int motorDirection = 1;

    public ExtendoCommand(ExtendoSubsystem extendoSubsystem, boolean isOut) {
        this.extendoSubsystem = extendoSubsystem;
        this.isOut = isOut;
        addRequirements(extendoSubsystem);
    }

    @Override
    public void initialize(){
        if (isOut == true){
            endPosition = Constants.Extendo.outPosition;
        } else{
            endPosition = Constants.Extendo.inPosition;
        }

        if (extendoSubsystem.getEncoderPosition() > endPosition){
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        extendoSubsystem.setMotorSpeed(Constants.Extendo.speed * motorDirection);
    }
    @Override
    public void end(boolean interrupted){
        extendoSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if (extendoSubsystem.getEncoderPosition() < endPosition && motorDirection == -1){
            return true;
        }
        if (extendoSubsystem.getEncoderPosition() > endPosition && motorDirection == 1){
            return true;
        }
        return false;
    }
}

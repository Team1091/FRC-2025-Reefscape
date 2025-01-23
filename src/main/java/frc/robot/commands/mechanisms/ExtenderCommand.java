package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

public class ExtenderCommand extends Command{
    private final ExtenderSubsystem extenderSubsystem;
    
    private boolean isOut;
    private double endPosition = 0;
    private int motorDirection = 1;

    public ExtenderCommand(ExtenderSubsystem extenderSubsystem, boolean isOut) {
        this.extenderSubsystem = extenderSubsystem;
        this.isOut = isOut;
        addRequirements(extenderSubsystem);
    }

    @Override
    public void initialize(){
        if (isOut == true){
            endPosition = Constants.Extender.outPosition;
        } else{
            endPosition = Constants.Extender.inPosition;
        }

        if (extenderSubsystem.getEncoderPosition() > endPosition){
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        extenderSubsystem.setMotorSpeed(Constants.Extender.speed * motorDirection);
    }
    @Override
    public void end(boolean interrupted){
        extenderSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if (extenderSubsystem.getEncoderPosition() < endPosition && motorDirection == -1){
            return true;
        }
        if (extenderSubsystem.getEncoderPosition() > endPosition && motorDirection == 1){
            return true;
        }
        return false;
    }
}

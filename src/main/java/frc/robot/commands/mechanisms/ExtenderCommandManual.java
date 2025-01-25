package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

public class ExtenderCommandManual extends Command{
    private final ExtenderSubsystem extenderSubsystem;
    
    private double speed = 0;

    public ExtenderCommandManual(ExtenderSubsystem extenderSubsystem, double speed) {
        this.extenderSubsystem = extenderSubsystem;
        this.speed = speed;
        addRequirements(extenderSubsystem);
    }

    @Override
    public void execute() {
        extenderSubsystem.setMotorSpeed(speed);
    }

    @Override
    public void end(boolean interrupted){
        extenderSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if(speed < 0 && extenderSubsystem.getLimitSwitch()){
            return true;
        }
        return false;
    }
}

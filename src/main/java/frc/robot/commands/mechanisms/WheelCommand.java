package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.ChuteSubsystem;

public class WheelCommand extends Command{
    private final ChuteSubsystem chuteSubsystem;

    private double speed;

    public WheelCommand(ChuteSubsystem troughSubsystem, double speed){
        this.chuteSubsystem = troughSubsystem;
        this.speed = speed;
        addRequirements(troughSubsystem);
    }

    @Override
    public void execute(){chuteSubsystem.setSpeed(speed);}

    @Override
    public void end(boolean interrupted){
        chuteSubsystem.setSpeed(0);
    }
}

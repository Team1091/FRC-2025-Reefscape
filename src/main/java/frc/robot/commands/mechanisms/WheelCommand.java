package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ChuteSubsystem;

public class WheelCommand extends Command{
    private final ChuteSubsystem troughSubsystem;

    private double speed;

    public WheelCommand(ChuteSubsystem troughSubsystem, double speed){
        this.troughSubsystem = troughSubsystem;
        this.speed = speed;
        addRequirements(troughSubsystem);
    }

    @Override
    public void execute(){troughSubsystem.setSpeed(speed);}

    @Override
    public void end(boolean interrupted){
        troughSubsystem.setSpeed(0.0);
    }
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.TroughSubsystem;

public class TroughCommand extends Command{
    private final TroughSubsystem troughSubsystem;
    public TroughCommand(TroughSubsystem troughSubsystem){
        this.troughSubsystem = troughSubsystem;
        addRequirements(troughSubsystem);

    }

    @Override
    public void execute(){troughSubsystem.fireZeMissle();}

    @Override
    public void end(boolean interrupted){
        troughSubsystem.loadZeMissle();
    }
}

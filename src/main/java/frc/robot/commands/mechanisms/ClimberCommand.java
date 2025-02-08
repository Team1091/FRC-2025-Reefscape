package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.ClimberSubsystem;


public class ClimberCommand extends Command {
    private final ClimberSubsystem climberSubsystem;

    private final double speed;

    public ClimberCommand(ClimberSubsystem climberSubsystem, double speed) {
        this.climberSubsystem = climberSubsystem;
        this.speed = speed;
        addRequirements(climberSubsystem);
    }

    @Override
    public void execute() {
        climberSubsystem.setSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        climberSubsystem.setSpeed(0.0);
    }
}

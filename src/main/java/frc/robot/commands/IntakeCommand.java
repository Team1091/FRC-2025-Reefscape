package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;


public class IntakeCommand extends Command{
    private final IntakeSubsystem intakeSubsystem;
    public IntakeCommand(IntakeSubsystem intakeSubsystem){
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);

    }

    @Override
    public void execute(){
        intakeSubsystem.setSpeed(Constants.Intake.intakeSpeed);
    }

    @Override
    public void end(boolean interrupted){
        intakeSubsystem.setSpeed(0.0);
    }
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystemFront;


public class IntakeCommandFront extends Command{
    private final IntakeSubsystemFront intakeSubsystemFront;
    public IntakeCommandFront(IntakeSubsystemFront intakeSubsystemFront){
        this.intakeSubsystemFront = intakeSubsystemFront;
        addRequirements(intakeSubsystemFront);

    }

    @Override
    public void execute(){
        intakeSubsystemFront.setSpeed(Constants.Intake.intakeSpeed);
    }

    @Override
    public void end(boolean interrupted){
        intakeSubsystemFront.setSpeed(0.0);
    }
}

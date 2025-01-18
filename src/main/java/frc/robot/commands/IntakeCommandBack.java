package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystemBack;


public class IntakeCommandBack extends Command{
    private final IntakeSubsystemBack intakeSubsystemBack;
    public IntakeCommandBack(IntakeSubsystemBack intakeSubsystemBack){
        this.intakeSubsystemBack = intakeSubsystemBack;
        addRequirements(intakeSubsystemBack);

    }

    @Override
    public void execute(){
        intakeSubsystemBack.setSpeed(Constants.Intake.intakeSpeed);
    }

    @Override
    public void end(boolean interrupted){
        intakeSubsystemBack.setSpeed(0.0);
    }
}

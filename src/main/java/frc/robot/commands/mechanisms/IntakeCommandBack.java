package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.IntakeSubsystemBack;


public class IntakeCommandBack extends Command {
    private final IntakeSubsystemBack intakeSubsystemBack;

    private double speed;

    public IntakeCommandBack(IntakeSubsystemBack intakeSubsystemBack, double speed) {
        this.intakeSubsystemBack = intakeSubsystemBack;
        this.speed = speed;
        addRequirements(intakeSubsystemBack);
    }

    @Override
    public void execute() {
        intakeSubsystemBack.setSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystemBack.setSpeed(0.0);
    }
}

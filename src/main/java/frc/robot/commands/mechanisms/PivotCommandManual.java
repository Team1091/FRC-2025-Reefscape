package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.PivotSubsystem;

public class PivotCommandManual extends Command {
    private final PivotSubsystem pivotSubsystem;

    private double speed;

    public PivotCommandManual(PivotSubsystem pivotSubsystem, double speed) {
        this.pivotSubsystem = pivotSubsystem;
        this.speed = speed;
        addRequirements(pivotSubsystem);
    }

    @Override
    public void execute() {
        if (speed < 0 && pivotSubsystem.getLimitSwitch()) {
            pivotSubsystem.setMotorSpeed(0);
        } else {
            pivotSubsystem.setMotorSpeed(speed);
        }
    }

    @Override
    public void end(boolean interrupted) {
        pivotSubsystem.setMotorSpeed(0);
    }
}

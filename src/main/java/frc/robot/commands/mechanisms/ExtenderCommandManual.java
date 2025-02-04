package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

public class ExtenderCommandManual extends Command {
    private final ExtenderSubsystem extenderSubsystem;

    private double speed;

    public ExtenderCommandManual(ExtenderSubsystem extenderSubsystem, double speed) {
        this.extenderSubsystem = extenderSubsystem;
        this.speed = speed;
        addRequirements(extenderSubsystem);
    }

    @Override
    public void execute() {
        if (speed < 0 && extenderSubsystem.getLimitSwitch()) {
            extenderSubsystem.setMotorSpeed(0);
        } else {
            extenderSubsystem.setMotorSpeed(speed);
        }
    }

    @Override
    public void end(boolean interrupted) {
        extenderSubsystem.setMotorSpeed(0);
    }
}

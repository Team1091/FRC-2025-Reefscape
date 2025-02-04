package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

public class ExtenderCommandAutomatic extends Command {
    private final ExtenderSubsystem extenderSubsystem;

    private final boolean isOut;
    private int motorDirection = -1;

    public ExtenderCommandAutomatic(ExtenderSubsystem extenderSubsystem, boolean isOut) {
        this.extenderSubsystem = extenderSubsystem;
        this.isOut = isOut;
        addRequirements(extenderSubsystem);
    }

    @Override
    public void initialize() {
        if (isOut) {
            motorDirection = 1;
        }
    }

    @Override
    public void execute() {
        extenderSubsystem.setMotorSpeed(Constants.Extender.speed * motorDirection);
    }

    @Override
    public void end(boolean interrupted) {
        extenderSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished() {
        if (isOut) {
            return extenderSubsystem.getEncoderPosition() >= Constants.Extender.outPosition;
        } else {
            return extenderSubsystem.getLimitSwitch();
        }
    }
}

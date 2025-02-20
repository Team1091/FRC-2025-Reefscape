package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

public class ExtenderCommandAutomatic extends Command {
    private final ExtenderSubsystem extenderSubsystem;

    private ExtenderPosition extenderPosition;
    private double endPosition = 0;
    private int motorDirection = 1;

    public ExtenderCommandAutomatic(ExtenderSubsystem extenderSubsystem, ExtenderPosition extenderPosition) {
        this.extenderSubsystem = extenderSubsystem;
        this.extenderPosition = extenderPosition;
        addRequirements(extenderSubsystem);
    }

    @Override
    public void initialize() {
        endPosition = switch (extenderPosition) {
            case score -> Constants.Extender.scorePosition;
            case algae -> Constants.Extender.algaePosition;
            default -> 0;
        };

        if (extenderSubsystem.getEncoderPosition() > endPosition) {
            motorDirection = -1;
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
        if (extenderSubsystem.getLimitSwitch() && motorDirection == -1) {
            return true;
        }
        if (endPosition == 0) {
            return extenderSubsystem.getLimitSwitch();
        } else {
            if (extenderSubsystem.getEncoderPosition() <= endPosition && motorDirection == -1) {
                return true;
            }
            return extenderSubsystem.getEncoderPosition() >= endPosition && motorDirection == 1;
        }
    }
}

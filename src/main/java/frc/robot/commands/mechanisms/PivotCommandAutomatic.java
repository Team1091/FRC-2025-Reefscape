package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.enums.PivotPosition;
import frc.robot.subsystems.mechanisms.PivotSubsystem;

public class PivotCommandAutomatic extends Command {
    private final PivotSubsystem pivotSubsystem;

    private final PivotPosition pivotPosition;
    private double endPosition = 0;
    private int motorDirection = 1;

    public PivotCommandAutomatic(PivotSubsystem pivotSubsystem, PivotPosition pivotPosition) {
        this.pivotSubsystem = pivotSubsystem;
        this.pivotPosition = pivotPosition;
        addRequirements(pivotSubsystem);
    }

    @Override
    public void initialize() {
        endPosition = switch (pivotPosition) {
            case score -> Constants.Pivot.scorePosition;
            case out -> Constants.Pivot.outPosition;
            default -> 0;
        };

        if (pivotSubsystem.getEncoderPosition() > endPosition) {
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        pivotSubsystem.setMotorSpeed(Constants.Pivot.speed * motorDirection);

    }

    @Override
    public void end(boolean interrupted) {
        pivotSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished() {
        if (pivotSubsystem.getLimitSwitch() && motorDirection == -1) {
            return true;
        }
        if (endPosition == 0) {
            return pivotSubsystem.getLimitSwitch();
        } else {
            if (pivotSubsystem.getEncoderPosition() <= endPosition && motorDirection == -1) {
                return true;
            }
            return pivotSubsystem.getEncoderPosition() >= endPosition && motorDirection == 1;
        }
    }
}

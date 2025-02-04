package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.enums.ElevatorPosition;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;

public class ElevatorCommandAutomatic extends Command {
    private final ElevatorSubsystem elevatorSubsystem;

    private final ElevatorPosition elevatorPosition;
    private double endPosition = 0;
    private int motorDirection = 1;

    public ElevatorCommandAutomatic(ElevatorSubsystem elevatorSubsystem, ElevatorPosition elevatorPosition) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.elevatorPosition = elevatorPosition;
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void initialize() {
        endPosition = switch (elevatorPosition) {
            case l2 -> Constants.Elevator.l2Position;
            case l3 -> Constants.Elevator.l3Position;
            case l4 -> Constants.Elevator.l4Position;
            case algae1 -> Constants.Elevator.algae1Position;
            case algae2 -> Constants.Elevator.algae2Position;
            default -> 0;
        };

        if (elevatorSubsystem.getEncoderPosition() > endPosition) {
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        elevatorSubsystem.setMotorSpeed(Constants.Elevator.speed * motorDirection);
    }

    @Override
    public void end(boolean interrupted) {
        elevatorSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished() {
        if (elevatorSubsystem.getLimitSwitchTop() && motorDirection == 1) {
            return true;
        }
        if (elevatorSubsystem.getLimitSwitchBottom() && motorDirection == -1) {
            return true;
        }
        if (endPosition == 0) {
            return elevatorSubsystem.getLimitSwitchBottom();
        } else {
            if (elevatorSubsystem.getEncoderPosition() <= endPosition && motorDirection == -1) {
                return true;
            }
            return elevatorSubsystem.getEncoderPosition() >= endPosition && motorDirection == 1;
        }
    }
}

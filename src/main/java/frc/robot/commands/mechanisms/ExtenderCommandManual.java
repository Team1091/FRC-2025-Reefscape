package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

public class ExtenderCommandManual extends Command {
    private final ExtenderSubsystem extenderSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;

    private final double speed;

    public ExtenderCommandManual(ExtenderSubsystem extenderSubsystem, ElevatorSubsystem elevatorSubsystem, double speed) {
        this.extenderSubsystem = extenderSubsystem;
        this.elevatorSubsystem = elevatorSubsystem;
        this.speed = speed;
        addRequirements(extenderSubsystem);
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void execute() {
        if (speed < 0 && extenderSubsystem.getLimitSwitch() || elevatorSubsystem.getEncoderPosition() > Constants.Elevator.retractPosition) {
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

package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

public class ElevatorCommandManual extends Command {
    private final ElevatorSubsystem elevatorSubsystem;
    private final ExtenderSubsystem extenderSubsystem;

    private final double speed;

    public ElevatorCommandManual(ElevatorSubsystem elevatorSubsystem, ExtenderSubsystem extenderSubsystem, double speed) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.extenderSubsystem = extenderSubsystem;
        this.speed = speed;
        addRequirements(elevatorSubsystem);
        addRequirements(extenderSubsystem);
    }

    @Override
    public void execute() {
        if (speed > 0 && elevatorSubsystem.getLimitSwitchTop() || speed < 0 && elevatorSubsystem.getLimitSwitchBottom() || elevatorSubsystem.getEncoderPosition() < Constants.Elevator.retractPosition) {
            elevatorSubsystem.setMotorSpeed(0);
        } else {
            elevatorSubsystem.setMotorSpeed(speed);
        }

        if (elevatorSubsystem.getEncoderPosition() < Constants.Elevator.retractPosition && !extenderSubsystem.getLimitSwitch()){
            extenderSubsystem.setMotorSpeed(-Constants.Extender.speed);
        }
    }

    @Override
    public void end(boolean interrupted) {
        elevatorSubsystem.setMotorSpeed(0);
    }
}

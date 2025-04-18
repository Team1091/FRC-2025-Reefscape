package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;

public class ElevatorCommandManual extends Command {
    private final ElevatorSubsystem elevatorSubsystem;

    private final double speed;

    public ElevatorCommandManual(ElevatorSubsystem elevatorSubsystem, double speed) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.speed = speed;
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void execute() {
        if (speed > 0 && elevatorSubsystem.getLimitSwitchTop() || speed < 0 && elevatorSubsystem.getEncoderPosition() < 3){
            elevatorSubsystem.setMotorSpeed(0);
        } else if (speed > 0 && elevatorSubsystem.getEncoderPosition() > Constants.Elevator.l4Position){
            elevatorSubsystem.setMotorSpeed(.4);
        } else {
            elevatorSubsystem.setMotorSpeed(speed);
        }
    }

    @Override
    public void end(boolean interrupted) {
        elevatorSubsystem.setMotorSpeed(0);
    }
}

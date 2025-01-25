package frc.robot.commands.mechanisms;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
public class ElevatorCommandManual extends Command{
    private final ElevatorSubsystem elevatorSubsystem;

    private double speed;

    public ElevatorCommandManual(ElevatorSubsystem elevatorSubsystem, double speed) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.speed = speed;
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void execute() {
        elevatorSubsystem.setMotorSpeed(speed);
    }

    @Override
    public void end(boolean interrupted){
        elevatorSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if(speed > 0 && elevatorSubsystem.getLimitSwitchTop()){
            return true;
        }
        if(speed < 0 && elevatorSubsystem.getLimitSwitchBottom()){
            return true;
        }
        return false;
    }
}

package frc.robot.commands.mechanisms;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.enums.ElevatorPosition;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
public class ElevatorCommand extends Command{
    private final ElevatorSubsystem elevatorSubsystem;

    private ElevatorPosition elevatorPosition;
    private double endPosition = 0;
    private int motorDirection = 1;
    //because Ben says it is cool

    public ElevatorCommand(ElevatorSubsystem elevatorSubsystem, ElevatorPosition elevatorPosition) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.elevatorPosition = elevatorPosition;
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void initialize(){
        switch (elevatorPosition) {
            case l2:
                endPosition = Constants.Elevator.l2Position;
                break;
            
            case l3:
                endPosition = Constants.Elevator.l3Position;
                break;

            case l4:
                endPosition = Constants.Elevator.l4Position;
                break;
            case algae1:
                endPosition = Constants.Elevator.algae1Position;
                break;

            case algae2:
                endPosition = Constants.Elevator.algae2Position;
            default:
                endPosition = 0;
                break;
        }

        if (elevatorSubsystem.getEncoderPosition()> endPosition){
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        elevatorSubsystem.setMotorSpeed(Constants.Elevator.speed * motorDirection);

    }
    @Override
    public void end(boolean interrupted){
        elevatorSubsystem.setMotorSpeed(0);
    }

    @Override
    public boolean isFinished(){
        if (elevatorSubsystem.getEncoderPosition() < endPosition && motorDirection == -1){
            return true;
        }
        if (elevatorSubsystem.getEncoderPosition() > endPosition && motorDirection == 1){
            return true;
        }
        return false;
    }
}

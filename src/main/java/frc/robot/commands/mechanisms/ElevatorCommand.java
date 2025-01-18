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
        if (elevatorPosition == ElevatorPosition.l2){
            endPosition = Constants.Elevator.l2EncoderPosition;
        }else if(elevatorPosition == ElevatorPosition.l3){
            endPosition = Constants.Elevator.l3EncoderPosition;
        }else if(elevatorPosition == ElevatorPosition.l4){
            endPosition = Constants.Elevator.l4Position;
        } else{
            endPosition = 0;
        }

        if (elevatorSubsystem.getEncoderPosition()> endPosition){
            motorDirection = -1;
        }
    }

    @Override
    public void execute() {
        elevatorSubsystem.setMotorSpeed(Constants.Elevator.elevatorSpeed * motorDirection);

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

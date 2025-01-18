package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.mechanisms.IntakeSubsystemFront;


public class IntakeCommandFront extends Command{
    private final IntakeSubsystemFront intakeSubsystemFront;

    private double speed;

    public IntakeCommandFront(IntakeSubsystemFront intakeSubsystemFront, double speed){
        this.intakeSubsystemFront = intakeSubsystemFront;
        this.speed = speed;
        addRequirements(intakeSubsystemFront);
    }

    @Override
    public void execute(){
        intakeSubsystemFront.setSpeed(speed);
    }

    @Override
    public void end(boolean interrupted){
        intakeSubsystemFront.setSpeed(0.0);
    }
}

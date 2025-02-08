package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.ChuteSubsystem;

public class WheelCommand extends Command {
    private final ChuteSubsystem chuteSubsystem;

    private final double speed;

    public WheelCommand(ChuteSubsystem chuteSubsystem, double speed) {
        this.chuteSubsystem = chuteSubsystem;
        this.speed = speed;
        addRequirements(chuteSubsystem);
    }

    @Override
    public void execute() {
        chuteSubsystem.setSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        chuteSubsystem.setSpeed(0);
    }
}

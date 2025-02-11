package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.ChuteSubsystem;

public class LimitSwitchWaitCommand extends Command {
    private final ChuteSubsystem chuteSubsystem;

    private final boolean on;

    public LimitSwitchWaitCommand(ChuteSubsystem chuteSubsystem, boolean on) {
        this.chuteSubsystem = chuteSubsystem;
        this.on = on;
        addRequirements(chuteSubsystem);
    }

    @Override
    public boolean isFinished() {
        return on && chuteSubsystem.getLimitSwitch() || !on && !chuteSubsystem.getLimitSwitch();
    }
}

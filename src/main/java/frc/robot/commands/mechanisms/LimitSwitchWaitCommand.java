package frc.robot.commands.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.enums.ElevatorPosition;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
import frc.robot.subsystems.mechanisms.ChuteSubsystem;

public class LimitSwitchWaitCommand extends Command{
    private final ChuteSubsystem troughSubsystem;
    
    private boolean on;

    public LimitSwitchWaitCommand(ChuteSubsystem troughSubsystem, boolean on) {
        this.troughSubsystem = troughSubsystem;
        this.on = on;
        addRequirements(troughSubsystem);
    }

    @Override
    public boolean isFinished(){
        return on && troughSubsystem.getLimitSwitch() || !on && !troughSubsystem.getLimitSwitch();
    }
}

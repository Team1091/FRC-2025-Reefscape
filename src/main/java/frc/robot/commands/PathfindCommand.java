package frc.robot.commands;

import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.PoseEstimationSubsystem;

public class PathfindCommand extends Command{
    private final PoseEstimationSubsystem poseEstimationSubsystem;

    private boolean toReef;

    public PathfindCommand(PoseEstimationSubsystem poseEstimationSubsystem, boolean toReef){
        this.poseEstimationSubsystem = poseEstimationSubsystem;
        this.toReef = toReef;
    }
    
    @Override
    public void initialize(){
        if(toReef){
            driveToReefCommand().schedule();
        } else {
            driveToCoralStationCommand().schedule();
        }
    }

    public Command driveToReefCommand() {
        PathConstraints constraints = new PathConstraints(1.0, 1.0, Units.degreesToRadians(120), Units.degreesToRadians(240));

        try {
            return AutoBuilder.pathfindThenFollowPath(PathPlannerPath.fromPathFile(poseEstimationSubsystem.getReefSide() + " " + poseEstimationSubsystem.getReefPosition()), constraints);
        } catch (FileVersionException | IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            return new SequentialCommandGroup();
        }
    }

    public Command driveToCoralStationCommand() {
        PathConstraints constraints = new PathConstraints(1.0, 1.0, Units.degreesToRadians(120), Units.degreesToRadians(240));

        try {
            return AutoBuilder.pathfindThenFollowPath(PathPlannerPath.fromPathFile(poseEstimationSubsystem.getCoralStation()), constraints);
        } catch (FileVersionException | IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            return new SequentialCommandGroup();
        }
    }
}

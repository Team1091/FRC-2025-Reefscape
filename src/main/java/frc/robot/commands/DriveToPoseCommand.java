package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.PoseEstimationSubsystem;

import java.util.List;

public class DriveToPoseCommand extends Command {
    private final PoseEstimationSubsystem poseEstimationSubsystem;
    private final double finalX;
    private final double finalY;
    private final Rotation2d finalRotation;

    public DriveToPoseCommand(PoseEstimationSubsystem poseEstimationSubsystem, double finalX, double finalY, double finalRotation) {
        this.poseEstimationSubsystem = poseEstimationSubsystem;
        this.finalX = finalX;
        this.finalY = finalY;
        this.finalRotation = new Rotation2d(finalRotation * (Math.PI / 180));
        addRequirements(poseEstimationSubsystem);
    }

    @Override
    public void initialize() {
        Pose2d initialPose = poseEstimationSubsystem.getCurrentPose();
        Rotation2d heading = initialPose.getTranslation().minus(new Translation2d(finalX, finalY)).getAngle();

        // Create a list of waypoints from poses. Each pose represents one waypoint.
        // The rotation component of the pose should be the direction of travel. Do not use holonomic rotation.
        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
            new Pose2d(initialPose.getX(), initialPose.getY(), heading),
            new Pose2d(finalX, finalY, heading)
        );

        PathConstraints constraints = new PathConstraints(1.0, 1.0, 1.0, 1.0); // The constraints for this path.
        // PathConstraints constraints = PathConstraints.unlimitedConstraints(12.0); // You can also use unlimited constraints, only limited by motor torque and nominal battery voltage

        // Create the path using the waypoints created above
        PathPlannerPath path = new PathPlannerPath(
                waypoints,
                constraints,
                null, // The ideal starting state, this is only relevant for pre-planned paths, so can be null for on-the-fly paths.
                new GoalEndState(0.0, finalRotation) // Goal end state. You can set a holonomic rotation here. If using a differential drivetrain, the rotation will have no effect.
        );

        // Prevent the path from being flipped if the coordinates are already correct
        path.preventFlipping = true;

        AutoBuilder.followPath(path);
    }
}

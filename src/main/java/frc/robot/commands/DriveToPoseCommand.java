// package frc.robot.commands;

// import com.pathplanner.lib.auto.AutoBuilder;
// import com.pathplanner.lib.path.GoalEndState;
// import com.pathplanner.lib.path.PathConstraints;
// import com.pathplanner.lib.path.PathPlannerPath;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.Constants;
// import frc.robot.subsystems.drive.Drive;

// import java.util.List;

// public class DriveToPoseCommand extends Command {
//     private final Drive drive;
//     private final double finalX;
//     private final double finalY;
//     private final Rotation2d finalRotation;

//     public DriveToPoseCommand(Drive drive, double finalX, double finalY, double finalRotation) {
//         this.drive = drive;
//         this.finalX = finalX;
//         this.finalY = finalY;
//         this.finalRotation = new Rotation2d(finalRotation * (Math.PI / 180));
//         addRequirements(drive);
//     }

//     @Override
//     public void initialize() {
//         Pose2d initialPose = drive.getCurrentPose();
//         Rotation2d heading = initialPose.getTranslation().minus(new Translation2d(finalX, finalY)).getAngle();

//         List<Translation2d> bezierPoints = PathPlannerPath.bezierFromPoses(
//                 new Pose2d(initialPose.getX(), initialPose.getY(), heading),
//                 new Pose2d(finalX, finalY, heading)
//         );

//         PathPlannerPath path = new PathPlannerPath(
//                 bezierPoints,
//                 new PathConstraints(Constants.Swerve.maxLinearSpeed, Constants.Swerve.maxLinearAcceleration, Constants.Swerve.maxAngularSpeed, Constants.Swerve.maxAngularAcceleration),
//                 new GoalEndState(0.0, finalRotation)
//         );

//         AutoBuilder.followPath(path);
//     }
// }

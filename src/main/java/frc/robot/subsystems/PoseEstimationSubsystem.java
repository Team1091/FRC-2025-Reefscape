package frc.robot.subsystems;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;

import java.util.function.Supplier;

public class PoseEstimationSubsystem extends SubsystemBase {
    private final SwerveDrivePoseEstimator poseEstimator;
    private final Supplier<Rotation2d> rotationSupplier;
    private final Supplier<SwerveModulePosition[]> modulePositionSupplier;

    private Field2d field;

    public PoseEstimationSubsystem(Supplier<Rotation2d> rotationSupplier, Supplier<SwerveModulePosition[]> modulePositionSupplier){
        this.rotationSupplier = rotationSupplier;
        this.modulePositionSupplier = modulePositionSupplier;

        poseEstimator = new SwerveDrivePoseEstimator(
                Constants.Swerve.kinematics,
                rotationSupplier.get(),
                modulePositionSupplier.get(),
                new Pose2d(),
                Constants.PoseEstimation.stateStdDevs,
                Constants.PoseEstimation.visionMeasurementStdDevs);

//        Shuffleboard.getTab("Main").add("Field", "Field2d", field);
    }

    @Override
    public void periodic(){
        poseEstimator.update(rotationSupplier.get(), modulePositionSupplier.get());

        LimelightHelpers.PoseEstimate limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight");
        poseEstimator.addVisionMeasurement(
                limelightMeasurement.pose,
                limelightMeasurement.timestampSeconds);
    }

    public Pose2d getCurrentPose() {
        return poseEstimator.getEstimatedPosition();
    }

    public void setCurrentPose(Pose2d newPose) {
        poseEstimator.resetPosition(rotationSupplier.get(), modulePositionSupplier.get(), newPose);
    }

    public void resetFieldPosition() {
        setCurrentPose(new Pose2d());
    }
}

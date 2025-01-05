package frc.robot.subsystems.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.PoseEstimationSubsystem;

import static frc.robot.Constants.Swerve.*;

public class Drive extends SubsystemBase {
    private final GyroIO gyroIO;
    private final GyroIO.GyroIOInputs gyroInputs = new GyroIO.GyroIOInputs();
    private final Module[] modules = new Module[4]; // FL, FR, BL, BR

    private boolean isFieldOriented = true;
    private ChassisSpeeds speeds;
    private SwerveModulePosition[] wheelDeltas = new SwerveModulePosition[4];
    private PoseEstimationSubsystem poseEstimationSubsystem;

    public Drive(
            GyroIO gyroIO,
            ModuleIO flModuleIO,
            ModuleIO frModuleIO,
            ModuleIO blModuleIO,
            ModuleIO brModuleIO) {
        this.gyroIO = gyroIO;
        modules[FRONT_LEFT] = new Module(flModuleIO, 0, "FL");
        modules[FRONT_RIGHT] = new Module(frModuleIO, 1, "FR");
        modules[BACK_LEFT] = new Module(blModuleIO, 2, "BL");
        modules[BACK_RIGHT] = new Module(brModuleIO, 3, "BR");

        for (int i = 0; i < 4; i++) {
            wheelDeltas[i] = new SwerveModulePosition();
        }
    }

    public void periodic() {
        gyroIO.updateInputs(gyroInputs);
        for (var module : modules) {
            module.periodic();
        }

        // Stop moving when disabled
        if (DriverStation.isDisabled()) {
            for (var module : modules) {
                module.stop();
            }
        }

        // Update odometry
        for (int i = 0; i < 4; i++) {
            wheelDeltas[i] = modules[i].getPositionDelta();
        }
    }

    /**
     * Runs the drive at the desired velocity.
     */
    public void runVelocity(Translation2d linearVelocity, double omega){
        Rotation2d rotation;
        if (isFieldOriented) {
            rotation = getPose().getRotation();
        } else {
            rotation = new Rotation2d(0);
        }
        ChassisSpeeds chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                linearVelocity.getX() * maxLinearSpeed,
                linearVelocity.getY() * maxLinearSpeed,
                omega * maxAngularSpeed,
                rotation
        );
        runVelocity(chassisSpeeds);
    }

    public void runVelocity(ChassisSpeeds chassisSpeeds) {
        speeds = chassisSpeeds;
        // Calculate module setpoints
        ChassisSpeeds discreteSpeeds = ChassisSpeeds.discretize(chassisSpeeds, 0.02);
        SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(discreteSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(setpointStates, maxLinearSpeed);

        // Send setpoints to modules
        SwerveModuleState[] optimizedSetpointStates = new SwerveModuleState[4];
        for (int i = 0; i < 4; i++) {
            // The module returns the optimized state, useful for logging
            optimizedSetpointStates[i] = modules[i].runSetpoint(setpointStates[i]);
        }

        optimizedSetpointStates = setpointStates;
    }

    /**
     * Stops the drive.
     */
    public void stop() {
        runVelocity(new ChassisSpeeds());
    }

    /**
     * Stops the drive and turns the modules to an X arrangement to resist movement. The modules will
     * return to their normal orientations the next time a nonzero velocity is requested.
     */
    public void stopWithX() {
        Rotation2d[] headings = new Rotation2d[4];
        for (int i = 0; i < 4; i++) {
            headings[i] = moduleTranslations[i].getAngle();
        }
        kinematics.resetHeadings(headings);
        stop();
    }

    public void straightenWheels() {
        Rotation2d[] headings = new Rotation2d[4];
        for (int i = 0; i < 4; i++) {
            headings[i] = new Rotation2d(0);
        }
        kinematics.resetHeadings(headings);
        stop();
    }

    public void setFieldState(boolean bool){
        isFieldOriented = bool;
    }

    public void toggleIsFieldOriented() {
        isFieldOriented = !isFieldOriented;
    }

    /**
     * Runs forwards at the commanded voltage.
     */
    public void runCharacterizationVolts(double volts) {
        for (int i = 0; i < 4; i++) {
            modules[i].runCharacterization(volts);
        }
    }

    /**
     * Returns the average drive velocity in radians/sec.
     */
    public double getCharacterizationVelocity() {
        double driveVelocityAverage = 0.0;
        for (var module : modules) {
            driveVelocityAverage += module.getCharacterizationVelocity();
        }
        return driveVelocityAverage / 4.0;
    }

    /**
     * Returns the module states (turn angles and drive velocities) for all of the modules.
     */

    private SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (int i = 0; i < 4; i++) {
            states[i] = modules[i].getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions() {
        return wheelDeltas;
    }

    public Rotation2d getGyroRotation() {
        return gyroInputs.yawPosition;
    }

    public Pose2d getPose() {
        return poseEstimationSubsystem.getCurrentPose();
    }

    public ChassisSpeeds getRobotRelativeSpeeds() {
        return speeds;
    }

    public void resetGyro() {
        gyroIO.resetGyro();
    }

    public void setPoseEstimationSubsystem(PoseEstimationSubsystem poseEstimationSubsystem) {
        this.poseEstimationSubsystem = poseEstimationSubsystem;
    }
}
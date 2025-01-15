package frc.robot;

import static frc.robot.Constants.Swerve.driveBaseRadius;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;

import java.util.Map;

import com.pathplanner.lib.config.*;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

public final class Constants {

    public static class Swerve {
        public static final int FRONT_LEFT = 0;
        public static final int FRONT_RIGHT = 1;
        public static final int BACK_LEFT = 2;
        public static final int BACK_RIGHT = 3;
        public static final double trackWidthX = Units.inchesToMeters(25.0);
        public static final double trackWidthY = Units.inchesToMeters(25.0);
        public static final double driveBaseRadius = Math.hypot(trackWidthX / 2.0, trackWidthY / 2.0);
        public static final double maxLinearSpeed = Units.feetToMeters(14.5);
        public static final double maxLinearAcceleration = Units.feetToMeters(10);
        public static final double maxAngularSpeed = maxLinearSpeed / driveBaseRadius;
        public static final double maxAngularAcceleration = maxLinearAcceleration / driveBaseRadius;
        public static final Translation2d[] moduleTranslations = {
                new Translation2d(trackWidthX / 2.0, trackWidthY / 2.0), // FL
                new Translation2d(trackWidthX / 2.0, -trackWidthY / 2.0), // FR
                new Translation2d(-trackWidthX / 2.0, trackWidthY / 2.0), // BL
                new Translation2d(-trackWidthX / 2.0, -trackWidthY / 2.0) // BR
                };
        public static final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(moduleTranslations);

    }

    public static final class Trough{
        public static final int outputMotorChannel = 1;
    }

    public static final class PoseEstimation {
        public final static Vector<N3> stateStdDevs = VecBuilder.fill(0.1, 0.1, 0.1);
        //public final static Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(.7,.7,9999999);
        public final static Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0,0,9999999);
    }

    public static final class PathPlanner {
        public static final PPHolonomicDriveController controller = new PPHolonomicDriveController(
                new PIDConstants(.5, 0, 0), // 2.0 Translation constants 3
                new PIDConstants(3, 0, 0) // 1.3 Rotation constants 3
                );
        //public static final RobotConfig config = new RobotConfig(null, null, null, null);

    }
    
    public static final double linearDeadband = 0.1;
    public static final double rotationalDeadband = .1;

}

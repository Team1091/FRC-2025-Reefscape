package frc.robot.subsystems.drive;

import edu.wpi.first.math.geometry.Rotation2d;

public interface ModuleIO {

    class ModuleIOInputs {
        public double drivePositionRad = 0.0;
        public double driveVelocityRadPerSec = 0.0;
        public double driveAppliedVolts = 0.0;
        public double[] driveCurrentAmps = new double[]{};

        public Rotation2d turnAbsolutePosition = new Rotation2d();
        public Rotation2d turnPosition = new Rotation2d();
        public double turnVelocityRadPerSec = 0.0;
        public double turnAppliedVolts = 0.0;
        public double[] turnCurrentAmps = new double[]{};
    }

    /**
     * Updates the set of loggable inputs.
     */
    default void updateInputs(ModuleIOInputs inputs) {
    }

    /**
     * Run the drive motor at the specified voltage.
     */
    default void setDriveVoltage(double volts) {
    }

    /**
     * Run the turn motor at the specified voltage.
     */
    default void setTurnVoltage(double volts) {
    }

    /**
     * Enable or disable brake mode on the drive motor.
     */
    default void setDriveBrakeMode(boolean enable) {
    }

    /**
     * Enable or disable brake mode on the turn motor.
     */
    default void setTurnBrakeMode(boolean enable) {
    }
}
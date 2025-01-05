package frc.robot.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * IO implementation for Pigeon2
 */
public class GyroIONavX implements GyroIO {
    private final AHRS ahrs = new AHRS(SerialPort.Port.kMXP);

    public GyroIONavX() {
        ahrs.reset();
    }

    @Override
    public void updateInputs(GyroIOInputs inputs) {
        inputs.connected = ahrs.isConnected();
        inputs.yawPosition = Rotation2d.fromDegrees(ahrs.getYaw());
        inputs.rollPosition = Rotation2d.fromDegrees(ahrs.getRoll());
        inputs.pitchPosition = Rotation2d.fromDegrees(ahrs.getPitch());
    }

    @Override
    public void resetGyro() {
        ahrs.reset();
    }
}
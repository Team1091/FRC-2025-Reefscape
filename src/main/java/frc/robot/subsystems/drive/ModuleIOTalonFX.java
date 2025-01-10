// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.drive;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.core.CoreCANcoder;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.servohub.ServoHub.ResetMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;

import static frc.robot.Constants.Swerve.*;

/**
 * Module IO implementation for Talon FX drive motor controller, Talon FX turn motor controller, and
 * CANcoder
 *
 * <p>NOTE: This implementation should be used as a starting point and adapted to different hardware
 * configurations (e.g. If using an analog encoder, copy from "ModuleIOSparkMax")
 *
 * <p>To calibrate the absolute encoder offsets, point the modules straight (such that forward
 * motion on the drive motor will propel the robot forward) and copy the reported values from the
 * absolute encoders using AdvantageScope. These values are logged under
 * "/Drive/ModuleX/TurnAbsolutePositionRad"
 */
public class ModuleIOTalonFX implements ModuleIO {
  private final SparkMax driveSparkMax;
  private final SparkMax turnSparkMax;
  private final SparkMaxConfig driveConfig;
  private final SparkMaxConfig turnConfig;
  private final RelativeEncoder driveEncoder;
  private final RelativeEncoder turnRelativeEncoder;
  private final CoreCANcoder cancoder;

  private final StatusSignal<Angle> turnAbsolutePosition;

  // Gear ratios for SDS MK4i L2, adjust as necessary
  private final double DRIVE_GEAR_RATIO = (50.0 / 14.0) * (17.0 / 27.0) * (45.0 / 15.0);
  private final double TURN_GEAR_RATIO = 150.0 / 7.0;

  private  boolean isTurnMotorInverted = true;
  private final Rotation2d absoluteEncoderOffset;

  public ModuleIOTalonFX(int index) {
    switch (index) {
      case FRONT_LEFT:
        driveSparkMax = new SparkMax(6, SparkLowLevel.MotorType.kBrushless);
        turnSparkMax = new SparkMax(5, SparkLowLevel.MotorType.kBrushless);
        cancoder = new CoreCANcoder(2);
        absoluteEncoderOffset = Rotation2d.fromDegrees(260);
        driveSparkMax.setInverted(false);
        break;
      case FRONT_RIGHT:
        driveSparkMax = new SparkMax(8, SparkLowLevel.MotorType.kBrushless);
        turnSparkMax = new SparkMax(7, SparkLowLevel.MotorType.kBrushless);
        cancoder = new CoreCANcoder(3);
        absoluteEncoderOffset = Rotation2d.fromDegrees(45);
        break;
      case BACK_LEFT:
        driveSparkMax = new SparkMax(1, SparkLowLevel.MotorType.kBrushless);
        turnSparkMax = new SparkMax(2, SparkLowLevel.MotorType.kBrushless);
        cancoder = new CoreCANcoder(4);
        absoluteEncoderOffset = Rotation2d.fromDegrees(262.33);
        break;
      case BACK_RIGHT:
        driveSparkMax = new SparkMax(3, SparkLowLevel.MotorType.kBrushless);
        turnSparkMax = new SparkMax(4, SparkLowLevel.MotorType.kBrushless);
        cancoder = new CoreCANcoder(1);
        absoluteEncoderOffset = Rotation2d.fromDegrees(217.5);
        break;
      default:
        throw new RuntimeException("Invalid module index");
    }
    cancoder.getConfigurator().apply(new CANcoderConfiguration());

    turnAbsolutePosition = cancoder.getPosition();
    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0,
        turnAbsolutePosition);

    driveConfig = new SparkMaxConfig();
    turnConfig = new SparkMaxConfig();

    turnConfig.inverted(isTurnMotorInverted);
    driveConfig.smartCurrentLimit(40);
    turnConfig.smartCurrentLimit(20);
    driveConfig.voltageCompensation(12.0);
    turnConfig.voltageCompensation(12.0);

    driveConfig.encoder.uvwMeasurementPeriod(10);
    driveConfig.encoder.uvwAverageDepth(2);

    turnConfig.encoder.uvwMeasurementPeriod(10);
    turnConfig.encoder.uvwAverageDepth(2);

    driveSparkMax.setCANTimeout(250);
    turnSparkMax.setCANTimeout(250);

    driveEncoder = driveSparkMax.getEncoder();
    turnRelativeEncoder = turnSparkMax.getEncoder();

    driveEncoder.setPosition(0.0);
    turnRelativeEncoder.setPosition(driveEncoder.getPosition() * 2.0 * Math.PI);
    //turnConfig.encoder.inverted(true);

    driveSparkMax.setCANTimeout(0);
    turnSparkMax.setCANTimeout(0);

    driveSparkMax.configure(driveConfig, com.revrobotics.spark.SparkBase.ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void updateInputs(ModuleIOInputs inputs) {
    inputs.drivePositionRad = Units.rotationsToRadians(driveEncoder.getPosition()) / DRIVE_GEAR_RATIO;
    inputs.driveVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(driveEncoder.getVelocity()) / DRIVE_GEAR_RATIO;
    inputs.driveAppliedVolts = driveSparkMax.getAppliedOutput() * driveSparkMax.getBusVoltage();
    inputs.driveCurrentAmps = new double[]{driveSparkMax.getOutputCurrent()};

    inputs.turnAbsolutePosition = new Rotation2d(driveEncoder.getPosition() * 2.0 * Math.PI).minus(absoluteEncoderOffset);
    inputs.turnPosition = new Rotation2d(Rotation2d.fromRotations(turnRelativeEncoder.getPosition() / TURN_GEAR_RATIO).getRadians() % (Math.PI * 2.0));
    //inputs.turnPosition = Rotation2d.fromRotations(turnRelativeEncoder.getPosition() / TURN_GEAR_RATIO);
    inputs.turnVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(turnRelativeEncoder.getVelocity()) / TURN_GEAR_RATIO;
    inputs.turnAppliedVolts = turnSparkMax.getAppliedOutput() * turnSparkMax.getBusVoltage();
    inputs.turnCurrentAmps = new double[]{turnSparkMax.getOutputCurrent()};

    BaseStatusSignal.refreshAll(turnAbsolutePosition);
    inputs.turnAbsolutePosition =
        Rotation2d.fromRotations(turnAbsolutePosition.getValueAsDouble())
            .minus(absoluteEncoderOffset);
  }
  @Override
  public void setDriveVoltage(double volts) {
    driveSparkMax.setVoltage(volts);
  }

  @Override
  public void setTurnVoltage(double volts) {
    turnSparkMax.setVoltage(volts);
  }

  @Override
  public void setDriveBrakeMode(boolean enable) {
    driveConfig.idleMode(enable ? SparkBaseConfig.IdleMode.kBrake : SparkBaseConfig.IdleMode.kCoast);
  }

  @Override
  public void setTurnBrakeMode(boolean enable) {
    turnConfig.idleMode(enable ? SparkBaseConfig.IdleMode.kBrake : SparkBaseConfig.IdleMode.kCoast);
  }
}
// RobotBuilder Version: 6.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: RobotContainer.

package frc.robot;
import frc.robot.Constants.Trough;
import frc.robot.commands.*;
import frc.robot.commands.mechanisms.ElevatorCommand;
import frc.robot.commands.mechanisms.TroughCommand;
import frc.robot.enums.ElevatorPosition;
import frc.robot.subsystems.*;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.mechanisms.DealgaeSubsystem;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
import frc.robot.subsystems.mechanisms.IntakeSubsystemBack;
import frc.robot.subsystems.mechanisms.IntakeSubsystemFront;
import frc.robot.subsystems.mechanisms.PivotSubsystem;
import frc.robot.subsystems.mechanisms.TroughSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import static frc.robot.Constants.Swerve.*;

import java.util.List;

public class RobotContainer {

  private static RobotContainer m_robotContainer = new RobotContainer();

  // The robot's subsystems
  private final Drive drive;
  private final PoseEstimationSubsystem poseEstimationSubsystem;
  private final DealgaeSubsystem dealgaeSubsystem;
  private final ElevatorSubsystem elevatorSubsystem;
  private final IntakeSubsystemFront intakeSubsystemFront;
  private final IntakeSubsystemBack intakeSubsystemBack;
  private final PivotSubsystem pivotSubsystem;
  private final TroughSubsystem troughSubsystem;

  // Joysticks
  private final CommandXboxController driver = new CommandXboxController(0);
  private final CommandXboxController secondDriver = new CommandXboxController(1);
  //private final TroughSubsystem troughSubsystem = new TroughSubsystem();

  // A chooser for autonomous commands
  SendableChooser<Command> autoChooser;

  /**
  * The container for the robot.  Contains subsystems, OI devices, and commands.
  */
  private RobotContainer() {
    drive = new Drive(
      new GyroIOPigeon2(),//change if using different gyro
      new ModuleIOTalonFX(FRONT_LEFT),
      new ModuleIOTalonFX(FRONT_RIGHT),
      new ModuleIOTalonFX(BACK_LEFT),
      new ModuleIOTalonFX(BACK_RIGHT)
    );
    
    poseEstimationSubsystem = new PoseEstimationSubsystem(
      drive::getGyroRotation,
      drive::getModulePositions
    );

    drive.configureAutoBuilder(poseEstimationSubsystem);

    dealgaeSubsystem = new DealgaeSubsystem();
    elevatorSubsystem = new ElevatorSubsystem();
    intakeSubsystemFront = new IntakeSubsystemFront();
    intakeSubsystemBack = new IntakeSubsystemBack();
    pivotSubsystem = new PivotSubsystem();
    troughSubsystem = new TroughSubsystem();

    configureButtonBindings();

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    robotEnabled();
  }

  public static RobotContainer getInstance() {
    return m_robotContainer;
  }

  public void robotEnabled(){
    poseEstimationSubsystem.setCurrentPose(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
    drive.straightenWheels();
    drive.resetGyro();
    drive.setFieldState(true);
    FollowPathCommand.warmupCommand().schedule();
  }

  
  private void configureButtonBindings() {
    //Drive
    driver.povUp().onTrue(Commands.runOnce(() -> poseEstimationSubsystem.resetDriveRotation(), poseEstimationSubsystem));
    driver.povLeft().onTrue(Commands.runOnce(drive::toggleIsFieldOriented));
    //driver.rightTrigger().whileTrue( new TroughCommand(troughSubsystem));
    drive.setDefaultCommand(
      DriveCommand.joystickDrive(
        drive,
        () -> { // x+ forward is front, x- is backward
          return driver.getLeftY();
        },
        () -> { // y+ is to the left, y- is to the right
          return -driver.getLeftX();
        },
        () -> { // z+ is rotating counterclockwise
          return -driver.getRightX();
        }
      )
    );

    driver.a().onTrue(generateDriveToPoseCommand(poseEstimationSubsystem, 13.524, 5.651, poseEstimationSubsystem.getCurrentPose().getRotation()));
  }

  public Command generateDriveToPoseCommand(PoseEstimationSubsystem poseEstimationSubsystem, double finalX, double finalY, Rotation2d finalRotation){
    Pose2d targetPose = new Pose2d(finalX, finalY, finalRotation);

    PathConstraints constraints = new PathConstraints(.5, .5, Units.degreesToRadians(180), Units.degreesToRadians(360));

    return AutoBuilder.pathfindToPose(targetPose, constraints, 0);
  }

  public Command scoreCommand(ElevatorPosition level){
    return new SequentialCommandGroup(
      new ElevatorCommand(elevatorSubsystem, level),
      new TroughCommand(troughSubsystem, Constants.Trough.shootSpeed),
      new ElevatorCommand(elevatorSubsystem, ElevatorPosition.down)
    );
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}


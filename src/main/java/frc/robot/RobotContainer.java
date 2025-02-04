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

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.TimerCommand;
import frc.robot.commands.mechanisms.EjectCommand;
import frc.robot.commands.mechanisms.ElevatorCommandAutomatic;
import frc.robot.commands.mechanisms.ElevatorCommandManual;
import frc.robot.commands.mechanisms.ExtenderCommandAutomatic;
import frc.robot.commands.mechanisms.ExtenderCommandManual;
import frc.robot.commands.mechanisms.IntakeCommandBack;
import frc.robot.commands.mechanisms.IntakeCommandFront;
import frc.robot.commands.mechanisms.LimitSwitchWaitCommand;
import frc.robot.commands.mechanisms.PivotCommandAutomatic;
import frc.robot.commands.mechanisms.PivotCommandManual;
import frc.robot.commands.mechanisms.WheelCommand;
import frc.robot.enums.ElevatorPosition;
import frc.robot.enums.PivotPosition;
import frc.robot.subsystems.PoseEstimationSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.mechanisms.ChuteSubsystem;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;
import frc.robot.subsystems.mechanisms.IntakeSubsystemBack;
import frc.robot.subsystems.mechanisms.IntakeSubsystemFront;
import frc.robot.subsystems.mechanisms.PivotSubsystem;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

import static frc.robot.Constants.Swerve.BACK_LEFT;
import static frc.robot.Constants.Swerve.BACK_RIGHT;
import static frc.robot.Constants.Swerve.FRONT_LEFT;
import static frc.robot.Constants.Swerve.FRONT_RIGHT;

public class RobotContainer {

    private static RobotContainer m_robotContainer = new RobotContainer();

    // The robot's subsystems
    private final Drive drive;
    private final PoseEstimationSubsystem poseEstimationSubsystem;
    private final ExtenderSubsystem extenderSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;
    private final IntakeSubsystemFront intakeSubsystemFront;
    private final IntakeSubsystemBack intakeSubsystemBack;
    private final PivotSubsystem pivotSubsystem;
    private final ChuteSubsystem chuteSubsystem;

    // Joysticks
    private final CommandXboxController driver = new CommandXboxController(0);
    private final CommandXboxController secondDriver = new CommandXboxController(1);

    // A chooser for autonomous commands
    private SendableChooser<Command> autoChooser;

    // Vars
    private ElevatorPosition scoreLevel = ElevatorPosition.l4;
    private final List<Translation2d> waypoints = List.of(
            new Translation2d(3, 4),
            new Translation2d(3.75, 2.742),
            new Translation2d(5.219, 2.742),
            new Translation2d(5.956, 4),
            new Translation2d(5.219, 5.316),
            new Translation2d(3.75, 5.316),
            new Translation2d(14.719, 4),
            new Translation2d(13.969, 5.316),
            new Translation2d(12.5, 5.316),
            new Translation2d(11.75, 4),
            new Translation2d(12.5, 2.742),
            new Translation2d(13.969, 2.742)
    );
    private String reefPosition = "right";

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
        extenderSubsystem = new ExtenderSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        intakeSubsystemFront = new IntakeSubsystemFront();
        intakeSubsystemBack = new IntakeSubsystemBack();
        pivotSubsystem = new PivotSubsystem();
        chuteSubsystem = new ChuteSubsystem();

        drive.configureAutoBuilder(poseEstimationSubsystem);

        configureAutonomous();
        configureButtonBindings();
        robotEnabled();
    }

    public static RobotContainer getInstance() {
        return m_robotContainer;
    }

    private void configureAutonomous() {
        NamedCommands.registerCommand("Score L2", scoreCommand(ElevatorPosition.l2));
        NamedCommands.registerCommand("Score L3", scoreCommand(ElevatorPosition.l3));
        NamedCommands.registerCommand("Score L4", scoreCommand(ElevatorPosition.l4));
        NamedCommands.registerCommand("Wait for Coral", new LimitSwitchWaitCommand(chuteSubsystem, true));
        NamedCommands.registerCommand("Dealgae Up", dealageWaitCommand(ElevatorPosition.algae2, 1000));
        NamedCommands.registerCommand("Dealgae Down", dealageWaitCommand(ElevatorPosition.algae1, 1000));

        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    private void configureButtonBindings() {
        //Drive
        driver.povUp().onTrue(Commands.runOnce(poseEstimationSubsystem::resetDriveRotation, poseEstimationSubsystem));
        driver.povLeft().onTrue(Commands.runOnce(drive::toggleIsFieldOriented));
        driver.povRight().onTrue(driveToReefCommand());
        driver.povDown().onTrue(driveToCoralStationCommand());

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

        //Mechanisms
        //Main Driver
        driver.rightTrigger().whileTrue(scoreCommand(scoreLevel));
        driver.rightBumper().whileTrue(scoreTroughCommand());
        driver.rightBumper().onFalse(new PivotCommandAutomatic(pivotSubsystem, PivotPosition.in));


        driver.a().whileTrue(dealgaeCommand(ElevatorPosition.algae1));
        driver.a().onFalse(returnDealgaeCommand());
        driver.x().whileTrue(dealgaeCommand(ElevatorPosition.algae2));
        driver.x().onFalse(returnDealgaeCommand());

        driver.y().whileTrue(new ElevatorCommandManual(elevatorSubsystem, Constants.Elevator.speed));
        driver.b().whileTrue(new ElevatorCommandManual(elevatorSubsystem, -Constants.Elevator.speed));

        driver.leftTrigger().onTrue(new PivotCommandAutomatic(pivotSubsystem, PivotPosition.out));
        driver.leftTrigger().onFalse(new PivotCommandAutomatic(pivotSubsystem, PivotPosition.in));
        driver.leftTrigger().whileTrue(new IntakeCommandFront(intakeSubsystemFront, Constants.Intake.frontSpeed));
        driver.leftBumper().onTrue(toChuteCommand());

        driver.back().whileTrue(new WheelCommand(chuteSubsystem, Constants.Chute.shootSpeed));

        //Second Driver
        secondDriver.povUp().onTrue(Commands.runOnce(() -> setScoreLevel(ElevatorPosition.l4)));
        secondDriver.povLeft().onTrue(Commands.runOnce(() -> setScoreLevel(ElevatorPosition.l3)));
        secondDriver.povDown().onTrue(Commands.runOnce(() -> setScoreLevel(ElevatorPosition.l2)));

        secondDriver.x().onTrue(Commands.runOnce(() -> setReefPosition("left")));
        secondDriver.a().onTrue(Commands.runOnce(() -> setReefPosition("algae")));
        secondDriver.b().onTrue(Commands.runOnce(() -> setReefPosition("right")));

        secondDriver.leftTrigger().whileTrue(new PivotCommandManual(pivotSubsystem, Constants.Pivot.speed));
        secondDriver.leftBumper().whileTrue(new PivotCommandManual(pivotSubsystem, -Constants.Pivot.speed));

        secondDriver.rightTrigger().whileTrue(new ExtenderCommandManual(extenderSubsystem, Constants.Pivot.speed));
        secondDriver.rightBumper().whileTrue(new ExtenderCommandManual(extenderSubsystem, -Constants.Pivot.speed));

        secondDriver.start().whileTrue(new IntakeCommandFront(intakeSubsystemFront, Constants.Intake.frontSpeed));
        secondDriver.back().whileTrue(new IntakeCommandFront(intakeSubsystemFront, -Constants.Intake.frontSpeed));

        secondDriver.y().whileTrue(new IntakeCommandFront(intakeSubsystemFront, Constants.Intake.frontSpeed));
        secondDriver.y().whileTrue(new IntakeCommandBack(intakeSubsystemBack, Constants.Intake.backSpeed));

        secondDriver.povRight().whileTrue(new IntakeCommandBack(intakeSubsystemBack, Constants.Intake.backSpeed));
    }

    public void robotEnabled() {
        poseEstimationSubsystem.setCurrentPose(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        drive.straightenWheels();
        drive.resetGyro();
        drive.setFieldState(true);
        FollowPathCommand.warmupCommand().schedule();
    }

    public void setScoreLevel(ElevatorPosition scoreLevel) {
        this.scoreLevel = scoreLevel;
        SmartDashboard.putString("Score Level", scoreLevel.toString());
    }

    public void setReefPosition(String reefPosition) {
        this.reefPosition = reefPosition;
        SmartDashboard.putString("Reef Position", reefPosition);
    }

    public Command driveToReefCommand() {
        int reefSide = waypoints.indexOf(poseEstimationSubsystem.getCurrentPose().getTranslation().nearest(waypoints)) % 6 + 1;
        PathConstraints constraints = new PathConstraints(3.0, 4.0, Units.degreesToRadians(540), Units.degreesToRadians(720));

        try {
            return AutoBuilder.pathfindThenFollowPath(PathPlannerPath.fromPathFile(reefSide + " " + reefPosition), constraints);
        } catch (FileVersionException | IOException | ParseException e) {
            e.printStackTrace();
            return new SequentialCommandGroup();
        }
    }

    public Command driveToCoralStationCommand() {
        String name = (poseEstimationSubsystem.getCurrentPose().getY() > 4) ? "Left Coral" : "Right Coral";
        if (drive.isOnRed()) {
            name = (poseEstimationSubsystem.getCurrentPose().getY() < 4) ? "Left Coral" : "Right Coral";
        }
        PathConstraints constraints = new PathConstraints(3.0, 4.0, Units.degreesToRadians(540), Units.degreesToRadians(720));

        try {
            return AutoBuilder.pathfindThenFollowPath(PathPlannerPath.fromPathFile(name), constraints);
        } catch (FileVersionException | IOException | ParseException e) {
            e.printStackTrace();
            return new SequentialCommandGroup();
        }
    }

    public Command scoreCommand(ElevatorPosition level) {
        return new SequentialCommandGroup(
                new ElevatorCommandAutomatic(elevatorSubsystem, level),
                new EjectCommand(chuteSubsystem, Constants.Chute.shootSpeed),
                new ElevatorCommandAutomatic(elevatorSubsystem, ElevatorPosition.down)
        );
    }

    public Command dealgaeCommand(ElevatorPosition level) {
        return new SequentialCommandGroup(
                new ElevatorCommandAutomatic(elevatorSubsystem, level),
                new ExtenderCommandAutomatic(extenderSubsystem, true),
                new WheelCommand(chuteSubsystem, Constants.Chute.shootSpeed)
        );
    }

    public Command returnDealgaeCommand() {
        return new ParallelCommandGroup(
                new ElevatorCommandAutomatic(elevatorSubsystem, ElevatorPosition.down),
                new ExtenderCommandAutomatic(extenderSubsystem, false)
        );
    }

    public Command dealageWaitCommand(ElevatorPosition level, int time) {
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        dealgaeCommand(level),
                        new TimerCommand(time)
                ),
                returnDealgaeCommand()
        );

    }

    public Command toChuteCommand() {
        return new SequentialCommandGroup(
                new PivotCommandAutomatic(pivotSubsystem, PivotPosition.in),
                new ParallelRaceGroup(
                        new IntakeCommandFront(intakeSubsystemFront, Constants.Intake.frontSpeed),
                        new IntakeCommandBack(intakeSubsystemBack, Constants.Intake.frontSpeed),
                        new LimitSwitchWaitCommand(chuteSubsystem, true)
                )
        );
    }

    public Command scoreTroughCommand() {
        return new SequentialCommandGroup(
                new PivotCommandAutomatic(pivotSubsystem, PivotPosition.score),
                new ParallelRaceGroup(
                        new IntakeCommandFront(intakeSubsystemFront, -Constants.Intake.frontSpeed),
                        new IntakeCommandBack(intakeSubsystemBack, -Constants.Intake.frontSpeed)
                )
        );
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
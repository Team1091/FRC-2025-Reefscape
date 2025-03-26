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
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.PathfindCommand;
import frc.robot.commands.TimerCommand;
import frc.robot.commands.mechanisms.ClimberCommand;
import frc.robot.commands.mechanisms.ClimberOverrideCommand;
import frc.robot.commands.mechanisms.ElevatorCommandAutomatic;
import frc.robot.commands.mechanisms.ElevatorCommandManual;
import frc.robot.commands.mechanisms.ExtenderCommandAutomatic;
import frc.robot.commands.mechanisms.ExtenderCommandManual;
import frc.robot.commands.mechanisms.LimitSwitchWaitCommand;
import frc.robot.commands.mechanisms.WheelCommand;
import frc.robot.enums.ElevatorPosition;
import frc.robot.enums.ExtenderPosition;
import frc.robot.enums.PivotPosition;
import frc.robot.subsystems.PoseEstimationSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.mechanisms.ChuteSubsystem;
import frc.robot.subsystems.mechanisms.ClimberSubsystem;
import frc.robot.subsystems.mechanisms.ElevatorSubsystem;
import frc.robot.subsystems.mechanisms.ExtenderSubsystem;

import static frc.robot.Constants.Swerve.BACK_LEFT;
import static frc.robot.Constants.Swerve.BACK_RIGHT;
import static frc.robot.Constants.Swerve.FRONT_LEFT;
import static frc.robot.Constants.Swerve.FRONT_RIGHT;

public class RobotContainer {

    private static final RobotContainer m_robotContainer = new RobotContainer();

    // The robot's subsystems
    private final Drive drive;
    private final PoseEstimationSubsystem poseEstimationSubsystem;
    private final ExtenderSubsystem extenderSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;
    //private final IntakeSubsystemFront intakeSubsystemFront;
    //private final PivotSubsystem pivotSubsystem;
    private final ChuteSubsystem chuteSubsystem;
    private final ClimberSubsystem climberSubsystem;

    // Joysticks
    private final CommandXboxController driver = new CommandXboxController(0);
    private final CommandXboxController secondDriver = new CommandXboxController(1);
    private final CommandXboxController buttonBoard = new CommandXboxController(2);

    // A chooser for autonomous commands
    private SendableChooser<Command> autoChooser;

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
        //intakeSubsystemFront = new IntakeSubsystemFront();
        //pivotSubsystem = new PivotSubsystem();
        chuteSubsystem = new ChuteSubsystem();
        climberSubsystem = new ClimberSubsystem();

        drive.configureAutoBuilder(poseEstimationSubsystem);

        configureAutonomous();
        configureButtonBindings();
    }

    public static RobotContainer getInstance() {
        return m_robotContainer;
    }

    public void robotInit() {
        // Set the defaults when powered on
        poseEstimationSubsystem.setCurrentPose(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        drive.straightenWheels();
        drive.resetGyro();
        drive.setFieldState(true);

        FollowPathCommand.warmupCommand().schedule();
    }

    public void robotEnabled() {
        climberSubsystem.resetEncoder();
    }

    private void configureAutonomous() {
        //NamedCommands.registerCommand("Score Trough", scoreTroughCommand());
        NamedCommands.registerCommand("Score L2", scoreCommand(ElevatorPosition.l2));
        NamedCommands.registerCommand("Score L3", scoreCommand(ElevatorPosition.l3));
        NamedCommands.registerCommand("Score L4", scoreCommand(ElevatorPosition.l4));
        NamedCommands.registerCommand("Wait for Coral", new LimitSwitchWaitCommand(chuteSubsystem, true));
        NamedCommands.registerCommand("Dealgae Up", dealageWaitCommand(ElevatorPosition.algae2, 1000));
        NamedCommands.registerCommand("Dealgae Down", dealageWaitCommand(ElevatorPosition.algae1, 1000));
        NamedCommands.registerCommand("Return Dealgae", returnDealgaeCommand());

        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    private void configureButtonBindings() {
        //Main Driver
        driver.povUp().onTrue(Commands.runOnce(poseEstimationSubsystem::resetDriveRotation, poseEstimationSubsystem));
        driver.povLeft().onTrue(Commands.runOnce(drive::toggleIsFieldOriented));
        driver.povRight().toggleOnTrue(new PathfindCommand(poseEstimationSubsystem, true));
        driver.povDown().toggleOnTrue(new PathfindCommand(poseEstimationSubsystem, false));

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

        driver.rightTrigger().toggleOnTrue(scoreCommand(ElevatorPosition.selected));
        driver.rightBumper().whileTrue(new WheelCommand(chuteSubsystem, Constants.Chute.shootSpeed));
        
        driver.a().whileTrue(dealgaeCommand(ElevatorPosition.algae1));
        driver.a().onFalse(returnDealgaeCommand());
        driver.x().whileTrue(dealgaeCommand(ElevatorPosition.algae2));
        driver.x().onFalse(returnDealgaeCommand());

        driver.y().whileTrue(new ElevatorCommandManual(elevatorSubsystem, Constants.Elevator.upSpeed));
        driver.b().whileTrue(new ElevatorCommandManual(elevatorSubsystem, -Constants.Elevator.downSpeed));

        // driver.leftTrigger().whileTrue(pickupCommand());
        // driver.leftTrigger().onFalse(new PivotCommandAutomatic(pivotSubsystem, PivotPosition.in));
        // driver.leftBumper().whileTrue(scoreTroughCommand());
        // driver.leftBumper().onFalse(new PivotCommandAutomatic(pivotSubsystem, PivotPosition.in));

        driver.start().whileTrue(new ClimberCommand(climberSubsystem, Constants.Climber.speed));
        driver.back().whileTrue(new ClimberCommand(climberSubsystem, -Constants.Climber.speed));

        //Button Board
        // buttonBoard.povRight().whileTrue(new PivotCommandManual(pivotSubsystem, Constants.Pivot.speed));
        // buttonBoard.povUp().whileTrue(new PivotCommandManual(pivotSubsystem, -Constants.Pivot.speed));
        // buttonBoard.povUp().toggleOnTrue(new PivotCommandManual(pivotSubsystem, -Constants.Pivot.speed));

        buttonBoard.leftTrigger().whileTrue(new ExtenderCommandManual(extenderSubsystem, Constants.Extender.speed));
        buttonBoard.rightTrigger().whileTrue(new ExtenderCommandManual(extenderSubsystem, -Constants.Extender.speed));

        // buttonBoard.povLeft().whileTrue(new IntakeCommandFront(intakeSubsystemFront, Constants.Intake.suckSpeed));
        // buttonBoard.povDown().whileTrue(new IntakeCommandFront(intakeSubsystemFront, -Constants.Intake.suckSpeed));

        buttonBoard.rightStick().whileTrue(new ClimberOverrideCommand(climberSubsystem, Constants.Climber.speed));
        buttonBoard.a().whileTrue(new ClimberOverrideCommand(climberSubsystem, -Constants.Climber.speed));

        buttonBoard.leftStick().whileTrue(new ElevatorCommandManual(elevatorSubsystem, Constants.Elevator.upSpeed));
        buttonBoard.x().whileTrue(new ElevatorCommandManual(elevatorSubsystem, -Constants.Elevator.downSpeed));

        buttonBoard.b().whileTrue(new WheelCommand(chuteSubsystem, Constants.Chute.shootSpeed));

        buttonBoard.leftBumper().onTrue(Commands.runOnce(drive::toggleDefenseMode));

        //Second Driver
        secondDriver.b().onTrue(Commands.runOnce(poseEstimationSubsystem::setReefPositionRight));
        secondDriver.y().onTrue(Commands.runOnce(poseEstimationSubsystem::setReefPositionAlgae));
        secondDriver.x().onTrue(Commands.runOnce(poseEstimationSubsystem::setReefPositionLeft));

        secondDriver.povUp().onTrue(Commands.runOnce(elevatorSubsystem::setScoreLevelL4));
        secondDriver.povLeft().onTrue(Commands.runOnce(elevatorSubsystem::setScoreLevelL3));
        secondDriver.povDown().onTrue(Commands.runOnce(elevatorSubsystem::setScoreLevelL2));
    }

    // public Command pickupCommand() {
    //     return new SequentialCommandGroup(
    //         new PivotCommandAutomatic(pivotSubsystem, PivotPosition.out),
    //         new IntakeCommandFront(intakeSubsystemFront, Constants.Intake.suckSpeed)
    //     );
    // }

    // public Command scoreTroughCommand() {
    //     return new SequentialCommandGroup(
    //             new PivotCommandAutomatic(pivotSubsystem, PivotPosition.score),
    //             new IntakeCommandFront(intakeSubsystemFront, -Constants.Intake.shootSpeed)
    //     );
    // }

    public Command scoreCommand(ElevatorPosition level) {
        return new SequentialCommandGroup(
                new ElevatorCommandAutomatic(elevatorSubsystem, level),
                new ParallelDeadlineGroup(
                    new TimerCommand(1000),
                    new WheelCommand(chuteSubsystem, Constants.Chute.shootSpeed)
                ),
                new ElevatorCommandAutomatic(elevatorSubsystem, ElevatorPosition.down)
        );
    }

    public Command dealgaeCommand(ElevatorPosition level) {
        return new SequentialCommandGroup(
                new ExtenderCommandAutomatic(extenderSubsystem, ExtenderPosition.algae),
                new ParallelCommandGroup(
                    new ElevatorCommandAutomatic(elevatorSubsystem, level),
                    new WheelCommand(chuteSubsystem, Constants.Chute.shootSpeed)
                    //new PivotCommandAutomatic(pivotSubsystem, PivotPosition.out)
                )
        );
    }

    public Command returnDealgaeCommand() {
        return new SequentialCommandGroup(
            new ExtenderCommandAutomatic(extenderSubsystem, ExtenderPosition.in),
            new ParallelCommandGroup(
                new ElevatorCommandAutomatic(elevatorSubsystem, ElevatorPosition.down)
                // new PivotCommandAutomatic(pivotSubsystem, PivotPosition.in)
            )
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
}
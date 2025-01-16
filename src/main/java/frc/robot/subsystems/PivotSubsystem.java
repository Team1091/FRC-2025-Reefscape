package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class PivotSubsystem extends SubsystemBase{
     public enum PivotPosition {
        in, out, score;
    }
    private boolean overwrite;

    private final SparkMax pivotMotor;
    private final Encoder pivotEncoder;
    private final DigitalInput limitSwitch;
    private static ShuffleboardTab tab = Shuffleboard.getTab("General");
    private static GenericEntry encoderPosition = tab.add("Encoder Position", 0.0).getEntry();
    private static GenericEntry limitSwitchOn = tab.add("Limit Switch", false).getEntry();


    private PivotPosition pivotPosition = PivotPosition.in;

    public PivotSubsystem() {
        this.pivotMotor = new SparkMax(Constants.Pivot.pivotMotor, MotorType.kBrushed);
        this.pivotEncoder = new Encoder(Constants.Pivot.pivotEncoder1, Constants.Pivot.pivotEncoder2, Constants.Pivot.pivotEncoder3);
        this.limitSwitch = new DigitalInput(Constants.Pivot.limitSwitch);
    }

    public void setPivotPosition(PivotPosition pivotPosition) {
        this.pivotPosition = pivotPosition;
    }
    public void setOverwrite(boolean overwrite){this.overwrite = overwrite;}

    @Override
    public void periodic() {
        encoderPosition.setDouble(pivotEncoder.get());
        if (limitSwitch.get() == false) {
            pivotEncoder.reset();
        }
        limitSwitchOn.setBoolean(!limitSwitch.get());

        var position = pivotEncoder.get();
        if (overwrite) {
            pivotMotor.set(-Constants.Pivot.pivotSpeed);
        } else if (pivotPosition == PivotPosition.out && position > Constants.Pivot.outEncoderPosition) {
            pivotMotor.set(Constants.Pivot.pivotSpeed);
        } else if (pivotPosition == PivotPosition.in && position < Constants.Pivot.inEncoderPosition && limitSwitch.get() == true) {
            pivotMotor.set(-Constants.Pivot.pivotSpeed);
        } else if (pivotPosition == PivotPosition.score && position > Constants.Pivot.scorePosition) {
            pivotMotor.set(Constants.Pivot.pivotSpeed);
        } else {
            pivotMotor.set(0);
        }

    }
}

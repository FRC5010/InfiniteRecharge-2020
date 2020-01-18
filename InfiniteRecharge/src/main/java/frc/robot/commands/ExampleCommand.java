/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.Trajectory.State;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RobotContainer;

/**
 * An example command that uses an example subsystem.
 */
public class ExampleCommand extends RamseteCommand {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Trajectory trajectory;
  private Timer timer;
  private double accXDiff = 0;
  private double accYDiff = 0;
  private double totalDistance = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ExampleCommand(Trajectory trajectory, Supplier<Pose2d> pose, RamseteController controller,
      SimpleMotorFeedforward feedforward, DifferentialDriveKinematics kinematics,
      Supplier<DifferentialDriveWheelSpeeds> wheelSpeeds, PIDController leftController, PIDController rightController,
      BiConsumer<Double, Double> outputVolts, Subsystem... requirements) {
    super(trajectory, pose, controller, feedforward, kinematics, wheelSpeeds, leftController, rightController,
        outputVolts, requirements);
    timer = new Timer();
    this.trajectory = trajectory;
    State finalState = trajectory.sample(trajectory.getTotalTimeSeconds());
    totalDistance = finalState.poseMeters.getTranslation().getX();
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    State expState = trajectory.sample(timer.get());
    Pose2d expectedPose = expState.poseMeters;
    DifferentialDriveOdometry odometer = RobotContainer.robotPose.getOdometry();
    Pose2d actualPose = odometer.getPoseMeters();
    double actualX = actualPose.getTranslation().getX();
    double actualY = actualPose.getTranslation().getY();
    double expectedX = expectedPose.getTranslation().getX();
    double expectedY = expectedPose.getTranslation().getY();

    accXDiff += Math.abs(expectedX - actualX);
    accYDiff += Math.abs(expectedY - actualY);
    SmartDashboard.putNumber("Expected xpos", expectedPose.getTranslation().getX());
    SmartDashboard.putNumber("Expected Y pos", expectedPose.getTranslation().getY());
    SmartDashboard.putNumber("Expected heading", expectedPose.getRotation().getDegrees());
    SmartDashboard.putNumber("Expected vel m/s", expState.velocityMetersPerSecond);

    super.execute();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    timer.stop();
    SmartDashboard.putNumber("Avg X Diff per meter ", accXDiff / totalDistance);
    SmartDashboard.putNumber("Avg Y Diff per meter", accYDiff / totalDistance);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return super.isFinished();
  }
}

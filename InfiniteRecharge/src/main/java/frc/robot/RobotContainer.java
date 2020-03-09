
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.commands.IntakeBalls;
import frc.robot.commands.RamseteFollower;
import frc.robot.commands.auto.PickUp2Shoot;
import frc.robot.commands.auto.Shoot3PickUp3;
import frc.robot.commands.auto.ShootAndMove;
import frc.robot.mechanisms.Drive;
import frc.robot.mechanisms.DriveConstants;
import frc.robot.mechanisms.IntakeMech;
import frc.robot.mechanisms.ShaftMechanism;
import frc.robot.mechanisms.Shoot;
import frc.robot.mechanisms.SpinControl;
import frc.robot.mechanisms.TelescopClimb;
import frc.robot.subsystems.DriveTrainMain;
import frc.robot.subsystems.Pose;
import frc.robot.subsystems.VisionOpenSight;
import frc.robot.subsystems.VisionSystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's Mechanisms are defined here...
  private Drive driveMechanism;
  private SpinControl spinControl;
  private ShaftMechanism shaftMechanism;
  private Joystick driver;
  private Joystick operator;
  public Shoot shooter;
  private IntakeMech intake;
  private TelescopClimb climb;
  private VisionSystem climbVision;
  private VisionSystem shooterVision;
  private VisionSystem intakeVision;
  public Pose robotPose;
  private DriveTrainMain driveTrain;
  private SendableChooser<Command> command = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // motors 1-4 are drivetrain,
    // motor 5 is the shooter,
    // motor 6 and 7 are the climb,
    // motor 8 is the shaft,
    // motor 9 is the intake
    // motor 10 is the spinner
    driver = new Joystick(0);
    operator = new Joystick(1);
    Shuffleboard.getTab(ControlConstants.SBTabDriverDisplay);

    // TODO: FIX THESE
    shooterVision = new VisionOpenSight("limelight", 26, 3, 90, ControlConstants.shooterVisionColumn);
    climbVision = new VisionOpenSight("climb", ControlConstants.shooterVisionColumn);
    
    intakeVision = new VisionOpenSight("intake", 20, 0, 3.5, ControlConstants.intakeVisionColumn);

    shooter = new Shoot(operator, driver, climbVision);
    intake = new IntakeMech(operator);
    shaftMechanism = new ShaftMechanism(driver, operator, intake.intakeMain, shooter.shooterMain, climbVision);
    driveMechanism = new Drive(driver, climbVision, intakeVision, intake.intakeMain);
    spinControl = new SpinControl(driver, operator, shaftMechanism.getSubsystem());
    climb = new TelescopClimb(driver, operator);

    robotPose = Drive.robotPose;
    driveTrain = driveMechanism.driveTrain;

    command.setDefaultOption("Shoot and Move", new ShootAndMove(shaftMechanism.shaftClimber,intake.intakeMain, shooter.shooterMain, driveTrain, climbVision, robotPose));
    command.addOption("Pickup 2",new PickUp2Shoot(shaftMechanism.getSubsystem(), shooter.shooterMain, intake.intakeMain, driveTrain, climbVision, robotPose) );
    command.addOption("Shoot and Pickup 3",new Shoot3PickUp3(shaftMechanism.getSubsystem(), shooter.shooterMain, intake.intakeMain, driveTrain, climbVision, robotPose) );
    Shuffleboard.getTab(ControlConstants.SBTabDriverDisplay)
      .getLayout("Auto", BuiltInLayouts.kList).withPosition(ControlConstants.autoColumn, 0).withSize(3, 1)
      .add("Choose an Auto Mode", command).withWidget(BuiltInWidgets.kSplitButtonChooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    robotPose.gyro.zeroYaw();

    return command.getSelected();
  }
}

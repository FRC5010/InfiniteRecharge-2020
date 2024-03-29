
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.auto.BarrelRace;
import frc.robot.commands.auto.BouncePath;
import frc.robot.commands.auto.LeftShoot3RP;
import frc.robot.commands.auto.Shoot3PickUp3;
import frc.robot.commands.auto.Shoot3RpShoot2;
import frc.robot.commands.auto.SlalomRun;
import frc.robot.mechanisms.Drive;
import frc.robot.mechanisms.IntakeMech;
import frc.robot.mechanisms.ShaftMechanism;
import frc.robot.mechanisms.Shoot;
import frc.robot.mechanisms.SpinControl;
import frc.robot.mechanisms.TelescopClimb;
import frc.robot.subsystems.DriveTrainMain;
import frc.robot.subsystems.Pose;
import frc.robot.subsystems.VisionLimeLight;
import frc.robot.subsystems.VisionLimeLightH;
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
  public static boolean singleDriverMode;
  public static Timer timer = new Timer();

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
    // motor 11 and 12 are arm climbers
    // motor 13 is the second shooter
    driver = new Joystick(0);
    operator = new Joystick(1);
    singleDriverMode = ControlConstants.setupSingleDriver(operator);
    operator = singleDriverMode ? driver : operator;
    Shuffleboard.getTab(ControlConstants.SBTabDriverDisplay);
    Shuffleboard.getTab(ControlConstants.SBTabDiagnostics);
    // vision system
    shooterVision = new VisionLimeLight("limelight-shooter", 20.25, 27.48, 90, ControlConstants.shooterVisionColumn);
    //intakeVision = new VisionLimeLightH("limelight-intake", 20, 0, 3.5, ControlConstants.intakeVisionColumn);

    //shooter = new Shoot(operator, driver,shooterVision );
    //intake = new IntakeMech(operator);
    //shaftMechanism = new ShaftMechanism(driver, operator, intake.intakeMain, shooter.shooterMain, shooterVision);
    driveMechanism = new Drive(driver,shooterVision);
    
    // if(singleDriverMode == false){
    //   spinControl = new SpinControl(driver, operator, shaftMechanism.getSubsystem());
    //   climb = new TelescopClimb(driver, operator);
    // }

    robotPose = Drive.robotPose;
    driveTrain = driveMechanism.driveTrain;

    command.setDefaultOption("Shoot3PR",new Shoot3RpShoot2(driveTrain,shooterVision));
    command.addOption("Shoot3TrenchRun", new Shoot3PickUp3(driveTrain, shooterVision));
    command.addOption("LeftShoot3PR",new LeftShoot3RP(driveTrain,shooterVision));
    Shuffleboard.getTab(ControlConstants.SBTabDriverDisplay)
      .getLayout("Auto", BuiltInLayouts.kList).withPosition(ControlConstants.autoColumn, 0).withSize(3, 1)
      .add("Choose an Auto Mode", command).withWidget(BuiltInWidgets.kSplitButtonChooser);
  }
public static boolean getDriverMode(){
  return singleDriverMode;
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

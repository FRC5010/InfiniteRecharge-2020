/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.mechanisms.ShooterConstants;
import frc.robot.subsystems.ShooterMain;
import frc.robot.subsystems.VisionSystem;

public class SpinShooter extends CommandBase {
  /**
   * Creates a new SpinShoot.
   */
  private VisionSystem vision;
  private double shotSpeed = 0;

  ShooterMain shooter;

  public SpinShooter(ShooterMain shooter, VisionSystem vision) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.vision = vision;
    shotSpeed = 0;
    addRequirements(shooter);
    addRequirements(vision);
  }

  public SpinShooter(ShooterMain shooter, VisionSystem vision, double shotSpeed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.vision = vision;
    this.shotSpeed = shotSpeed;
    addRequirements(shooter);
  }

  // gets ball velocity required to reach target point (targetX, targetY) with
  // robot at a specified angle (radians)
  // x = horizontal distance from shooter end to target
  // y = vertical distance from shooter end and target
  // assumptions:
  // no air resistance (would increase required vel)
  // no magnus effect (would change required vel, depending on ball spin)
  public double getBallVel(double targetX, double targetY, double angle) {
    return (Math.sqrt(ShooterConstants.earthGravity) * targetX * (1 / Math.cos(angle)))
        / (Math.sqrt(2) * Math.sqrt(targetX * Math.tan(angle) - targetY));
  }
  // it will probably return NaN if impossible
  // vel approaches infinity as the shooter gets closer to pointing directly at
  // target

  // some rough estimates for required velocities
  // from init line (tx ~= 3.05m, ty ~= 1.6m, a = 54 deg)
  // vel = 7.130 m/s
  // from trench run (tx ~= 5.68m, ty ~= 1.6m, a = 54 deg)
  // vel = 8.858 m/s
  // from middle of field (tx ~= 7.79m, ty ~= 1.6m, a = 54 deg)
  // vel = 9.718 m/s

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (shotSpeed == 0) {
      double distance = vision.getRawValues().getDistance();

      if (distance > 48 && distance < 480) {
        shooter.setPoint(distance * ShooterConstants.distanceToRPM + ShooterConstants.baseSpeed);
      } else {
        shooter.setPoint(ShooterConstants.baseSpeed);
      }
    } else {
      shooter.setPoint(shotSpeed);
    }
    shooter.spinUpWheel();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.end();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

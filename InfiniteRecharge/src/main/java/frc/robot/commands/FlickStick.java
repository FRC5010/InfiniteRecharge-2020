/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ControlConstants;
import frc.robot.subsystems.DriveTrainMain;
import frc.robot.subsystems.Pose;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainMain;

public class FlickStick extends CommandBase {
  /**
   * Creates a new flickStick.
   */
  DriveTrainMain driveTrain;
  Joystick joystick;
  Pose gyro;
  double headingSet = 0;
  double gyroValue = 0;
  boolean firstRun = true;
  double currSet = 0;

  public FlickStick(DriveTrainMain driveTrain, Joystick driver, Pose pose) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driveTrain = driveTrain;
    joystick = driver;
    gyro = pose;
    addRequirements(driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double steerX = driveTrain.scaleInputs(joystick.getRawAxis(ControlConstants.steer));
    double steerY = driveTrain.scaleInputs(-joystick.getRawAxis(ControlConstants.steerY));
    gyroValue = gyro.getHeading();
    double gyroError;
    double orginalGyroError;
    double turnPow;
    double addTurn;
    

    
    if(Math.sqrt(Math.pow(steerX,2) + Math.pow(steerY,2)) > .7){
      if(firstRun){
        currSet = gyroValue;
        firstRun = false;
      }


      addTurn = Math.atan(steerY/steerX);

      if(steerX == 0){
        if(steerY > 0){
          addTurn = 90;
        }else{
          addTurn = 270;
        }

      }else if(steerY == 0){
        if(steerX > 0){
          addTurn = 0;
        }else{
          addTurn = 180;
        }
      }

      headingSet = currSet + addTurn;
      System.out.println(headingSet);
    }else{
      firstRun = false;
    }

    gyroError = headingSet - gyroValue;
    orginalGyroError=gyroError;
    if(gyroError < -180){
      gyroError += 360;
    }else if(gyroError > 180){
      gyroError -=360;
    }

    turnPow = gyroError/180 * .08;
    System.out.println("gyro: " + gyroValue + " gyro Error: " + gyroError + " oge: " + orginalGyroError +" turn pow: " + turnPow + " heading set: " + headingSet);
    SmartDashboard.putNumber("Gyro Error", gyroError);
    SmartDashboard.putNumber("Heading Set", headingSet);
    SmartDashboard.putNumber("Curr Heading", currSet);
    
    if(Math.abs(gyroError) < 2){
      turnPow = 0;
    }

    driveTrain.arcadeDrive(
      driveTrain.scaleInputs(-joystick.getRawAxis(ControlConstants.throttle)), 
      turnPow);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

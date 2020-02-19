/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ExtendBottomClimb extends CommandBase {
  /**
   * Creates a new ExtendBottom.
   */
  private Climber climberSubsystem;
  private Joystick operatorJoystick;
  private boolean isExtended;
  public ExtendBottomClimb(Climber climberSubsystem,Joystick operatorJoystick) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.climberSubsystem = climberSubsystem;
    this.operatorJoystick = operatorJoystick;
    addRequirements(climberSubsystem);
    isExtended = false;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(operatorJoystick.getRawButtonPressed(7) && isExtended == false){
      climberSubsystem.extendBottom();
      isExtended = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isExtended;
  }
}
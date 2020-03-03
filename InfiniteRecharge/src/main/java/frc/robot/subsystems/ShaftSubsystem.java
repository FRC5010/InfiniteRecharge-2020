/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ControlConstants;
import frc.robot.commands.BarrelDefault;

public class ShaftSubsystem extends SubsystemBase {
  /**
   * Creates a new Shooter.
   */

  // Change to speed controller later
  private CANSparkMax barrelMotor;
  private CANPIDController m_pidController;
  private DoubleSolenoid solenoid;
  private boolean isExtended;
  private Joystick controller;
  private DigitalInput bb1;
  private DigitalInput bb2;
  private DigitalInput bb3;
  private boolean isRunning = false;
  private Timer timer;
  private Spinner spinner;

  private int shotCount = 0;
  private int shotTimes = 0;
  private Solenoid ledRing;
  private ShuffleboardLayout barrelLayout;
  private NetworkTableEntry sbState, sbHeight;
  private boolean isLedOn;

  public enum ShaftState {
    fullStop, runningClear, indexing, shooting, shootIndex, shootWait, manual
  }

  public ShaftState state = ShaftState.fullStop;

  public ShaftSubsystem(DigitalInput bb1, DigitalInput bb2, DigitalInput bb3, CANSparkMax motor, Joystick operator,
      DoubleSolenoid solenoid, Solenoid ledRing
      ) {
    this.ledRing = ledRing;
    this.bb1 = bb1;
    this.bb2 = bb2;
    this.bb3 = bb3;
    this.barrelMotor = motor;
    this.solenoid = solenoid;
    this.controller = operator;
    timer = new Timer();
    setDefaultCommand(new BarrelDefault(this));

    ShuffleboardTab driverTab = Shuffleboard.getTab(ControlConstants.SBTabDriverDisplay);
    barrelLayout = driverTab.getLayout("Barrel", BuiltInLayouts.kList).withPosition(ControlConstants.barrelColumn, 0).withSize(1, 4);
    barrelLayout.addBoolean("BB1", bb1::get).withWidget(BuiltInWidgets.kBooleanBox).withSize(1, 1);
    barrelLayout.addBoolean("BB2", bb2::get).withWidget(BuiltInWidgets.kBooleanBox).withSize(1, 1);
    barrelLayout.addBoolean("BB3", bb3::get).withWidget(BuiltInWidgets.kBooleanBox).withSize(1, 1);
    barrelLayout.addString("State", this::getState).withSize(1, 1);
    barrelLayout.addBoolean("Extended", this::isExtended).withSize(1, 1);
    barrelLayout.addNumber("Shot Count", this::getShotCount).withSize(1, 1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("bb1", bb1.get());
    SmartDashboard.putBoolean("bb2", bb2.get());
    SmartDashboard.putBoolean("bb3", bb3.get());
    SmartDashboard.putString("Beam break", state.toString());
    SmartDashboard.putNumber(" shotcount", shotCount);
  }

  public void spinUpShaft(double speed) {
    barrelMotor.set(speed);
  }

  public String getState() {
    return state.toString();
  }

  public ShaftState getShaftState() {
    return state;
  }
  public boolean getBB1() { return bb1.get(); }  
  public boolean getBB2() { return bb2.get(); }  
  public boolean getBB3() { return bb3.get(); }
  public void incShotCount() { shotCount++; }
  public void setShaftState(ShaftState newState) { state = newState; }  
  public void toggleShaftHeight() {
    if (isExtended) {
      lowerShaft();
    } else {
      raiseShaft();
    }
  }

  public void raiseShaft() {
    isExtended = true;
    solenoid.set(DoubleSolenoid.Value.kForward);
    ledRing.set(true);
  }

  public void lowerShaft(){
    isExtended = false;
    solenoid.set(DoubleSolenoid.Value.kReverse);
    ledRing.set(false);
  }
  public void retractSpinner(){
    if(isExtended && spinner.isDeployed()){
      spinner.retract();
    }
  }
  public void setSpinner(Spinner spinner){
    this.spinner = spinner;
  }
  public int getShotCount(){
    return shotCount;
  }
  
 public void toggleLight(){
   
   isLedOn = !isLedOn;
   ledRing.set(isLedOn);  
 }
  public boolean isExtended() { return isExtended; }
}

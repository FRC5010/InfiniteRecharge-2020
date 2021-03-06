/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.mechanisms;

/**
 * Add your docs here.
 */
public class ShaftConstants {
   public static double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
   public static int fwdChannel, revChannel;

   static {
      fwdChannel = 0;
      revChannel = 1;
      kP = .01;
      kI = 0;
      kD = 0;
      kIz = 0;
      kFF = .1;
      kMaxOutput = 40;
      kMinOutput = 1;
      maxRPM = 2900;
   }
}

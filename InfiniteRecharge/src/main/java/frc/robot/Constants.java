/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final double ksVolts = 0.171;
    public static final double kvVoltSecondsPerMeter = 1.55;
    public static final double kaVoltSecondsSquaredPerMeter = 0.319;
    public static final double kPDriveVel = 2.64;

    public static final double kTrackwidthMeters = 0.616;
    public static final DifferentialDriveKinematics kDriveKinematics =
        new DifferentialDriveKinematics(kTrackwidthMeters);

    public static final double kMaxSpeedMetersPerSecond = 7.74;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3.13;
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;

    public static final double distancePerPulse = (.155*Math.PI) / (42*6);
    public static final boolean gyroReversed = false;
}


// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
// 你好。你可以读中文吗？我们是三四0八。

package frc.robot;

import com.ctre.phoenix6.HootAutoReplay;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.LimelightHelpers.PoseEstimate;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.TurretSubsystem;


public class Robot extends TimedRobot {
    private Command m_autonomousCommand;
    TurretSubsystem m_turret = new TurretSubsystem();

    private final RobotContainer m_robotContainer;
    
    /* log and replay timestamp and joystick data */
    private final HootAutoReplay m_timeAndJoystickReplay = new HootAutoReplay()
        .withTimestampReplay()
        .withJoystickReplay();

    public Robot() {
        m_robotContainer = new RobotContainer();
    }

    @Override
    public void robotInit() {
        m_turret.m_turret.setPosition(0);
    }

    @Override
    public void robotPeriodic() {
        m_timeAndJoystickReplay.update();
        CommandScheduler.getInstance().run(); 

        LimelightHelpers.SetIMUMode("limelight", 0);
        // Updating vision with limelight
        var driveState = m_robotContainer.drivetrain.getState();
        double headingDeg = driveState.Pose.getRotation().getDegrees();
        double omegaRps = Units.radiansToRotations(driveState.Speeds.omegaRadiansPerSecond);  

       
        //use tag filtering to decide whether to use mt1 or mt2

        LimelightHelpers.SetRobotOrientation("limelight", headingDeg, omegaRps, 0, 0, 0, 0);
        PoseEstimate llMeasurement;
        if (DriverStation.isAutonomous())
        {
            llMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight");
            //llMeasurement= LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight");
        }
        else 
        {
            llMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight");
        }
        if (llMeasurement != null && llMeasurement.tagCount > 0 && Math.abs(omegaRps) < 2.0) {
            m_robotContainer.drivetrain.addVisionMeasurement(llMeasurement.pose, llMeasurement.timestampSeconds);
        }

       
        

    }

    @Override
    public void disabledInit() {
        LimelightHelpers.SetIMUMode("limelight", 1);
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {
        HttpCamera limelightFeed = new HttpCamera("Limelight Stream", "http://limelight.local:5800/stream.mjpg");
        
        CameraServer.startAutomaticCapture(limelightFeed);
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(m_autonomousCommand);
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {
        HttpCamera limelightFeed = new HttpCamera("Limelight Stream", "http://limelight.local:5800/stream.mjpg");
        
        CameraServer.startAutomaticCapture(limelightFeed);
        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().cancel(m_autonomousCommand);
        }
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}

    @Override
    public void simulationPeriodic() {}
}

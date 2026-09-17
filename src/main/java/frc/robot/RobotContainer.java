// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
// 7.25: autos show up, but behaviour erratic. shooting works, adjustment needed.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Constants.operatorConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IntakeMoverSubsystem;
import frc.robot.subsystems.KickerSubsystem;
import frc.robot.commands.RunIntakeHopper;
import frc.robot.commands.Shoot;


//import frc.robot.subsystems.ShooterSubsystem;

public class RobotContainer {
    private final SendableChooser<Command> autoSelector;

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController driverController0 = new CommandXboxController(0);
    private final CommandXboxController driverController1 = new CommandXboxController(1);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    //subsystem and command declaration 
    private final ShooterSubsystem m_ShooterSubsystem = new ShooterSubsystem();
    private final TurretSubsystem m_TurretSubsystem = new TurretSubsystem();
    private final HopperSubsystem m_HopperSubsystem = new HopperSubsystem();
    private final IntakeSubsystem m_IntakeSubsystem = new IntakeSubsystem();
    private final KickerSubsystem m_KickerSubsystem = new KickerSubsystem();
    private final IntakeMoverSubsystem m_IntakeMoverSubsystem = new IntakeMoverSubsystem();

    

    public RobotContainer() {

        //Named commands for auto
        //these commands MUST have end condition in "isFInished" part of command for it to be able to move on to the next step in pathplanner



        drivetrain.initTelemetry();
        configureBindings();
        autoSelector = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto", autoSelector);
    }

    private void configureBindings() {
        //DRIVETRAIN KEYBINDS
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-driverController0.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-driverController0.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-driverController0.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        driverController0.a().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController0.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController0.getLeftY(), -driverController0.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.

        driverController0.back().and(driverController0.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        driverController0.back().and(driverController0.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        driverController0.start().and(driverController0.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        driverController0.start().and(driverController0.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.
        driverController0.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);

        //COMMAND KEYBINDS
        //CONTROLLER 0
        driverController0.y().whileTrue(new Shoot(m_ShooterSubsystem, m_TurretSubsystem, drivetrain, m_KickerSubsystem));
        driverController0.x().whileTrue(new RunIntakeHopper(m_HopperSubsystem, m_IntakeSubsystem));

        driverController0.rightBumper().whileTrue(m_IntakeMoverSubsystem.intakeMoverSpeed(0.3));
        driverController0.leftBumper().whileTrue(m_IntakeMoverSubsystem.intakeMoverSpeed(-0.3));

        //CONTROLLER 1
        //driverController1.x().whileTrue();

        
    }

    public Command getAutonomousCommand() {
        // Simple drive forward auton
        drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero));
        //return new PathPlannerAuto("Example Auto");
        return autoSelector.getSelected();
    }
}

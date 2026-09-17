// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.Constants.operatorConstants;

/** An example command that uses an example subsystem. */
public class RunIntakeHopper extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  HopperSubsystem m_hopper;
  IntakeSubsystem m_intake;

  private final Timer m_timer = new Timer();
  //private final RunIntakeHopper m_subsystem;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public RunIntakeHopper(HopperSubsystem hopper, IntakeSubsystem intake) {
    m_hopper = hopper;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(hopper);

    m_intake = intake;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_hopper.setHopperSpeed(operatorConstants.kHopperSpeed);
    m_intake.setIntakeSpeed(operatorConstants.kIntakeSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_hopper.stopHopper();
    m_intake.stopIntake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (DriverStation.isAutonomous()){
      if (m_timer.get()>=3) {
        return true;
      }
      else {
        return false;
      }
    }
    return false;
  }
}

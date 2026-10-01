// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.IntakeMoverSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.Constants.operatorConstants;

/** An example command that uses an example subsystem. */
public class DeployIntake extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  IntakeMoverSubsystem m_intakeMoverMotor;
  

  private final Timer m_timer = new Timer();
  //private final RunIntakeHopper m_subsystem;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public DeployIntake(IntakeMoverSubsystem intakeMoverMotor) {
    

    m_intakeMoverMotor = intakeMoverMotor;
    addRequirements(intakeMoverMotor);
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
    m_intakeMoverMotor.setSpeed(operatorConstants.kIntakeSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeMoverMotor.stopIntakeMover();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (DriverStation.isAutonomous()){
      if (m_timer.get()>=operatorConstants.kIntakeDeployTime) {
        return true;
      }
      else {
        return false;
      }
    }
    return false;
  }
}

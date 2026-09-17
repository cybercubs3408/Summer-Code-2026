// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import frc.robot.Constants.operatorConstants;

public class ShooterSubsystem extends SubsystemBase 
{
  public TalonFX m_leader;
  public TalonFX m_follower;
  /** Creates a new ExampleSubsystem. */

  // setup shooter subsystem
  public ShooterSubsystem()
   {
   m_leader= new TalonFX(operatorConstants.kShooterLeaderId); 
   m_follower= new TalonFX(operatorConstants.kShooterFollowerId);
   m_follower.setControl(new Follower(operatorConstants.kShooterLeaderId, MotorAlignmentValue.Opposed));

  var talonFXConfigs = new TalonFXConfiguration();

  // set slot 0 gains
  var slot0Configs = talonFXConfigs.Slot0;
  slot0Configs.kS = operatorConstants.kShooterS; // Add 0.25 V output to overcome static friction
  slot0Configs.kV = operatorConstants.kShooterV; // A velocity target of 1 rps results in 0.12 V output
  slot0Configs.kA = operatorConstants.kShooterA; // An acceleration of 1 rps/s requires 0.01 V output
  slot0Configs.kP = operatorConstants.kShooterP; // An error of 1 rps results in 0.11 V output
  slot0Configs.kI = operatorConstants.kShooterI; // no output for integrated error
  slot0Configs.kD = operatorConstants.kShooterD; // no output for error derivative

  // set Motion Magic Velocity settings
  var motionMagicConfigs = talonFXConfigs.MotionMagic;
  motionMagicConfigs.MotionMagicAcceleration = 400; // Target acceleration of 400 rps/s (0.25 seconds to max)
  motionMagicConfigs.MotionMagicJerk = 4000; // Target jerk of 4000 rps/s/s (0.1 seconds)

  m_leader.getConfigurator().apply(talonFXConfigs);

  }
  public void setShooterSpeed(double speed)
  {
    final MotionMagicVelocityVoltage m_request = new MotionMagicVelocityVoltage(0); //should this be zero??
    m_leader.setControl(m_request.withVelocity(speed));
    
  }

  public void stopShooter()
  {
    m_leader.set(0);
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command runShooter()
  {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return this.startEnd(
      ()->{
        setShooterSpeed(operatorConstants.kShooterSpeed);
      },
      () -> {
        stopShooter();
      }

    );
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}

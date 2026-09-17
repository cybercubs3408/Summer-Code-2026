// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import frc.robot.Constants.operatorConstants;

public class TurretSubsystem extends SubsystemBase 
{
  public TalonFX m_turret;
  
  /** Creates a new ExampleSubsystem. */

  // setup shooter subsystem
  public TurretSubsystem()
   {
   m_turret= new TalonFX(operatorConstants.kTurretMotorId); /**add correct device id's later */
   // in init function
    var talonFXConfigs = new TalonFXConfiguration();

    // set slot 0 gains
    var slot0Configs = talonFXConfigs.Slot0;
    slot0Configs.kS = operatorConstants.kTurretS; // Add 0.25 V output to overcome static friction
    slot0Configs.kV = operatorConstants.kTurretV; // A velocity target of 1 rps results in 0.12 V output
    slot0Configs.kA = operatorConstants.kTurretA; // An acceleration of 1 rps/s requires 0.01 V output
    slot0Configs.kP = operatorConstants.kTurretP; // A position error of 2.5 rotations results in 12 V output
    slot0Configs.kI = operatorConstants.kTurretI; // no output for integrated error
    slot0Configs.kD = operatorConstants.kTurretD; // A velocity error of 1 rps results in 0.1 V output

    // set Motion Magic settings
    var motionMagicConfigs = talonFXConfigs.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 20; // Target cruise velocity of 80 rps
    motionMagicConfigs.MotionMagicAcceleration = 40; // Target acceleration of 160 rps/s (0.5 seconds)
    motionMagicConfigs.MotionMagicJerk = 400; // Target jerk of 1600 rps/s/s (0.1 seconds)

    m_turret.getConfigurator().apply(talonFXConfigs);
    m_turret.setPosition(0); //resets zero position of turret at startup
    

  }
  public void setTurretSpeed(double speed)
  {
    m_turret.set(speed);
  }

  public void stopTurret()
  {
    m_turret.set(0);
  }

  public void goToLocation(double rotations)
  {
    // create a Motion Magic request, voltage output
    final MotionMagicVoltage m_request = new MotionMagicVoltage(0);

    // set target position to rotations
    m_turret.setControl(m_request.withPosition(rotations));
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command moveTurretToZero()
  {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return this.startEnd(
      ()->{
        goToLocation(0);
      },
      () -> {
        stopTurret();
      }

    );
  }

  public Command moveTurretToOne()
  {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return this.startEnd(
      ()->{
        goToLocation(1);
      },
      () -> {
        stopTurret();
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

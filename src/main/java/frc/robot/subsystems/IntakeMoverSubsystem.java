// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import frc.robot.Constants.operatorConstants;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeMoverSubsystem extends SubsystemBase 
{
  
    private final SparkMax m_IntakeMoverMotor;
    private SparkMaxConfig m_config;
    
  /** Creates a new ExampleSubsystem. */

  // setup hopper subsystem
  public IntakeMoverSubsystem()
   {
   
    m_IntakeMoverMotor = new SparkMax(operatorConstants.kIntakeMoverMotorId, MotorType.kBrushless);
   //brushed or brushless?
    m_config = new SparkMaxConfig();
    m_config.idleMode(IdleMode.kBrake);
    /*m_config.softLimit
            .forwardSoftLimit(42.0)            // Max position threshold
            .forwardSoftLimitEnabled(true)     // Turn the forward limit on
            .reverseSoftLimit(0.0)             // Min position threshold
            .reverseSoftLimitEnabled(true);*/ 
    m_IntakeMoverMotor.configure(m_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }


    

  
  public void setSpeed(double speed)
  {
    m_IntakeMoverMotor.set(speed);
    SmartDashboard.putNumber("adjustedShootAngle", m_IntakeMoverMotor.getEncoder().getPosition());
  }

  public void stopIntakeMover()
  {
    m_IntakeMoverMotor.set(0);
  }

  

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command intakeMoverSpeed(double speed)
  {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return this.startEnd(
      ()->{
        setSpeed(speed);
      },
      () -> {
        stopIntakeMover();
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

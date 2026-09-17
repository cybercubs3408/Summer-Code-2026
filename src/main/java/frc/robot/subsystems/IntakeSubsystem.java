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

public class IntakeSubsystem extends SubsystemBase 
{
  
    private final SparkMax m_IntakeMotor;
  /** Creates a new ExampleSubsystem. */

  // setup hopper subsystem
  public IntakeSubsystem()
   {
   
    m_IntakeMotor = new SparkMax(operatorConstants.kIntakeMotorId, MotorType.kBrushless);
   //brushed or brushless?
    

  }
  public void setIntakeSpeed(double speed)
  {
    m_IntakeMotor.set(speed);
  }

  public void stopIntake()
  {
    m_IntakeMotor.set(0);
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command runIntake()
  {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return this.startEnd(
      ()->{
        setIntakeSpeed(operatorConstants.kIntakeSpeed);
      },
      () -> {
        stopIntake();
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

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

public class KickerSubsystem extends SubsystemBase 
{
  
    private final SparkMax m_KickerMotor;
  /** Creates a new ExampleSubsystem. */

  // setup hopper subsystem
  public KickerSubsystem()
   {
   
    m_KickerMotor = new SparkMax(operatorConstants.kKickerMotorId, MotorType.kBrushless);
   //brushed or brushless?
    

  }
  public void setKickerSpeed(double speed)
  {
    m_KickerMotor.set(speed);
  }

  public void stopKicker()
  {
    m_KickerMotor.set(0);
  }

  /**
   * 
   * Example command factory method.
   *
   * @return a command
   */
  public Command runKicker()
  {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return this.startEnd(
      ()->{
        setKickerSpeed(operatorConstants.kKickerSpeed);
      },
      () -> {
        stopKicker();
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

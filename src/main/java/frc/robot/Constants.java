package frc.robot;

public class Constants {
    public static class operatorConstants{
        /**Shooter Variables */
        public static final int kShooterLeaderId=14;
        public static final int kShooterFollowerId=15;
        public static final double kShooterSpeed=40; //in rps
        public static final double kShooterBoost = 1.15;

        //constants below are copied from ctre website
        public static final double kShooterS=0.25;
        public static final double kShooterV=0.12;
        public static final double kShooterA=0.01;
        public static final double kShooterP=0.11;
        public static final double kShooterI=0;
        public static final double kShooterD=0;

        /**Turret variables */
        public static final int kTurretMotorId=16;
        
        public static final double kTurretS=0.41;
        public static final double kTurretV=0.069;
        public static final double kTurretA=0.01;
        public static final double kTurretP=15;
        public static final double kTurretI=0;
        public static final double kTurretD=0.25;

        //Hopper variables
        public static final int kHopperMotorId=19;
        //hopper motor is inverted. negative speed=ball goes in
        public static final double kHopperSpeed=-0.5;

        //intake variables
        public static final int kIntakeMotorId=17;
        //intake motor is inverted. Negative speed=ball goes in
        public static final double kIntakeSpeed=-0.5;

        //intake mover varialbes
         public static final int kIntakeMoverMotorId=18;
        public static final double kIntakeMoverSpeed=0.5;

         //kicker varialbes
         public static final int kKickerMotorId=22;
         public static final double kKickerSpeed=0.5;

    
    }
}

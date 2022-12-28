package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.concurrent.TimeUnit;

// @Disabled
@TeleOp(name = "Stack Pickup 2")

public class ServoAndSlidesManualControl_2 extends LinearOpMode {

    static final double SCALE       = 0.01;     // Joystick scaling for servo increment value
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position

    // Define class members
    Servo  spinOne;
    Servo  spinTwo;
    Servo  armRote;
    Servo  liftWrist;
    Servo  armGrip;
    DcMotor slideOne;
    DcMotor slideTwo;
    DcMotor leftFront;
    DcMotor leftRear;
    DcMotor rightFront;
    DcMotor rightRear;
    DistanceSensor rearDistance;
    DistanceSensor clawDistance;
    DistanceSensor frontDistance;

    double  position1 = 0.95;
    double  position2 = 0.95;
    double  position3 = 0.13;
    double  position4 = 0.6;
    double  position5 = 0.0;
    int desiredPos = 0;
    boolean high = false;
    boolean movingDown = false;
    double ARM_POWER    =  1 ;
    boolean stackPickUp = false;

    @Override
    public void runOpMode() {



        // Slide Init
        slideOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideOne.setDirection(DcMotor.Direction.FORWARD);
        slideTwo.setDirection(DcMotor.Direction.FORWARD);
        slideOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Servo direction
        spinOne.setDirection(Servo.Direction.FORWARD);
        spinTwo.setDirection(Servo.Direction.REVERSE);
        armRote.setDirection(Servo.Direction.FORWARD);
        liftWrist.setDirection(Servo.Direction.FORWARD);
        armGrip.setDirection(Servo.Direction.FORWARD);

        // Wait for the start button
        telemetry.addData(">", "Press Start to move Servos with left joystick and triggers." );
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            if((gamepad1.right_stick_y < 0)&&(gamepad1.right_stick_button==false)&&(slideOne.getCurrentPosition()<4900)&&(slideTwo.getCurrentPosition()<4900)){ //slides up
                slideOne.setPower(-gamepad1.right_stick_y);
                slideTwo.setPower(-gamepad1.right_stick_y);
            }
            else if((gamepad1.right_stick_y > 0)&&(gamepad1.right_stick_button==false)&&(slideOne.getCurrentPosition()>10)&&(slideTwo.getCurrentPosition()>10)) {
                slideOne.setPower(-gamepad1.right_stick_y);
                slideTwo.setPower(-gamepad1.right_stick_y);
            }
            else {
                slideOne.setPower(0);
                slideTwo.setPower(0);
            }

            if (gamepad1.left_stick_y != 0) { //swing
                position1 += gamepad1.left_stick_y * SCALE;
                position2 += gamepad1.left_stick_y * SCALE;
                if (position1 >= MAX_POS) {
                    position1 = MAX_POS;
                }
                if (position1 <= MIN_POS) {
                    position1 = MIN_POS;
                }
                if (position2 >= MAX_POS) {
                    position2 = MAX_POS;
                }
                if (position2 <= MIN_POS) {
                    position2 = MIN_POS;
                }
            }
            if ((gamepad1.right_stick_x != 0)&&(gamepad1.right_stick_button==false)) { //armRotate
                position3 += gamepad1.right_stick_x * SCALE;
                if (position3 >= MAX_POS) {
                    position3 = MAX_POS;
                }
                if (position3 <= MIN_POS) {
                    position3 = MIN_POS;
                }
            }

            if (gamepad1.right_stick_button) { //wrist up/down
                position4 += gamepad1.right_stick_y * SCALE;
                if (position4 >= MAX_POS) {
                    position4 = MAX_POS;
                }
                if (position4 <= MIN_POS) {
                    position4 = MIN_POS;
                }
            }

            if (gamepad1.x) {
               position1 = 0;
               position2 = 0;
               position3 = 0;
            }
            if (gamepad1.y) {
                position1 = 1;
                position2 = 1;
                position3 = 1;
            }



            if (gamepad1.left_bumper) {
                position5 = .18;
            }
            if (gamepad1.right_bumper) {
                position5 = 0;
            }
            double rangeRear = rearDistance.getDistance(DistanceUnit.CM);
            double rangeClaw = clawDistance.getDistance(DistanceUnit.CM);
            double rangeFront = frontDistance.getDistance(DistanceUnit.CM);
            if (gamepad1.dpad_up){
                position5 = 0;
                desiredPos = 1600;
                slideOne.setTargetPosition(desiredPos);
                slideTwo.setTargetPosition(desiredPos);
                slideOne.setPower(ARM_POWER);
                slideTwo.setPower(ARM_POWER);
                slideOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideTwo.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideOne.setPower(0);
                slideTwo.setPower(0);
                for (long stop = System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(800); stop>System.nanoTime();) {

                }
                stackPickUp = true;
                while (stackPickUp){
                    ARM_POWER = .75;
                    desiredPos = 5;
                    slideOne.setTargetPosition(desiredPos);
                    slideTwo.setTargetPosition(desiredPos);
                    slideOne.setPower(ARM_POWER);
                    slideTwo.setPower(ARM_POWER);
                    slideOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slideTwo.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slideOne.setPower(0);
                    slideTwo.setPower(0);
                    if (rangeClaw <= 2.75){
                        stackPickUp = false;
                        ARM_POWER = 1;
                        armGrip.setPosition(position5);
                        desiredPos = 1600;
                        slideOne.setTargetPosition(desiredPos);
                        slideTwo.setTargetPosition(desiredPos);
                        slideOne.setPower(ARM_POWER);
                        slideTwo.setPower(ARM_POWER);
                        slideOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        slideTwo.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        slideOne.setPower(0);
                        slideTwo.setPower(0);


                    }
                }
            }

            // Display the current value
            telemetry.addData("spinOne Servo Position", "%5.2f", spinOne.getPosition());
            telemetry.addData("spinTwo Servo Position", "%5.2f", spinTwo.getPosition());
            telemetry.addData("armRote Servo Position", "%5.2f", armRote.getPosition());
            telemetry.addData("liftWrist Servo Position", "%5.2f", liftWrist.getPosition());
            telemetry.addData("armGrip Servo Position", "%5.2f", armGrip.getPosition());
            telemetry.addData("slideOnePos: ", slideOne.getCurrentPosition());
            telemetry.addData("slideTwoPos: ", slideTwo.getCurrentPosition());
            telemetry.addData(">", "Press Stop to end test." );
            telemetry.update();

            // Set the servos to the new positions
            spinOne.setPosition(position1);
            spinTwo.setPosition(position2);
            armRote.setPosition(position3);
            liftWrist.setPosition(position4);
            armGrip.setPosition(position5);

            idle();
        }
        // Servo names in hardware map on Control or Expansion Hubs
        spinOne = hardwareMap.get(Servo.class, "spinOne");
        spinTwo = hardwareMap.get(Servo.class, "spinTwo");
        armRote = hardwareMap.get(Servo.class, "armRote");
        liftWrist = hardwareMap.get(Servo.class, "liftWrist");
        armGrip = hardwareMap.get(Servo.class, "armGrip");
        slideOne = hardwareMap.get(DcMotor.class, "slideOne");
        slideTwo = hardwareMap.get(DcMotor.class, "slideTwo");
        clawDistance = hardwareMap.get(DistanceSensor.class, "clawDistance");
    }

}

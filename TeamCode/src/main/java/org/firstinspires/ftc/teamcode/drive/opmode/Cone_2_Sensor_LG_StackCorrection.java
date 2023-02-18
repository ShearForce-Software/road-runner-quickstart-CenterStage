package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "2 Cone Auto Color - LG Stack Correction")

public class Cone_2_Sensor_LG_StackCorrection extends LinearOpMode {

    ColorSensor sensorColorLeft;
    ColorSensor sensorColorRight;
    DistanceSensor sensorDistanceLeft;
    DistanceSensor sensorDistanceRight;
    double rawRangeRight;
    double rawRangeLeft;


    @Override
    public void runOpMode() {

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose=new Pose2d(0,0,Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        // get a reference to the color sensor.
        sensorColorLeft = hardwareMap.get(ColorSensor.class, "frontDistance");
        sensorColorRight = hardwareMap.get(ColorSensor.class, "rearDistance");

        // get a reference to the distance sensor that shares the same name.
        sensorDistanceLeft = hardwareMap.get(DistanceSensor.class, "frontDistance");
        sensorDistanceRight = hardwareMap.get(DistanceSensor.class, "rearDistance");

        // wait for the start button to be pressed.
        waitForStart();

        //while (opModeIsActive()) {
        if (isStopRequested()) return;
        if (opModeIsActive()) {

            rawRangeLeft = sensorDistanceLeft.getDistance(DistanceUnit.INCH);
            rawRangeRight = sensorDistanceRight.getDistance(DistanceUnit.INCH);
            double rangeLeft = 0.0;
            double rangeRight = 0.0;
            double forwardLG = 0.0;
            double shiftLG = 0.0;

            if ((sensorColorLeft.red() > 75 || sensorColorRight.red() > 75) && (sensorColorLeft.red() > sensorColorLeft.blue())) {
                if (rawRangeLeft < 1.78) {
                    rangeLeft = rawRangeLeft * 1.352 - 0.089;
                } else if (rawRangeLeft > 1.78) {
                    rangeLeft = rawRangeLeft * 3.333 - 3.777;
                } else {
                    rangeLeft = rawRangeLeft;
                }
                if (rawRangeRight < 2.23) {
                    rangeRight = rawRangeRight * 1.050 - 0.040;
                } else if (rawRangeRight > 2.23) {
                    rangeRight = rawRangeRight * 2.038 - 2.399;
                } else {
                    rangeRight = rawRangeRight;
                }
                shiftLG = (rangeRight - rangeLeft) * .8;
                forwardLG = ((rangeRight + rangeLeft)/2) - .6;

                if (shiftLG > 1) {
                    shiftLG = 1;
                    forwardLG = rangeLeft - 0.25;
                }
                else if (shiftLG < -1){
                    shiftLG = -1;
                    forwardLG = rangeRight - 0.25;
                }
            }

            if ((sensorColorLeft.blue() > 75 || sensorColorRight.blue() > 75) && (sensorColorLeft.blue() > sensorColorLeft.red())) {
                if (rawRangeLeft < 2.0) {
                    rangeLeft = rawRangeLeft * 1.290 - 0.292;
                } else if (rawRangeLeft > 2.0) {
                    rangeLeft = rawRangeLeft * 3.947 - 5.623;
                } else {
                    rangeLeft = rawRangeLeft;
                }
                if (rawRangeRight < 2.55) {
                    rangeRight = rawRangeRight * 0.948 - 0.085;
                } else if (rawRangeRight > 2.55) {
                    rangeRight = rawRangeRight * 2.838 - 5.360;
                } else {
                    rangeRight = rawRangeRight;
                }
                shiftLG = (rangeRight - rangeLeft) * .85;
                forwardLG = ((rangeRight + rangeLeft)/2) - .5;

                if (shiftLG > 1) {
                    shiftLG = 1;
                    forwardLG = rangeLeft - 0.35;
                }
                else if (shiftLG < -1){
                    shiftLG = -1;
                    forwardLG = rangeRight - 0.35;
                }
            }
            TrajectorySequence stackCorrect = drive.trajectorySequenceBuilder(startPose)

                    .strafeLeft(shiftLG,
                            SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(25))
                    .forward(forwardLG,
                            SampleMecanumDrive.getVelocityConstraint(25,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(25))
                    .build();

            // send the info back to driver station using telemetry function.
            telemetry.addData("Raw Distance Left (in)","%.2f", sensorDistanceLeft.getDistance(DistanceUnit.INCH));
            telemetry.addData("Raw Distance Right (in)","%.2f", sensorDistanceRight.getDistance(DistanceUnit.INCH));

            telemetry.addData("Auto Distance Left / Right (in)","%.2f in   %.2f in", rangeLeft, rangeRight);
            telemetry.addData("Shift Distance Left / Right (in)","%.2f in", shiftLG);
            telemetry.addData("Forward Distance (in)","%.2f in", forwardLG);

            telemetry.addData("Left / Right Red ", "%3d  %3d", sensorColorLeft.red(),sensorColorRight.red());
            telemetry.addData("Left / Right Blue ", "%3d  %3d", sensorColorLeft.blue(),sensorColorRight.blue());
            telemetry.update();

            drive.followTrajectorySequence(stackCorrect);
        }


    }
}

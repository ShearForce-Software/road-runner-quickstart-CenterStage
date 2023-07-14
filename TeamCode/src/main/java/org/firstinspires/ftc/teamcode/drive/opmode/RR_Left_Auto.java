package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

/*
 * Op mode to experiment with Road Runner Autonomous Routes.
 * Utilization of the dashboard is recommended to visualize routes and check path following.
 * To access the dashboard, connect your computer to the RC's WiFi network.
 * In your browser, navigate to https://192.168.49.1:8080/dash if you're using the RC phone
 * or https://192.168.43.1:8080/dash if you are using the Control Hub.
 * Once successfully connected, start the program, and Li'l Gerry will begin driving the route.
 * You should observe the target position (green) and your pose estimate (blue) and adjust your
 * follower PID coefficients such that you follow the target position as accurately as possible.
 * If you are using SampleMecanumDrive, you should be tuning TRANSLATIONAL_PID and HEADING_PID.
 * These coefficients can be tuned live in dashboard.
 */
//@Disabled
@Config
@Autonomous(name = "RR Left Auto")
public class RR_Left_Auto extends LinearOpMode {
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    static final double FEET_PER_METER = 3.28084;
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;
    double tagsize = 0.166;
    AprilTagDetection tagOfInterest = null;
    public static double stackY = -12;
    public static double stackX = -56;
    public static double junctionX = -26;
    public static double junctionY = -6;
    public static double firstConeVel = 55;
    public static double toStackVel = 25;
    public static double toHighVel = 25;

    @Override
    public void runOpMode() throws InterruptedException {
        ArmControl armControl = new ArmControl(false, false, this);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        String step = "none";
        armControl.STACK_POS = 550;
        Pose2d startPose = new Pose2d(-36, -64.5, Math.toRadians(-90));

        Vector2d junctionVec = new Vector2d(junctionX, junctionY);
        Vector2d junctionFirstVec = new Vector2d(-26.5, -6.5);
        Pose2d junctionPos = new Pose2d(junctionX,junctionY, Math.toRadians(-135));

        Vector2d realStackVec = new Vector2d(stackX,stackY);
        Pose2d almostStackPos = new Pose2d(stackX-2, stackY, Math.toRadians(-180));
        Pose2d realStackPos = new Pose2d(stackX, stackY, Math.toRadians(-180));
        TrajectorySequence ToRealStack;
        TrajectorySequence ToHighJunction;
        drive.setPoseEstimate(startPose);
        armControl.Init(hardwareMap);
        armControl.StartPosition(null, false);

        TrajectorySequence FirstCone = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-36, -20), Math.toRadians(90),
                        SampleMecanumDrive.getVelocityConstraint(firstConeVel,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(40))
                .splineToSplineHeading(new Pose2d(junctionFirstVec.getX()-(2*(1/Math.sqrt(2))), junctionFirstVec.getY()-(2*(1/Math.sqrt(2))), Math.toRadians(-135)), Math.toRadians(45),
                        SampleMecanumDrive.getVelocityConstraint(firstConeVel,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(35))
                .splineToConstantHeading(junctionFirstVec, Math.toRadians(45),
                        SampleMecanumDrive.getVelocityConstraint(firstConeVel-20,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(35))
                .build();

        TrajectorySequence ToAlmostStack = drive.trajectorySequenceBuilder(junctionPos)
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(-38, stackY, Math.toRadians(180)), Math.toRadians(180),
                        SampleMecanumDrive.getVelocityConstraint(toStackVel,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(toStackVel))
                .splineToLinearHeading(almostStackPos, Math.toRadians(180),
                        SampleMecanumDrive.getVelocityConstraint(toStackVel,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(toStackVel))
                .build();

        AprilTags();
        waitForStart();
        if (isStopRequested()) return;
        if (opModeIsActive()) {
            
            drive.followTrajectorySequenceAsync(FirstCone);
            SlidesToHighHardCode(armControl, drive);
            armControl.GoToHigh180(drive);
            armControl.WaitForTrajectoryToFinish(drive);
            //armControl.SpecialSleep(drive, 1350);
            armControl.openClaw();

            for (int i = 0; i < 3; i++){
                armControl.openClaw();
                step = "one";
                telemetry.addData("step: ", step);
                telemetry.update();
                drive.followTrajectorySequenceAsync(ToAlmostStack);
                step = "two";
                telemetry.addData("step: ", step);
                telemetry.update();
                armControl.SpecialSleep(drive, 450);
                armControl.closeClaw(); //god only knows why we need this here but it doesn't like to close the claw so
                armControl.ReadyToGrabFromStack(drive);
                step = "two a";
                telemetry.addData("step: ", step);
                telemetry.update();
                armControl.WaitForTrajectoryToFinish(drive);
                step = "two b";
                telemetry.addData("step: ", step);
                telemetry.update();
                //armControl.FindConeCenter();
                ToRealStack = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                        .splineToConstantHeading(realStackVec, Math.toRadians(180),
                                SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(20))
                        .build();
                drive.followTrajectorySequenceAsync(ToRealStack);
                step = "three";
                telemetry.addData("step: ", step);
                telemetry.update();
                armControl.WaitForTrajectoryToFinish(drive);
                step = "four";
                telemetry.addData("step: ", step);
                telemetry.update();
                armControl.GrabFromStack(drive);
                step = "five";
                telemetry.addData("step: ", step);
                telemetry.update();
                armControl.SpecialSleep(drive, 200);
                ToHighJunction = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                        .setReversed(true)
                        .strafeTo(new Vector2d(-44,-12),
                                SampleMecanumDrive.getVelocityConstraint(toHighVel,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(toHighVel))
                        .splineToSplineHeading(new Pose2d(-36, -12, Math.toRadians(-135)), Math.toRadians(0),
                                SampleMecanumDrive.getVelocityConstraint(toHighVel,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(toHighVel))
                        .splineToConstantHeading(junctionVec, Math.toRadians(45),
                                SampleMecanumDrive.getVelocityConstraint(toHighVel,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(toHighVel))
                        .build();
                drive.followTrajectorySequenceAsync(ToHighJunction);
                SlidesToHighHardCode(armControl, drive); //love it sm
                armControl.autoArmToHigh(drive);
                armControl.SpecialSleep(drive, 1850);
                armControl.STACK_POS -= 125;
            }
            armControl.openClaw();

            //~~~~~~~~~~parking testing note: test between pose estimate and actual pose

            if (tagOfInterest.id==11){
                //to first spot
                TrajectorySequence Park1 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                        .setReversed(false)
                        .splineToSplineHeading(new Pose2d(-48, realStackPos.getY(), Math.toRadians(180)), Math.toRadians(180),
                                SampleMecanumDrive.getVelocityConstraint(50,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(30))
                        .forward(10,
                                SampleMecanumDrive.getVelocityConstraint(50,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(30))
                        .build();
                drive.followTrajectorySequenceAsync(Park1);
            }
            else if (tagOfInterest.id==14){
                //to second spot
                TrajectorySequence Park2 = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                        .setReversed(false)
                        .splineToLinearHeading(new Pose2d(-36, realStackPos.getY(), Math.toRadians(180)), Math.toRadians(-135),
                                SampleMecanumDrive.getVelocityConstraint(40,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(30))
                        .build();
                drive.followTrajectorySequenceAsync(Park2);
            }
            else if(tagOfInterest.id==19) {
                //to third spot
                Pose2d temp = drive.getPoseEstimate();
                TrajectorySequence Park3 = drive.trajectorySequenceBuilder(temp)
                        .setReversed(false)
                        .forward(2.5,
                                SampleMecanumDrive.getVelocityConstraint(40,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(30))
                        .splineToSplineHeading(new Pose2d(temp.getX()-(6*(1/Math.sqrt(2))), temp.getY()-(6*(1/Math.sqrt(2))), Math.toRadians(180)), Math.toRadians(-135),
                                SampleMecanumDrive.getVelocityConstraint(40,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(30))
                        .splineToConstantHeading(new Vector2d(-24, realStackPos.getY()), Math.toRadians(0),
                                SampleMecanumDrive.getVelocityConstraint(40,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(30))
                        .splineToConstantHeading(new Vector2d(-12, realStackPos.getY()), Math.toRadians(0),
                                SampleMecanumDrive.getVelocityConstraint(55,DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(45))
                        .build();
                drive.followTrajectorySequenceAsync(Park3);
            }
            armControl.SpecialSleep(drive, 1000); //~~~~~EXPERIMENT with this time
            armControl.ReturnFromHigh(drive);
            armControl.closeClaw();
            armControl.liftWrist.setPosition(1);
            armControl.WaitForTrajectoryToFinish(drive);
            telemetry.addData("step: ", step);
            telemetry.update();
             //*/

        }
    }

    private void SlidesToStowHardCode(ArmControl armControl, SampleMecanumDrive drive) {
        armControl.slideOne.setTargetPosition(armControl.STOW_POS);
        armControl.slideTwo.setTargetPosition(armControl.STOW_POS);
        armControl.slideOne.setPower(armControl.ARM_POWER);
        armControl.slideTwo.setPower(armControl.ARM_POWER);
        armControl.WaitForSlides(drive);
        armControl.slideOne.setPower(0);
        armControl.slideTwo.setPower(0);
    }

    private void SlidesToHighHardCode(ArmControl armControl, SampleMecanumDrive drive) {
        armControl.slideOne.setTargetPosition(armControl.HIGH_POS);
        armControl.slideTwo.setTargetPosition(armControl.HIGH_POS);
        armControl.slideOne.setPower(armControl.ARM_POWER);
        armControl.slideTwo.setPower(armControl.ARM_POWER);
//        armControl.WaitForSlides(drive);
//        armControl.slideOne.setPower(0);
//        armControl.slideTwo.setPower(0);
    }


    private void AprilTags() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
        telemetry.setMsTransmissionInterval(50);
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == 11 || tag.id == 14 || tag.id == 19)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */
        if(tagOfInterest == null){
            /*
             * Insert your autonomous code here, presumably running some default configuration
             * since the tag was never sighted during INIT
             */
        }
        else{
            /*
             * Insert your autonomous code here, probably using the tag pose to decide your configuration.
             */

            // e.g.
        }
    }

    void tagToTelemetry(AprilTagDetection detection){
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
    }
}
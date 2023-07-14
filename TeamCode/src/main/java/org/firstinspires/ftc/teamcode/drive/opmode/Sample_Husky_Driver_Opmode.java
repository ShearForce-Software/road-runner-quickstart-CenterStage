package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.external.samples.Sample_HuskyDriver;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*  Initial attempt at writing I2C port for HuskeyLens AI Vision Camera
 *
 *   Using instructions posted here:
 *   https://github.com/ftctechnh/ftc_app/wiki/Writing-an-I2C-Driver
 *   https://github.com/FIRST-Tech-Challenge/WikiSupport/blob/master/SampleOpModes/java/i2cExample/MCP9808.java
 *
 *   Process:
 *   1) Create driver class - leverage ModernRoboticsI2cRangeSensor as an example
 *   2) Extend I2CDeviceSynch class and add required methods
 *   3) Add address and registers of sensor
 *   4) Create write and read methods
 *   5) Write user methods
 */

/*
 * Uses dashboard for adjusting program variable values (@Config).  Variables to be adjusted need
 * as assigned as "Public Static". To access the dashboard, connect your computer to the RC's WiFi network.
 * In your browser, navigate to https://192.168.49.1:8080/dash if you're using the RC phone or
 * https://192.168.43.1:8080/dash if you are using the Control Hub.
 *
 * Once you've successfully connected, start the program, and adjust variable values to see how
 * the telemetry output of the Huskeylens changes.  These variable values can be tuned live in dashboard.
 */

@Config
@TeleOp(name="Sample_Husky_Driver")
//@Disabled
public class Sample_Husky_Driver_Opmode extends LinearOpMode {
    //   Sample_HuskyDriver Sample_HuskyDriver= new Sample_HuskyDriver();
    Sample_HuskyDriver cameraThing;

    public void runOpMode () throws InterruptedException {
        cameraThing = hardwareMap.get(Sample_HuskyDriver.class, "AI Camera");
        waitForStart();
        if (isStopRequested()) return;
        telemetry.addData("Knock Results: ", cameraThing.knock2());
        while(opModeIsActive()){
            telemetry.update();
        }
    }
}

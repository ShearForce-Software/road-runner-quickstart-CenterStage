package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * Op mode to test real-time tuning of the TeleOp variables with dashboard.
 * To access the dashboard, connect your computer  * to the RC's WiFi network.
 * In your browser, navigate to https://192.168.49.1:8080/dash if using the RC phone
 * navigate to https://192.168.43.1:8080/dash if you are using the Control Hub.
 */

@Config
@TeleOp(group = "TeleOp -- Dashboard")

public class TeleOp_Dashboard_Test extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors and servo.

    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private Servo testServo = null;

    public static double MAX_VELOCITY_SCALE = 1;    // Scale velocity using dashboard
    public static double SERVO_MIN_POS = 0;         // Change Min servo position using dashboard
    public static double SERVO_MAX_POS = 1;         // Change Max servo position using dashboard
    public static double SERVO_START_POS = 0.5;     // Change Start servo position using dashboard
    static final double SERVO_SCALE = 0.01;         // Servo scaling for servo increment value

    double testServo_pos = SERVO_START_POS;         // testServo start position

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFront");
        leftBackDrive = hardwareMap.get(DcMotor.class, "leftRear");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightRear");
        testServo = hardwareMap.get(Servo.class, "testServo");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Servo initial position
        testServo.setPosition(testServo_pos);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double y = -gamepad2.left_stick_y;
            double x = gamepad2.left_stick_x;
            double rx = gamepad2.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            // Send scaled calculated power to wheels
            leftFrontDrive.setPower(frontLeftPower * MAX_VELOCITY_SCALE);
            leftBackDrive.setPower(backLeftPower * MAX_VELOCITY_SCALE);
            rightFrontDrive.setPower(frontRightPower * MAX_VELOCITY_SCALE);
            rightBackDrive.setPower(backRightPower * MAX_VELOCITY_SCALE);


            if (gamepad1.right_trigger != 0) {
                testServo_pos += gamepad1.right_trigger * SERVO_SCALE;
                if (testServo_pos >= SERVO_MAX_POS) {
                    testServo_pos = SERVO_MAX_POS;
                }

                if (gamepad1.left_trigger != 0) {
                    testServo_pos += gamepad1.left_stick_y * SERVO_SCALE;
                    if (testServo_pos <= SERVO_MIN_POS) {
                        testServo_pos = SERVO_MIN_POS;
                    }
                }

                // Set the servos to the new positions
                testServo.setPosition(testServo_pos);

                // Show dashboard changed variables.
                telemetry.addData("Max Velocity Scale Factor", MAX_VELOCITY_SCALE);
                telemetry.addData("Servo Min Position", SERVO_MIN_POS);
                telemetry.addData("Servo Max Position", SERVO_MAX_POS);
                telemetry.addData("Servo Start Position", SERVO_START_POS);
                telemetry.update();
            }
        }
    }
}
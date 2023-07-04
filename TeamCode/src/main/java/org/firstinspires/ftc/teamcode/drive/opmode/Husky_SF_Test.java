package org.firstinspires.ftc.teamcode.drive.opmode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.TypeConversion;

import java.io.UnsupportedEncodingException;

//@Disabled
@TeleOp(name = "Husky_SF_Test")
public class Husky_SF_Test extends LinearOpMode {

    Husky_SF camera;
    long delay = 1;

    @SuppressLint("DefaultLocale")
    public void runOpMode() throws InterruptedException
    {
        camera = hardwareMap.get(Husky_SF.class, "AI Camera");

        telemetry.addData("Connection Info", camera.getConnectionInfo());
        telemetry.update();

        waitForStart();

        camera.writeShort(Husky_SF.Register.WRITE, camera.write_knock);
        camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Color_algorithm);

        while(opModeIsActive())
        {


            camera.writeShort(Husky_SF.Register.WRITE, camera.write_command_request_blocks);
            sleep(delay);

            telemetry.addData("Read Byte HEADER", camera.readByte(Husky_SF.Register.HEADER));
            telemetry.addData("Read Byte HEADER2", camera.readByte(Husky_SF.Register.HEADER2));

            sleep(delay);

            //telemetry.addData("Read Short HEADER", camera.readShort2(Huskey_UpdateMR.Register.HEADER));
            sleep(delay);
            //telemetry.addData("Read Short HEADER2", camera.readShort2(Huskey_UpdateMR.Register.HEADER2));
            sleep(delay);
            telemetry.addData("Read Short ADDRESS", camera.readByte(Husky_SF.Register.ADDRESS));
            sleep(delay);
            telemetry.addData("Read Short COMMAND", camera.readByte(Husky_SF.Register.COMMAND));
            sleep(delay);
            telemetry.addData("Read Short DATA_LENGTH", camera.readByte(Husky_SF.Register.DATA_LENGTH));
            sleep(delay);
            telemetry.addData("Read Short DATA0", camera.readByte(Husky_SF.Register.DATA0));
            sleep(delay);
            telemetry.addData("Read Short DATA1", camera.readByte(Husky_SF.Register.DATA1));
            sleep(delay);
            telemetry.addData("Read Short DATA2", camera.readByte(Husky_SF.Register.DATA2));
            sleep(delay);
            telemetry.addData("Read Short DATA3", camera.readByte(Husky_SF.Register.DATA3));
            sleep(delay);
            telemetry.addData("Read Short DATA4", camera.readByte(Husky_SF.Register.DATA4));
            sleep(delay);
            telemetry.addData("Read Short DATA5", camera.readByte(Husky_SF.Register.DATA5));
            sleep(delay);
            telemetry.addData("Read Short DATA6", camera.readByte(Husky_SF.Register.DATA6));
            sleep(delay);
            telemetry.addData("Read Short DATA7", camera.readByte(Husky_SF.Register.DATA7));
            sleep(delay);
            telemetry.addData("Read Short DATA8", camera.readByte(Husky_SF.Register.DATA8));
            sleep(delay);
            telemetry.addData("Read Short DATA9", camera.readByte(Husky_SF.Register.DATA9));
            sleep(delay);
            telemetry.addData("Read Short CHECKSUM", camera.readByte(Husky_SF.Register.CHECKSUM));

            telemetry.update();
            idle();
        }
    }
}
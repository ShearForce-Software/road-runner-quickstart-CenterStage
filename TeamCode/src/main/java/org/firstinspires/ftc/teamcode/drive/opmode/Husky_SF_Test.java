package org.firstinspires.ftc.teamcode.drive.opmode;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


//@Disabled
@Config
@TeleOp(name = "Husky_SF_Test")
public class Husky_SF_Test extends LinearOpMode {

    Husky_SF camera;
    public static int single_double_register = 1;
    public static int algorithm_select = 0;

//0x00 0x00	ALGORITHM_FACE_RECOGNITION      - 0
//0x01 0x00	ALGORITHM_OBJECT_TRACKING       - 1
//0x02 0x00	ALGORITHM_OBJECT_RECOGNITION    - 2
//0x03 0x00	ALGORITHM_LINE_TRACKING         - 3
//0x04 0x00	ALGORITHM_COLOR_RECOGNITION     - 4
//0x05 0x00	ALGORITHM_TAG_RECOGNITION       - 5



    @SuppressLint("DefaultLocale")
    public void runOpMode() throws InterruptedException
    {
        camera = hardwareMap.get(Husky_SF.class, "AI Camera");
        telemetry.addData("Connection Info", camera.getConnectionInfo());
        telemetry.update();

        waitForStart();

        camera.writeShort(Husky_SF.Register.WRITE, camera.write_knock);

        while(opModeIsActive())
        {
            if (single_double_register == 0){
                if (algorithm_select == 0) {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Face_algorithm);
                }else if (algorithm_select == 1) {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_ObjectTrack_algorithm);
                } else if (algorithm_select == 2)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_ObjectRecognition_algorithm);
                } else if (algorithm_select == 3)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_LineTrack_algorithm);
                } else if (algorithm_select == 4)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Color_algorithm);
                } else if (algorithm_select == 5)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Tag_algorithm);
                } else {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Face_algorithm);
                }

                camera.writeShort(Husky_SF.Register.WRITE, camera.write_command_request_all);

                telemetry.addData("Read Byte HEADER", camera.readByte(Husky_SF.Register.HEADER));
                telemetry.addData("Read Byte HEADER2", camera.readByte(Husky_SF.Register.HEADER2));
                telemetry.addData("Read Byte ADDRESS", camera.readByte(Husky_SF.Register.ADDRESS));
                telemetry.addData("Read Byte COMMAND", camera.readByte(Husky_SF.Register.COMMAND));
                telemetry.addData("Read Byte DATA_LENGTH", camera.readByte(Husky_SF.Register.DATA_LENGTH));
                telemetry.addData("Read Byte DATA0", camera.readByte(Husky_SF.Register.DATA0));
                telemetry.addData("Read Byte DATA1", camera.readByte(Husky_SF.Register.DATA1));
                telemetry.addData("Read Byte DATA2", camera.readByte(Husky_SF.Register.DATA2));
                telemetry.addData("Read Byte DATA3", camera.readByte(Husky_SF.Register.DATA3));
                telemetry.addData("Read Byte DATA4", camera.readByte(Husky_SF.Register.DATA4));
                telemetry.addData("Read Byte DATA5", camera.readByte(Husky_SF.Register.DATA5));
                telemetry.addData("Read Byte DATA6", camera.readByte(Husky_SF.Register.DATA6));
                telemetry.addData("Read Byte DATA7", camera.readByte(Husky_SF.Register.DATA7));
                telemetry.addData("Read Byte DATA8", camera.readByte(Husky_SF.Register.DATA8));
                telemetry.addData("Read Byte DATA9", camera.readByte(Husky_SF.Register.DATA9));
                telemetry.addData("Read Byte CHECKSUM", camera.readByte(Husky_SF.Register.CHECKSUM));
                telemetry.update();

            } else {

                if (algorithm_select == 0) {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Face_algorithm);
                }else if (algorithm_select == 1) {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_ObjectTrack_algorithm);
                } else if (algorithm_select == 2)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_ObjectRecognition_algorithm);
                } else if (algorithm_select == 3)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_LineTrack_algorithm);
                } else if (algorithm_select == 4)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Color_algorithm);
                } else if (algorithm_select == 5)  {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Tag_algorithm);
                } else {
                    camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Face_algorithm);
                }


                camera.writeShort(Husky_SF.Register.WRITE, camera.write_command_request_all);

                telemetry.addData("Read Short HEADER/HEADER2", camera.readShort2(Husky_SF.Register.HEADER));
                telemetry.addData("Read Short ADDRESS/COMMAND", camera.readShort2(Husky_SF.Register.ADDRESS));
                telemetry.addData("Read Short DATA_LENGTH/DATA0", camera.readShort2(Husky_SF.Register.DATA_LENGTH));
                telemetry.addData("Read Short DATA1/DATA2", camera.readShort2(Husky_SF.Register.DATA1));
                telemetry.addData("Read Short DATA3/DATA4", camera.readShort2(Husky_SF.Register.DATA3));
                telemetry.addData("Read Short DATA5/DATA6", camera.readShort2(Husky_SF.Register.DATA5));
                telemetry.addData("Read Short DATA7/DATA8", camera.readShort2(Husky_SF.Register.DATA7));
                telemetry.addData("Read Short DATA9/CHECKSUM", camera.readShort2(Husky_SF.Register.DATA9));
                telemetry.update();
            }
        }
    }
}
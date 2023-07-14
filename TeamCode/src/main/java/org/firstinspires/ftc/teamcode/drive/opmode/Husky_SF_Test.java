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
    public static String string_format = "%2h";
    public static int single_double_register = 0;
    public static int algorithm_select = 1;
    public static int reg_Count = 2;
    public static int reg_select = 0x55;


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

        camera.write_knock();

        while(opModeIsActive())

        {
            byte[] blockArray = camera.seesBlock();
//            telemetry.addData("HEADER ", String.format("%02X", blockArray[0]));
//            telemetry.addData("HEADER2 ", String.format("%02X", blockArray[1]));
//            telemetry.addData("ADDRESS ", String.format("%02X", blockArray[2]));
//            telemetry.addData("DATA_LENGTH ", String.format("%02X", blockArray[3]));
//            telemetry.addData("COMMAND ", String.format("%02X", blockArray[4]));
//            telemetry.addData("# OF BLOCKS LOW", String.format("%02X", blockArray[5]));
//            telemetry.addData("# OF BLOCKS HIGH ", String.format("%02X", blockArray[6]));
//            telemetry.addData("# OF IDS LOW ", String.format("%02X", blockArray[7]));
//            telemetry.addData("# OF IDS HIGH ", String.format("%02X", blockArray[8]));
//            telemetry.addData("CURRENT FRAME LOW ", String.format("%02X", blockArray[9]));
//            telemetry.addData("CURRENT FRAME HIGH ", String.format("%02X", blockArray[10]));
//            telemetry.addData("DATA6 ", String.format("%02X", blockArray[11]));
//            telemetry.addData("DATA7 ", String.format("%02X", blockArray[12]));
//            telemetry.addData("DATA8 ", String.format("%02X", blockArray[13]));
//            telemetry.addData("DATA9 ", String.format("%02X", blockArray[14]));
//            telemetry.addData("CHECKSUM ", String.format("%02X", blockArray[15]));

            int numBlocks = camera.bytesToInt(blockArray[5],blockArray[6]);
            int numObjects = camera.bytesToInt(blockArray[5],blockArray[6]);
            int xCenter = camera.bytesToInt(blockArray[21],blockArray[22]);
            int yCenter = camera.bytesToInt(blockArray[23],blockArray[24]);
            telemetry.addData("x center: ", xCenter);
            telemetry.addData("y center: ", yCenter);
            telemetry.addData("number of blocks ", numBlocks);
            telemetry.addData("number of objects detected ", numObjects);
            telemetry.addData("ID ", String.format("%02X", blockArray[29]));

//                telemetry.addData("HEADER", camera.read());
//                telemetry.addData("HEADER2", camera.read());
//                telemetry.addData("ADDRESS", camera.read()");
//                telemetry.addData("DATA_LENGTH", camera.read());
//                telemetry.addData("COMMAND", camera.read());
//                telemetry.addData("DATA0", camera.read());
//                telemetry.addData("DATA1", camera.read());
//                telemetry.addData("DATA2", camera.read());
//                telemetry.addData("DATA3", camera.read());
//                telemetry.addData("DATA4", camera.read());
//                telemetry.addData("DATA5", camera.read());
//                telemetry.addData("DATA6", camera.read());
//                telemetry.addData("DATA7", camera.read());
//                telemetry.addData("DATA8", camera.read());
//                telemetry.addData("DATA9", camera.read());
//                telemetry.addData("CHECKSUM", camera.read());
//
//            telemetry.addData("HEADER", camera.read());
//            telemetry.addData("HEADER2", camera.read());
//            telemetry.addData("ADDRESS", camera.read());
//            telemetry.addData("DATA_LENGTH", camera.read());
//            telemetry.addData("COMMAND", camera.read());
//            telemetry.addData("DATA0", camera.read());
//            telemetry.addData("DATA1", camera.read());
//            telemetry.addData("DATA2", camera.read());
//            telemetry.addData("DATA3", camera.read());
//            telemetry.addData("DATA4", camera.read());
//            telemetry.addData("DATA5", camera.read());
//            telemetry.addData("DATA6", camera.read());
//            telemetry.addData("DATA7", camera.read());
//            telemetry.addData("DATA8", camera.read());
//            telemetry.addData("DATA9", camera.read());
//            telemetry.addData("CHECKSUM", camera.read());

                telemetry.update();
            //}


//            if (algorithm_select == 0) {
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Face_algorithm);
//            }else if (algorithm_select == 1) {
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_ObjectTrack_algorithm);
//            } else if (algorithm_select == 2)  {
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_ObjectRecognition_algorithm);
//            } else if (algorithm_select == 3)  {
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_LineTrack_algorithm);
//            } else if (algorithm_select == 4)  {
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Color_algorithm);
//            } else if (algorithm_select == 5)  {
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Tag_algorithm);
//            } else {
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_request_Face_algorithm);
//            }
//            if (single_double_register == 0){
//
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_command_request_all);
//
//
//                for (int i = 0; i < 1; i++) {
//                    telemetry.addData("Read Byte HEADER", string_format,camera.readByte(Husky_SF.Register.HEADER));
//                    telemetry.addData("Read Byte HEADER2", string_format,camera.readByte(Husky_SF.Register.HEADER2));
//                    telemetry.addData("Read Byte ADDRESS", string_format,camera.readByte(Husky_SF.Register.ADDRESS));
//                    telemetry.addData("Read Byte COMMAND", string_format,camera.readByte(Husky_SF.Register.DATA_LENGTH));
//                    telemetry.addData("Read Byte DATA_LENGTH", string_format,camera.readByte(Husky_SF.Register.COMMAND));
//                    telemetry.addData("Read Byte DATA0", string_format,camera.readByte(Husky_SF.Register.DATA0));
//                    telemetry.addData("Read Byte DATA1", string_format,camera.readByte(Husky_SF.Register.DATA1));
//                    telemetry.addData("Read Byte DATA2", string_format,camera.readByte(Husky_SF.Register.DATA2));
//                    telemetry.addData("Read Byte DATA3", string_format,camera.readByte(Husky_SF.Register.DATA3));
//                    telemetry.addData("Read Byte DATA4", string_format,camera.readByte(Husky_SF.Register.DATA4));
//                    telemetry.addData("Read Byte DATA5", string_format,camera.readByte(Husky_SF.Register.DATA5));
//                    telemetry.addData("Read Byte DATA6", string_format,camera.readByte(Husky_SF.Register.DATA6));
//                    telemetry.addData("Read Byte DATA7", string_format,camera.readByte(Husky_SF.Register.DATA7));
//                    telemetry.addData("Read Byte DATA8", string_format,camera.readByte(Husky_SF.Register.DATA8));
//                    telemetry.addData("Read Byte DATA9", string_format,camera.readByte(Husky_SF.Register.DATA9));
//                    telemetry.addData("Read Byte CHECKSUM", string_format,camera.readByte(Husky_SF.Register.CHECKSUM));
//                    telemetry.update();
//                }
//
//
//            } else {
//
//                camera.writeShort(Husky_SF.Register.WRITE, camera.write_command_request_all);
//                for (int i = 0; i < 1; i++) {
//                    telemetry.addData("Read Short HEADER/HEADER2", string_format,camera.readShort2(Husky_SF.Register.HEADER));
//                    telemetry.addData("Read Short ADDRESS/DATA_LENGTH", string_format,camera.readShort2(Husky_SF.Register.ADDRESS));
//                    telemetry.addData("Read Short COMMAND/DATA0", string_format,camera.readShort2(Husky_SF.Register.COMMAND));
//                    telemetry.addData("Read Short DATA1/DATA2", string_format,camera.readShort2(Husky_SF.Register.DATA1));
//                    telemetry.addData("Read Short DATA3/DATA4", string_format,camera.readShort2(Husky_SF.Register.DATA3));
//                    telemetry.addData("Read Short DATA5/DATA6", string_format,camera.readShort2(Husky_SF.Register.DATA5));
//                    telemetry.addData("Read Short DATA7/DATA8", string_format,camera.readShort2(Husky_SF.Register.DATA7));
//                    telemetry.addData("Read Short DATA9/CHECKSUM", string_format,camera.readShort2(Husky_SF.Register.DATA9));
//                    telemetry.update();
//                }
//
//            }
        }
    }
}

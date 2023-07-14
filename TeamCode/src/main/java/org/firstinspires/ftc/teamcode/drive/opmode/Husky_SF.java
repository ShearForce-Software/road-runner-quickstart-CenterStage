package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
@I2cDeviceType
@DeviceProperties(name = "Huskylens_SF", description = "AI Camera", xmlTag = "Husky_SF", builtIn = true)
public class Husky_SF<value> extends I2cDeviceSynchDevice<I2cDeviceSynch> //implements I2cAddrConfig
{
    //----------------------------------------------------------------------------------------------
    // Constants
    //----------------------------------------------------------------------------------------------

    public final static I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(0x32);

    String commandHeaderAndAddress = "55AA11";
    private Object value;

    public enum Register
    {
        FIRST(0x55),
        HEADER(0x55),
        HEADER2(0xaa),
        ADDRESS(0x11),
        DATA_LENGTH(0x0a),
        COMMAND(0x2a),
        DATA0(0x2c),
        DATA1(0x01),
        DATA2(0xc8),
        DATA3(0x00),
        DATA4(0x0a),
        DATA5(0x00),
        DATA6(0x14),
        DATA7(0x00),
        DATA8(0x01),
        DATA9(0x00),
        CHECKSUM(0x58),
        LAST(CHECKSUM.bVal),
        WRITE(0x12);

        public int bVal;
        Register(int bVal) { this.bVal = bVal; }
    }

    //----------------------------------------------------------------------------------------------
    // Read / Write
    //----------------------------------------------------------------------------------------------
//    protected void writeShort(Register reg, short value)
//    {
//        deviceClient.write(reg.bVal, TypeConversion.shortToByteArray(value));
//    }
    protected void writeShort(Register reg, byte[] value)
    {
        deviceClient.write(reg.bVal, value);
    }

    protected short readShort2(Register reg)
    {
        return TypeConversion.byteArrayToShort(deviceClient.read(reg.bVal, 2));
    }


    protected short readByte(Register reg)
    {
        return (deviceClient.read8(reg.bVal));
    }

    protected short read(int registers, int value) {
        return TypeConversion.byteArrayToShort(deviceClient.read(registers, value));
    }
    protected String read() {
        String hex = String.format("%02X", deviceClient.read8());
        return hex;
    }



    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public Husky_SF(I2cDeviceSynch deviceClient, boolean deviceClientIsOwned)
    {
        super(deviceClient, deviceClientIsOwned);

        this.setOptimalReadWindow();
        this.deviceClient.setI2cAddress(ADDRESS_I2C_DEFAULT);

        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    protected void setOptimalReadWindow()
    {
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(
                Register.FIRST.bVal,
                Register.LAST.bVal - Register.FIRST.bVal + 1,
                I2cDeviceSynch.ReadMode.REPEAT);
        this.deviceClient.setReadWindow(readWindow);
    }


   @Override
    protected synchronized boolean doInitialize()
    {
        return true;    // nothing to do
    }

    //----------------------------------------------------------------------------------------------
    // Write Commands
    //----------------------------------------------------------------------------------------------

    public byte[] knock() {
        byte[] cmd = cmdToBytes(commandHeaderAndAddress + "002c3c");
        deviceClient.write(12, cmd);
        //return processReturnData();
        return cmd;
    }
    public byte[] cmdToBytes(String cmd) {
        int length = cmd.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(cmd.substring(i, i + 2), 16);
        }
        return bytes;
    }

    public byte[] write_knock(){
        byte[] cmd = {(byte) 0x55,(byte) 0xAA,(byte) 0x11,(byte) 0x00,(byte) 0x2C,(byte) 0x3C};
        deviceClient.write(12, cmd);
        return cmd;
    }
    public String knock2() {
        byte[] bytesToSend = new byte[] {0x55, (byte)0xAA, 0x11, 0x00, 0x2C, 0x3C};
        deviceClient.write(12, bytesToSend);
        byte[] bytesReceived = deviceClient.read(6);
        byte[] expectedBytes = new byte[] {0x55, (byte)0xAA, 0x11, 0x00, 0x2E, 0x3E};
        for (int i = 0; i < 6; i++) {
            if (bytesReceived[i] != expectedBytes[i]) return "Knock2 Return Didn't Match";
        }
        //return "Knock2 Received";
        return String.format("%02X", bytesReceived[0]);
    }
    public byte[] seesBlock()
    {
        deviceClient.write(12, write_command_request_blocks);
        byte[] bytesReceived = deviceClient.read(32);// TODO: return more blocks later
        return bytesReceived;
    }

    public int bytesToInt(byte low, byte high) {
        return ((int) low & 0xff) | (((int) high & 0xff) << 8);
    }

    public byte[] write_knock = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x00,
            (byte) 0x2C,
            (byte) 0x3C,
    };

    public byte[] write_command_request_all = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x00,
            (byte) 0x20,
            (byte) 0x30,
    };

    public byte[] write_command_request_blocks = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x00,
            (byte) 0x21,
            (byte) 0x31,
    };

    public byte[] write_command_request_arrows = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x00,
            (byte) 0x22,
            (byte) 0x32,
    };
//0x00 0x00	ALGORITHM_FACE_RECOGNITION      - checksum 3F
//0x01 0x00	ALGORITHM_OBJECT_TRACKING       - checksum 40
//0x02 0x00	ALGORITHM_OBJECT_RECOGNITION    - checksum 41
//0x03 0x00	ALGORITHM_LINE_TRACKING         - checksum 42
//0x04 0x00	ALGORITHM_COLOR_RECOGNITION     - checksum 43
//0x05 0x00	ALGORITHM_TAG_RECOGNITION       - checksum 44

    public byte[] write_request_blocks_ID = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x02,
            (byte) 0x27,
            (byte) 0x01, //block ID 1
            (byte) 0x00, //blcok ID 1
            (byte) 0x3A,
    };

    public byte[] write_request_Face_algorithm = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x02,
            (byte) 0x2d,
            (byte) 0x00, //Face Rec
            (byte) 0x00, //Face Rec
            (byte) 0x3F,
    };

    public byte[] write_request_ObjectTrack_algorithm = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x02,
            (byte) 0x2d,
            (byte) 0x01, //Object Track
            (byte) 0x00, //Object Track
            (byte) 0x40,
    };

    public byte[] write_request_ObjectRecognition_algorithm = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x02,
            (byte) 0x2d,
            (byte) 0x02, //Object Recognize
            (byte) 0x00, //Object Recognize
            (byte) 0x41,
    };

    public byte[] write_request_LineTrack_algorithm = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x02,
            (byte) 0x2d,
            (byte) 0x03, //Line Track
            (byte) 0x00, //Line Track
            (byte) 0x42,
    };

    public byte[] write_request_Color_algorithm = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x02,
            (byte) 0x2d,
            (byte) 0x04, //Color Detect
            (byte) 0x00, //Color Detect
            (byte) 0x43,
    };

    public byte[] write_request_Tag_algorithm = {
            (byte) 0x55,
            (byte) 0xAA,
            (byte) 0x11,
            (byte) 0x02,
            (byte) 0x2d,
            (byte) 0x05, //Tag Detect
            (byte) 0x00, //Tag Detect
            (byte) 0x44,
    };

    //----------------------------------------------------------------------------------------------
    // I2cAddressConfig
    //----------------------------------------------------------------------------------------------

//    @Override public void setI2cAddress(I2cAddr newAddress)
//    {
//        this.deviceClient.setI2cAddress(newAddress);
//    }
//
//    @Override public I2cAddr getI2cAddress()
//    {
//        return this.deviceClient.getI2cAddress();
//    }


    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override
    public Manufacturer getManufacturer()
    {
        return Manufacturer.Unknown;
    }

    @Override public String getDeviceName()
    {
        return null;
    }


}

package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

@SuppressWarnings("WeakerAccess")
@I2cDeviceType
@DeviceProperties(name = "Huskylens_SF", description = "AI Camera", xmlTag = "Husky_SF", builtIn = true)
public class Husky_SF<value> extends I2cDeviceSynchDevice<I2cDeviceSynch> implements I2cAddrConfig
{
    //----------------------------------------------------------------------------------------------
    // Constants
    //----------------------------------------------------------------------------------------------

    public final static I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(0x32);

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

    protected byte readByte(Register reg)
    {
        return (deviceClient.read8(reg.bVal));
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

    @Override public void setI2cAddress(I2cAddr newAddress)
    {
        this.deviceClient.setI2cAddress(newAddress);
    }

    @Override public I2cAddr getI2cAddress()
    {
        return this.deviceClient.getI2cAddress();
    }


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

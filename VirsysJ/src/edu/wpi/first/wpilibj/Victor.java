package edu.wpi.first.wpilibj;

import utilities.Channels;
import crio.hardware.CRIO;
import crio.hardware.DigitalSidecar;

public class Victor implements SpeedController {

    public double value;
    int virsysPacketIndex;
    int digitalSidecarChannel;

    double[] motor_stall_torques = {
            346.9, // OZ*in
            346.9,
            700.0,
            100.0,
            100.0,
    };
    double[] motor_free_speeds = {
            5342 * 2 * Math.PI / 60.0, // rad / sec
            5342 * 2 * Math.PI / 60.0,
            700.0,
            100.0,
            100.0
    };

    Client c;

    public Victor(int channel) {
        if (CRIO.runWithPhysics) {
            virsysPacketIndex = CRIO.virsysOutputMap[channel];
            c = CRIO.client;
        }
        digitalSidecarChannel = channel;
    }

    public Victor(int slot, int channel) {
        this(channel);
    }

    public double get() {
        return value;
    }

    public void pidWrite(double output) {
        value = output;
        if(CRIO.runWithPhysics && virsysPacketIndex >= 0) {
            c.threadS.toSend[virsysPacketIndex] = (float)(output * maxcurrenttorque());
        }
    }

    public void set(double speed) {
        pidWrite(speed);
    }

    public void set(double speed, byte syncgroup) {
        set(speed);
    }

    double maxcurrenttorque() {
        double slope = -1 * motor_stall_torques[virsysPacketIndex] / motor_free_speeds[virsysPacketIndex];
        double currentspeed = Math.abs(c.getdata()[virsysPacketIndex + 5]); // +5 to convert the channel number to the correct index of the receive data array
        return motor_stall_torques[virsysPacketIndex] + currentspeed * slope;
    }

    public void disable() {
        
    }
}

package edu.wpi.first.wpilibj;

import utilities.Channels;
import crio.hardware.CRIO;
import crio.hardware.DigitalSidecar;

public class Victor implements SpeedController {

    double prevspeed;
    int virsysPakcetIndex;
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
       virsysPakcetIndex = CRIO.virsysOutputMap[channel];
       digitalSidecarChannel = channel;
       c = CRIO.client;
    }

    public Victor(int slot, int channel) {
        this(channel);
    }

    public double get() {
        return prevspeed;
    }

    public void pidWrite(double output) {
        prevspeed = output;
	c.threadS.toSend[virsysPakcetIndex] = (float)(output * maxcurrenttorque());
        DigitalSidecar.register[digitalSidecarChannel-1] = output;
    }

    public void set(double speed) {
        pidWrite(speed);
    }

    public void set(double speed, byte syncgroup) {
        set(speed);
    }

    double maxcurrenttorque() {
        double slope = -1 * motor_stall_torques[virsysPakcetIndex] / motor_free_speeds[virsysPakcetIndex];
        double currentspeed = Math.abs(c.getdata()[virsysPakcetIndex + 5]); // +5 to convert the channel number to the correct index of the receive data array
        return motor_stall_torques[virsysPakcetIndex] + currentspeed * slope;
    }

    public void disable() {
        
    }
}

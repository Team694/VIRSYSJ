/*file that sends input/output info to virsys in a specific order
refer to /doc/data_format.txt*/
package edu.wpi.first.wpilibj;

import java.io.*;
import java.net.*;
import java.nio.*;

public class Client implements Networkconf {
    private Runnable recieveThread;
    private Thread  threadR;
    public Send  threadS;
    private float[] receivedData;
    DatagramSocket sender;
    DatagramSocket receiver;
    private boolean done = false; // threads exit when this is true

    public Client() {
        recieveThread = new Recieve();
        threadR = new Thread(recieveThread);
        threadS = new Send();
        receivedData = new float[10];
	try {
	    sender = new DatagramSocket();
	    receiver = new DatagramSocket(LOCAL_RECV_PORT);
	}
	catch (IOException e) {
	    e.printStackTrace();
	    System.out.println("can't bind port; aborting");
	    System.exit(1);
	}
	threadR.start();
        threadS.start();
    }

    public void end() {
	done = true;
	sender.close();
	receiver.close();
    }

    public float[] getdata() throws IOException{
        if(receivedData == null){
            throw new IOException("Recieve data null");
        }            
        return receivedData;
    }

    public void send(float left, float right, float arm, float wrist, float grip){
        send(new float[]{(float)Timer.getFPGATimestamp(),left,right,arm,wrist,grip});
    }

    private void send(float[] f) {
        try {
            if (f.length != 6) {
                System.out.print("error:need 6 floats in packet");
            } else {
                byte[] b = new byte[24];
                for (int i = 0; i < f.length; i++) {
                    byte[] test = float2arr(f[i]);
                    b[i * 4] = test[0];
                    b[i * 4 + 1] = test[1];
                    b[i * 4 + 2] = test[2];
                    b[i * 4 + 3] = test[3];
                }
                DatagramPacket dp = new DatagramPacket(b, b.length, InetAddress.getByName(Virsys_IP), VIRSYS_RECV_PORT);
                sender.send(dp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private float[] recieve() {
        float[] ans = new float[10];
        try {
            byte[] buffer = new byte[40];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
	    //System.out.println(receiver);
	    receiver.receive(incoming);
            byte[] datas = incoming.getData();
            for (int i = 0; i < datas.length / 4; i++) {
                ans[i] = arr2float(new byte[]{datas[4 * i], datas[4 * i + 1], datas[4 * i + 2], datas[4 * i + 3]});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }

    private float arr2float(byte[] buf) {
        byte[] asdf = new byte[4];
        for (int i = 0; i < 4; i++) {
            asdf[i] = buf[3 - i];
        }
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.put(asdf);
        float value = bb.getFloat(0);
        return value;
    }

    private byte[] float2arr(float f) {
        int bits = Float.floatToIntBits(f);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (bits & 0xff);
        bytes[1] = (byte) ((bits >> 8) & 0xff);
        bytes[2] = (byte) ((bits >> 16) & 0xff);
        bytes[3] = (byte) ((bits >> 24) & 0xff);
        return bytes;
    }
    private class Recieve implements Runnable {
        public void run() {
            done = false;
            while (!done) {
                receivedData = recieve();
                Thread.yield();
            }
        }
    }

    public class Send extends Thread {

        public float[] toSend;

        public Send() {
            toSend = new float[5];
	    toSend[4] = 2;
        }
        
        public void run() {
            while (!done) {
                send(toSend[0], toSend[1], toSend[2], toSend[3], toSend[4]);
                //System.out.printf("sending: %f %f %f %f %f\n", toSend[0], toSend[1], toSend[2], toSend[3], toSend[4]);
                Thread.yield();
            }
        }
    }
}

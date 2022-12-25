package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
class Network {
    static int connection_Size;
    static int size;
}


class Router implements Runnable {
    private ArrayList<Device> devices;
    private Semaphore semaphore;
    int k = 0;
    static File output = new File("output.txt");

    public Router(int size, int connectionSize) throws IOException {
        output.createNewFile();
        devices = new ArrayList<Device>(size);
        semaphore = new Semaphore(connectionSize);
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void switchOn() {
        for (int j = 0; j < devices.size(); j++) {
            Thread thread = new Thread(this, devices.get(j).getName());
            thread.start();
           /* JButton button=new JButton(devices.get(j).getName()+" "+devices.get(j).getType());
            button.setBounds();*/
        }
    }

    @Override
    public void run() {
        Device dum = devices.get(k++);
        try {
            semaphore.P(dum);
            dum.setPort(semaphore.getCounter());

            System.out.println("Connection " + dum.getPort() + ": " + dum.getName() + " Occupied");
            // send to gui -> connection state changed to "occupied"  && change color && change label on button to which device
            // connnection 1 : pc1 (pc) : log in //green
            //                          : perform online activities //blue
            //                          : log out //red
            JButton butt = new JButton("Connection " + dum.getPort() + ": " + dum.getName() + " Occupied" );
            butt.setBounds(100, dum.getPort() * 50, 500, 30);
            GUI.frame3.AddObject(butt);
            Thread.currentThread().sleep(1000);
            dum.Connect(output);
            butt.setBackground(Color.green);
            butt.setText(dum.getName()+" logged in");
            Thread.currentThread().sleep(dum.getPort()*1000);
            dum.OnlineActivity(output);
            butt.setBackground(Color.magenta);
            butt.setText(dum.getName()+" online activity");
            Thread.currentThread().sleep(1000);
            dum.Disconnect(output);
            butt.setBackground(Color.orange);
            butt.setText(dum.getName()+" logged out");
            Thread.currentThread().sleep(1000);
            butt.setBackground(Color.white);
            butt.setText("Free to use");
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        semaphore.setCounter(dum.getPort());
        semaphore.V(dum);
    }
}

class Device {
    private String name;
    private String type;
    private int port;

    public Device(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void Connect(File op) throws IOException {
        String s = "Device " + name + " logged in \n";
        FileWriter destFile = new FileWriter(op, true);
        destFile.write(s);
        destFile.close();
        System.out.print(s);
    }

    public void OnlineActivity(File op) throws IOException {
        String s = "Device " + name + " is performing online activities\n";
        FileWriter destFile = new FileWriter(op, true);
        destFile.write(s);
        destFile.close();
        System.out.print(s);
    }

    public void Disconnect(File op) throws IOException {
        String s = "Device " + name + " logged out\n";
        FileWriter destFile = new FileWriter(op, true);
        destFile.write(s);
        destFile.close();
        System.out.print(s);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}


class Semaphore {
    private int value = 0;
    private int counter = 1;

    public Semaphore() {
        value = 0;
    }

    public Semaphore(int initial) {
        value = initial;
    }

    public synchronized void P(Device device) {
        value--;
        if (value < 0) {
            System.out.println("(" + device.getName() + ") (" + device.getType() + ") arrived and waiting");
            try {
                wait();
            } catch (InterruptedException e) {
            }
        } else {
            System.out.println("(" + device.getName() + ") (" + device.getType() + ") arrived");
        }
    }

    public synchronized void V(Device device) {
        value++;
        if (value <= 0) notify();
    }

    public int getCounter() {
        return counter++;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}


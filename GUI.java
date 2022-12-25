package com.company;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

import static javax.swing.JOptionPane.*;


public class GUI implements ActionListener {
    static NewFrame frame3;
    JTextField text1, text2;
    JLabel label1, label2, label3;
    JButton enter, add, simulate;
    Device device;
    String name = "dum";
    String type = "hok";
    Router router;

    public GUI() throws IOException {
        NewFrame frame1 = new NewFrame("Input Frame");
        text1 = new JTextField("");
        text1.setBounds(180, 100, 150, 30);
        text2 = new JTextField("");
        text2.setBounds(180, 150, 150, 30);
        label1 = new JLabel("Connections count:");
        label1.setBounds(50, 100, 150, 30);
        label2 = new JLabel("Devices count:");
        label2.setBounds(50, 150, 100, 30);
        enter = new JButton("Enter");
        enter.setBounds(210, 400, 80, 30);
        enter.addActionListener(this);
        frame1.AddObject(text1);
        frame1.AddObject(text2);
        frame1.AddObject(label1);
        frame1.AddObject(label2);
        frame1.AddObject(enter);
        frame1.SetV_L();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        NewFrame frame2 = new NewFrame("Device frame");
        if (e.getSource() == enter) {
            Network.connection_Size = Integer.parseInt(text1.getText());
            Network.size = Integer.parseInt(text2.getText());
            try {
                router = new Router(Network.size, Network.connection_Size);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            text1.setText("");
            text2.setText("");
            add = new JButton("Add device");
            add.setBounds(150, 400, 100, 30);
            add.addActionListener(this);
            simulate = new JButton("Satrt");
            simulate.setBounds(250, 400, 100, 30);
            simulate.addActionListener(this);
            label1.setText("Device name");
            label2.setText("Device type");
            text1.setText("");
            text2.setText("");
            frame2.AddObject(text1);
            frame2.AddObject(text2);
            frame2.AddObject(label1);
            frame2.AddObject(label2);
            frame2.AddObject(add);
            frame2.AddObject(simulate);
            frame2.SetV_L();
        } else if (e.getSource() == add) {
            name = text1.getText();
            type = text2.getText();
            device = new Device(name, type);
            router.addDevice(device);
            text2.setText("");
            text1.setText("");
            showMessageDialog(frame2.getFrame(), "Device added");
        } else if (e.getSource() == simulate) {
            router.switchOn();
            frame3=new NewFrame("Simulation frame",1000,1000);
            frame3.SetV_L();



        }
    }

    public static void main(String[] args) throws IOException {
        GUI myGui = new GUI();
    }

}

class NewFrame {
    JFrame frame;

    public NewFrame(String frameName) {
        frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);


    }

    public NewFrame(String frameName, int width, int length) {
        frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, length);
    }

    public void AddObject(Component obj) {
        frame.add(obj);
    }

    public void SetV_L() {
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
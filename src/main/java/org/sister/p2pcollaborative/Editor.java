package org.sister.p2pcollaborative;

// Java Program to create a text editor using java
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.*;
import javax.swing.text.*;

public class Editor extends JFrame implements ActionListener {
    // Text component
    JTextArea t;
    JTextField tf;

    // Frame
    JFrame f;

    // Panel
    JPanel p;
    private KeyListener keyListener;


    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
        t.addKeyListener(keyListener);
    }

    public JTextArea getT() {
        return t;
    }

    // Constructor
    Editor() {
        // Create a frame
        f = new JFrame("Editor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            // Set metl look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            // Set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }
        catch (Exception e) {
        }

        // Create a panel
        p = new JPanel();
        p.setBounds(0,0,500,500);
        p.setBackground(Color.DARK_GRAY);

        // Text component
        t = new JTextArea(500,400);
        t.setBounds(0,0,500,400);

        // Create a menubar
        JMenuBar mb = new JMenuBar();

        // Create File menu
        JMenu m1 = new JMenu("File");

        JMenuItem mi1 = new JMenuItem("Save");

        mi1.addActionListener(this);
        m1.add(mi1);


        // Add File menu to menu bar
        mb.add(m1);

//        tf = new JTextField();
//        tf.setBounds(490,490,500,50);
        //Add Menu and Text to Frame
        f.setJMenuBar(mb);
        f.add(t);
//        p.add(tf);
//        f.add(p);
        f.setSize(500, 500);
        f.setLayout(null);
        f.setVisible(true);
    }

    // If a button is pressed
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if (s.equals("Save")) {
            // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser("f:");

            // Invoke the showsSaveDialog function to show the save dialog
            int r = j.showSaveDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {

                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    // Create a file writer
                    FileWriter wr = new FileWriter(fi, false);

                    // Create buffered writer to write
                    BufferedWriter w = new BufferedWriter(wr);

                    // Write
                    w.write(t.getText());

                    w.flush();
                    w.close();
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
            // If the user cancelled the operation
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation");
        }
    }

    public void insertChar(char c, int position){
        t.insert(String.valueOf(c),position);
    }

    public void deleteChar(int position){
        t.replaceRange(null,position,position+1);
    }

    public int getPosition() {
        return t.getCaretPosition();
    }

    public interface KeyListener extends java.awt.event.KeyListener {

    }
}


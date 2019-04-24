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
    JLabel l,l2;

    //Button
    JButton b;

    // Frame
    JFrame f;

    // Panel
    JPanel p;
    private KeyListener keyListener;

    //Insets
    private static final Insets insets = new Insets(0, 0, 0, 0);

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
        p.setBackground(Color.DARK_GRAY);
        p.setLayout(new GridBagLayout());


        // Create a menubar
        JMenuBar mb = new JMenuBar();

        // Create File menu
        JMenu m1 = new JMenu("File");

        JMenuItem mi1 = new JMenuItem("Save");

        mi1.addActionListener(this);
        m1.add(mi1);


        // Add File menu to menu bar
        mb.add(m1);

        // Panel Layout
        // Text component
        t = new JTextArea(40,100);
        t.setLineWrap(true);
        tf = new JTextField();

        l = new JLabel();
        l.setText("Signal Server Address");
        l.setBackground(Color.WHITE);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        l.setHorizontalAlignment(SwingConstants.CENTER);

        l2 = new JLabel();
        l2.setText("Status : Disconnected");
        l2.setBackground(Color.WHITE);
        l2.setOpaque(true);
        l2.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        l2.setHorizontalAlignment(SwingConstants.CENTER);

        b = new JButton();
        b.setText("Connect");
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.addActionListener(this);

        addComponent(p, t, 0, 0, 4, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,1,80);
        addComponent(p,l,0,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,1,1);
        addComponent(p,tf,1,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,5,1);
        addComponent(p,b,2,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,1,1);
        addComponent(p,l2,3,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,1,1);

        //        tf = new JTextField();
//        tf.setBounds(490,490,500,50);
        //Add Menu and Text to Frame
        f.setJMenuBar(mb);
//        f.add(t);
//        p.add(tf);
//        f.add(p);
        f.setSize(800, 550);
        f.setLayout(new BorderLayout());
        f.add(p, BorderLayout.CENTER);
//        f.setLayout(null);
        f.pack();
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
        } else if (s.equals("Connect")) {
            Controller controller = Controller.getInstance();

            //Parse input text
            String address = tf.getText();
            String host = address.split(":")[0];
            int port = Integer.parseInt(address.split(":")[1]);

            controller.startClient(host, port);
            System.out.println(host + port);
            changeConnectionStatus(true);
        } else if (s.equals("Disconnect")) {
            changeConnectionStatus(false);
            Controller controller = Controller.getInstance();
            controller.disconnect();
            changeConnectionStatus(false);
        }
    }

    public void insertChar(char c, int position){
        t.insert(String.valueOf(c),position);
    }

    public void deleteChar(int position){
        t.replaceRange(null,position,position+1);
    }

    public void changeConnectionStatus(boolean connected){
        if (connected) {
            l2.setText("Status : Connected");
            b.setText("Disconnect");
        }
        else {
            l2.setText("Status : Disconnected");
            b.setText("Connect");
        }
    }
    public int getPosition() {
        return t.getCaretPosition();
    }

    private static void addComponent(Container container, Component component, int gridx, int gridy,
                                     int gridwidth, int gridheight, int anchor, int fill, float weightx, float weighty) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty,
                anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }

    public interface KeyListener extends java.awt.event.KeyListener {

    }
}


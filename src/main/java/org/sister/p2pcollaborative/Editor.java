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
    // Frame
    JFrame f;

    private DocumentListener documentListener;


    public void setDocumentListener(DocumentListener documentListener) {
        this.documentListener = documentListener;
        t.getDocument().addDocumentListener(documentListener);
    }

    public JTextArea getT() {
        return t;
    }

    // Constructor
    Editor() {
        // Create a frame
        f = new JFrame("Editor");

        try {
            // Set metl look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            // Set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }
        catch (Exception e) {
        }

        // Text component
        t = new JTextArea();

        // Create a menubar
        JMenuBar mb = new JMenuBar();

        // Create File menu
        JMenu m1 = new JMenu("File");

        JMenuItem mi1 = new JMenuItem("Save");

        mi1.addActionListener(this);
        m1.add(mi1);


        // Add File menu to menu bar
        mb.add(m1);

        //Add document listener
        //Add Menu and Text to Frame
        f.setJMenuBar(mb);
        f.add(t);
        f.setSize(500, 500);
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

    public int getPosition() {
        return t.getCaretPosition();
    }

    public interface DocumentListener extends javax.swing.event.DocumentListener {

    }
}


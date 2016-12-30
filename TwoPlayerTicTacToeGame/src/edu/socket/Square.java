package edu.socket;

import javax.swing.*;
import java.awt.*;

public class Square extends JPanel {

    private JLabel label = new JLabel((Icon) null);

    public Square() {
        setBackground(Color.white);
        add(label);
    }

    public void setIcon(Icon icon) {
        label.setIcon(icon);
    }
}

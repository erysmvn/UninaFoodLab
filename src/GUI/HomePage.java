package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JButton loginButton;
    private JPanel panel1;

    private Controller controller;

    public HomePage(Controller controller) {
        this.controller = controller;
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.loginStudenteClicked();
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }
}

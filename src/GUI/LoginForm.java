package GUI;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

import Controller.Controller;

public class LoginForm extends JFrame {
    private JPanel panel1;
    private JTextField txtField;
    private JPasswordField pswField;
    private JButton doneButton;

    private Controller controller;

    public LoginForm(Controller controller) {
        this.controller = controller;
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String email = txtField.getText();
                    String psw = pswField.getText();
                    boolean check = controller.validateLoginStudente(email, psw);
                    if (check) {
                        JOptionPane.showMessageDialog(doneButton, "You have successfully logged in");
                    } else {
                        JOptionPane.showMessageDialog(doneButton, "Invalid email address or password");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }
}

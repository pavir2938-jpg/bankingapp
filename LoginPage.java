package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class LoginPage extends JPanel {
    private final JLabel titleLabel = new JLabel("Customer Login");
    private final JTextField accField = new JTextField();
    private final JPasswordField pinField = new JPasswordField();
    private final JButton loginBtn = new JButton("Login \u00BB");
    private final JButton backBtn = new JButton("\u00AB Back");
    
    private String acctName = "";

    public LoginPage(JFrame mainFrame, JPanel previousPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.bgColor);

        titleLabel.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        titleLabel.setForeground(Theme.grayColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        accField.setPreferredSize(Theme.fieldSize);
        accField.setMaximumSize(Theme.fieldSize);
        accField.setFont(Theme.fontStyle);
        accField.setAlignmentX(Component.CENTER_ALIGNMENT);
        accField.setBackground(Theme.bgColor);
        accField.setBorder(BorderFactory.createTitledBorder("Account Number"));

        pinField.setPreferredSize(Theme.fieldSize);
        pinField.setMaximumSize(Theme.fieldSize);
        pinField.setFont(Theme.fontStyle);
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinField.setBackground(Theme.bgColor);
        pinField.setBorder(BorderFactory.createTitledBorder("PIN"));

        loginBtn.setPreferredSize(Theme.btnSize);
        loginBtn.setMaximumSize(Theme.btnSize);
        loginBtn.setFont(Theme.fontStyle);
        loginBtn.setBackground(Theme.grayColor);
        loginBtn.setForeground(Theme.whiteColor);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.setPreferredSize(Theme.btnSize);
        backBtn.setMaximumSize(Theme.btnSize);
        backBtn.setFont(Theme.fontStyle);
        backBtn.setBackground(Theme.whiteColor);
        backBtn.setForeground(Theme.grayColor);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        accField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || accField.getText().length() >= 4) {
                    e.consume();
                }
            }
        });

        pinField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || pinField.getPassword().length >= 5) {
                    e.consume();
                }
            }
        });

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(accField);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(pinField);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(loginBtn);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(backBtn);
        add(Box.createVerticalGlue());

        loginBtn.addActionListener(e -> {
            String accNum = accField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (accNum.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Please enter fields",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
               
            boolean isRegisteredAndValid = false;
            File file = new File("accounts.csv");

            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        String csvAcc = parts[0].trim();
                        String csvPin = parts[1].trim();
                        if (csvAcc.equals(accNum) && csvPin.equals(pin)) {
                           isRegisteredAndValid = true;
                           acctName = parts[2];
                           break;
                       }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, 
                            "Account configuration error", 
                            "Configuration error", 
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (isRegisteredAndValid) {
                JOptionPane.showMessageDialog(this,
                    "Account logged into successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                UserSession session = new UserSession();
                session.setAcctNum(accNum);
                session.setAcctName(acctName);
                
                accField.setText("");
                pinField.setText("");
                
                mainFrame.setTitle("Services");
                mainFrame.setContentPane(new Menu(mainFrame, previousPanel, session));
                mainFrame.revalidate();
                mainFrame.repaint();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Credentials are not valid",
                        "Invalid Login",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            mainFrame.setTitle("Getting Started");
            mainFrame.setContentPane(previousPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }
}
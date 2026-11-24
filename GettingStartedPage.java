package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;

public class GettingStartedPage{
    JFrame getStartedFrame = new JFrame("Getting Started");
    JButton goToLoginBtn = new JButton("Go to Login \u00BB");
    JButton goToRegisterBtn = new JButton("New User? Register Here \u00BB");
    JLabel welcomeLabel = new JLabel("Welcome to The Bank");

    public GettingStartedPage(){
        // Frame container should stack contents top to bottom
        JPanel getStartedPanel = new JPanel();
        getStartedPanel.setLayout(new BoxLayout(getStartedPanel, BoxLayout.Y_AXIS));
        
        // Welcome label styling
        welcomeLabel.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 35f));
        welcomeLabel.setForeground(Theme.grayColor);
        
        // Button styling
        goToLoginBtn.setPreferredSize(Theme.btnSize);
        goToLoginBtn.setMaximumSize(Theme.btnSize);
        goToLoginBtn.setFont(Theme.fontStyle); 
        goToLoginBtn.setBackground(Theme.grayColor);
        goToLoginBtn.setForeground(Theme.whiteColor);
        goToLoginBtn.setFocusPainted(false);
        
        goToRegisterBtn.setPreferredSize(Theme.btnSize);  // ideal
        goToRegisterBtn.setMaximumSize(Theme.btnSize);    // Max stretching allowed
        goToRegisterBtn.setFont(Theme.fontStyle);
        goToRegisterBtn.setBackground(Theme.grayColor);
        goToRegisterBtn.setForeground(Theme.whiteColor);
        goToRegisterBtn.setFocusPainted(false);
        
        // Components should align to the middle of the vertical line of the window
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        goToLoginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        goToRegisterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Component Layout
        getStartedPanel.add(Box.createVerticalGlue()); // Top spring to push buttons down
        getStartedPanel.add(welcomeLabel);
        getStartedPanel.add(Box.createRigidArea(new Dimension(0, 55))); // 55px gap between label and button
        getStartedPanel.add(goToLoginBtn);
        getStartedPanel.add(Box.createRigidArea(new Dimension(0, 15))); // 15px gap between buttons
        getStartedPanel.add(goToRegisterBtn);
        getStartedPanel.add(Box.createVerticalGlue()); // Bottom spring to push buttons up
        
        // Register button listener
        goToRegisterBtn.addActionListener(e -> {
            getStartedFrame.setTitle("Register");
            getStartedFrame.setContentPane(new RegistrationPage(getStartedFrame, getStartedPanel));
            getStartedFrame.revalidate(); // Tells LM to calc positions and sizes of new components
            getStartedFrame.repaint(); // Redraw the window
        });
        
        goToLoginBtn.addActionListener(e -> {
            getStartedFrame.setTitle("Login");
            getStartedFrame.setContentPane(new LoginPage(getStartedFrame, getStartedPanel));
            getStartedFrame.revalidate(); // Tells LM to calc positions and sizes of new components
            getStartedFrame.repaint(); // Redraw the window
        });
        
        // Frame properties
        getStartedFrame.setContentPane(getStartedPanel);
        getStartedFrame.setSize(750, 600);
        getStartedFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getStartedFrame.setLocationRelativeTo(null);
        getStartedFrame.setVisible(true);
    }
}

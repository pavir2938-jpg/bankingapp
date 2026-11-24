package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RegistrationPage extends JPanel {
    JLabel titleLabel = new JLabel("Create an Account");
    JTextField nameField = new JTextField();
    JTextField pinField = new JTextField();
    JTextField amountField = new JTextField();
    JTextField phoneField = new JTextField();
    
    JButton registerBtn = new JButton("Register \u00BB");
    JButton backBtn = new JButton("\u00AB Back");

    public RegistrationPage(JFrame mainFrame, JPanel previousPanel) {
        // LM for RegPage, which is a jpanel, top-down layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Title styling
        titleLabel.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        titleLabel.setForeground(Theme.grayColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input fields styling
        nameField.setPreferredSize(Theme.fieldSize);
        nameField.setMaximumSize(Theme.fieldSize);
        nameField.setFont(Theme.fontStyle);
        nameField.setBackground(Theme.bgColor);
        nameField.setBorder(BorderFactory.createTitledBorder("Name"));
        
        pinField.setPreferredSize(Theme.fieldSize);
        pinField.setMaximumSize(Theme.fieldSize);
        pinField.setFont(Theme.fontStyle);
        pinField.setBackground(Theme.bgColor);
        pinField.setBorder(BorderFactory.createTitledBorder("PIN (Max. 5 digits)"));
        pinField.setTransferHandler(null); // Block pasting which exceeds limit of 5
        
        amountField.setPreferredSize(Theme.fieldSize);
        amountField.setMaximumSize(Theme.fieldSize);
        amountField.setFont(Theme.fontStyle);
        amountField.setBackground(Theme.bgColor);
        amountField.setBorder(BorderFactory.createTitledBorder("Initial Deposit (Rs. 500 to Rs. 5000)"));
        
        phoneField.setPreferredSize(Theme.fieldSize);
        phoneField.setMaximumSize(Theme.fieldSize);
        phoneField.setFont(Theme.fontStyle);
        phoneField.setBackground(Theme.bgColor);
        phoneField.setBorder(BorderFactory.createTitledBorder("Phone Number (Max. 10 digits)"));
        
        // Register button styling
        registerBtn.setPreferredSize(Theme.btnSize);
        registerBtn.setMaximumSize(Theme.btnSize);
        registerBtn.setFont(Theme.fontStyle);
        registerBtn.setBackground(Theme.grayColor);
        registerBtn.setForeground(Theme.whiteColor);
        registerBtn.setFocusPainted(false);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back button styling
        backBtn.setPreferredSize(Theme.btnSize);
        backBtn.setMaximumSize(Theme.btnSize);
        backBtn.setFont(Theme.fontStyle);
        backBtn.setBackground(Theme.whiteColor);
        backBtn.setForeground(Theme.grayColor);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        // Do not allow non-digits, and set limit for the fields
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if ((c != ' ' && !Character.isLetter(c)) || nameField.getText().length() >= 20) {
                    e.consume(); // Ignore key press
                }
            }
        });
        
        pinField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || pinField.getText().length() >= 5) {
                    e.consume(); // Ignore key press
                }
            }
        });
        
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || phoneField.getText().length() >= 10) {
                    e.consume(); // Ignore key press
                }
            }
        });
        
        amountField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || amountField.getText().length() >= 4) {
                    e.consume(); // Ignore key press
                }
            }
        });

        // Register details submission
        registerBtn.addActionListener(e -> regAcct(mainFrame, previousPanel));
        // Return to GettingStartedPage in the same window
        backBtn.addActionListener(e -> goBack(mainFrame, previousPanel));

        // Layout
        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(nameField);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(pinField);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(phoneField);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(amountField);      
        add(Box.createRigidArea(new Dimension(0, 30)));
        
        add(registerBtn);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(backBtn);
        add(Box.createVerticalGlue());
    }
    
    private void regAcct(JFrame mainFrame, JPanel previousPanel){
        String name = nameField.getText().trim();
        String pin = pinField.getText();
        String amount = amountField.getText();
        String phone = phoneField.getText();
        String account;
        
        
        if (name.isEmpty() || pin.isEmpty() || phone.isEmpty() || amount.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter fields",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Respect name character limit
        if (name.length() > 20){
            JOptionPane.showMessageDialog(null, "Please enter 20 characters for name");
            return;
        }
        // PIN should not be less than 5 digits
        if (pin.length() != 5) {
            JOptionPane.showMessageDialog(null, "Please enter 5 digit PIN");
            return;
        }
        // Phone number should not be less than 10 digits
        if (!phone.matches("^[6-9]\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "Please enter valid phone number");
            return;
        }
        
        int initDeposit = Integer.parseInt(amount);
        // Respect initial deposit limits
        if (initDeposit > 5000 || initDeposit < 500){
            JOptionPane.showMessageDialog(null, "Please make initial deposit between Rs. 500 to Rs. 5000");
            return;
        }
        
        // Save details to file
        File csvFile = new File("accounts.csv");
        boolean isNewFile = !csvFile.exists();

        try (FileWriter fw = new FileWriter(csvFile, true);
             PrintWriter pw = new PrintWriter(fw)) {

            if (isNewFile) {
                pw.println("AccountNumber,PIN,Name,Balance,PhoneNumber");
                account = "0001";
            } else {
                List<String> lines = Files.readAllLines(Paths.get("accounts.csv"));
                if (lines.size() < 2){
                    account = "0001";
                } else{
                    String lastAcctNum = lines.get(lines.size() - 1).split(",")[0].trim();
                    int nextAcct = Integer.parseInt(lastAcctNum) + 1;
                    
                    if (nextAcct > 9999) {
                        JOptionPane.showMessageDialog(this, 
                            "Account Number exceeded", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    account = String.format("%04d", nextAcct);
                }  
            }

            pw.println(account + "," + pin + "," + name + "," + amount + "," + phone);

            JOptionPane.showMessageDialog(this,
                    "Account registered successfully!  Your account number is " + account,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            nameField.setText("");
            pinField.setText("");
            amountField.setText("");
            phoneField.setText("");
            
            goBack(mainFrame, previousPanel);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving account: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void goBack(JFrame mainFrame, JPanel previousPanel) {
        mainFrame.setTitle("Getting Started");
        mainFrame.setContentPane(previousPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

        
    
    
}

package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Deposit extends JPanel {
    private final JLabel depositTitle = new JLabel("Deposit Money");
    private final JLabel accountLabel = new JLabel();
    private final JTextField amountField = new JTextField();
    private final JButton depositButton = new JButton("Confirm Deposit");
    private final JButton backBtn = new JButton("\u00AB Back");

    private final String accountNumber;

    public Deposit(JFrame mainFrame, JPanel previousPanel, UserSession session) {
        this.accountNumber = session.getAcctNum();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.bgColor);

        depositTitle.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        depositTitle.setForeground(Theme.grayColor);
        depositTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountLabel.setText("Account: " + accountNumber);
        accountLabel.setFont(Theme.fontStyle.deriveFont(Font.PLAIN, 16f));
        accountLabel.setForeground(Theme.grayColor);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountField.setPreferredSize(Theme.fieldSize);
        amountField.setMaximumSize(Theme.fieldSize);
        amountField.setFont(Theme.fontStyle);
        amountField.setBackground(Theme.bgColor);
        amountField.setBorder(BorderFactory.createTitledBorder("Amount to Deposit (Max. Rs. 50k)"));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return; // allow Backspace, Delete, etc.
                }
                if (!Character.isDigit(c) || amountField.getText().length() >= 5) {
                    e.consume();
                }
            }
        });

        depositButton.setPreferredSize(Theme.btnSize);
        depositButton.setMaximumSize(Theme.btnSize);
        depositButton.setFont(Theme.fontStyle);
        depositButton.setBackground(Theme.grayColor);
        depositButton.setForeground(Theme.whiteColor);
        depositButton.setOpaque(true);
        depositButton.setBorderPainted(false);
        depositButton.setFocusPainted(false);
        depositButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.setPreferredSize(Theme.btnSize);
        backBtn.setMaximumSize(Theme.btnSize);
        backBtn.setFont(Theme.fontStyle);
        backBtn.setBackground(Theme.whiteColor);
        backBtn.setForeground(Theme.grayColor);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        depositButton.addActionListener(e -> depositAmount());
        backBtn.addActionListener(e -> {
            mainFrame.setTitle("ATM Services");
            mainFrame.setContentPane(previousPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        add(Box.createVerticalGlue());
        add(depositTitle);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(accountLabel);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(amountField);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(depositButton);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(backBtn);
        add(Box.createVerticalGlue());
    }

    public static double getBalance(String accountNo) {
        if (accountNo == null || accountNo.isBlank()) {
            return -1;
        }
        try {
            List<String> lines = Files.readAllLines(Path.of("accounts.csv"));

            for (int i = 1; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                if (data.length > 3 && data[0].trim().equals(accountNo.trim())) {
                    return Double.parseDouble(data[3].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error reading account balance.");
        }
        return -1;
    }

    public static void updateBalance(String accountNo, double newBalance) {
        if (accountNo == null || accountNo.isBlank()) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(Path.of("accounts.csv"));
            for (int i = 1; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                if (data.length > 3 && data[0].trim().equals(accountNo.trim())) {
                    data[3] = String.valueOf(newBalance);
                    lines.set(i, String.join(",", data));
                    break;
                }
            }
            Files.write(Path.of("accounts.csv"), lines);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating account balance.");
        }
    }

    private void depositAmount() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            double balance = getBalance(this.accountNumber);

            if (balance == -1) {
                JOptionPane.showMessageDialog(this, "Account not found");
            } else if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount");
            } else if (amount > 50000) {
                JOptionPane.showMessageDialog(this, "Very large amount");
            } else {
                double newBalance = balance + amount;
                updateBalance(accountNumber, newBalance);

                JOptionPane.showMessageDialog(
                        this,
                        "Deposit Successful!\nAmount: Rs. " + amount + "\nNew Balance: Rs. " + newBalance
                );
                amountField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }
}
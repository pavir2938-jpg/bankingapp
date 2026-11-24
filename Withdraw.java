package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Withdraw extends JPanel {
    private final JLabel withdrawTitle = new JLabel("Withdraw Money");
    private final JLabel accountLabel = new JLabel();
    private final JTextField amountField = new JTextField();
    private final JButton withdrawButton = new JButton("Confirm Withdrawal");
    private final JButton backBtn = new JButton("\u00AB Back");
    
    private final String accountNumber;

    public Withdraw(JFrame mainFrame, JPanel previousPanel, UserSession session) {
        this.accountNumber = session.getAcctNum();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.bgColor);

        withdrawTitle.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        withdrawTitle.setForeground(Theme.grayColor);
        withdrawTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountLabel.setText("Account: " + accountNumber);
        accountLabel.setFont(Theme.fontStyle.deriveFont(Font.PLAIN, 16f));
        accountLabel.setForeground(Theme.grayColor);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountField.setPreferredSize(Theme.fieldSize);
        amountField.setMaximumSize(Theme.fieldSize);
        amountField.setFont(Theme.fontStyle);
        amountField.setBackground(Theme.bgColor);
        amountField.setBorder(BorderFactory.createTitledBorder("Amount to Withdraw"));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return; // allow Backspace, Delete, etc.
                }
                if (!Character.isDigit(c) || amountField.getText().length() >= 4) {
                    e.consume();
                }
            }
        });

        withdrawButton.setPreferredSize(Theme.btnSize);
        withdrawButton.setMaximumSize(Theme.btnSize);
        withdrawButton.setFont(Theme.fontStyle);
        withdrawButton.setBackground(Theme.grayColor);
        withdrawButton.setForeground(Theme.whiteColor);
        withdrawButton.setOpaque(true);
        withdrawButton.setBorderPainted(false);
        withdrawButton.setFocusPainted(false);
        withdrawButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.setPreferredSize(Theme.btnSize);
        backBtn.setMaximumSize(Theme.btnSize);
        backBtn.setFont(Theme.fontStyle);
        backBtn.setBackground(Theme.whiteColor);
        backBtn.setForeground(Theme.grayColor);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        withdrawButton.addActionListener(e -> withdrawAmount());
        backBtn.addActionListener(e -> {
            mainFrame.setTitle("ATM Services");
            mainFrame.setContentPane(previousPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        add(Box.createVerticalGlue());
        add(withdrawTitle);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(accountLabel);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(amountField);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(withdrawButton);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(backBtn);
        add(Box.createVerticalGlue());
    }

    public static double getBalance(String accountNo) {
        try{
            List<String> lines = Files.readAllLines(Path.of("accounts.csv"));

            for (int i = 1; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                if (data[0].trim().equals(accountNo.trim())) {
                    return Double.parseDouble(data[3].trim());
                }
            }
        } catch (IOException e){
            JOptionPane.showMessageDialog(
                        null,
                        "Error"
                );
        }
        return -1;
    }

    public static void updateBalance(String accountNo, double newBalance){
        try{
            List<String> lines = Files.readAllLines(Path.of("accounts.csv"));
            for (int i = 1; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                if (data[0].trim().equals(accountNo.trim())) {
                    data[3] = String.valueOf(newBalance);
                    lines.set(i, String.join(",", data));
                    break;
                }
            }
            Files.write(Path.of("accounts.csv"), lines);
        } catch (IOException e){
            JOptionPane.showMessageDialog(
                        null,
                        "Error"
                );
        }
    }

    private void withdrawAmount() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            double balance = getBalance(this.accountNumber);

            if (balance == -1) {
                JOptionPane.showMessageDialog(this, "Account not found");
            } else if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount");
            } else if (amount > balance) {
                JOptionPane.showMessageDialog(this, "Insufficient Balance");
            } else {
                double newBalance = balance - amount;
                updateBalance(accountNumber, newBalance);

                JOptionPane.showMessageDialog(
                        this,
                        "Withdrawal Successful!\nAmount: Rs. " + amount + "\nRemaining Balance: Rs. " + newBalance
                );
                amountField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }
}
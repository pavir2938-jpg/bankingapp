package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Transfer extends JPanel {
    private final JLabel transferTitle = new JLabel("Transfer Money");
    private final JLabel accountLabel = new JLabel();
    private final JTextField toAccountField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JButton transferBtn = new JButton("Confirm Transfer");
    private final JButton backBtn = new JButton("\u00AB Back");

    private final String accountNumber;

    public Transfer(JFrame mainFrame, JPanel previousPanel, UserSession session) {
        this.accountNumber = session.getAcctNum();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.bgColor);

        transferTitle.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        transferTitle.setForeground(Theme.grayColor);
        transferTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountLabel.setText("Account: " + accountNumber);
        accountLabel.setFont(Theme.fontStyle.deriveFont(Font.PLAIN, 16f));
        accountLabel.setForeground(Theme.grayColor);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        toAccountField.setPreferredSize(Theme.fieldSize);
        toAccountField.setMaximumSize(Theme.fieldSize);
        toAccountField.setFont(Theme.fontStyle);
        toAccountField.setBackground(Theme.bgColor);
        toAccountField.setBorder(BorderFactory.createTitledBorder("To Account"));
        toAccountField.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountField.setPreferredSize(Theme.fieldSize);
        amountField.setMaximumSize(Theme.fieldSize);
        amountField.setFont(Theme.fontStyle);
        amountField.setBackground(Theme.bgColor);
        amountField.setBorder(BorderFactory.createTitledBorder("Amount to Transfer (Max. Rs. 50k)"));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Allow digits and control keys (like Backspace) up to 4 digits for account number
        toAccountField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return;
                }
                if (!Character.isDigit(c) || toAccountField.getText().length() >= 4) {
                    e.consume();
                }
            }
        });

        // Allow digits and control keys (like Backspace) up to 5 digits for amount
        amountField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return;
                }
                if (!Character.isDigit(c) || amountField.getText().length() >= 5) {
                    e.consume();
                }
            }
        });

        transferBtn.setPreferredSize(Theme.btnSize);
        transferBtn.setMaximumSize(Theme.btnSize);
        transferBtn.setFont(Theme.fontStyle);
        transferBtn.setBackground(Theme.grayColor);
        transferBtn.setForeground(Theme.whiteColor);
        transferBtn.setOpaque(true);
        transferBtn.setBorderPainted(false);
        transferBtn.setFocusPainted(false);
        transferBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.setPreferredSize(Theme.btnSize);
        backBtn.setMaximumSize(Theme.btnSize);
        backBtn.setFont(Theme.fontStyle);
        backBtn.setBackground(Theme.whiteColor);
        backBtn.setForeground(Theme.grayColor);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        transferBtn.addActionListener(e -> transferAmount());
        backBtn.addActionListener(e -> {
            mainFrame.setTitle("ATM Services");
            mainFrame.setContentPane(previousPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        add(Box.createVerticalGlue());
        add(transferTitle);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(accountLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(toAccountField);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(amountField);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(transferBtn);
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

    private void transferAmount() {
        String toAccountNo = toAccountField.getText().trim();

        if (toAccountNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a recipient account number");
            return;
        }
        if (accountNumber != null && toAccountNo.equals(accountNumber.trim())) {
            JOptionPane.showMessageDialog(this, "Cannot transfer to the same account");
            return;
        }

        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            double senderBalance = getBalance(this.accountNumber);
            double recipientBalance = getBalance(toAccountNo);

            if (senderBalance == -1) {
                JOptionPane.showMessageDialog(this, "Sender account not found");
            } else if (recipientBalance == -1) {
                JOptionPane.showMessageDialog(this, "Recipient account not found");
            } else if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount");
            } else if (amount > 50000) {
                JOptionPane.showMessageDialog(this, "Very large amount");
            } else if (amount > senderBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient Balance");
            } else {
                double newSenderBalance = senderBalance - amount;
                double newRecipientBalance = recipientBalance + amount;

                updateBalance(this.accountNumber, newSenderBalance);
                updateBalance(toAccountNo, newRecipientBalance);

                JOptionPane.showMessageDialog(
                        this,
                        "Transfer Successful!\nTo Account: " + toAccountNo +
                        "\nAmount: Rs. " + amount +
                        "\nRemaining Balance: Rs. " + newSenderBalance
                );
                toAccountField.setText("");
                amountField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }
}
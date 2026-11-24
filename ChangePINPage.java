package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChangePINPage extends JPanel {
    private final JLabel titleLabel = new JLabel("Change PIN");
    private final JLabel accountLabel = new JLabel();
    private final JPasswordField currentPinField = new JPasswordField();
    private final JPasswordField newPinField = new JPasswordField();
    private final JPasswordField confirmPinField = new JPasswordField();
    private final JButton changePinBtn = new JButton("Update PIN");
    private final JButton backBtn = new JButton("\u00AB Back");

    private final String accountNumber;

    public ChangePINPage(JFrame mainFrame, JPanel previousPanel, UserSession session) {
        this.accountNumber = session.getAcctNum();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.bgColor);

        titleLabel.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        titleLabel.setForeground(Theme.grayColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountLabel.setText("Account: " + accountNumber);
        accountLabel.setFont(Theme.fontStyle.deriveFont(Font.PLAIN, 16f));
        accountLabel.setForeground(Theme.grayColor);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        stylePinField(currentPinField, "Current PIN");
        stylePinField(newPinField, "New PIN (5 digits)");
        stylePinField(confirmPinField, "Confirm New PIN");

        attachDigitLimit(currentPinField, 5);
        attachDigitLimit(newPinField, 5);
        attachDigitLimit(confirmPinField, 5);

        changePinBtn.setPreferredSize(Theme.btnSize);
        changePinBtn.setMaximumSize(Theme.btnSize);
        changePinBtn.setFont(Theme.fontStyle);
        changePinBtn.setBackground(Theme.grayColor);
        changePinBtn.setForeground(Theme.whiteColor);
        changePinBtn.setOpaque(true);
        changePinBtn.setBorderPainted(false);
        changePinBtn.setFocusPainted(false);
        changePinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.setPreferredSize(Theme.btnSize);
        backBtn.setMaximumSize(Theme.btnSize);
        backBtn.setFont(Theme.fontStyle);
        backBtn.setBackground(Theme.whiteColor);
        backBtn.setForeground(Theme.grayColor);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        changePinBtn.addActionListener(e -> handleChangePin());
        backBtn.addActionListener(e -> {
            mainFrame.setTitle("ATM Services");
            mainFrame.setContentPane(previousPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(accountLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(currentPinField);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(newPinField);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(confirmPinField);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(changePinBtn);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(backBtn);
        add(Box.createVerticalGlue());
    }

    private void stylePinField(JPasswordField field, String title) {
        field.setPreferredSize(Theme.fieldSize);
        field.setMaximumSize(Theme.fieldSize);
        field.setFont(Theme.fontStyle);
        field.setBackground(Theme.bgColor);
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void attachDigitLimit(JPasswordField field, int maxDigits) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return;
                }
                String current = new String(field.getPassword());
                if (!Character.isDigit(c) || current.length() >= maxDigits) {
                    e.consume();
                }
            }
        });
    }

    private void handleChangePin() {
        String currentPin = new String(currentPinField.getPassword()).trim();
        String newPin = new String(newPinField.getPassword()).trim();
        String confirmPin = new String(confirmPinField.getPassword()).trim();

        if (accountNumber == null || accountNumber.isBlank()) {
            JOptionPane.showMessageDialog(this, "No active account session.");
            return;
        }
        if (currentPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all PIN fields.");
            return;
        }
        if (newPin.length() != 5) {
            JOptionPane.showMessageDialog(this, "New PIN must be 5 digits.");
            return;
        }
        if (!newPin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(this, "New PIN and confirmation do not match.");
            return;
        }
        if (newPin.equals(currentPin)) {
            JOptionPane.showMessageDialog(this, "New PIN must be different from the current PIN.");
            return;
        }

        try {
            Path csvPath = Path.of("accounts.csv");
            List<String> lines = Files.readAllLines(csvPath);
            boolean accountFound = false;
            boolean pinUpdated = false;

            for (int i = 1; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                if (data.length > 1 && data[0].trim().equals(accountNumber.trim())) {
                    accountFound = true;
                    String storedPin = data[1].trim();

                    if (!storedPin.equals(currentPin)) {
                        JOptionPane.showMessageDialog(this, "Current PIN is incorrect.");
                        return;
                    }

                    data[1] = newPin;
                    lines.set(i, String.join(",", data));
                    pinUpdated = true;
                    break;
                }
            }

            if (!accountFound) {
                JOptionPane.showMessageDialog(this, "Account not found.");
                return;
            }

            if (pinUpdated) {
                Files.write(csvPath, lines);
                currentPinField.setText("");
                newPinField.setText("");
                confirmPinField.setText("");
                JOptionPane.showMessageDialog(this, "PIN updated successfully!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating PIN in accounts.csv.");
        }
    }
}
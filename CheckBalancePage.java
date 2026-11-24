package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CheckBalancePage extends JPanel {
    private final JLabel titleLabel = new JLabel("Check Balance");
    private final JLabel descLabel = new JLabel("Your account balance is:");
    private final JLabel amountLabel = new JLabel("Rs. 0.00");
    private final JButton backBtn = new JButton("\u00AB Back");

    private final String accountNumber;

    public CheckBalancePage(JFrame mainFrame, JPanel previousPanel, UserSession session) {
        this.accountNumber = session.getAcctNum();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.bgColor);

        titleLabel.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        titleLabel.setForeground(Theme.grayColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        descLabel.setFont(Theme.fontStyle);
        descLabel.setForeground(Theme.grayColor);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountLabel.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        amountLabel.setForeground(Color.BLACK);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.setPreferredSize(Theme.btnSize);
        backBtn.setMaximumSize(Theme.btnSize);
        backBtn.setFont(Theme.fontStyle);
        backBtn.setBackground(Theme.whiteColor);
        backBtn.setForeground(Theme.grayColor);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.addActionListener(e -> {
            mainFrame.setTitle("ATM Services");
            mainFrame.setContentPane(previousPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(descLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(amountLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(backBtn);
        add(Box.createVerticalGlue());

        loadBalance();
    }

    private void loadBalance() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("accounts.csv"));
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 4 && parts[0].trim().equals(accountNumber.trim())) {
                    String balance = parts[3].trim();
                    amountLabel.setText("Rs. " + balance);
                    return;
                }
            }
            amountLabel.setText("NA");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error reading account balance: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
            amountLabel.setText("Error");
        }
    }
}
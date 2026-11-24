package com.mycompany.bankingapp;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    private final JLabel titleLabel = new JLabel("Choose a Service");
    private final UserSession session;
    private final JLabel accountLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final String accountNumber;
    private final String accountName;

    public Menu(JFrame mainFrame, JPanel previousPanel, UserSession session) {
        this.session = session;
        this.accountNumber = session.getAcctNum();
        this.accountName = session.getAcctName();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.bgColor);

        titleLabel.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 30f));
        titleLabel.setForeground(Theme.grayColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountLabel.setText("Account: " + accountNumber);
        accountLabel.setFont(Theme.fontStyle.deriveFont(Font.PLAIN, 16f));
        accountLabel.setForeground(Theme.grayColor);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        nameLabel.setText("Hello,  " + accountName + "!");
        nameLabel.setFont(Theme.fontStyle.deriveFont(Font.PLAIN, 16f));
        nameLabel.setForeground(Theme.grayColor);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        gridPanel.setBackground(Theme.bgColor);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setMaximumSize(new Dimension(540, 250));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] options = {
            "Withdraw", "Deposit",
            "Transfer", "Check Balance",
            "Change PIN", "Logout"
        };

        for (String option : options) {
            JButton button = new JButton(option);
            button.setFont(Theme.fontStyle.deriveFont(Font.BOLD, 15f));
            button.setPreferredSize(Theme.btnSize);
            button.setMaximumSize(Theme.btnSize);
            button.setBackground(Theme.grayColor);
            button.setForeground(Theme.whiteColor);
            button.setFocusPainted(false);

            switch (option) {
                
                case "Withdraw" -> {
                    button.addActionListener(e -> navigateTo(
                            mainFrame,
                            "Withdraw Money",
                            new Withdraw(mainFrame, this, session)
                    ));
                }
                case "Deposit" -> {
                    button.addActionListener(e -> navigateTo(
                            mainFrame,
                            "Deposit Money",
                            new Deposit(mainFrame, this, session)
                    ));
                }
                case "Transfer" -> {
                    button.addActionListener(e -> navigateTo(
                            mainFrame,
                            "Transfer Money",
                            new Transfer(mainFrame, this, session)
                    ));
                }
                case "Check Balance" -> {
                    button.setBackground(Theme.grayColor);
                    button.setForeground(Theme.whiteColor);
                    button.addActionListener(e -> navigateTo(
                            mainFrame,
                            "Check Balance",
                            new CheckBalancePage(mainFrame, this, session)
                    ));
                }
                case "Change PIN" -> {
                    button.addActionListener(e -> navigateTo(
                            mainFrame,
                            "Change PIN",
                            new ChangePINPage(mainFrame, this, session)
                    ));
                }
                case "Logout" -> {
                    button.setBackground(Theme.whiteColor);
                    button.setForeground(Theme.grayColor);
                    button.addActionListener(e -> {
                        this.session.clearNum();
                        navigateTo(
                            mainFrame,
                            "Login",
                            new LoginPage(mainFrame, previousPanel)
                    );});
                }
            }

            gridPanel.add(button);
        }

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(accountLabel);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(nameLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(gridPanel);
        add(Box.createVerticalGlue());
    }

    private void navigateTo(JFrame mainFrame, String title, JPanel nextPanel) {
        mainFrame.setTitle(title);
        mainFrame.setContentPane(nextPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
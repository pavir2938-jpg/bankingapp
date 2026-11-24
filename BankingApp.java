package com.mycompany.bankingapp;

import javax.swing.SwingUtilities;

public class BankingApp extends GettingStartedPage{
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GettingStartedPage());
    }
}
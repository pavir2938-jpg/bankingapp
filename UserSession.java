package com.mycompany.bankingapp;

import org.apache.commons.text.WordUtils;
    
public class UserSession {
    private String acctNum;
    private String acctName;

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public void clearNum() {
        this.acctNum = null;
    }
    
    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = WordUtils.capitalizeFully(acctName);
    }
    
    public void clearName() {
        this.acctName = null;
    }
}
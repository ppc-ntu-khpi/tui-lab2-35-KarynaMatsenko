/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mybank.tui;

import com.mybank.data.DataSource;
import com.mybank.domain.*;
import jexer.TAction;
import jexer.TApplication;
import jexer.TField;
import jexer.TText;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
/**
 *
 * @author Admin
 */
public class TUIdemo extends TApplication {

    private static final int ABOUT_APP = 2000;
    private static final int CUST_INFO = 2010;

    public static void main(String[] args) throws Exception {
        // Load bank data from file
        DataSource dataSource = new DataSource("src\\com\\mybank\\data\\test.dat");
        dataSource.loadData();

        TUIdemo tdemo = new TUIdemo();
        (new Thread(tdemo)).start();
    }

    public TUIdemo() throws Exception {
        super(BackendType.SWING);

        addToolMenu();
        // custom 'File' menu
        TMenu fileMenu = addMenu("&File");
        fileMenu.addItem(CUST_INFO, "&Customer Info");
        fileMenu.addDefaultItem(TMenu.MID_SHELL);
        fileMenu.addSeparator();
        fileMenu.addDefaultItem(TMenu.MID_EXIT);
        // end of 'File' menu

        addWindowMenu();
        
        // custom 'Help' menu
        TMenu helpMenu = addMenu("&Help");
        helpMenu.addItem(ABOUT_APP, "&About...");
        // end of 'Help' menu

        setFocusFollowsMouse(true);
        // Customer window
        ShowCustomerDetails();
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        if (menu.getId() == ABOUT_APP) {
            messageBox("About",
                    "\t\t\t\t\t   Just a simple Jexer demo.\n\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich")
                            .show();
            return true;
        }
        if (menu.getId() == CUST_INFO) {
            ShowCustomerDetails();
            return true;
        }
        return super.onMenu(menu);
    }

     private void ShowCustomerDetails() {
        TWindow custWin = addWindow("Customer Window", 2, 1, 40, 12, TWindow.NOZOOMBOX);
        custWin.newStatusBar("Enter valid customer number and press Show");

        custWin.addLabel("Enter customer number: ", 2, 2);
        TField custNo = custWin.addField(24, 2, 3, false);
        
        TText details = custWin.addText("Owner Name: \nAccount Type: \nAccount Balance: ", 2, 4, 38, 8);
        custWin.addButton("&Show", 28, 2, new TAction() {
            @Override
            public void DO() {
                try {
                    int custNum = Integer.parseInt(custNo.getText());
                    Customer customer = Bank.getCustomer(custNum);

                    String sb = "";
                    sb = "Owner Name: " +customer.getFirstName()+" "+customer.getLastName()+ "\n";
                   
                    for (int i = 0; i < customer.getNumberOfAccounts(); i++) {
                        sb = sb + "Account Type: ";
                        Account account = customer.getAccount(i);
                        
                        if (account instanceof SavingsAccount){ 
                            sb = sb +"'Savings'\n";
                        }
                        else if (account instanceof CheckingAccount){
                            sb = sb + "'Checking'\n";
                        }
                        
                        sb = sb + "Account Balance: $" + account.getBalance() +"\n";
                    }

                    details.setText(sb.toString());
                } catch (Exception e) {
                    messageBox("Error","Input valid customer id").show();
                }
            }
        });    
    }
}
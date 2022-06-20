/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.controller;

import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Snack;
import com.sg.vendingmachine.service.VendingMachineDataValidationException;
import com.sg.vendingmachine.service.VendingMachineDuplicateNameException;
import com.sg.vendingmachine.service.VendingMachineInsufficientFundsException;
import com.sg.vendingmachine.service.VendingMachineOutOfStockException;
import com.sg.vendingmachine.service.VendingMachineServiceLayerImpl;
import com.sg.vendingmachine.ui.UserIO;
import com.sg.vendingmachine.ui.UserIOConsoleImpl;
import com.sg.vendingmachine.ui.VendingMachineView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author abdulrahman
 */
public class VendingMachineController {
    
    private final VendingMachineView view;
    private final VendingMachineServiceLayerImpl serv;

    public VendingMachineController(VendingMachineView view, VendingMachineServiceLayerImpl serv) {
        this.view = view;
        this.serv = serv;
    }
    
    
    
    private Scanner scanner;
    private UserIO io  = new UserIOConsoleImpl();
    
    public void run() {
        
        boolean keepGoing = true;
        int menuSelection = 0;
        BigDecimal userMoney = new BigDecimal("0");
        
        try {
            while (keepGoing) {
                String name;
                BigDecimal price;
                int inventory;

                List<Snack> snackList = serv.getAllSnacks();
                menuSelection = view.printMenuGetChoice(snackList);

                switch (menuSelection) {
                    case 1:
                        this.getAllSnacks();
                        break;
                    case 2:
                        this.addSnack();

                        break;
                    case 3:
                        this.getSnack();

                        break;
                    case 4:
                        this.removeSnack();

                        break;
                    case 5:
                        keepGoing = false;
                    default:
                        io.print("UNKNOWN COMMAND");

                }   

            }
        } catch (VendingMachineInsufficientFundsException | 
                VendingMachinePersistenceException | 
                VendingMachineOutOfStockException e ) {
            
            view.displayError(e.getMessage());
            
        }
    
    }
    
    
    public void addSnack() throws VendingMachinePersistenceException {
        view.displayCreateSnackBanner();
        
        boolean hasErrors = false;
        
        do {
            
            try {
                
                Snack snack = view.getNewSnackInfo();
        
                serv.addSnack(snack);
                
            } catch (VendingMachineDuplicateNameException | VendingMachineDataValidationException e) {
                
                hasErrors = true;
                view.displayError(e.getMessage());
                
            }
            
        } while (hasErrors);
        
        view.displayCreateSuccessBanner();
        
    }
    
    public void getAllSnacks() throws VendingMachinePersistenceException {
        
        view.displayAllSnackBanner();
        List<Snack> snackList = serv.getAllSnacks();
        
        view.displaySnacks(snackList);
        
    }
    
    public Snack getSnack() throws 
            VendingMachineInsufficientFundsException, 
            VendingMachineOutOfStockException,
            VendingMachinePersistenceException {
        
        String name = view.getSnackChoice();
        
        BigDecimal funds = view.getInputFunds();
        
        Snack snack = serv.getSnack(name, funds);
        BigDecimal price = snack.getPrice();
        
        List<BigDecimal> numberOfCoins = new ArrayList<>();
        
        if (funds.compareTo(snack.getPrice()) == 1) {
        
            numberOfCoins = serv.returnChange(funds, price);
        
        }
        
        view.displaySnack(snack);
        
        view.returnChangeInfo(numberOfCoins, price, funds);
        
        return snack;
    }
    
    public void removeSnack() throws VendingMachinePersistenceException {
        
        view.removeSnackBanner();
        String name = view.getSnackChoice();
        Snack removedSnack = serv.removeSnack(name);
        
        view.removeSnack(removedSnack);
        
    }
    
}
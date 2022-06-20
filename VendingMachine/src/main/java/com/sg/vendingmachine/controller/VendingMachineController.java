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
                        this.addSnack();

                        break;
                    case 2:
                        this.getSnack();

                        break;
                    case 3:
                        this.removeSnack();

                        break;
                    case 4:
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
    
    
    public void addSnack() throws 
            VendingMachinePersistenceException,
            VendingMachineOutOfStockException{
        view.displayCreateSnackBanner();
        
        boolean hasErrors = false;
        
        do {
            
            try {
                
                String password = view.getAuthorizationKey();
                boolean isAuthorized = serv.isAuthorizedUser(password);
                
                if (isAuthorized) {
                    Snack snack = view.getNewSnackInfo();

                    serv.addSnack(snack);
                    
                    view.displayCreateSuccessBanner();
                } else {
                    
                    view.displayError("UNAUTHORIZED!");
                    
                }
                
            } catch (VendingMachineDuplicateNameException | VendingMachineDataValidationException e) {
                
                hasErrors = true;
                view.displayError(e.getMessage());
                
            }
            
        } while (hasErrors);
        
    }
    
    public void getAllSnacks() throws 
            VendingMachinePersistenceException,
            VendingMachineOutOfStockException {
        
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
        
        if (snack == null) {
            
            view.displayError("Could not find " + name + ", please try again.");
            return null;
            
        } else {
            
            try{
                BigDecimal price = snack.getPrice();

                List<BigDecimal> numberOfCoins = new ArrayList<>();

                if (snack.getInventory() < 1) {
                    throw new VendingMachineOutOfStockException(snack.getName() + " is currently out of stock!");

                } else if (serv.enoughFunds(price, funds)) {
                    
                    serv.reduceInventory(snack);
                    numberOfCoins = serv.returnChange(funds, price);
                    view.displaySnack(snack);

                    view.returnChangeInfo(numberOfCoins, price, funds);


                } else if (!serv.enoughFunds(price, funds)) {

                    view.displayError("Insufficient Funds! " + snack.getName() + " costs " + snack.getPrice().toString() + ". You paid with $" + funds.toString() + ".");
                    return null;

                } else if (snack.getInventory() < 1) {

                    view.displayError(snack.getName() + " is currently out of stock!");

                }
            } catch (VendingMachineOutOfStockException e) {
                view.displayError(snack.getName() + " is currently out of stock!");
            }
        }
        return snack;
        
    }
    
    public void removeSnack() throws 
            VendingMachinePersistenceException,
            VendingMachineOutOfStockException {
        
        String password = view.getAuthorizationKey();
        boolean isAuthorized = serv.isAuthorizedUser(password);
        
        if (isAuthorized) {
            view.removeSnackBanner();
            String name = view.getSnackChoice();
            Snack removedSnack = serv.removeSnack(name);
        
            view.removeSnack(removedSnack);
        } else {
                    
            view.displayError("UNAUTHORIZED!");
                    
        }
        
    }
    
}

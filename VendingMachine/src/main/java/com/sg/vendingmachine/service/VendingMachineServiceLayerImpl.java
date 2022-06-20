/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineAuditDao;
import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Coin;
import com.sg.vendingmachine.dto.Snack;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abdulrahman
 */
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer{

    private VendingMachineDao dao;
    private VendingMachineAuditDao auditDao;

    public VendingMachineServiceLayerImpl(VendingMachineDao dao, VendingMachineAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }
    
    private void validateSnackData(Snack snack) throws VendingMachineDataValidationException {
        
        if (snack.getName() == null
                || snack.getName().trim().length() == 0
                || snack.getPrice() == null 
                || snack.getPrice().compareTo(new BigDecimal("0")) == 0
                || snack.getInventory() == 0) {
            throw new VendingMachineDataValidationException (
                    "ERROR: All fields [First Name, Last Name, Cohort] are required to be non-zero!"
            );
        }
    }
    
    @Override
    public void addSnack(Snack snack) throws 
            VendingMachinePersistenceException, 
            VendingMachineDuplicateNameException,
            VendingMachineDataValidationException{
        
        if (dao.getSnack(snack.getName()) != null) {
            
            throw new VendingMachineDuplicateNameException (
                    "ERROR: Could not create snack. Snack "
                    + snack.getName() 
                    + " already exists!"
            );
        }
        
        this.validateSnackData(snack);
        dao.addSnack(snack.getName(), snack);
        
        
        auditDao.writeAuditEntry("Added snack " + snack.getName() + ".");
    }

    @Override
    public List<Snack> getAllSnacks() throws VendingMachinePersistenceException {
        try {
            List<Snack> snackList = dao.getAllSnacks();
            List<Snack> snacksInStock = new ArrayList<Snack>();
            
            for (Snack snack: snackList) {
                if (snack.getInventory() > 0) {
                    snacksInStock.add(snack);
                }
            }
            
            return snacksInStock;
        } catch (VendingMachinePersistenceException e) {
            throw new VendingMachinePersistenceException("Failed to add snack.");
        }
    }

    @Override
    public Snack getSnack(String name, BigDecimal funds) throws
            VendingMachineInsufficientFundsException, 
            VendingMachineOutOfStockException,
            VendingMachinePersistenceException{
        
        Snack snack;
        
        try {
            snack = dao.getSnack(name);
            
            if (snack == null) {
            
            return null;
            
            }
        } catch (VendingMachinePersistenceException e) {
            throw new VendingMachinePersistenceException("Failed to get snack.");
        }
        BigDecimal price = snack.getPrice();
        List<Integer> numberOfCoins;
        
        if (snack == null) {
            
            return null;
            
        } else if (funds.compareTo(snack.getPrice()) == -1) {
            
            throw new VendingMachineInsufficientFundsException(name + " costs " + snack.getPrice().toString() + ". You paid with $" + funds.toString() + ".");
            
        } else if (snack.getInventory() < 0) {
            
            throw new VendingMachineOutOfStockException(name + " is currently out of stock!");
            
        }
        
        return snack;
    }

    @Override
    public Snack removeSnack(String name) throws VendingMachinePersistenceException {
        
        Snack removedSnack = dao.removeSnack(name);
        
        if (removedSnack == null) {
            
            auditDao.writeAuditEntry("No such snack");
            return null;
            
        } else {
        
            auditDao.writeAuditEntry("Removed snack " + removedSnack.getName() + ".");
        
        }
        
        return removedSnack;
        
    }
    
    public List<BigDecimal> returnChange(BigDecimal payment, BigDecimal price) {
        
        List<BigDecimal> numberOfCoins = new ArrayList<>();
        
        BigDecimal change = payment.subtract(price);
        
        BigDecimal changeLeft = change;
        
        BigDecimal numQuarters = new BigDecimal(0);
        BigDecimal numDimes = new BigDecimal(0);
        BigDecimal numNickels = new BigDecimal(0);
        BigDecimal numPennies = new BigDecimal(0);
        
        BigDecimal zero = new BigDecimal("0");
        
        BigDecimal quarter = Coin.QUARTER.getCents();
        BigDecimal dime = Coin.DIME.getCents();
        BigDecimal nickel = Coin.NICKEL.getCents();
        BigDecimal penny = Coin.PENNY.getCents();
        
        while(changeLeft.compareTo(zero) != 0){
            
            if (changeLeft.compareTo(quarter) == 1) {
                
                numQuarters = numQuarters.add(new BigDecimal("1"));
                changeLeft = changeLeft.subtract(quarter);
                
            } else if (changeLeft.compareTo(dime) == 1) {
                
                numDimes = numDimes.add(new BigDecimal("1"));
                changeLeft = changeLeft.subtract(dime);
                
            } else if (changeLeft.compareTo(nickel) == 1) {
                
                numNickels = numNickels.add(new BigDecimal("1"));
                changeLeft = changeLeft.subtract(nickel);
                
            } else {
                
                numPennies = numPennies.add(new BigDecimal("1"));
                changeLeft = changeLeft.subtract(penny);
                
            }
            
        }
        
        
        numberOfCoins.add(numQuarters);
        numberOfCoins.add(numDimes);
        numberOfCoins.add(numNickels);
        numberOfCoins.add(numPennies);
        numberOfCoins.add(change);
        
        return numberOfCoins;
    }
    
}

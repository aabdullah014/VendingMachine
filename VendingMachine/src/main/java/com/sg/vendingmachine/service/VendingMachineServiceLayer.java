/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Snack;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author abdulrahman
 */
public interface VendingMachineServiceLayer {
    
    void addSnack(Snack snack) throws 
            VendingMachinePersistenceException, 
            VendingMachineDuplicateNameException,
            VendingMachineDataValidationException;
 
    List<Snack> getAllSnacks() throws 
            VendingMachinePersistenceException;
 
    Snack getSnack(String name, BigDecimal funds) throws 
            VendingMachineInsufficientFundsException,
            VendingMachinePersistenceException;
 
    Snack removeSnack(String name) throws 
            VendingMachinePersistenceException;
    
    List<BigDecimal> returnChange(BigDecimal payment, BigDecimal price);
    
    boolean isAuthorizedUser(String password);
    
    boolean enoughFunds(BigDecimal price, BigDecimal funds) throws 
            VendingMachineInsufficientFundsException;
    
    boolean inStock(Snack snack) throws 
            VendingMachineOutOfStockException;
    
    Snack reduceInventory(Snack snack) throws 
            VendingMachinePersistenceException;
    
}

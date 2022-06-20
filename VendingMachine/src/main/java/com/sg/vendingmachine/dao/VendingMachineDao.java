/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Snack;
import com.sg.vendingmachine.service.VendingMachineOutOfStockException;
import java.util.List;

/**
 *
 * @author abdulrahman
 */
public interface VendingMachineDao {
    
    Snack addSnack(String name, Snack snack) throws 
            VendingMachinePersistenceException;
    
    List<Snack> getAllSnacks() throws VendingMachinePersistenceException;
    
    Snack getSnack(String name) throws VendingMachinePersistenceException;
    
    Snack removeSnack(String name) throws 
            VendingMachinePersistenceException;
    
    Snack reduceInventory(Snack snack) throws 
            VendingMachinePersistenceException ;
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Snack;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abdulrahman
 */
public class VendingMachineDaoStubImpl implements VendingMachineDao{
    
    public Snack onlySnack;
    
    public VendingMachineDaoStubImpl() {
        
        onlySnack = new Snack("Mars");
        onlySnack.setPrice(new BigDecimal("1.15"));
        onlySnack.setInventory(1);
        
    }
    
    public VendingMachineDaoStubImpl(Snack testSnack) {
        
        this.onlySnack = testSnack;
        
    }
    
    @Override
    public Snack addSnack(String name, Snack snack) throws VendingMachinePersistenceException {
        
        if (name.equals(onlySnack.getName())) {
            
            return onlySnack;
            
        } else {
        
        return null;
        
        }
        
    }

    @Override
    public List<Snack> getAllSnacks() throws VendingMachinePersistenceException {
        
        List<Snack> snackList = new ArrayList<>();
        snackList.add(onlySnack);
        return snackList;
        
    }

    @Override
    public Snack getSnack(String name) throws VendingMachinePersistenceException {
        
        if (name.equals(onlySnack.getName())) {
            
            return onlySnack;
            
        } else {
            
            return null;
            
        }
        
    }

    @Override
    public Snack removeSnack(String name) throws VendingMachinePersistenceException {
        if (name.equals(onlySnack.getName())) {
            return onlySnack;
        } else {
            return null;
        }
    }
    
    @Override
    public Snack reduceInventory(Snack snack) throws 
            VendingMachinePersistenceException, 
            VendingMachineOutOfStockException {
        
        int inventory = snack.getInventory();
        
        snack.setInventory(inventory-1);
        
        
        return snack;
    }
    
}

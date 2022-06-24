/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Snack;
import com.sg.vendingmachine.service.VendingMachineOutOfStockException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author abdulrahman
 */

@Component
public class VendingMachineDaoFileImpl implements VendingMachineDao{

    public VendingMachineDaoFileImpl() {
        
        this.VENDINGMACHINE_FILE = "vendingMachine.txt";
        
    }
    
    public VendingMachineDaoFileImpl(String VENDINGMACHINE_FILE) {
        
        this.VENDINGMACHINE_FILE = VENDINGMACHINE_FILE;
        
    }
    
    private Map<String, Snack> snackList = new HashMap<>();
    public final String VENDINGMACHINE_FILE;
    public static final String DELIMETER = " :: ";
    
    @Override
    public Snack addSnack(String name, Snack snack) throws 
            VendingMachinePersistenceException{
        
        this.loadRoster();
        Snack newSnack = snackList.put(name, snack);
        this.writeRoster();
        
        return newSnack;
        
    }

    @Override
    public List<Snack> getAllSnacks() throws VendingMachinePersistenceException {
        
        this.loadRoster();
        return new ArrayList<Snack>(snackList.values());
        
    }

    @Override
    public Snack getSnack(String name) throws VendingMachinePersistenceException {
        
        this.loadRoster();
        
        Snack snack = null;
        
        // method to equate "lays" and "Lays" for example
        for (Snack checkSnack: new ArrayList<Snack>(snackList.values())) {
            
            if (checkSnack.getName().toLowerCase().equals(name.toLowerCase())) {
                snack = checkSnack;
            }
        }
        
        return snack;
   
    }

    @Override
    public Snack removeSnack(String name) throws 
            VendingMachinePersistenceException{
        
        this.loadRoster();
        
        Snack removedSnack = null;
        
        for (Snack checkSnack: new ArrayList<Snack>(snackList.values())) {
            
            if (checkSnack.getName().toLowerCase().equals(name.toLowerCase())) {
                removedSnack = checkSnack;
                
            }
        }
        
        if (removedSnack == null) {
            return null;
        }
        
        removedSnack = snackList.remove(removedSnack.getName());
        this.writeRoster();
        
        return removedSnack;
        
    }
    
    @Override
    public Snack reduceInventory(Snack snack) throws 
            VendingMachinePersistenceException{
        
        // reduce inventory when purchasing a snack
        this.loadRoster();
        snackList.remove(snack.getName());
        int inventory = snack.getInventory();
        
        snack.setInventory(inventory-1);
        snackList.put(snack.getName(), snack);
        
        this.writeRoster();
        
        return snack;
        
    }
    
    private Snack unmarshallSnack(String snackAsText){
        String[] snackTokens = snackAsText.split(DELIMETER);
        
        String name = snackTokens[0];
        BigDecimal price = new BigDecimal(snackTokens[1]);
        int inventory = Integer.parseInt(snackTokens[2]);
        
        Snack snack = new Snack(name);
        snack.setPrice(price);
        snack.setInventory(inventory);
        
        return snack;
    }
    
    private String marshallSnack(Snack snack) throws VendingMachineOutOfStockException {
        String snackAsText = snack.getName() + DELIMETER;
        
        snackAsText += snack.getPrice().toString() + DELIMETER 
                        + String.valueOf(snack.getInventory());
        
        return snackAsText;
    }
    
    private void writeRoster() throws 
            VendingMachinePersistenceException,
            VendingMachineOutOfStockException {
        PrintWriter out;
        
        try{
            out = new PrintWriter(new FileWriter(VENDINGMACHINE_FILE));
        } catch (IOException e){
            throw new VendingMachinePersistenceException("Could not save student data.", e);
        }
        
        String snackAsText;
        
        List<Snack> snackList = this.getAllSnacks();
        for (Snack snack: snackList) {
            snackAsText = marshallSnack(snack);
            out.println(snackAsText);
            out.flush();
        }
        
        out.close();
    }
    
    private void loadRoster() throws VendingMachinePersistenceException {
        Scanner scanner;
        
        try{
            scanner = new Scanner(
                            new BufferedReader(
                                new FileReader(VENDINGMACHINE_FILE)));
        } catch (FileNotFoundException e) {
            throw new VendingMachinePersistenceException("Could not load data into memory.", e);
        }
        
        String currentLine;
        
        Snack currentSnack;
        
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            
            currentSnack = unmarshallSnack(currentLine);
            
            snackList.put(currentSnack.getName(), currentSnack);
        }
        
        scanner.close();
    }
    
}

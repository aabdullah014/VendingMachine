/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.ui;

import com.sg.vendingmachine.dto.Snack;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author abdulrahman
 */

@Component
public class VendingMachineView {
    
    private final UserIO io;

    @Autowired
    public VendingMachineView(UserIO io) {
        this.io = io;
    }
    
    
    public int printMenuGetChoice(List<Snack> snackList){
        this.delineater();
        this.displaySnacks(snackList);
        this.delineater();
        io.print("1. Create a New Snack [ADMIN ONLY]");
        io.print("2. Purchase a Snack");
        io.print("3. Remove a Snack [ADMIN ONLY]");
        io.print("4. Exit");
            
        return io.readInt("Please Select from the numbers above.", 1, 5);
    }
    
    public Snack getNewSnackInfo(){
        String name = io.readString("Please enter snack name.");
        BigDecimal price = io.readBigDecimal("Please enter the price per unit.");
        int inventory = io.readInt("Please enter " + name + "'s inventory.");
        
        Snack snack = new Snack(name);
        
        snack.setPrice(price);
        snack.setInventory(inventory);
        
        return snack;
    }
    
    public void returnChangeInfo(List<BigDecimal> numberOfCoins, BigDecimal price, BigDecimal funds) {
        BigDecimal change = numberOfCoins.get(numberOfCoins.size()-1);
        
        BigDecimal numberOfQuarters = numberOfCoins.get(0);
        BigDecimal numberOfDimes = numberOfCoins.get(1);
        BigDecimal numberOfNickels = numberOfCoins.get(2);
        BigDecimal numberOfPennies = numberOfCoins.get(3);
        
        io.print("You paid " + funds.toString() + " dollars.");
        io.print("The price was " + price.toString() + ", so your change is " + String.valueOf(change));
        io.print("Returned " + String.valueOf(numberOfQuarters) + " quarters, "
                + String.valueOf(numberOfDimes) + " dimes, " 
                + String.valueOf(numberOfNickels) +" nickels, and " 
                + String.valueOf(numberOfPennies) +" pennies.");
        
        io.readString("Please press ENTER to proceed.");
    }
    
    public void displayCreateSnackBanner(){
        io.print("**********************CREATE SNACK**********************");
    }
    
    public void displayCreateSuccessBanner(){
        io.print("**********************SUCCESS*************************");
        io.readString("Please hit ENTER to proceed.");
    }
    
    public void displaySnacks(List<Snack> snackList){
        for(Snack s: snackList){
            String snackInfo = String.format("%s : costs $%s.",
                    s.getName(),
                    s.getPrice());
            io.print(snackInfo);
        }
    }
    
    public void displayAllSnackBanner(){
        io.print("**********************ALL SNACKS**********************");
    }
    
    public void showSnackBanner(){
        io.print("**********************DISPLAY SNACK**********************");
    }
    
    public String getSnackChoice(){
        return io.readString("Please enter snack name to fetch.");
    }
    
    public BigDecimal getInputFunds() {
        return io.readBigDecimal("How much money would you like to input?");
    }
    
    public void displaySnack(Snack snack){
        if (snack != null){
            io.print( "Successfully purchased " + snack.getName() + ".");
            io.print("Price: " + snack.getPrice().toString() + ".");
        } else {
            io.print("No such snack found.");
        }
    }
    
    public void removeSnackBanner(){
        io.print("**********************REMOVE SNACK**********************");
    }
    
    public void removeSnack(Snack snack){
        if (snack != null){
            io.print("Snack successfully removed.");
        } else {
            io.print("No such snack found.");
        }
        
        io.readString("Please hit ENTER to proceed.");
    }
    
    public void displayExitBanner(){
        io.print("Goodbye!");
    }
    
    public void displayUnknownCommandBanner(){
        io.print("Unknown command!");
    }
    
    public void displayError(String error){
        io.print("**********************ERROR************************");
        io.print(error);
    }
    
    public String getAuthorizationKey() {
        
        String password = io.readString("Please input admin password");
        
        return password;
        
    }
    
    public void delineater() {
        
        io.print("------------------------------------------------");
        
    }
    
}

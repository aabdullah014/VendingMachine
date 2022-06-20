/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 *
 * @author abdulrahman
 */
public class VendingMachineAuditDaoFileImpl implements VendingMachineAuditDao {

    public static final String AUDIT_FILE = "audit.txt";
    
    @Override
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException {
        
        PrintWriter out;
        
        try{
            
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
            
        } catch (IOException e) {
            
            throw new VendingMachinePersistenceException(
                    "Could not write to audit file!"
            );
            
        }
        
        LocalDateTime timeStamp = LocalDateTime.now();
        out.println(timeStamp.toString() + ": " + entry);
        out.flush();
        
    }
    
    
    
}

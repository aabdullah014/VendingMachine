/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineAuditDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import org.springframework.stereotype.Component;

/**
 *
 * @author abdulrahman
 */
@Component
public class VendingMachineAuditDaoStubImpl implements VendingMachineAuditDao {
    
    @Override
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException {
        // placeholder
    }
    
}

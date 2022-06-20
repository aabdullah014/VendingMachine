/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineAuditDao;
import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Snack;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abdulrahman
 */
public class VendingMachineServiceLayerImplTest {
    
    private VendingMachineServiceLayer testService;
    
    public VendingMachineServiceLayerImplTest() {
        
        VendingMachineDao dao = new VendingMachineDaoStubImpl();
        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoStubImpl();
        
        this.testService = new VendingMachineServiceLayerImpl(dao, auditDao);
        
    }

    @Test
    public void testCreateValidSnack() {
        // ARRANGE
        Snack testSnack = new Snack("KitKat");
        testSnack.setPrice(new BigDecimal("1.35"));
        testSnack.setInventory(2);
        
        // ACT
        try{
            
            testService.addSnack(testSnack);
            
        } catch ( VendingMachineDuplicateNameException 
                | VendingMachineDataValidationException 
                | VendingMachinePersistenceException e) {
            
            fail("Something went wrong.");
            
        }
        
    }
    
    @Test
    public void testDuplicateValidSnack() {
        
        // ARRANGE
        Snack testSnack = new Snack("Mars");
        testSnack.setPrice(new BigDecimal("1.15"));
        testSnack.setInventory(1);
        
        // ACT
        try {
            
            testService.addSnack(testSnack);
            fail("Something went wrong.");
            
        } catch (VendingMachineDataValidationException | VendingMachinePersistenceException e) {
            
            fail("Something went wrong. That's the wrong exception.");
            
        } catch (VendingMachineDuplicateNameException e) {
            
            return;
            
        }
        
    }
    
    @Test
    public void testCreateInvalidData() throws Exception {
        // ARRANGE
        Snack testSnack = new Snack("Mars");
        testSnack.setPrice(new BigDecimal("0"));
        testSnack.setInventory(1);
        
        //ACT
        try {
            
            testService.addSnack(testSnack);
            fail("Something went wrong. That should not have worked.");
            
        } catch ( VendingMachinePersistenceException e ) {
            
            fail("Something went wrong. That's the wrong exception.");
            
        } catch ( VendingMachineDuplicateNameException | VendingMachineDataValidationException e ) {
            
            return;
            
        }
        
    }
    
    @Test
    public void testGetAllSnacks() throws Exception {
        // ARRANGE
        Snack testSnack = new Snack("Mars");
        testSnack.setPrice(new BigDecimal("1.15"));
        testSnack.setInventory(1);
        
        // ACT & ASSERT
        assertEquals("Snack stored under 001 should be Mars.", 1, testService.getAllSnacks().size());
        assertTrue("The one student should be Mars", testService.getAllSnacks().contains(testSnack));
        
    }
    
    @Test
    public void testGetSnack() throws Exception {
        // ARRANGE
       Snack testSnack = new Snack("Mars");
        testSnack.setPrice(new BigDecimal("1.15"));
        testSnack.setInventory(1);
        
        // ACT & ASSERT
        Snack shouldBeMars = testService.getSnack("Mars", new BigDecimal("1.16"));
        assertNotNull("Getting Mars should not be null.", shouldBeMars);
        assertEquals("Snack stored under Mars should be Mars.", testSnack, shouldBeMars);
        
        Snack shouldBeNull = testService.getSnack("KitKat", new BigDecimal("1.12"));
        assertNull("Getting KitKat should be null", shouldBeNull);
        
    }
    
    @Test
    public void testRemove() throws Exception {
        // ARRANGE
        Snack testSnack = new Snack("Mars");
        testSnack.setPrice(new BigDecimal("1.15"));
        testSnack.setInventory(1);
        
        // ACT
        Snack shouldBeMars = testService.getSnack("Mars", new BigDecimal("1.15"));
        assertNotNull("Should be Ada", shouldBeMars);
        assertEquals("Should be a clone", shouldBeMars, testSnack);
        
        Snack shouldBeNull = testService.removeSnack("0042");
        assertNull("Removing 0042 should be null", shouldBeNull);
        
    }
    
}

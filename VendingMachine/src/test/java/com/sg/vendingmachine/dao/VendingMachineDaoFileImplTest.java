/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Snack;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abdulrahman
 */
public class VendingMachineDaoFileImplTest {
    
    VendingMachineDao testDao;
    
    public VendingMachineDaoFileImplTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        
        String testFile = "testVendingMachine.txt";
        
        new FileWriter(testFile);
        testDao = new VendingMachineDaoFileImpl(testFile);
        
    }

    @Test
    public void addSnack() throws Exception{
        
        // Create our method test inputs
        Snack snack = new Snack("Mars");
        snack.setPrice(new BigDecimal("1.15"));
        snack.setInventory(1);
        
        // Add Snack to the Dao
        testDao.addSnack(snack.getName(), snack);
        
        // Get the snack from the DAO
        Snack fetchDaoSnack = testDao.getSnack(snack.getName());
        
        // Check if the data is equal
        assertEquals("Checking Snack name", snack.getName(), fetchDaoSnack.getName());
        assertEquals("Checking Snack price", snack.getPrice(), fetchDaoSnack.getPrice());
        assertEquals("Checking Snack inventory", snack.getInventory(), fetchDaoSnack.getInventory());
        
    }
    
    @Test
    public void testAddGetAllSnacks() throws Exception {
        
        Snack snack1 = new Snack("Mars");
        snack1.setPrice(new BigDecimal("1.15"));
        snack1.setInventory(1);
        
        Snack snack2 = new Snack("KitKat");
        snack2.setPrice(new BigDecimal("1.35"));
        snack2.setInventory(2);
        
        testDao.addSnack(snack1.getName(), snack1);
        testDao.addSnack(snack2.getName(), snack2);
        
        List<Snack> allSnacks = testDao.getAllSnacks();
        
        assertNotNull("List must not be null", allSnacks);
        assertEquals("Should be two snacks in list.", 2, allSnacks.size());
        
        assertTrue("Should have Mars", allSnacks.contains(snack1));
        assertTrue("Should have KitKat", allSnacks.contains(snack2));
        
    }
    
    @Test
    public void testRemoveSnack() throws Exception {
        
        Snack snack1 = new Snack("Mars");
        snack1.setPrice(new BigDecimal("1.15"));
        snack1.setInventory(1);
        
        Snack snack2 = new Snack("KitKat");
        snack2.setPrice(new BigDecimal("1.35"));
        snack2.setInventory(2);
        
        testDao.addSnack(snack1.getName(), snack1);
        testDao.addSnack(snack2.getName(), snack2);
        
        Snack removedSnack = testDao.removeSnack(snack1.getName());
        
        assertEquals("Mars should be the one removed", snack1, removedSnack);
        
        List<Snack> allSnacks = testDao.getAllSnacks();
        
        assertNotNull("All snacks list should not be null", allSnacks);
        assertEquals("All snacks list should only have one snack", 1, allSnacks.size());
        
        assertFalse("Mars should not be in the list", allSnacks.contains(snack1));
        assertTrue("KitKat should be in the list", allSnacks.contains(snack2));
        
        removedSnack = testDao.removeSnack(snack2.getName());
        
        assertEquals("The removed snack should be KitKat", removedSnack, snack2);
        
        allSnacks = testDao.getAllSnacks();
        
        assertTrue("The retreived list of snacks should be empty.", allSnacks.isEmpty());
        
        Snack fetchDaoSnack = testDao.getSnack(snack1.getName());
        assertNull("Mars was removed, should be null", fetchDaoSnack);
        
        fetchDaoSnack = testDao.getSnack(snack2.getName());
        assertNull("KitKat was removed, should be null", fetchDaoSnack);
        
    }
    
}

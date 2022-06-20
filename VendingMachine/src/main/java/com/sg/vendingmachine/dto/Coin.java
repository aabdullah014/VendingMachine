/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sg.vendingmachine.dto;

import java.math.BigDecimal;

/**
 *
 * @author abdulrahman
 */
public enum Coin {
   
    PENNY(new BigDecimal("0.01")), NICKEL(new BigDecimal("0.05")), DIME(new BigDecimal("0.10")), QUARTER(new BigDecimal("0.25"));
    
    private BigDecimal cents;
    
    private Coin(BigDecimal cents) {
        this.cents = cents;
    }

    public static Coin getPENNY() {
        return PENNY;
    }

    public static Coin getNICKEL() {
        return NICKEL;
    }

    public static Coin getDIME() {
        return DIME;
    }

    public static Coin getQUARTER() {
        return QUARTER;
    }
    
    public BigDecimal getCents(){
        return this.cents;
    }

}

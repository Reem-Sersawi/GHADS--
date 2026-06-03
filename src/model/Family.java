/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author TOP
 */

public class Family {
    private int familyId;
    private String householdName;
    private String phone;
    private String location;
    private int familySize;
    private String nationalId;
    private String vulnerabilityLevel;
    private LocalDate registrationDate;
    private LocalDate lastAidDate;
    private String notes;
    
    public Family() {}
    
    // Getters and Setters
    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }
    
    public String getHouseholdName() { return householdName; }
    public void setHouseholdName(String householdName) { this.householdName = householdName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public int getFamilySize() { return familySize; }
    public void setFamilySize(int familySize) { this.familySize = familySize; }
    
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    
    public String getVulnerabilityLevel() { return vulnerabilityLevel; }
    public void setVulnerabilityLevel(String vulnerabilityLevel) { this.vulnerabilityLevel = vulnerabilityLevel; }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    
    public LocalDate getLastAidDate() { return lastAidDate; }
    public void setLastAidDate(LocalDate lastAidDate) { this.lastAidDate = lastAidDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

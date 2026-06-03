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


public class AidDistribution {
    private int distributionId;
    private int familyId;
    private int orgId;
    private int distributedBy;
    private LocalDate distributionDate;
    private String aidType;
    private int quantity;
    private String notes;
    
    public AidDistribution() {}
    
    // Getters and Setters
    public int getDistributionId() { return distributionId; }
    public void setDistributionId(int distributionId) { this.distributionId = distributionId; }
    
    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }
    
    public int getOrgId() { return orgId; }
    public void setOrgId(int orgId) { this.orgId = orgId; }
    
    public int getDistributedBy() { return distributedBy; }
    public void setDistributedBy(int distributedBy) { this.distributedBy = distributedBy; }
    
    public LocalDate getDistributionDate() { return distributionDate; }
    public void setDistributionDate(LocalDate distributionDate) { this.distributionDate = distributionDate; }
    
    public String getAidType() { return aidType; }
    public void setAidType(String aidType) { this.aidType = aidType; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

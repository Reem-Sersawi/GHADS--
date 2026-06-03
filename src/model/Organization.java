/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDateTime;

/**
 *
 * @author TOP
 */


public class Organization {
    private int orgId;
    private String name;
    private String type;
    private String contactInfo;
    private LocalDateTime createdAt;
    
    public Organization() {}
    
    public Organization(int orgId, String name, String type, String contactInfo) {
        this.orgId = orgId;
        this.name = name;
        this.type = type;
        this.contactInfo = contactInfo;
    }
    
    // Getters and Setters
    public int getOrgId() { return orgId; }
    public void setOrgId(int orgId) { this.orgId = orgId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return name;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.Organization;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TOP
 */


public class OrganizationDAO {
    
    public List<Organization> getAllOrganizations() {
        List<Organization> orgs = new ArrayList<>();
        String sql = "SELECT * FROM Organization ORDER BY name";
        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Organization org = new Organization();
                org.setOrgId(rs.getInt("org_id"));
                org.setName(rs.getString("name"));
                org.setType(rs.getString("type"));
                org.setContactInfo(rs.getString("contact_info"));
                orgs.add(org);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orgs;
    }
    
    public Organization getOrganizationById(int id) {
        String sql = "SELECT * FROM Organization WHERE org_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Organization org = new Organization();
                org.setOrgId(rs.getInt("org_id"));
                org.setName(rs.getString("name"));
                org.setType(rs.getString("type"));
                org.setContactInfo(rs.getString("contact_info"));
                return org;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addOrganization(Organization org) {
        String sql = "INSERT INTO Organization (name, type, contact_info) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, org.getName());
            stmt.setString(2, org.getType());
            stmt.setString(3, org.getContactInfo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateOrganization(Organization org) {
        String sql = "UPDATE Organization SET name=?, type=?, contact_info=? WHERE org_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, org.getName());
            stmt.setString(2, org.getType());
            stmt.setString(3, org.getContactInfo());
            stmt.setInt(4, org.getOrgId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteOrganization(int orgId) {
        // First check if organization has users
        String checkSql = "SELECT COUNT(*) FROM User WHERE org_id = ?";
        try (PreparedStatement checkStmt = DatabaseConnection.getInstance().getConnection().prepareStatement(checkSql)) {
            checkStmt.setInt(1, orgId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        String sql = "DELETE FROM Organization WHERE org_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, orgId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isNameUnique(String name, int excludeId) {
        String sql = "SELECT COUNT(*) FROM Organization WHERE name = ? AND org_id != ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int getOrganizationCount() {
        String sql = "SELECT COUNT(*) FROM Organization";
        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

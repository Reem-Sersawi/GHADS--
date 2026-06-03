/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.AidDistribution;
import model.Family;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author TOP
 */

public class AidDistributionDAO {
    
    private FamilyDAO familyDAO = new FamilyDAO();
    
    public boolean recordDistribution(AidDistribution distribution) {
        String sql = "INSERT INTO AidDistribution (family_id, org_id, distributed_by, distribution_date, aid_type, quantity, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, distribution.getFamilyId());
            stmt.setInt(2, distribution.getOrgId());
            stmt.setInt(3, distribution.getDistributedBy());
            stmt.setDate(4, Date.valueOf(distribution.getDistributionDate()));
            stmt.setString(5, distribution.getAidType());
            stmt.setInt(6, distribution.getQuantity());
            stmt.setString(7, distribution.getNotes());
            
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                familyDAO.updateLastAidDate(distribution.getFamilyId(), distribution.getDistributionDate());
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Map<String, Object> checkDuplicateAid(int familyId, String aidType, LocalDate distributionDate) {
        Map<String, Object> result = new HashMap<>();
        result.put("isDuplicate", false);
        
      
        Family family = familyDAO.getFamilyById(familyId);
        if (family == null) {
            result.put("isDuplicate", true);
            result.put("error", "Family not found");
            return result;
        }
        
        String vulnerability = family.getVulnerabilityLevel();

        if ("HIGH".equals(vulnerability)) {
            return result;
        }
        
        
        String sql = "SELECT ad.distribution_date, o.name as org_name, ad.aid_type " +
                     "FROM AidDistribution ad " +
                     "JOIN Organization o ON ad.org_id = o.org_id " +
                     "WHERE ad.family_id = ? AND ad.aid_type = ? AND ad.distribution_date >= DATE_SUB(?, INTERVAL 30 DAY)";
        
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, familyId);
            stmt.setString(2, aidType);
            stmt.setDate(3, Date.valueOf(distributionDate));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                result.put("isDuplicate", true);
                result.put("vulnerability", vulnerability);
                result.put("lastDate", rs.getDate("distribution_date").toLocalDate());
                result.put("orgName", rs.getString("org_name"));
                result.put("aidType", aidType);
                result.put("familyName", family.getHouseholdName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public List<AidDistribution> getAllDistributions() {
        List<AidDistribution> distributions = new ArrayList<>();
        String sql = "SELECT * FROM AidDistribution ORDER BY distribution_date DESC";
        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                distributions.add(extractDistributionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distributions;
    }
    
    public List<AidDistribution> getDistributionsByOrg(int orgId) {
        List<AidDistribution> distributions = new ArrayList<>();
        String sql = "SELECT * FROM AidDistribution WHERE org_id = ? ORDER BY distribution_date DESC";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, orgId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                distributions.add(extractDistributionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distributions;
    }
    
    public List<AidDistribution> getDistributionsByFamily(int familyId) {
        List<AidDistribution> distributions = new ArrayList<>();
        String sql = "SELECT * FROM AidDistribution WHERE family_id = ? ORDER BY distribution_date DESC";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, familyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                distributions.add(extractDistributionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distributions;
    }
    
    public int getTotalFamiliesServed() {
        String sql = "SELECT COUNT(DISTINCT family_id) FROM AidDistribution";
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
    
    public int getFamiliesServedByOrg(int orgId) {
        String sql = "SELECT COUNT(DISTINCT family_id) FROM AidDistribution WHERE org_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, orgId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getTotalDistributions() {
        String sql = "SELECT COUNT(*) FROM AidDistribution";
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
    
    private AidDistribution extractDistributionFromResultSet(ResultSet rs) throws SQLException {
        AidDistribution dist = new AidDistribution();
        dist.setDistributionId(rs.getInt("distribution_id"));
        dist.setFamilyId(rs.getInt("family_id"));
        dist.setOrgId(rs.getInt("org_id"));
        dist.setDistributedBy(rs.getInt("distributed_by"));
        dist.setDistributionDate(rs.getDate("distribution_date").toLocalDate());
        dist.setAidType(rs.getString("aid_type"));
        dist.setQuantity(rs.getInt("quantity"));
        dist.setNotes(rs.getString("notes"));
        return dist;
    }
}

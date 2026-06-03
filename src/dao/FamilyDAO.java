/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Family;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TOP
 */

public class FamilyDAO {
    
    public List<Family> getAllFamilies() {
        List<Family> families = new ArrayList<>();
        String sql = "SELECT * FROM Family ORDER BY vulnerability_level = 'HIGH' DESC, registration_date DESC";
        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                families.add(extractFamilyFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return families;
    }
    
    public List<Family> getFamiliesByVulnerability(String level) {
        List<Family> families = new ArrayList<>();
        String sql = "SELECT * FROM Family WHERE vulnerability_level = ? ORDER BY registration_date DESC";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                families.add(extractFamilyFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return families;
    }
    
    public List<Family> getUnderservedFamilies() {
        List<Family> families = new ArrayList<>();
        String sql = "SELECT f.* FROM Family f WHERE f.family_id NOT IN (SELECT DISTINCT family_id FROM AidDistribution) ORDER BY f.vulnerability_level = 'HIGH' DESC, f.registration_date DESC";
        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                families.add(extractFamilyFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return families;
    }
    
    public List<Family> getMostVulnerableFamilies() {
        return getFamiliesByVulnerability("HIGH");
    }
    
    public boolean addFamily(Family family) {
        String sql = "INSERT INTO Family (household_name, phone, location, family_size, national_id, vulnerability_level, registration_date, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, family.getHouseholdName());
            stmt.setString(2, family.getPhone());
            stmt.setString(3, family.getLocation());
            stmt.setInt(4, family.getFamilySize());
            stmt.setString(5, family.getNationalId());
            stmt.setString(6, family.getVulnerabilityLevel());
            stmt.setDate(7, Date.valueOf(family.getRegistrationDate()));
            stmt.setString(8, family.getNotes());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    family.setFamilyId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateFamily(Family family) {
        String sql = "UPDATE Family SET household_name=?, phone=?, location=?, family_size=?, national_id=?, vulnerability_level=?, notes=? WHERE family_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, family.getHouseholdName());
            stmt.setString(2, family.getPhone());
            stmt.setString(3, family.getLocation());
            stmt.setInt(4, family.getFamilySize());
            stmt.setString(5, family.getNationalId());
            stmt.setString(6, family.getVulnerabilityLevel());
            stmt.setString(7, family.getNotes());
            stmt.setInt(8, family.getFamilyId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteFamily(int familyId) {
        String sql = "DELETE FROM Family WHERE family_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, familyId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isNationalIdUnique(String nationalId, int excludeFamilyId) {
        String sql = "SELECT COUNT(*) FROM Family WHERE national_id = ? AND family_id != ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, nationalId);
            stmt.setInt(2, excludeFamilyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Family getFamilyByNationalId(String nationalId) {
        String sql = "SELECT * FROM Family WHERE national_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, nationalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractFamilyFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Family getFamilyById(int familyId) {
        String sql = "SELECT * FROM Family WHERE family_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, familyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractFamilyFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void updateLastAidDate(int familyId, LocalDate date) {
        String sql = "UPDATE Family SET last_aid_date = ? WHERE family_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setInt(2, familyId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getTotalFamiliesCount() {
        String sql = "SELECT COUNT(*) FROM Family";
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
    
    private Family extractFamilyFromResultSet(ResultSet rs) throws SQLException {
        Family family = new Family();
        family.setFamilyId(rs.getInt("family_id"));
        family.setHouseholdName(rs.getString("household_name"));
        family.setPhone(rs.getString("phone"));
        family.setLocation(rs.getString("location"));
        family.setFamilySize(rs.getInt("family_size"));
        family.setNationalId(rs.getString("national_id"));
        family.setVulnerabilityLevel(rs.getString("vulnerability_level"));
        family.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
        Date lastAidDate = rs.getDate("last_aid_date");
        family.setLastAidDate(lastAidDate != null ? lastAidDate.toLocalDate() : null);
        family.setNotes(rs.getString("notes"));
        return family;
    }
}

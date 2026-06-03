/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 *
 * @author TOP
 */


public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^05[0-9]{8}$|^[0-9]{10,15}$"
    );
    
    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile(
        "^[0-9]{9,15}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    public static boolean isNotEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidNationalId(String nationalId) {
        return nationalId != null && NATIONAL_ID_PATTERN.matcher(nationalId).matches();
    }
    
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    public static boolean isValidFamilySize(int size) {
        return size >= 1 && size <= 20;
    }
    
    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }
    
    public static boolean isValidVulnerabilityLevel(String level) {
        return level != null && (level.equals("HIGH") || level.equals("MEDIUM") || level.equals("LOW"));
    }
    
    public static boolean isValidAidType(String type) {
        String[] validTypes = {"food", "water", "medicine", "tent", "blankets", "clothes", "cash"};
        for (String t : validTypes) {
            if (t.equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getArabicAidType(String type) {
        switch (type) {
            case "food": return "طعام";
            case "water": return "ماء";
            case "medicine": return "دواء";
            case "tent": return "خيمة";
            case "blankets": return "بطانيات";
            case "clothes": return "ملابس";
            case "cash": return "نقدي";
            default: return type;
        }
    }
    
    public static String getEnglishAidType(String arabicType) {
        switch (arabicType) {
            case "طعام": return "food";
            case "ماء": return "water";
            case "دواء": return "medicine";
            case "خيمة": return "tent";
            case "بطانيات": return "blankets";
            case "ملابس": return "clothes";
            case "نقدي": return "cash";
            default: return arabicType;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import javafx.scene.Scene;
import javafx.scene.Parent;

/**
 *
 * @author TOP
 */


public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDarkMode = false;
    private Scene currentScene;
    
    private ThemeManager() {}
    
    public static ThemeManager getInstance() {
        if (instance == null) {
            synchronized (ThemeManager.class) {
                if (instance == null) {
                    instance = new ThemeManager();
                }
            }
        }
        return instance;
    }
    
    public void setScene(Scene scene) {
        this.currentScene = scene;
        applyTheme();
    }
    
    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
    }
    
    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
        applyTheme();
    }
    public void setLightMode() {
    this.isDarkMode = false;
    applyTheme();
}

    public void applyTheme() {
        if (currentScene != null) {
            String themePath = isDarkMode ? 
                getClass().getResource("/css/dark-theme.css").toExternalForm() :
                getClass().getResource("/css/light-theme.css").toExternalForm();
            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(themePath);
        }
    }
    
    public boolean isDarkMode() {
        return isDarkMode;
    }
    
}


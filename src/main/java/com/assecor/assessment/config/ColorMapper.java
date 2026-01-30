package com.assecor.assessment.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to map color IDs to their German color names.
 */
@Component
public class ColorMapper {
    
    private static final Map<Integer, String> COLOR_MAP = new HashMap<>();
    
    static {
        COLOR_MAP.put(1, "blau");
        COLOR_MAP.put(2, "grün");
        COLOR_MAP.put(3, "violett");
        COLOR_MAP.put(4, "rot");
        COLOR_MAP.put(5, "gelb");
        COLOR_MAP.put(6, "türkis");
        COLOR_MAP.put(7, "weiß");
    }
    
    /**
     * Get the color name for a given color ID.
     * 
     * @param colorId the color ID
     * @return the color name, or "unknown" if ID is invalid
     */
    public String getColorName(int colorId) {
        return COLOR_MAP.getOrDefault(colorId, "unknown");
    }
    
    /**
     * Get the color ID for a given color name.
     * 
     * @param colorName the color name
     * @return the color ID, or -1 if name is invalid
     */
    public int getColorId(String colorName) {
        return COLOR_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(colorName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }
}

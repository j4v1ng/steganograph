package com.javing.steganograph.util;

/**
 * Utility class for bit manipulation operations used in steganography.
 */
public class BitManipulationUtil {

    /**
     * Sets the least significant bit of a value.
     *
     * @param value The value to modify
     * @param setBit True to set the bit to 1, false to set it to 0
     * @return The modified value
     */
    public static int setLeastSignificantBit(int value, boolean setBit) {
        // Clear the least significant bit
        int result = (value >> 1) << 1;
        
        // Set the least significant bit if needed
        if (setBit) {
            result |= 1;
        }
        
        return result;
    }
    
    /**
     * Checks if the least significant bit of a value is set.
     *
     * @param value The value to check
     * @return True if the least significant bit is 1, false otherwise
     */
    public static boolean isLeastSignificantBitSet(int value) {
        return (value & 1) == 1;
    }
    
    /**
     * Shifts a value left by one bit and optionally adds a bit at the end.
     *
     * @param value The value to shift
     * @param addBit True to add a 1 bit, false to add a 0 bit
     * @return The shifted value
     */
    public static int shiftLeftAndAdd(int value, boolean addBit) {
        int result = value << 1;
        if (addBit) {
            result |= 1;
        }
        return result;
    }
}
/**
 * This a simple Enum simple Enum named LightValue, which lists the phases a particular stoplight lane may be in. 
 * These states should include GREEN, RED, and LEFT_SIGNAL 
*/

public enum LightValue {
    GREEN, //indicates that the right and middle lanes may proceed, but the left lane cannot (for both directions).
    RED, //indicates that no lane may proceed (for both directions).
    LEFT_SIGNAL; //indicates that left can proceed, but the right and middle lanes cannot (for both directions).
}

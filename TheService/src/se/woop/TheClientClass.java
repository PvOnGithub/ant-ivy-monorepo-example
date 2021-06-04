package se.woop;

public class TheClientClass {

    private TheClientClass() {
        throw new IllegalStateException("TheClientClass");
    }
    
    public static boolean isThisTrue(boolean b) {
        boolean a = !!b;
        return a;
    }

}

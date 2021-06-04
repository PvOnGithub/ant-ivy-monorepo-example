package se.woop;

public class TheSchemaClass {
    
    private TheSchemaClass() {
        throw new IllegalStateException("TheClientClass");
    }
    
    public static void doSomething() {
        System.out.println("I am doing something!!");
    }

}

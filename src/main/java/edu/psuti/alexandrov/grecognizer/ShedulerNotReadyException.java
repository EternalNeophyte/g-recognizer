package edu.psuti.alexandrov.grecognizer;

public class ShedulerNotReadyException extends RuntimeException {

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public String toString() {
        return "ShedulerNotReadyException: 'Task or service still not defined'";
    }
}

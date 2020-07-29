package plexsupply.testtask.exceptions;

public class FtpExeption extends RuntimeException {

    public FtpExeption(String message, Throwable e) {
        super(message, e);
    }

    public FtpExeption(String message) {
        super(message);
    }
}

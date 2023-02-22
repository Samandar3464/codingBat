package uz.pdp.spring_boot_security_web.exception;

public class RecordNotFountException extends RuntimeException{
    public RecordNotFountException(String name){
        super(name);
    }
}

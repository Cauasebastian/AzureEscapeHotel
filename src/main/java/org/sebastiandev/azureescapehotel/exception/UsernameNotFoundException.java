package org.sebastiandev.azureescapehotel.exception;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String message) {
        super(message);
    }
}

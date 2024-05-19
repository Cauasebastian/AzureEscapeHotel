package org.sebastiandev.azureescapehotel.exception;

import java.sql.SQLException;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String Message, SQLException e) {
        super(Message, e);
    }
}

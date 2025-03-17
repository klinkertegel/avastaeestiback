package ee.avastaeesti.gameback.validation;

import ee.avastaeesti.gameback.infrastructure.exception.DataNotFoundException;

import static ee.avastaeesti.gameback.infrastructure.Error.FOREIGN_KEY_NOT_FOUND;
import static ee.avastaeesti.gameback.infrastructure.Error.PRIMARY_KEY_NOT_FOUND;

public class ValidationService {

    public static DataNotFoundException throwPrimaryKeyNotFoundException(String primaryKeyName, Integer value) {
        return new DataNotFoundException(PRIMARY_KEY_NOT_FOUND.getMessage() + primaryKeyName + " = " + value, PRIMARY_KEY_NOT_FOUND.getErrorCode());
    }

    public static DataNotFoundException throwForeignKeyNotFoundException(String fieldName, Integer value) {
        return new DataNotFoundException(FOREIGN_KEY_NOT_FOUND.getMessage() + fieldName + " = " + value, FOREIGN_KEY_NOT_FOUND.getErrorCode());
    }
}

package co.quind.peajes.tollcollection.domain.exception;

import co.quind.peajes.tollcollection.domain.valueobject.TagId;

/** Se lanza cuando el tag RFID está en estado INACTIVE o SUSPENDED. */
public class TagInactiveException extends RuntimeException {
    public TagInactiveException(TagId tagId) {
        super("Tag inactivo o suspendido: " + tagId.toMasked());
    }
}

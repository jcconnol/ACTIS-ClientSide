package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum RelayFieldName implements FieldNameInterface {

    RELAY_ONE("relay1"),
    RELAY_TWO("relay2");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    RelayFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

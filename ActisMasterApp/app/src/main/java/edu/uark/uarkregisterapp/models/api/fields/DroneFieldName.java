package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum DroneFieldName implements FieldNameInterface {
    DRONE_ONE("droneOne"),
    DRONE_PIC("dronePic");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    DroneFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

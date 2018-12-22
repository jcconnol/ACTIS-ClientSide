package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum CommandListingFieldName implements FieldNameInterface {
	COMMANDS("commands");

	private String fieldName;
	public String getFieldName() {
		return this.fieldName;
	}

	CommandListingFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}

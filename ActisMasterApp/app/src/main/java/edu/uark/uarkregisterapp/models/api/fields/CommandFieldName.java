package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum CommandFieldName implements FieldNameInterface {

	COMMAND_NAME("commandName"),
	STATUS("status");

	private String fieldName;
	public String getFieldName() {
		return this.fieldName;
	}

	CommandFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}

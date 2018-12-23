package edu.uark.uarkregisterapp.models.api;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uark.uarkregisterapp.models.api.fields.CommandFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
import edu.uark.uarkregisterapp.models.transition.CommandTransition;

public class Command implements ConvertToJsonInterface, LoadFromJsonInterface<Command> {
	private String name;
	public String getName() {
		return this.name;
	}
	public Command setName(String name) {
		this.name = name;
		return this;
	}

	private boolean status;
	public boolean getStatus() {
		return this.status;
	}
	public Command setStatus(boolean status) {
		this.status = status;
		return this;
	}

	private String extra;
	public String getExtra() {
		return this.extra;
	}
	public Command setExtra(String extra) {
		this.extra = extra;
		return this;
	}

	@Override
	public Command loadFromJson(JSONObject rawJsonObject) {
		this.name = rawJsonObject.optString(CommandFieldName.COMMAND_NAME.getFieldName());
		this.status = rawJsonObject.optBoolean(CommandFieldName.STATUS.getFieldName());
		this.extra = rawJsonObject.optString(CommandFieldName.EXTRA.getFieldName());
		return this;
	}

	@Override
	public JSONObject convertToJson() {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(CommandFieldName.COMMAND_NAME.getFieldName(), this.name);
			jsonObject.put(CommandFieldName.STATUS.getFieldName(), this.status);
			jsonObject.put(CommandFieldName.EXTRA.getFieldName(), this.extra);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	public Command() {
		this.name = "";
		this.status = false;
	}

	public Command(CommandTransition commandTransition) {
		this.name = commandTransition.getCommandName();
		this.status = commandTransition.getCommandStatus();
		this.extra = commandTransition.getCommandExtra();
	}
}

package edu.uark.uarkregisterapp.models.api.services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Command;
import edu.uark.uarkregisterapp.models.api.enums.ApiObject;
import edu.uark.uarkregisterapp.models.api.enums.CommandApiMethod;
import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public class CommandService extends BaseRemoteService {
	public ApiResponse<Command> getCommand(String commandName) {
		return this.readCommandDetailsFromResponse(
			this.<Command>performGetRequest(
				this.buildPath(commandName)
			)
		);
	}

	public ApiResponse<Command> getCommandByName(String commandName) {
		return this.readCommandDetailsFromResponse(
			this.<Command>performGetRequest(
				this.buildPath(
					(new PathElementInterface[] { CommandApiMethod.BY_COMMAND })
					, commandName
				)
			)
		);
	}

	public ApiResponse<List<Command>> getCommands() {
		ApiResponse<List<Command>> apiResponse = this.performGetRequest(
			this.buildPath()
		);

		JSONArray rawJsonArray = this.rawResponseToJSONArray(apiResponse.getRawResponse());
		if (rawJsonArray != null) {
			ArrayList<Command> commands = new ArrayList<>(rawJsonArray.length());
			for (int i = 0; i < rawJsonArray.length(); i++) {
				try {
					commands.add((new Command()).loadFromJson(rawJsonArray.getJSONObject(i)));
				} catch (JSONException e) {
					Log.d("GET COMMANDS", e.getMessage());
				}
			}

			apiResponse.setData(commands);
		} else {
			apiResponse.setData(new ArrayList<Command>(0));
		}

		return apiResponse;
	}

	public ApiResponse<Command> updateCommand(Command command) {
		return this.readCommandDetailsFromResponse(
			this.<Command>performPutRequest(
				this.buildPath(command.getName())
				, command.convertToJson()
			)
		);
	}

	private ApiResponse<Command> readCommandDetailsFromResponse(ApiResponse<Command> apiResponse) {
		JSONObject rawJsonObject = this.rawResponseToJSONObject(
			apiResponse.getRawResponse()
		);

		if (rawJsonObject != null) {
			apiResponse.setData(
				(new Command()).loadFromJson(rawJsonObject)
			);
		}

		return apiResponse;
	}

	public CommandService() { super(ApiObject.COMMAND); }
}

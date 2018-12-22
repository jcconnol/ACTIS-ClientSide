package edu.uark.uarkregisterapp.models.transition;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
import edu.uark.uarkregisterapp.models.api.Command;

public class CommandTransition implements Parcelable {
	private String name;
	public String getCommandName() {
		return this.name;
	}
	public CommandTransition setCommandName(String name) {
		this.name = name;
		return this;
	}

	private boolean status;
	public boolean getStatus() {
		return this.status;
	}
	public CommandTransition setStatus(boolean status) {
		this.status = status;
		return this;
	}

	@Override
	public void writeToParcel(Parcel destination, int flags) {
		destination.writeString(this.name);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<CommandTransition> CREATOR = new Parcelable.Creator<CommandTransition>() {
		public CommandTransition createFromParcel(Parcel commandTransitionParcel) {
			return new CommandTransition(commandTransitionParcel);
		}

		public CommandTransition[] newArray(int size) {
			return new CommandTransition[size];
		}
	};

	public CommandTransition() {
		this.name = StringUtils.EMPTY;
	}

	public CommandTransition(Command command) {
		this.name = command.getName();
	}

	private CommandTransition(Parcel commandTransitionParcel) {
		this.name = commandTransitionParcel.readString();
		//this.status = commandTransitionParcel.readBooleanArray();
	}
}

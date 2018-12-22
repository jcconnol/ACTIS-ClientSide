package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.Command;

public class CommandListAdapter extends ArrayAdapter<Command> {
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			view = inflater.inflate(R.layout.content_drone_listing, parent, false);
		}
/*
		Command command = this.getItem(position);
		if (command != null) {
			TextView lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_command_name);
			if (lookupCodeTextView != null) {
				lookupCodeTextView.setText(command.getName());
			}
		}
*/
		return view;
	}

	public CommandListAdapter(Context context, List<Command> commands) {
		super(context, R.layout.content_drone_listing, commands);
	}
}

package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.uark.uarkregisterapp.adapters.CommandListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Command;
import edu.uark.uarkregisterapp.models.api.fields.DroneFieldName;
import edu.uark.uarkregisterapp.models.api.services.CommandService;
import edu.uark.uarkregisterapp.models.transition.CommandTransition;

public class DroneListing extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drone_listing);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		ActionBar actionBar = this.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private float x1, x2;

	public boolean onTouchEvent(MotionEvent touchEvent){
		switch (touchEvent.getAction()){
			case MotionEvent.ACTION_DOWN:
				x1 = touchEvent.getX();
				break;
			case MotionEvent.ACTION_UP:
				x2 = touchEvent.getX();
				if(x1 > x2){
					startActivity((new Intent(DroneListing.this, LandingActivity.class)));
				}
				break;
		}
		return false;
	}

	public void droneUpdateButtonOnClick(View view){
		(new RetrieveDroneTask()).execute();
	}

	public void droneSaveButtonOnClick(View view) {
		(new SaveCommandTask()).execute();
	}

	private class SaveCommandTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			this.savingCommandAlert.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Command command = (new Command()).
					setName(DroneFieldName.DRONE_ONE.getFieldName());

			ApiResponse<Command> apiResponse = (
					(new CommandService()).updateCommand(command)
			);

			if (apiResponse.isValidResponse()) {
				commandTransition.setCommandName(apiResponse.getData().getName());
			}

			return apiResponse.isValidResponse();
		}

		@Override
		protected void onPostExecute(Boolean successfulSave) {
			String message;

			savingCommandAlert.dismiss();

			if (successfulSave) {
				message = getString(R.string.alert_dialog_command_save_success);
			} else {
				message = getString(R.string.alert_dialog_command_save_failure);
			}

			new AlertDialog.Builder(DroneListing.this).
					setMessage(message).
					setPositiveButton(
							R.string.button_dismiss,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();
								}
							}
					).
					create().
					show();
		}

		private AlertDialog savingCommandAlert;

		private SaveCommandTask() {
			this.savingCommandAlert = new AlertDialog.Builder(DroneListing.this).
					setMessage(R.string.alert_dialog_command_save).
					create();
		}
	}

	private CommandTransition commandTransition;

	private class RetrieveDroneTask extends AsyncTask<Void, Void, ApiResponse<Command>> {
		@Override
		protected void onPreExecute() {
			this.loadingCommandAlert.show();
		}

		@Override
		protected ApiResponse<Command> doInBackground(Void... params) {
			ApiResponse<Command> apiResponse = (new CommandService()).getCommandByName(DroneFieldName.DRONE_ONE.getFieldName());

			if (apiResponse.isValidResponse()) {
				command = new Command();
				command = apiResponse.getData();
			}

			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse<Command> apiResponse) {
			if (apiResponse.isValidResponse()) {
				ImageView image =(ImageView)findViewById(R.id.image_view_drone_one);
				byte[]imageBytes = Base64.decode(command.getExtra(), Base64.DEFAULT);
				Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
				image.setImageBitmap(decodedImage);
			}

			this.loadingCommandAlert.dismiss();

			if (!apiResponse.isValidResponse()) {
				new AlertDialog.Builder(DroneListing.this).
						setMessage(R.string.alert_dialog_commands_load_failure).
						setPositiveButton(
								R.string.button_dismiss,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
									}
								}
						).
						create().
						show();
			}
		}

		private AlertDialog loadingCommandAlert;

		private RetrieveDroneTask() {
			this.loadingCommandAlert = new AlertDialog.Builder(DroneListing.this).
					setMessage(R.string.alert_dialog_command_loading).
					create();
		}
	}

	private Command command;
}

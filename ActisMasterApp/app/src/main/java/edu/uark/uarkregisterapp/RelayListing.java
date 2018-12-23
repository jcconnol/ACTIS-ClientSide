package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Command;
import edu.uark.uarkregisterapp.models.api.fields.CommandFieldName;
import edu.uark.uarkregisterapp.models.api.fields.DroneFieldName;
import edu.uark.uarkregisterapp.models.api.fields.RelayFieldName;
import edu.uark.uarkregisterapp.models.api.services.CommandService;
import edu.uark.uarkregisterapp.models.transition.CommandTransition;

public class RelayListing extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relay_listing);
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
					startActivity((new Intent(RelayListing.this, DroneListing.class)));
				}
				break;
		}
		return false;
	}

	public boolean getRelayOneButtonState() {
        ToggleButton syncSwitch = (ToggleButton)findViewById(R.id.toggle_relay_one);
        return syncSwitch.isChecked();
    }

	public boolean getRelayTwoButtonState() {
        ToggleButton syncSwitch = (ToggleButton)findViewById(R.id.toggle_relay_two);
        return syncSwitch.isChecked();
	}

	public void saveButtonOnClick(View view){
        (new SaveCommandTask()).execute();
    }

	private class SaveCommandTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			this.savingCommandAlert.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Command commandOne = (new Command())
                    .setName(RelayFieldName.RELAY_ONE.getFieldName())
					.setStatus(getRelayOneButtonState());

            Command commandTwo = (new Command())
                    .setName(RelayFieldName.RELAY_TWO.getFieldName())
                    .setStatus(getRelayTwoButtonState());

			ApiResponse<Command> apiResponseOne = (
					(new CommandService()).updateCommand(commandOne)
			);

            ApiResponse<Command> apiResponseTwo = (
                    (new CommandService()).updateCommand(commandTwo)
            );

			if (apiResponseOne.isValidResponse() && apiResponseTwo.isValidResponse()) {
				commandTransition.setCommandName(apiResponseOne.getData().getName());
                return true;
			}

			return false;
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

			new AlertDialog.Builder(RelayListing.this).
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
			this.savingCommandAlert = new AlertDialog.Builder(RelayListing.this).
					setMessage(R.string.alert_dialog_command_save).
					create();
		}
	}

    private boolean relayOneStatus;
	private boolean relayTwoStatus;
	private CommandTransition commandTransition;
}

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

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Command;
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

	public void relayOneSaveButtonOnClick(View view) {
		buttonPressName = RelayFieldName.RELAY_ONE.getFieldName();
		(new SaveCommandTask()).execute();
	}

	public void relayTwoSaveButtonOnClick(View view) {
		buttonPressName = RelayFieldName.RELAY_TWO.getFieldName();
		(new SaveCommandTask()).execute();
	}

	public void saveButtonOnClick(View view) {
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
					setName(buttonPressName);

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
				message = getString(R.string.alert_dialog_product_save_success);
			} else {
				message = getString(R.string.alert_dialog_product_save_failure);
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
					setMessage(R.string.alert_dialog_product_save).
					create();
		}
	}

	private String buttonPressName;
	private CommandTransition commandTransition;
}

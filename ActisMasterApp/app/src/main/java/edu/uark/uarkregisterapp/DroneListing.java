package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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

	public void DroneSaveButtonOnClick(View view) {
		buttonPressName = DroneFieldName.DRONE_ONE.getFieldName();
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
					setMessage(R.string.alert_dialog_product_save).
					create();
		}
	}

	private String buttonPressName;
	private CommandTransition commandTransition;

	private class RetrieveProductsTask extends AsyncTask<Void, Void, ApiResponse<List<Command>>> {
		@Override
		protected void onPreExecute() {
			this.loadingProductsAlert.show();
		}

		@Override
		protected ApiResponse<List<Command>> doInBackground(Void... params) {
			ApiResponse<List<Product>> apiResponse = (new ProductService()).getProducts();

			if (apiResponse.isValidResponse()) {
				products.clear();
				products.addAll(apiResponse.getData());
			}

			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse<List<Product>> apiResponse) {
			if (apiResponse.isValidResponse()) {
				productListAdapter.notifyDataSetChanged();
			}

			this.loadingProductsAlert.dismiss();

			if (!apiResponse.isValidResponse()) {
				new AlertDialog.Builder(ProductsListingActivity.this).
						setMessage(R.string.alert_dialog_products_load_failure).
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

		private AlertDialog loadingProductsAlert;

		private RetrieveProductsTask() {
			this.loadingProductsAlert = new AlertDialog.Builder(ProductsListingActivity.this).
					setMessage(R.string.alert_dialog_products_loading).
					create();
		}
	}

	private List<Product> products;
	private ProductListAdapter productListAdapter;
}

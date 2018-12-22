package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class LandingActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);
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
					startActivity((new Intent(LandingActivity.this, RelayListing.class)));
				}
				break;
		}
		return false;
	}
}

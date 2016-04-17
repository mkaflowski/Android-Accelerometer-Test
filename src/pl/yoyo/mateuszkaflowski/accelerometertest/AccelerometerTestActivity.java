package pl.yoyo.mateuszkaflowski.accelerometertest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AccelerometerTestActivity extends Activity implements
		SensorEventListener {
	/** Called when the activity is first created. */
	TextView x, y, z, dx, dy, dz, tchanges;
	Float x1, y1, z1;
	SensorManager sm;
	Float changeToNote = (float) 10;
	String changesNoted;
	int counter = 1;
	Sensor s;
	Boolean positive = true, negative = true, noteX = true, noteY = true,
			noteZ = true;
	int sensorDelay = 3;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			Log.i("onKeyDown", Integer.toString(sensorDelay));
			sensorDelay--;
			if (sensorDelay < 0)
				sensorDelay = 0;
			sm.unregisterListener(this);
			sm.registerListener(this, s, sensorDelay);
			showDelay();
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			Log.i("onKeyDown", Integer.toString(sensorDelay));
			sensorDelay++;
			if (sensorDelay > 3)
				sensorDelay = 3;
			sm.unregisterListener(this);
			sm.registerListener(this, s, sensorDelay);
			showDelay();
			return true;
		case KeyEvent.KEYCODE_BACK:
			finish();
			return true;
		default:
			return false;
		}
	}

	private void showDelay() {
		Toast toast;
		switch (sensorDelay) {
		case 3:
			toast = Toast.makeText(this, "SENSOR_DELAY_NORMAL", 1000);
			toast.show();
			break;
		case 2:
			toast = Toast.makeText(this, "SENSOR_DELAY_UI", 1000);
			toast.show();
			break;
		case 1:
			toast = Toast.makeText(this, "SENSOR_DELAY_GAME", 1000);
			toast.show();
			break;
		case 0:
			toast = Toast.makeText(this, "SENSOR_DELAY_FASTEST", 1000);
			toast.show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		x1 = y1 = z1 = (float) 0;

		x = (TextView) findViewById(R.id.tx);
		y = (TextView) findViewById(R.id.ty);
		z = (TextView) findViewById(R.id.tz);
		dx = (TextView) findViewById(R.id.tdx);
		dy = (TextView) findViewById(R.id.tdy);
		dz = (TextView) findViewById(R.id.tdz);
		tchanges = (TextView) findViewById(R.id.changes);

		changesNoted = "Changes from - " + Float.toString(changeToNote) + ":\n";
		tchanges.setText(changesNoted);
		tchanges.setTextColor(Color.WHITE);

		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sm.registerListener(this, s, sensorDelay);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		positive = sp.getBoolean("first", true);
		negative = sp.getBoolean("second", true);
		changeToNote = Float.parseFloat(sp.getString("third", "10"));
		noteX = sp.getBoolean("x", true);
		noteY = sp.getBoolean("y", true);
		noteZ = sp.getBoolean("z", true);

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		x.setText(Float.toString(event.values[0]));
		y.setText(Float.toString(event.values[1]));
		z.setText(Float.toString(event.values[2]));

		if (positive && negative) {
			if (noteX)
				if (x1 - event.values[0] > changeToNote
						|| -(x1 - event.values[0]) > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\tx\t\t"
							+ Float.toString(x1 - event.values[0]) + "\n";
					counter++;
					tchanges.setTextColor(Color.RED);
				}
			if (noteY)
				if (y1 - event.values[1] > changeToNote
						|| -(y1 - event.values[1]) > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\ty\t\t"
							+ Float.toString(y1 - event.values[1]) + "\n";
					counter++;
					tchanges.setTextColor(Color.CYAN);
				}
			if (noteZ)
				if (z1 - event.values[2] > changeToNote
						|| -(z1 - event.values[2]) > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\tz\t\t"
							+ Float.toString(z1 - event.values[2]) + "\n";
					counter++;
					tchanges.setTextColor(Color.GREEN);
				}
		} else if (positive) {
			if (noteX)
				if (x1 - event.values[0] > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\tx\t\t"
							+ Float.toString(x1 - event.values[0]) + "\n";
					counter++;
					tchanges.setTextColor(Color.RED);
				}
			if (noteY)
				if (y1 - event.values[1] > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\ty\t\t"
							+ Float.toString(y1 - event.values[1]) + "\n";
					counter++;
					tchanges.setTextColor(Color.CYAN);
				}
			if (noteZ)
				if (z1 - event.values[2] > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\tz\t\t"
							+ Float.toString(z1 - event.values[2]) + "\n";
					counter++;
					tchanges.setTextColor(Color.GREEN);
				}
		} else if (negative) {
			if (noteX)
				if (-(x1 - event.values[0]) > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\tx\t\t"
							+ Float.toString(x1 - event.values[0]) + "\n";
					counter++;
					tchanges.setTextColor(Color.RED);
				}
			if (noteY)
				if (-(y1 - event.values[1]) > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\ty\t\t"
							+ Float.toString(y1 - event.values[1]) + "\n";
					counter++;
					tchanges.setTextColor(Color.CYAN);
				}
			if (noteZ)
				if (-(z1 - event.values[2]) > changeToNote) {
					changesNoted += Integer.toString(counter) + ".\t\tz\t\t"
							+ Float.toString(z1 - event.values[2]) + "\n";
					counter++;
					tchanges.setTextColor(Color.GREEN);
				}
		}

		dx.setText(Float.toString(x1 - event.values[0]));
		x1 = event.values[0];
		dy.setText(Float.toString(y1 - event.values[1]));
		y1 = event.values[1];
		dz.setText(Float.toString(z1 - event.values[2]));
		z1 = event.values[2];

		tchanges.setText(changesNoted);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mymenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			changesNoted = "Changes from - " + Float.toString(changeToNote)
					+ ":\n";
			tchanges.setText(changesNoted);
			tchanges.setTextColor(Color.WHITE);
			counter = 0;
			break;

		case R.id.item2:
			changesNoted = "Changes from - " + Float.toString(changeToNote)
					+ ":\n";
			tchanges.setText(changesNoted);
			tchanges.setTextColor(Color.WHITE);
			counter = 0;
			Intent intent = new Intent(AccelerometerTestActivity.this,
					Prefs.class);
			startActivity(intent);

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		sm.unregisterListener(this);
		super.onDestroy();
	}
}
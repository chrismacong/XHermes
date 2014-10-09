package com.xhermes.android.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhermes.android.R;
import com.xhermes.android.dao.OBDDataDao;
import com.xhermes.android.dao.OBDParametersDao;
import com.xhermes.android.model.OBDData;
import com.xhermes.android.model.OBDParameters;
import com.xhermes.android.network.DataReceiver;

public class VehicleInfoFragment extends Fragment{
	private static final int ANIMATION_TIME = 600;

	private static final float VOLTAGE_MIN_VALUE = 9;
	private static final float VOLTAGE_MAX_VALUE = 16;
	private static final float VOLTAGE_MAX_ANGLE = 129.0f;
	private static final float DEGREE_PER_VOLTAGE =
			VOLTAGE_MAX_ANGLE / (VOLTAGE_MAX_VALUE - VOLTAGE_MIN_VALUE);
	private static final float ROTATE_SPEED_MIN_VALUE = 0;
	private static final float ROTATE_SPEED_MAX_VALUE = 10000;
	private static final float ROTATE_SPEED_MAX_ANGLE = 252f;
	private static final float DEGREE_PER_ROTATE_SPEED =
			ROTATE_SPEED_MAX_ANGLE / (ROTATE_SPEED_MAX_VALUE - ROTATE_SPEED_MIN_VALUE);
	private static final float TEMPERATURE_MIN_VALUE = 0;
	private static final float TEMPERATURE_MAX_VALUE = 180;
	private static final float TEMPERATURE_MAX_ANGLE = 126f;
	private static final float DEGREE_PER_TEMPERATURE =
			TEMPERATURE_MAX_ANGLE / (TEMPERATURE_MAX_VALUE - TEMPERATURE_MIN_VALUE);
	private static final float PRESSURE_MIN_VALUE = 0f;
	private static final float PRESSURE_MAX_VALUE = 300f;
	private static final float PRESSURE_MAX_ANGLE = 305f;
	private static final float DEGREE_PER_PRESSURE =
			PRESSURE_MAX_ANGLE / (PRESSURE_MAX_VALUE - PRESSURE_MIN_VALUE);
	private static final float SPEED_MIN_VALUE = 0;
	private static final float SPEED_MAX_VALUE = 180;
	private static final float SPEED_MAX_ANGLE = 246.33f;
	private static final float DEGREE_PER_SPEED =
			SPEED_MAX_ANGLE / (SPEED_MAX_VALUE - SPEED_MIN_VALUE);


	private String tid;
	private OBDDataDao oDao;
	private OBDParametersDao opDao;
	private Context act;
	private Handler handler;
	private ImageView mVoltageDialPointer;
	private ImageView mRotateSpeedDialPointer;
	private ImageView mTemperatureDialPointer;
	private ImageView mPressureDialPointer;
	private ImageView mSpeedDialPointer;
	private TextView mVoltageText;
	private TextView mRotateSpeedText;
	private TextView mTemperatureText;
	private TextView mPressureText;
	private TextView mTotalDistanceText;
	private TextView mAverageFuelConsumptionText;
	private TextView mTotalFuelConsumptionText;
	private float mVoltageAngle;
	private float mRotateSpeedAngle;
	private float mTemperatureAngle;
	private float mPressureAngle;
	private float mSpeedAngle;
	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				refreshOBDP();
				break;
			case 1:
				refreshOBD();
				break;
			}
		}
	}
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Bundle bundle=getArguments();
		tid=bundle.getString("terminalId");
		act=getActivity();
		Log.d("tid",tid);
		oDao =new OBDDataDao(act);
		opDao =new OBDParametersDao(act);
		handler=new MyHandler();
		DataReceiver.setObdHandler(handler);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootview = inflater.inflate(R.layout.vehicleinfo_view, container, false);
		mVoltageDialPointer = (ImageView) rootview.findViewById(R.id.voltage_dial_pointer);
		mRotateSpeedDialPointer = (ImageView) rootview.findViewById(R.id.rotate_speed_dial_pointer);
		mTemperatureDialPointer = (ImageView) rootview.findViewById(R.id.temperature_dial_pointer);
		mPressureDialPointer = (ImageView) rootview.findViewById(R.id.pressure_dial_pointer);
		mSpeedDialPointer = (ImageView) rootview.findViewById(R.id.speed_dial_pointer);
		mVoltageText = (TextView) rootview.findViewById(R.id.voltage);
		mRotateSpeedText = (TextView) rootview.findViewById(R.id.rotate_speed);
		mTemperatureText = (TextView) rootview.findViewById(R.id.temperature);
		mPressureText = (TextView) rootview.findViewById(R.id.pressure);
		mTotalDistanceText = (TextView) rootview.findViewById(R.id.total_distance);
		mAverageFuelConsumptionText = (TextView) rootview.findViewById(R.id.average_fuel_consumption);
		mTotalFuelConsumptionText = (TextView) rootview.findViewById(R.id.total_fuel_consumption);
		set();
		return rootview;
	}
	public void set(){
		UpdateOBDData();
		UpdateOBDPData();
	}
	public void refreshOBDP(){
		UpdateOBDPData();
	}
	public void refreshOBD(){
		UpdateOBDData();
	}
	public void UpdateOBDData(){
		ArrayList<OBDData> dataList = oDao.query(tid, "time desc", "1");
		OBDData latestData;
		System.out.println(dataList.size());
		if(dataList.size()>0){
			latestData = dataList.get(0);
			updateVoltage(latestData.getOvoltage());
			updateRotateSpeed(latestData.getOrpm());
			updateTemperature(latestData.getOwaterTemp());
			updatePressure(latestData.getOpressure());
			updateSpeed(latestData.getOspeed());
		}
	}
	public void UpdateOBDPData(){
		ArrayList<OBDParameters> dataList = opDao.query(tid, "time desc", "1");
		OBDParameters latestData;
		System.out.println(dataList.size());
		if(dataList.size()>0){
			latestData = dataList.get(0);
			System.out.println(latestData);
			updateTotalDistance(latestData.getCurrent_miles());
			updateAverageFuelConsumption(latestData.getMaintenance_gap());
			updateTotalFuelConsumption(latestData.getMaintenance_next());
		}
	}
	private float rotatePointer(ImageView pointer, float pivotY, float currentValue,
			float fromDegree, float minValue, float maxValue,
			float degreePerValue) {
		if (currentValue > maxValue) {
			currentValue = maxValue;
		} else if (currentValue < minValue) {
			currentValue = minValue;
		}
		currentValue -= minValue;
		float toDegree = currentValue * degreePerValue;
		if (fromDegree != toDegree) {
			pointer.startAnimation(getRotateAnimation(fromDegree, toDegree, pivotY));
		}
		return toDegree;
	}
	private RotateAnimation getRotateAnimation(float fromDegree, float toDegree, float pivotY) {
		RotateAnimation rotateAnimation = new RotateAnimation(fromDegree, toDegree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, pivotY);
		rotateAnimation.setInterpolator(act, android.R.anim.accelerate_decelerate_interpolator);
		rotateAnimation.setDuration(ANIMATION_TIME);
		rotateAnimation.setFillEnabled(true);
		rotateAnimation.setFillAfter(true);
		return rotateAnimation;
	}
	private void rotateVoltagePointer(float voltageValue) {
		mVoltageAngle = rotatePointer(mVoltageDialPointer, 1.0f, voltageValue, mVoltageAngle,
				VOLTAGE_MIN_VALUE, VOLTAGE_MAX_VALUE, DEGREE_PER_VOLTAGE);
	}

	private void rotateRotateSpeedPointer(float rotateSpeedValue) {
		mRotateSpeedAngle = rotatePointer(mRotateSpeedDialPointer, 0.5f, rotateSpeedValue, mRotateSpeedAngle,
				ROTATE_SPEED_MIN_VALUE, ROTATE_SPEED_MAX_VALUE, DEGREE_PER_ROTATE_SPEED);
	}

	private void rotateTemperaturePointer(float temperatureValue) {
		mTemperatureAngle = rotatePointer(mTemperatureDialPointer, 1.0f, temperatureValue, mTemperatureAngle,
				TEMPERATURE_MIN_VALUE, TEMPERATURE_MAX_VALUE, DEGREE_PER_TEMPERATURE);
	}

	private void rotatePressurePointer(float pressureValue) {
		mPressureAngle = rotatePointer(mPressureDialPointer, 0.5f, pressureValue, mPressureAngle,
				PRESSURE_MIN_VALUE, PRESSURE_MAX_VALUE, DEGREE_PER_PRESSURE);
	}

	private void rotateSpeedPointer(float speedValue) {
		mSpeedAngle = rotatePointer(mSpeedDialPointer, 0.5f, speedValue, mSpeedAngle,
				SPEED_MIN_VALUE, SPEED_MAX_VALUE, DEGREE_PER_SPEED);
	}
	private void updateVoltage(String voltageString) {
		float voltage = Float.parseFloat(voltageString);
		rotateVoltagePointer(voltage);
		mVoltageText.setText(voltageString+" V");
	}

	private void updateRotateSpeed(String rotateSpeedString) {
		float rotateSpeed = Float.parseFloat(rotateSpeedString);
		rotateRotateSpeedPointer(rotateSpeed);
		mRotateSpeedText.setText(rotateSpeedString+" ×ª/·Ö");
	}

	private void updateTemperature(String temperatureString) {
		float temperature = Float.parseFloat(temperatureString);
		rotateTemperaturePointer(temperature);
		mTemperatureText.setText(temperatureString+" ¡ãC");
	}

	private void updatePressure(String pressureString) {
		float pressure = Float.parseFloat(pressureString);
		rotatePressurePointer(pressure);
		mPressureText.setText(pressureString+" Kpa");
	}

	private void updateSpeed(String speedString) {
		float speed = Float.parseFloat(speedString);
		rotateSpeedPointer(speed);
	}
	private void updateTotalDistance(String _totalDistance) {
		final String totalDistance = _totalDistance;
		if (totalDistance != null && totalDistance.length() > 0) {
			mTotalDistanceText.setText(totalDistance+"km");
		}
	}

	private void updateAverageFuelConsumption(String _averageFuelConsumption) {
		final String averageFuelConsumption = _averageFuelConsumption;
		if (averageFuelConsumption != null && averageFuelConsumption.length() > 0) {
			mAverageFuelConsumptionText.setText(averageFuelConsumption+"km");
		}
	}

	private void updateTotalFuelConsumption(String _totalFuelConsumption) {
		final String totalFuelConsumption = _totalFuelConsumption;        
		if (totalFuelConsumption != null && totalFuelConsumption.length() > 0) {
			mTotalFuelConsumptionText.setText(_totalFuelConsumption+"km");
		}
	}
}

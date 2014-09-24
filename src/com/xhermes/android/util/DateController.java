package com.xhermes.android.util;

import java.util.Calendar;

public class DateController {
	public static int year;
	public static int getYear() {
		return year;
	}
	public static void setYear(int year) {
		DateController.year = year;
	}
	public static int getMonthOfYear() {
		return monthOfYear;
	}
	public static void setMonthOfYear(int monthOfYear) {
		DateController.monthOfYear = monthOfYear;
	}
	public static int getDayOfMonth() {
		return dayOfMonth;
	}
	public static void setDayOfMonth(int dayOfMonth) {
		DateController.dayOfMonth = dayOfMonth;
	}
	public static int monthOfYear;
	public static int dayOfMonth;
	public static int start_hourOfDay;
	public static int getStart_hourOfDay() {
		return start_hourOfDay;
	}
	public static void setStart_hourOfDay(int start_hourOfDay) {
		DateController.start_hourOfDay = start_hourOfDay;
	}
	public static int getStart_minute() {
		return start_minute;
	}
	public static void setStart_minute(int start_minute) {
		DateController.start_minute = start_minute;
	}
	public static int getEnd_hourOfDay() {
		return end_hourOfDay;
	}
	public static void setEnd_hourOfDay(int end_hourOfDay) {
		DateController.end_hourOfDay = end_hourOfDay;
	}
	public static int getEnd_minute() {
		return end_minute;
	}
	public static void setEnd_minute(int end_minute) {
		DateController.end_minute = end_minute;
	}
	public static int start_minute;
	public static int end_hourOfDay;
	public static int end_minute;
	public static void init(){
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		monthOfYear = calendar.get(Calendar.MONTH);
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		start_hourOfDay = 0;
		start_minute = 0;
		end_hourOfDay = 23;
		end_minute = 59;
	}
	
}

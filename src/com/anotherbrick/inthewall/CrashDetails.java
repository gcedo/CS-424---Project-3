package com.anotherbrick.inthewall;

public class CrashDetails {
    private static final String UNKNOWN = "Not provided";
    private Integer id;
    private String weather;

    private String light;
    private String gender;
    private String vehicles;
    private String month;
    private String dayOfWeek;
    private String day;
    private String year;
    private String time;
    private String speedLimit;
    private String fatalities;
    private String avgAge;
    private String avgSpeed;
    private String maxSpeed;

    public CrashDetails(Integer id, String weather, String light,
	    String gender, String vehicles, String month, String dayOfWeek,
	    String day, String year, String time, String speedLimit,
	    String fatalities, String avgAge, String avgSpeed, String maxSpeed) {
	super();
	this.id = id;
	this.weather = weather;
	this.light = light;
	this.gender = gender;
	this.vehicles = vehicles;
	this.month = month;
	this.dayOfWeek = dayOfWeek;
	this.day = day;
	this.year = year;
	this.time = time;
	this.speedLimit = speedLimit;
	this.fatalities = fatalities;
	this.avgAge = avgAge;
	this.avgSpeed = avgSpeed;
	this.maxSpeed = maxSpeed;
    }

    public Integer getId() {
	return id;
    }

    public String getWeather() {

	return weather == null ? UNKNOWN : weather;
    }

    public String getLight() {
	return light == null ? UNKNOWN : light;
    }

    public String getGender() {
	return gender == null ? UNKNOWN : gender;
    }

    public String getVehicles() {
	return vehicles == null ? UNKNOWN : vehicles;
    }

    public String getDate() {
	return dayOfWeek + ", " + month + "-" + day + "-" + year;
    }

    public String getTime() {
	return time;
    }

    public String getSpeedLimit() {
	return speedLimit == null ? UNKNOWN : speedLimit;
    }

    public String getFatalities() {
	return fatalities == null ? UNKNOWN : weather;
    }

    public String getAvgAge() {
	return avgAge == null ? UNKNOWN : avgAge;
    }

    public String getAvgSpeed() {
	return avgSpeed == null ? UNKNOWN : avgSpeed;
    }

    public String getMaxSpeed() {
	return maxSpeed == null ? UNKNOWN : maxSpeed;
    }

    @Override
    public String toString() {
	return "weather : " + weather + "\n light=" + light + "\n gender : "
		+ gender + "\n vehicles : " + vehicles + "\n month : " + month
		+ "\n dayOfWeek : " + dayOfWeek + "\n day : " + day
		+ "\n year : " + year + "\n time : " + time
		+ "\n speedLimit : " + speedLimit + "\n fatalities : "
		+ fatalities + "\n avgAge : " + avgAge + "\n avgSpeed : "
		+ avgSpeed + "\n maxSpeed : " + maxSpeed;
    }

}

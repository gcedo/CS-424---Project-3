package application;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PVector;

import com.anotherbrick.inthewall.CrashDetails;
import com.anotherbrick.inthewall.LocationWrapper;
import com.modestmaps.geo.Location;
import com.mysql.jdbc.Connection;

public class DBUtil {
    private static DBUtil instance = null;
    private Connection con = null;
    private static final boolean DEBUG = true;

    public static DBUtil getInstance() {
	if (instance == null)
	    instance = new DBUtil();
	return instance;
    }

    private DBUtil() {
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = (Connection) DriverManager.getConnection(
		    "jdbc:mysql://178.63.199.31:3306/car-crash", "cs424-p3",
		    "idontknow");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public ArrayList<LocationWrapper> getPointsByLocation(Location loc) {
	ArrayList<LocationWrapper> ret = new ArrayList<LocationWrapper>();
	try {
	    Statement stm = con.createStatement();
	    ResultSet r = stm
		    .executeQuery("SELECT id, latitude, longitude FROM crashes WHERE GLength( LineString(( PointFromWKB( position)), ( PointFromWKB( POINT( "
			    + loc.lat
			    + ", "
			    + loc.lon
			    + " ) )))) < 0.5 GROUP BY crash_id");
	    while (r.next()) {
		ret.add(new LocationWrapper(new Integer(r.getInt(1)), r
			.getFloat(2), r.getFloat(3)));
	    }
	    return ret;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}

    }

    public CrashDetails getCrashDetailsById(Integer crashId) {
	try {
	    Statement stm = con.createStatement();
	    ResultSet r = stm
		    .executeQuery("SELECT id, weather, lightcondition, sex, vehicletype, month, dayofweek, daynum, year, time, speedlimit, numfatal, age, avgspeed, maxspeed FROM crashes WHERE id = "
			    + crashId + " LIMIT 1");
	    if (r.next()) {
		return new CrashDetails(r.getInt(1), r.getString(2),
			r.getString(3), r.getString(4), r.getString(5),
			r.getString(6), r.getString(7), r.getString(8),
			r.getString(9), r.getString(10), r.getString(11),
			r.getString(12), r.getString(13), r.getString(14),
			r.getString(15));
	    }
	    return null;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public ArrayList<LocationWrapper> getPointsByState(Integer stateId,
	    Integer year) {
	ArrayList<LocationWrapper> ret = new ArrayList<LocationWrapper>();
	try {
	    Statement stm = con.createStatement();
	    ResultSet r = stm
		    .executeQuery("SELECT id, latitude, longitude, weather, lightcondition, sex FROM crashes WHERE stateid = "
			    + stateId + " AND year = " + year);
	    while (r.next()) {
		ret.add(new LocationWrapper(new Integer(r.getInt(1)), r
			.getFloat(2), r.getFloat(3), r.getString(4), r
			.getString(5), r.getString(6)));
	    }
	    return ret;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public Location getStateCenter(Integer stateId) {
	try {
	    Statement stm = con.createStatement();
	    ResultSet r = stm
		    .executeQuery("SELECT lat, lon FROM states WHERE id = "
			    + stateId + "LIMIT 1");
	    if (r.next()) {
		return new Location(r.getFloat(1), r.getFloat(2));
	    }
	    return null;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}

    }

    public ArrayList<PVector> getCounts(FilterWrapper f) {
	int minYear = Integer.MAX_VALUE, maxYear = Integer.MIN_VALUE;
	ArrayList<PVector> ret = new ArrayList<PVector>();
	try {

	    String q = buildQueryString(f);
	    Statement stm = con.createStatement();
	    ResultSet r = stm.executeQuery(q);
	    while (r.next()) {
		int year = r.getInt(1);
		if (year > maxYear)
		    maxYear = year;
		if (year < minYear)
		    minYear = year;
		ret.add(new PVector(year, r.getInt(2)));
		if (DEBUG)
		    System.out.println(year + " -> " + r.getInt(2));
	    }
	    if (DEBUG)
		System.out.println("year range for the query: " + minYear
			+ " - " + maxYear);
	} catch (Exception e) {
	    e.printStackTrace();

	}
	return ret;
    }

    private String buildQueryString(FilterWrapper fw) {
	HashMap<String, ArrayList<String>> filters = fw.getConditions();
	String ret = "SELECT year, count(*) FROM crashes ";
	if (filters.size() > 0) {
	    ret += "WHERE ";
	    for (String key : filters.keySet()) {
		ret += "( ";
		ArrayList<String> f = filters.get(key);
		for (String s : f) {
		    ret += key + " = '" + s + "' OR ";
		}
		ret += "FALSE ) AND ";
	    }
	    ret += "TRUE ";
	}

	ret += "GROUP BY year ORDER BY year";
	if (DEBUG)
	    System.out.println(ret);
	return ret;
    }

}

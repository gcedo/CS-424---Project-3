package application;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.anotherbrick.inthewall.LocationWrapper;
import com.modestmaps.geo.Location;
import com.mysql.jdbc.Connection;

public class DBUtil {
    private static DBUtil instance = null;
    private Connection con = null;

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
		    .executeQuery("SELECT crash_id, latitude, longitude FROM crashes WHERE GLength( LineString(( PointFromWKB( position)), ( PointFromWKB( POINT( "
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

    public ArrayList<LocationWrapper> getPointsByState(Integer stateId) {
	ArrayList<LocationWrapper> ret = new ArrayList<LocationWrapper>();
	try {
	    Statement stm = con.createStatement();
	    ResultSet r = stm
		    .executeQuery("SELECT crash_id, latitude, longitude FROM crashes WHERE istatenum = "
			    + stateId + " GROUP BY crash_id");
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
}

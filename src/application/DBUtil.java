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
import com.anotherbrick.inthewall.StateInfo;
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
      con = (Connection) DriverManager.getConnection("jdbc:mysql://178.63.199.31:3306/car-crash",
          "cs424-p3", "idontknow");
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
              + loc.lat + ", " + loc.lon + " ) )))) < 0.5 GROUP BY crash_id");
      while (r.next()) {
        ret.add(new LocationWrapper(new Integer(r.getInt(1)), r.getFloat(2), r.getFloat(3)));
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
          .executeQuery("SELECT id, weather, lightcondition, sex, vehicletype, month, dayofweek, daynum, year, time, speedlimit, numfatal, age, avgspeed, maxspeed, alcohol FROM crashes WHERE id = "
              + crashId + " LIMIT 1");
      if (r.next()) {
        return new CrashDetails(r.getInt(1), r.getString(2), r.getString(3), r.getString(4),
            r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
            r.getString(10), r.getString(11), r.getString(12), r.getString(13), r.getString(14),
            r.getString(15), r.getString(16));
      }
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ArrayList<LocationWrapper> getPointsByState(Integer stateId, Integer year, FilterWrapper fw) {
    System.out.println("Getting counts...");
    ArrayList<LocationWrapper> ret = new ArrayList<LocationWrapper>();
    try {
      Statement stm = con.createStatement();
      String queryString = "SELECT id, latitude, longitude, weather, lightcondition, sex FROM crashes WHERE stateid = "
          + stateId + " AND year = " + year;
      HashMap<String, ArrayList<String>> filters = fw.getConditions();
      if (filters.size() > 0) {
        queryString += " AND ";
        for (String key : filters.keySet()) {
          queryString += "( ";
          queryString += generateFilters(key, filters.get(key));
          queryString += " FALSE ) AND ";
        }
        queryString += "TRUE ";
      }
      ResultSet r = stm.executeQuery(queryString);
      while (r.next()) {
        ret.add(new LocationWrapper(new Integer(r.getInt(1)), r.getFloat(2), r.getFloat(3), r
            .getString(4), r.getString(5), r.getString(6)));
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public StateInfo getStateCenter(String stateName) {
    try {
      Statement stm = con.createStatement();
      ResultSet r = stm
          .executeQuery("SELECT id, name, lat, lon, zoomlevel FROM states WHERE name = '"
              + stateName + "' LIMIT 1");
      if (r.next()) {
        return new StateInfo(r.getInt(1), r.getString(2),
            new Location(r.getFloat(3), r.getFloat(4)), r.getInt(5));
      }
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

  }

  public ArrayList<PVector> getCounts(FilterWrapper f, Integer stateId) {
    int minYear = Integer.MAX_VALUE, maxYear = Integer.MIN_VALUE;
    ArrayList<PVector> ret = new ArrayList<PVector>();
    try {

      String q = buildQueryString(f, stateId);
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
        System.out.println("year range for the query: " + minYear + " - " + maxYear);
    } catch (Exception e) {
      e.printStackTrace();

    }
    return ret;
  }

  private String buildQueryString(FilterWrapper fw, Integer stateId) {
    HashMap<String, ArrayList<String>> filters = fw.getConditions();
    String ret = "SELECT year, count(*) FROM crashes ";
    ret += "WHERE stateid = " + stateId + " ";
    if (filters.size() > 0) {
      ret += "AND ";
      for (String key : filters.keySet()) {
        ret += "( ";
        ret += generateFilters(key, filters.get(key));
        ret += " FALSE ) AND ";
      }
      ret += "TRUE ";
    }

    ret += "GROUP BY year ORDER BY year";
    if (DEBUG)
      System.out.println(ret);
    return ret;
  }

  public String generateFilters(String key, ArrayList<String> values) {
    String ret = "";
    if (key.equals("vehicletype") || key.equals("sex")) {
      String temp = "('";
      for (String s : values)
        temp += s + " ";
      temp += "' IN BOOLEAN MODE)";
      return "MATCH(" + key + ") AGAINST " + temp + " OR ";
    }
    if (key.equals("age"))
      return key + ">" + "'" + values.get(values.size() - 1) + "' OR ";
    if (key.equals("alcohol"))
      return key + ">" + values.get(values.size() - 1) + " OR ";
    if (key.equals("avgspeed"))
      return key + ">" + values.get(values.size() - 1).split(" ")[0] + " OR ";

    for (String s : values) {
      ret += key + " = '" + s + "' OR ";
    }
    return ret;
  }

  public ArrayList<PVector> getScatterData(String x, String y, Integer year, Integer stateId) {
    ArrayList<PVector> points = new ArrayList<PVector>();
    x = x.toLowerCase();
    y = y.toLowerCase();

    String q = "SELECT DISTINCT " + x + ", " + y + " FROM crashes WHERE year = '" + year.toString()
        + "' AND stateid =" + stateId + " AND " + x + " IS NOT NULL AND " + y
        + " IS NOT NULL ORDER BY " + x + ", " + y;
    System.out.println("Query Scatter: " + q);
    Statement stm;
    try {
      stm = con.createStatement();
      ResultSet r = stm.executeQuery(q);
      while (r.next()) {
        points.add(new PVector(r.getFloat(1), r.getFloat(2)));
      }
    } catch (SQLException e) {

    }

    return points;
  }
}

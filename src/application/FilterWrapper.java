package application;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterWrapper {
    private String keywordType;
    private HashMap<String, ArrayList<String>> conditions;
    private ArrayList<String> regions;

    public FilterWrapper(String k) {
	keywordType = k;
	conditions = new HashMap<String, ArrayList<String>>();
    }

    public FilterWrapper(String k, HashMap<String, ArrayList<String>> d) {
	keywordType = k;
	conditions = d;
    }

    public void addFilter(String key, String value) {
	if (conditions.get(key) == null) {
	    ArrayList<String> a = new ArrayList<String>();
	    a.add(value);
	    conditions.put(key, a);
	} else
	    conditions.get(key).add(value);
    }

    public void removeFilter(String key) {
	conditions.remove(key);
    }

    public void clearFilters(String newKey) {
	keywordType = newKey;
	// conditions.clear();
    }

    public String getKeywordType() {
	return keywordType;
    }

    public HashMap<String, ArrayList<String>> getConditions() {
	return conditions;
    }

    public ArrayList<String> getRegionsFilters() {
	return regions;
    }

    public void setConditions(String key, ArrayList<String> options) {
	conditions.put(key, options);
    }

}

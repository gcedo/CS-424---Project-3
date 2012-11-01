package com.anotherbrick.inthewall;

public enum MapStyles {
    MICROSOFT_ROAD("Road Map"), MICROSOFT_HYBRID("Hybrid Map"), MICROSOFT_AERIAL(
	    "Aerial Map");

    private MapStyles(final String label) {
	this.label = label;
    }

    private final String label;

    @Override
    public String toString() {
	return label;
    }
}

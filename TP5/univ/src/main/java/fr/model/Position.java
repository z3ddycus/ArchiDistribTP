package fr.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * A position representing by a latitude and a longitude
 */
public class Position {

	private double latitude,longitude;
	
	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Position() {	}

	public boolean equals(Object o){
		boolean result = false;
		if (o instanceof Position){
			Position otherPosition = (Position)o;
			result = otherPosition.latitude == this.latitude && 
					 otherPosition.longitude == this.longitude;
		}
		return result;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String toString(){
		final StringBuffer buffer = new StringBuffer();
		buffer.append("(").append(latitude).append(", ").append(longitude).append(")");
		return buffer.toString();
	}

	public double distance(Position p) {
		double lon1 = p.getLongitude();
		double lon2 = getLongitude();
		double lat1 = p.getLatitude();
		double lat2 = getLatitude();
		double theta = lon1 - lon2;
		double dist = Math.acos(
				Math.sin(lat1 * Math.PI / 180.) * Math.sin(lat2 * Math.PI / 180.)
				+ Math.cos(lat1 * Math.PI / 180.) * Math.cos(lat2 * Math.PI / 180.)
				* Math.cos(theta * Math.PI / 180.)
		);
		dist = dist * 4804.617576276;
		return dist;
	}
	
}

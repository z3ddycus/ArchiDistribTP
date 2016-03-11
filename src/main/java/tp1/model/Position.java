package tp1.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * A position with longitude/latitude
 */
public class Position {

	/**
	 * La position
	 */
	private double latitude,longitude;

	/**
	 * Une position
	 * @param latitude
	 * 		La latitude
	 * @param longitude
	 *		La longitude
     */
	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Position() {}
	@Override
	public boolean equals(Object o){
		boolean result = false;
		if (o instanceof Position){
			Position otherPosition = (Position)o; 
			result = otherPosition.latitude == this.latitude && 
					 otherPosition.longitude == this.longitude;
		}
		return result;
	}

	/**
	 * La latitude
	 * @return
	 * 		La latitude de this
     */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Remplace la latitude
	 * @param latitude
	 * 		la nouvelle latitude
     */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * La longitude
	 * @return
	 * 		La longitude de this
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Remplace la longitude
	 * @param longitude
	 * 		La nouvelle longitude
     */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Fonction d'abstraction
	 * @return
	 * 		"(" + getLatitude() + ", " + getLongitude() + ")"
     */
	public String toString(){
		final StringBuffer buffer = new StringBuffer();
		buffer.append("(").append(latitude).append(", ").append(longitude).append(")");
		return buffer.toString();
	}

	/**
	 * Renvoit la distance entre this et p en km.
	 * @param p
	 * 		La position
	 * @return
	 * 		La distance entre this et p en km
     */
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

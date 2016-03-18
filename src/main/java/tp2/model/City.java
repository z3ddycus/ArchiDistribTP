package tp2.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represent a city with its  
 * <ul>
 * 	<li>name</li>
 * 	<li>latitude</li>
 * 	<li>longitude</li>
 * 	<li>country</li>
 * </ul>
 *
 */
@XmlRootElement
public class City {

	private String name;
	private Position location;
	private String country;

	/**
	 * Creates a city with its name, its latitude, its longitude and its country
	 * @param name the name of the city
	 * @param latitude the latitude of the city in WGS84
	 * @param longitude the longitude of the city in WGS84
	 * @param country the country of the city
	 */
	public City(String name, double latitude, double longitude, String country) {
		this.name = name;
		this.location = new Position(latitude,longitude);
		this.country = country;
	}

	public City() {
	}

	public Position getPosition() {
		return location;
	}
	public void setPosition(Position position) {
		this.location = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String toString(){
		final StringBuffer buffer = new StringBuffer();
		buffer.append(name).append(" in ").append(country).append(" at ").append(location);
		return buffer.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof City)) return false;

		City city = (City) o;

		if (getName() != null ? !getName().equals(city.getName()) : city.getName() != null) return false;
		if (location != null ? !location.equals(city.location) : city.location != null) return false;
		return getCountry() != null ? getCountry().equals(city.getCountry()) : city.getCountry() == null;

	}

	@Override
	public int hashCode() {
		int result = getName() != null ? getName().hashCode() : 0;
		result = 31 * result + (location != null ? location.hashCode() : 0);
		result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
		return result;
	}
}

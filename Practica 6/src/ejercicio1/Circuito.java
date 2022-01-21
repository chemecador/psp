package ejercicio1;

public class Circuito {
    double lon;
    double lat;
    int zoom;
    String location;
    String name;
    String id;

    public Circuito() {
    }

    public Circuito(double lon, double lat, int zoom, String location, String name, String id) {
        this.lon = lon;
        this.lat = lat;
        this.zoom = zoom;
        this.location = location;
        this.name = name;
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Circuito{" +
                "Longitud=" + lon +
                ", Latitud=" + lat +
                ", Zoom=" + zoom +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

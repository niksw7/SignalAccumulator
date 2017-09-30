package hackathon.telefire.signalaccumulator.queue;

public class AlertFloorSensor {
    private String floor;
    private String sensor;

    public AlertFloorSensor(String floor, String sensor) {
        this.floor = floor;
        this.sensor = sensor;
    }

    public String getFloor() {
        return floor;
    }
}

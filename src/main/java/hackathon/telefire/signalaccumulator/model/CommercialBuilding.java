package hackathon.telefire.signalaccumulator.model;

import java.util.List;

public class CommercialBuilding {
    List<FloorInformation> floorSensors;

    public String getFloor(String sensorId) {
        return floorSensors.stream().filter((FloorInformation f) -> f.sensors.contains(sensorId)).findFirst().get().floorNumber;
    }

    static class FloorInformation {
        String floorNumber;
        List<String> sensors;

        public FloorInformation(String floorNumber, List<String> sensors) {
            this.floorNumber = floorNumber;
            this.sensors = sensors;
        }
    }

    public CommercialBuilding(List<FloorInformation> floorSensors) {
        this.floorSensors = floorSensors;
    }
}


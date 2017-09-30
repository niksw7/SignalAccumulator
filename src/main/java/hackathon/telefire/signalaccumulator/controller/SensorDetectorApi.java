package hackathon.telefire.signalaccumulator.controller;

import com.google.gson.Gson;
import hackathon.telefire.signalaccumulator.model.CommercialBuilding;
import hackathon.telefire.signalaccumulator.queue.AlertFloorSensor;
import hackathon.telefire.signalaccumulator.queue.FireWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class SensorDetectorApi {

    private CommercialBuilding building;
    private FireWatcher fireWatcher;

    @Autowired
    SensorDetectorApi(FireWatcher fireWatcher) throws IOException {
        File file = new File(getClass().getResource("/sensor-configuration.json").getFile());
        byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
        StringReader reader = new StringReader(new String(bytes));
        building = new Gson().fromJson(reader, CommercialBuilding.class);
        this.fireWatcher = fireWatcher;
    }

    @PutMapping("/sensorTriggered/{sensor-id}")
    public void sensorTriggered(@PathVariable("sensor-id") String sensorId) {
        //find out which floor this sensor belongs?
        String floorOnFire = building.getFloor(sensorId);
        fireWatcher.addToQueue(new AlertFloorSensor(floorOnFire, sensorId));

    }
}

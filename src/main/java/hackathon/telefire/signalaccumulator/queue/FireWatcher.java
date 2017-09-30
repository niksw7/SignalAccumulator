package hackathon.telefire.signalaccumulator.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class FireWatcher {
    private HashMap<String, Integer> currentAlerts;
    private boolean timerStarted;

    @Autowired
    public FireWatcher() {
        this.currentAlerts = new HashMap<>();
        this.timerStarted = false;
    }

    public void addToQueue(AlertFloorSensor alertFloorSensor) {
        startTimer();
        if (currentAlerts.containsKey(alertFloorSensor.getFloor())) {
            Integer sensorsOnFire = currentAlerts.get(alertFloorSensor.getFloor());
            currentAlerts.put(alertFloorSensor.getFloor(), sensorsOnFire + 1);
        } else {
            currentAlerts.put(alertFloorSensor.getFloor(), 1);
        }
    }

    private void startTimer() {
        if (timerStarted) {
            return;
        }
        timerStarted = true;
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(() -> {
            //print json request
            System.out.print("sending request as...");
            currentAlerts.entrySet().stream().forEach(e -> System.out.print("{floor:" + e.getKey() + ",sensors:" + e.getValue() +"}\n"));
            currentAlerts.clear();
            timerStarted = false;
        }, 30, TimeUnit.SECONDS);

    }
}

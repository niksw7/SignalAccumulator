package hackathon.telefire.signalaccumulator.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.USER_AGENT;

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
            currentAlerts.entrySet().stream().forEach(e -> System.out.print("{floora:" + e.getKey() + ",sensors:" + e.getValue() + "}\n"));
            try {

                //post request for telefire.
               // getRequest("https://tadhack.restcomm.com:443/restcomm-rvd/services/apps/APb42b453d526f4dd281b1e905f5b6f6d2/start?from=%2B12016768922&to=%2B918149660151&token=somesecret");
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentAlerts.clear();
            timerStarted = false;
        }, 30, TimeUnit.SECONDS);

    }

    private void getRequest(String url) throws Exception {


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

}

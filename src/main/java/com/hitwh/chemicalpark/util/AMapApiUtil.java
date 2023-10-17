package com.hitwh.chemicalpark.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AMapApiUtil {
    private final String AMAP_API_KEY = "7bd3a4d4acf1cac08989eae03915b3dd";
    private final String AMAP_DRIVING_API = "https://restapi.amap.com/v3/direction/driving";

    public double calculateDrivingDistance(String originLatitude, String originLongitude, String destinationLatitude, String destinationLongitude) {


        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = AMAP_DRIVING_API + "?key=" + AMAP_API_KEY +
                    "&origin=" + originLongitude + "," + originLatitude +
                    "&destination=" + destinationLongitude + "," + destinationLatitude;

            HttpGet httpGet = new HttpGet(url);
            String response = EntityUtils.toString(httpClient.execute(httpGet).getEntity());

            // 解析API返回的响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            double distance = rootNode.path("route").path("paths").get(0).path("distance").asDouble();

            return distance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
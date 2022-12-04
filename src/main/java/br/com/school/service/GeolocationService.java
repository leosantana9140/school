package br.com.school.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class GeolocationService {
    public List<Double> getLatitudeAndLongitude(String address) throws ApiException, InterruptedException, IOException {
        GeoApiContext geoApiContext = new GeoApiContext().setApiKey("");

        GeocodingApiRequest geocodingApiRequest = GeocodingApi.newRequest(geoApiContext).address(address);

        GeocodingResult[] response = geocodingApiRequest.await();

        GeocodingResult geocodingResult = response[0];

        Geometry geometry = geocodingResult.geometry;

        LatLng location = geometry.location;

        return Arrays.asList(location.lat, location.lng);
    }
}

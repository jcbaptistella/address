package com.apirest.address.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apirest.address.models.Address;
import com.apirest.address.models.Geocoding;
import com.apirest.address.repository.AddressRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AddressService {

	private String uri = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	private String key = "AIzaSyDTK0igIQTCi5EYKL9tzOIJ9N6FUASGZos";

	@Autowired
	AddressRepository addressRepository;

	public List<Address> get() {
		return addressRepository.findAll();
	}
	
	public Address findById(long id) {
		
		return addressRepository.findById(id);
	}

	public ResponseEntity<Object> insert(Address request) throws JsonMappingException, JsonProcessingException {
		ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);

		if (request != null) {

			if (request.getLatitude().isEmpty() && request.getLongitude().isEmpty()) {

				String urlFormat = getUrl(request);

				Geocoding GeocodingResult = getGeocoding(urlFormat);

				request.setLatitude(GeocodingResult.getLatitude());
				request.setLongitude(GeocodingResult.getLongitute());
			}

			addressRepository.save(request);

		} else {
			response = ResponseEntity.notFound().build();
		}

		return response;
	}

	private Geocoding getGeocoding(String urlFormat) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Geocoding geocoding = new Geocoding();

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(urlFormat)).build();

		CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(httpRequest,
				BodyHandlers.ofString());
		String response = futureResponse.join().body();
		JsonNode responseJsonNode = mapper.readTree(response);

		JsonNode results = responseJsonNode.get("results");

		for (JsonNode result : results) {
			JsonNode geometries = result.get("geometry");
			JsonNode location = geometries.get("location");

			geocoding.setLatitude(location.get("lat").asText());
			geocoding.setLongitute(location.get("lng").asText());
		}

		return geocoding;
	}

	private String getUrl(Address request) {
		String urlAddress = request.getNumber() + " " + request.getStreetName() + " " + request.getNeighbourhood() + " "
				+ request.getState();

		String urlAddressFormat = urlAddress.replaceAll("\\s+", "+");

		String urlFormat = this.uri.concat(urlAddressFormat).concat("&key=").concat(key);

		return urlFormat;
	}

	public ResponseEntity<Object> delete(long id) {
		ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);

		if (id > 0) {

			addressRepository.deleteById(id);

		} else {
			response = ResponseEntity.notFound().build();
		}

		return response;
	}

	public ResponseEntity<Object> update(long id, Address request)
			throws JsonMappingException, JsonProcessingException {
		ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);

		if (id > 0) {
			Address address = addressRepository.findById(id);
			
			if (address.getId() > 0) {
				request.setId(id);

				if (request.getLatitude().isEmpty() && request.getLongitude().isEmpty()) {

					String urlFormat = getUrl(request);

					Geocoding GeocodingResult = getGeocoding(urlFormat);

					request.setLatitude(GeocodingResult.getLatitude());
					request.setLongitude(GeocodingResult.getLongitute());
				}

				addressRepository.save(request);
			}
		} else {
			response = ResponseEntity.notFound().build();
		}

		return response;
	}
}

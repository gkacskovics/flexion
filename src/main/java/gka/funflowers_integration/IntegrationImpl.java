package gka.funflowers_integration;

import gka.funflowers_integration.developer.Developer;
import gka.funflowers_integration.exception.IntegrationException;
import gka.funflowers_integration.purchase.IntegrationPurchase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LogEvent;

import com.flexionmobile.codingchallenge.integration.Integration;
import com.flexionmobile.codingchallenge.integration.Purchase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class IntegrationImpl implements Integration {

	private static final Logger LOGGER = Logger.getLogger(IntegrationImpl.class);
	private static final String PURCHASES = "purchases";
	private final String basePath;
	private final String buyPath;
	private final String consumePath;
	private final String allPath;
	private final Client client;
	private final Gson gson;

	public IntegrationImpl(String api, Developer dev) {
		this.basePath = api + "/developer/" + dev.getName() + "/";
		this.buyPath = basePath + "buy/";
		this.consumePath = basePath + "consume/";
		this.allPath = basePath + "all";
		this.client = new Client();
		this.gson = new Gson();

	}

	public Purchase buy(String id) {
		try {
			LOGGER.info("Purchasing id: " + id);

			WebResource resource = createResource(buyPath + id);
			ClientResponse response = resource.post(ClientResponse.class);

			LOGGER.info("Purchase response: " + response.getStatus());

			validateResponse(response);
			String stringResponse = response.getEntity(String.class);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(stringResponse);
			}

			IntegrationPurchase fromJson = gson.fromJson(stringResponse, IntegrationPurchase.class);
			return fromJson;
		} catch (IntegrationException ie) {
			throw ie;
		} catch (Exception e) {
			LOGGER.error("Exception occured while buying " + id, e);
			throw new IntegrationException("Buying " + id + " failed.");
		}
	}

	public void consume(Purchase purchase) {
		try {
			LOGGER.info("Consuming purchase: " + purchase);

			WebResource resource = createResource(consumePath + purchase.getId());
			ClientResponse response = resource.post(ClientResponse.class);

			LOGGER.info("Purchase response: " + response.getStatus());

			validateResponse(response);
		} catch (IntegrationException ie) {
			throw ie;
		} catch (Exception e) {
			LOGGER.error("Exception occured while purchasing " + purchase.toString(), e);
			throw new IntegrationException("Coinsuming " + purchase.toString() + " failed.");
		}
	}

	public List<Purchase> getPurchases() {
		try {
			LOGGER.info("Retrieveing all purchases");

			WebResource resource = createResource(allPath);
			ClientResponse response = resource.get(ClientResponse.class);

			LOGGER.info("Purchase response: " + response.getStatus());
			validateResponse(response);

			String stringResponse = response.getEntity(String.class);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(stringResponse);
			}
			String jsonArray = gson.fromJson(stringResponse, JsonObject.class).getAsJsonArray(PURCHASES).toString();
			IntegrationPurchase[] fromJson = gson.fromJson(jsonArray, IntegrationPurchase[].class);
			return new ArrayList<Purchase>(Arrays.asList(fromJson));
		} catch (IntegrationException ie) {
			throw ie;
		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving all purchases", e);
			throw new IntegrationException("Retrieving purchases failed.");
		}
	}

	private WebResource createResource(String path) {
		WebResource resource = client.resource(path);
		resource.accept("application/json");
		return resource;
	}

	private void validateResponse(ClientResponse response) {
		if (response.getStatus() != 200) {
			throw new IntegrationException("Failed : HTTP error code : " + response.getStatus());
		}
	}

}

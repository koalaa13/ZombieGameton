package org.example.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.model.request.AllRequest;
import org.example.model.response.CommandResponse;
import org.example.model.response.RegisterResponse;
import org.example.model.response.UnitsResponse;
import org.example.model.response.ZpotsResponse;

import java.io.IOException;
import java.util.ArrayList;

public class ApiController implements Controller {
    private static final String TOKEN = "668d4dd964d54668d4dd964d56";

    private static final String API_AUTH_HEADER = "X-Auth-Token";

    private static final String API_URL = "https://games.datsteam.dev";

    private static final String TEST_API_URL = "https://games-test.datsteam.dev";

    private final boolean isTest;
    private final ObjectMapper objectMapper;

    private static ApiController testInstance = null;
    public static ApiController getTestInstance() {
        if (testInstance == null) {
            testInstance = new ApiController(true);
        }
        return testInstance;
    }

    private static ApiController instance = null;
    public static ApiController getInstance() {
        if (instance == null) {
            instance = new ApiController(false);
        }
        return instance;
    }

    private ApiController(boolean isTest) {
        this.isTest = isTest;
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String getApiUrl() {
        return isTest ? TEST_API_URL : API_URL;
    }

    private <T> T responseHandling(HttpResponse response, Class<? extends T> okResponseClass) throws IOException {
        var content = response.getEntity().getContent();
        if (response.getStatusLine().getStatusCode() != 200) {
            String errorMessage = objectMapper.readTree(content).get("error").asText();
            System.err.println("Code " + response.getStatusLine().getStatusCode() + ": " + errorMessage);
            return null;
        }
        return objectMapper.readValue(content, okResponseClass);
    }

    public RegisterResponse register() {
        final String url = getApiUrl();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(url + "/play/zombidef/participate");
            request.setHeader(API_AUTH_HEADER, TOKEN);

            return client.execute(request, response -> responseHandling(response, RegisterResponse.class));
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    public ZpotsResponse getZpots() {
        final String url = getApiUrl();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url + "/play/zombidef/world");
            request.setHeader(API_AUTH_HEADER, TOKEN);

            var resp = client.execute(request, response -> responseHandling(response, ZpotsResponse.class));
            if (resp.zpots == null) {
                resp.zpots = new ArrayList<>();
            }
            return resp;
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    public UnitsResponse getUnitsInfo() {
        final String url = getApiUrl();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url + "/play/zombidef/units");
            request.setHeader(API_AUTH_HEADER, TOKEN);

            var resp = client.execute(request, response -> responseHandling(response, UnitsResponse.class));
            if (resp.enemyBlocks == null) {
                resp.enemyBlocks = new ArrayList<>();
            }
            if (resp.base == null) {
                resp.base = new ArrayList<>();
            }
            if(resp.zombies == null) {
                resp.zombies = new ArrayList<>();
            }
            return resp;
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    public CommandResponse command(AllRequest allRequest) {
        final String url = getApiUrl();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            final String json = objectMapper.writeValueAsString(allRequest);
            final StringEntity entity = new StringEntity(json);

            HttpPost request = new HttpPost(url + "/play/zombidef/command");
            request.setHeader(API_AUTH_HEADER, TOKEN);
            request.setEntity(entity);

            return client.execute(request, response -> responseHandling(response, CommandResponse.class));
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }
}

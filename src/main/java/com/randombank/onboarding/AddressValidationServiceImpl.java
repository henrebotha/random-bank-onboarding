package com.randombank.onboarding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AddressValidationServiceImpl implements AddressValidationService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean isValid(String country, String postalCode, String streetAddress) {
        String countryPath = switch (country.toLowerCase()) {
            case "nl" -> "nld";
            case "be" -> "bel";
            default -> throw new UserCountryInvalidException();
        };

        URI uri = UriComponentsBuilder.fromUriString("https://www.postcode.eu/json/api-demo/validate/{countryPath}").queryParam("streetAndBuilding",
                "{streetAddress}"
        ).queryParam("postcode", "{postalCode}").encode().buildAndExpand(
                countryPath,
                streetAddress,
                postalCode
        ).toUri();

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newBuilder().build()) {
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException("Failed", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }

        logger.info(response.body());
        // [
        //   {"status":{"grade":"A","validationLevel":"Street","isAmbiguous":false},"mailLines":["Vrouwestraat","2011 JV  Haarlem"]},
        //   {"status":{"grade":"A","validationLevel":"Street","isAmbiguous":false},"mailLines":["Bakenessergracht","2011 JV  Haarlem"]}
        // ]

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode array = mapper.readTree(response.body());
            String grade = array.asArray().get(0).asObject().get("status").asObject().get("grade").asString();
            return grade.equalsIgnoreCase("a");
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }
}

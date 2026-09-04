package com.randombank.onboarding.services;

import com.randombank.onboarding.Address;
import com.randombank.onboarding.exceptions.UserCountryInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * This class wraps an external API (from <a href="https://www.postcode.eu">postcode.eu</a>) that does real address
 * validation for EU addresses. I'm only using a free demo endpoint, but even so, I've created a
 * MockAddressValidationServiceImpl that I can swap this one out with so that I'm not always hitting the API during
 * development.
 * <p>
 * The implementation is somewhat naive; I look for a "grade A" result and call that "valid", and anything else is
 * "invalid". A proper implementation would perhaps need to better define what "valid" means.
 */
@Service
public class AddressValidationServiceImpl implements AddressValidationService {
    private final Logger logger = LoggerFactory.getLogger(AddressValidationServiceImpl.class);

    @Override
    public boolean isValid(Address address) {
        List<String> grades = fetchGrades(address);
        return grades.stream().anyMatch("a"::equalsIgnoreCase);
    }

    private String getCountryPath(String country) {
        return switch (country.toLowerCase()) {
            // This does not currently consume our configurable list of valid countries, thus somewhat invalidating the
            // whole idea of making it easy to configure; after updating the config, you'd still have to add a branch
            // here manually.
            case "nl" -> "nld";
            case "be" -> "bel";
            default -> throw new UserCountryInvalidException();
        };
    }

    protected List<String> fetchGrades(Address address) {
        String countryPath = getCountryPath(address.country());

        URI uri = UriComponentsBuilder
                .fromUriString("https://www.postcode.eu/json/api-demo/validate/{countryPath}")
                .queryParam("streetAndBuilding", "{streetAddress}")
                .queryParam("postcode", "{postalCode}")
                .encode()
                .buildAndExpand(countryPath, address.streetAddress(), address.postalCode())
                .toUri();

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

        // Example of response body from API:
        // [
        //   {"status":{"grade":"A","validationLevel":"Street","isAmbiguous":false},"mailLines":["Vrouwestraat","2011 JV  Haarlem"]},
        //   {"status":{"grade":"A","validationLevel":"Street","isAmbiguous":false},"mailLines":["Bakenessergracht","2011 JV  Haarlem"]}
        // ]

        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayNode array = mapper.readTree(response.body()).asArray();
            return StreamSupport.stream(array.spliterator(), false).map(e -> e
                    .asObject()
                    .get("status")
                    .asObject()
                    .get("grade")
                    .asString()).toList();
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }
}

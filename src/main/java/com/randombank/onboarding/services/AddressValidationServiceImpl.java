package com.randombank.onboarding.services;

import com.randombank.onboarding.exceptions.UserCountryInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class AddressValidationServiceImpl implements AddressValidationService {
    private final Logger logger = LoggerFactory.getLogger(AddressValidationServiceImpl.class);

    @Value("${random-bank-onboarding.validate-address:false}")
    private boolean useAddressValidationApi;

    @Override
    public boolean isValid(String country, String postalCode, String streetAddress) {
        String countryPath = getCountryPath(country);

        return validateAddress(countryPath, postalCode, streetAddress);
    }

    private String getCountryPath(String country) {
        return switch (country.toLowerCase()) {
            case "nl" -> "nld";
            case "be" -> "bel";
            default -> throw new UserCountryInvalidException();
        };
    }

    protected List<String> getGrades(String countryPath, String postalCode, String streetAddress) {
        if (!useAddressValidationApi) {
            // Assume address would pass validation
            var result = new ArrayList<String>();
            result.add("a");
            return result;
        }

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
            ArrayNode array = mapper.readTree(response.body()).asArray();
            return StreamSupport.stream(array.spliterator(), false).map(e -> e.asObject().get("status").asObject().get(
                    "grade").asString()).toList();
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateAddress(String countryPath, String postalCode, String streetAddress) {
        List<String> grades = getGrades(countryPath, postalCode, streetAddress);
        return grades.stream().anyMatch("a"::equalsIgnoreCase);
    }
}

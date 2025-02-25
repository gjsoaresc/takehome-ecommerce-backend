package com.example.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UnsplashService {

    @Value("${unsplash.api.url}")
    private String unsplashUrl;

    @Value("${unsplash.api.key}")
    private String unsplashApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getShoeImageUrl(String color) {
        String url = UriComponentsBuilder.fromUriString(unsplashUrl)
                .queryParam("query", "shoes")
                .queryParam("color", color)
                .queryParam("client_id", unsplashApiKey)
                .toUriString();

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("results")) {
                Object resultsObj = response.get("results");

                if (resultsObj instanceof List<?> resultsList && !resultsList.isEmpty()) {
                    Object firstResult = resultsList.get(0);

                    if (firstResult instanceof Map<?, ?> firstResultMap) {
                        Object urlsObj = firstResultMap.get("urls");

                        if (urlsObj instanceof Map<?, ?> urlsMap) {
                            return Objects.toString(urlsMap.get("regular"), getDefaultImage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("🔴 Error fetching image from Unsplash: " + e.getMessage());
        }

        return getDefaultImage();
    }

    private String getDefaultImage() {
        return "https://source.unsplash.com/600x600/?shoes";
    }
}

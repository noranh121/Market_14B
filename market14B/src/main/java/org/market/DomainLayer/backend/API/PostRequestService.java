package org.market.DomainLayer.backend.API;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class PostRequestService {

    private final RestTemplate restTemplate;

    public PostRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendPostRequest(String url, Map<String, String> postContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(postContent, headers);

        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.POST,requestEntity,String.class);

        return response.getBody();
    }
}
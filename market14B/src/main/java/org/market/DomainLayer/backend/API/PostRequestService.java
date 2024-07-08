package org.market.DomainLayer.backend.API;


import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PostRequestService {

    private final RestTemplate restTemplate;

    public PostRequestService() {
        this.restTemplate = new RestTemplate();
    }

    public String sendPostRequest(String url, MultiValueMap<String, String> postContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postContent, headers);

        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.POST,requestEntity,String.class);

        return response.getBody();
    }
}
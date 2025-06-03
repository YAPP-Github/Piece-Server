package org.yapp.infra.qdrant.application;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QdrantService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${qdrant.host}")
    private String host;
    @Value("${qdrant.port}")
    private String port;
    private String baseUrl;

    @PostConstruct
    public void init() {
        baseUrl = "http://" + host + ":" + port + "/collections/";
    }

    public void createCollection(String collectionName, int size, String distance) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = baseUrl + collectionName;

        Map<String, Object> requestBody = Map.of(
            "vectors", Map.of(
                "size", size,               // 벡터 차원 수
                "distance", distance     // 유사도 기준
            )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.put(url, entity);
    }

    public void deleteCollection(String collectionName) {
        String url = baseUrl + collectionName;
        restTemplate.delete(url);
    }

    public void upsertVector(String collectionName, Long id, List<Double> vector) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = baseUrl + collectionName + "/points";

        Map<String, Object> point = Map.of(
            "id", id,
            "vector", vector,
            "payload", Map.of("userId", id)
        );

        Map<String, Object> request = Map.of("points", List.of(point));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        restTemplate.put(url, entity);
    }

    public List<Long> searchVectorIdsExcluding(String collectionName, List<Double> queryVector,
        int topK, List<Long> excludeIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = baseUrl + collectionName + "/points/query";

        // must_not 필터 구성
        List<Map<String, Object>> mustNotList = excludeIds.stream()
            .map(id -> Map.of("key", "userId", "match", Map.of("value", id)))
            .toList();

        Map<String, Object> filter = Map.of("must_not", mustNotList);

        Map<String, Object> request = Map.of(
            "query", queryVector,
            "limit", topK,
            "filter", filter
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        Map<String, Object> body = response.getBody();
        Map<String, Object> result = (Map<String, Object>) body.get("result");
        List<Map<String, Object>> points = (List<Map<String, Object>>) result.get("points");

        return points.stream()
            .map(point -> ((Number) point.get("id")).longValue())  // 명시적 캐스팅
            .toList();
    }

    public List<Double> getVectorById(String collectionName, Long id) {
        String url = baseUrl + collectionName + "/points/" + id;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = response.getBody();

        // result: Map<String, Object>
        Map<String, Object> result = (Map<String, Object>) body.get("result");

        // vector: List<Object>
        List<Object> rawVector = (List<Object>) result.get("vector");

        // 안전하게 List<Double>로 변환
        return rawVector.stream()
            .map(val -> ((Number) val).doubleValue()) // float, double, int 대응
            .toList();
    }
}

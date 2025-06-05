package xyz.ConstruTec.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class GeocodingService {
    
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeocodingService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Double> geocodeAddress(String logradouro, String numero, String bairro, String cidade, String estado) {
        try {
            // Formata o endereço completo
            StringBuilder endereco = new StringBuilder();
            
            // Adiciona logradouro e número
            if (logradouro != null && !logradouro.trim().isEmpty()) {
                endereco.append(logradouro.trim());
                if (numero != null && !numero.trim().isEmpty()) {
                    endereco.append(", ").append(numero.trim());
                }
            }
            
            // Adiciona bairro
            if (bairro != null && !bairro.trim().isEmpty()) {
                if (endereco.length() > 0) endereco.append(", ");
                endereco.append(bairro.trim());
            }
            
            // Adiciona cidade
            if (cidade != null && !cidade.trim().isEmpty()) {
                if (endereco.length() > 0) endereco.append(", ");
                endereco.append(cidade.trim());
            }
            
            // Adiciona estado
            if (estado != null && !estado.trim().isEmpty()) {
                if (endereco.length() > 0) endereco.append(", ");
                endereco.append(estado.trim());
            }
            
            // Adiciona país
            endereco.append(", Brasil");

            System.out.println("Endereço formatado para geocodificação: " + endereco.toString());

            // Constrói a URL com os parâmetros
            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                .queryParam("q", URLEncoder.encode(endereco.toString(), StandardCharsets.UTF_8))
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .queryParam("addressdetails", 1)
                .queryParam("countrycodes", "br")
                .queryParam("bounded", 1)
                .queryParam("viewbox", "-47.0,-19.0,-46.0,-18.0") // Região aproximada de Patos de Minas
                .build()
                .toString();

            System.out.println("URL de geocodificação: " + url);

            // Adiciona o User-Agent header para respeitar os termos de uso do Nominatim
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "ConstruTec/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Faz a requisição
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );

            // Parse da resposta JSON
            JsonNode results = objectMapper.readTree(response.getBody());
            
            System.out.println("Resposta da API: " + response.getBody());
            
            Map<String, Double> coordinates = new HashMap<>();
            
            if (results.isArray() && results.size() > 0) {
                JsonNode result = results.get(0);
                coordinates.put("latitude", result.get("lat").asDouble());
                coordinates.put("longitude", result.get("lon").asDouble());
                
                System.out.println("Coordenadas encontradas:");
                System.out.println("Latitude: " + coordinates.get("latitude"));
                System.out.println("Longitude: " + coordinates.get("longitude"));
                
                return coordinates;
            }

            System.out.println("Nenhuma coordenada encontrada para o endereço");
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao geocodificar endereço: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
} 
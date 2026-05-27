package com.portfolio.cryptoapi.service;

//IMPORTS
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class BinanceService {

    // URL pública de Binance para consultar el precio de cotización.
    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/price?symbol=";

    public Double obtenerPrecioEnTiempoReal(String simboloCripto, String simboloFiat) {
        try {
            // BTC/EUR.
            String par = simboloCripto.toUpperCase() + simboloFiat.toUpperCase();
            String urlFinal = BINANCE_API_URL + par;

            // Hacemos peticiones webs a otras APIs (RestTemplate).
            RestTemplate RestTemplate = new RestTemplate();

            // Hacemos GET a la URL de Binance y guardamos su respuesta JSON en un Map.
            @SuppressWarnings("unchecked")
            Map<String, Object> respuesta = RestTemplate.getForObject(urlFinal, Map.class);

            if (respuesta != null && respuesta.containsKey("price")) {
                // Convertimos a Double el precio de Binance (lo devuelven en texto).
                return Double.valueOf(respuesta.get("price").toString());
            }

            throw new RuntimeException("No se encontró el precio en la respuesta de Binance.");
        } catch (Exception e) {
            // En caso de que se caiga Binance o ponemos un símbolo que no exista.
            throw new RuntimeException("Error al conectar con el mercado en tiempo real: " + e.getMessage());
        }
    }
}

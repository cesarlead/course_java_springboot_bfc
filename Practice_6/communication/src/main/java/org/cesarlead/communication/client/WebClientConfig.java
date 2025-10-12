package org.cesarlead.communication.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Configuration // 1. Marcamos esta clase como una fuente de configuración para Spring.
public class WebClientConfig {

    @Value("${product-service.url}")
    private String baseUrl;

    @Bean // 2. Definimos un Bean que estará disponible en el contexto de Spring.
    public WebClient externalApiClient(WebClient.Builder webClientBuilder) {

        // 3. Spring inyecta aquí el Builder preconfigurado.
        return webClientBuilder
                .baseUrl(baseUrl) // 4. Establecemos la URL base para todas las llamadas.
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // 5. Configuramos cabeceras por defecto.
                .build(); // 6. Construimos la instancia inmutable de WebClient.
    }

    @Bean("resilientWebClient") // Le damos un nombre específico a este Bean
    public WebClient webClientWithTimeouts() {
        // 1. Creamos la configuración del cliente HTTP  (Reactor Netty)
        HttpClient httpClient = HttpClient.create()
                // 2. Timeout de Conexión: Tiempo máximo para establecer la conexión TCP inicial.
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 5 segundos

                // 3. Timeout de Respuesta: Tiempo máximo total desde que se envía la petición hasta que se recibe el inicio de la respuesta.
                .responseTimeout(Duration.ofSeconds(5))

                // 4. Timeouts de Lectura/Escritura por canal: Se aplica una vez la conexión está establecida.
                .doOnConnected(conn -> conn
                        // Lanza un error si no se leen datos en 5 segundos.
                        .addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                        // Lanza un error si no se pueden escribir datos en 5 segundos.
                        .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl) // Podemos configurar la URL base aquí también
                .build();

    }
}

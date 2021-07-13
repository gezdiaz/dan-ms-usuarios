package dan.tp2021.danmsusuarios.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dan.pedido")
public class PedidoRestProperties {

    private String url = "http://localhost:9002";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

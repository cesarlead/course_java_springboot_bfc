package org.cesarlead.communication.config;

import org.cesarlead.communication.dto.ConfigDTO;
import org.springframework.stereotype.Component;

@Component
public class AppConfiguration {

    private ConfigDTO config;

    public ConfigDTO getConfig() {
        return config;
    }

    public void setConfig(ConfigDTO config) {
        this.config = config;
    }

    public boolean isNewCheckoutEnabled() {
        if (config == null || config.featureToggles() == null) {
            return false;
        }
        return config.featureToggles().enableNewCheckout();
    }
}

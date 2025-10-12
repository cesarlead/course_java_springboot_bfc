package org.cesarlead.communication.dto;

public record FeatureTogglesDTO(
        boolean enableNewCheckout,
        boolean showPromotions
) {
}

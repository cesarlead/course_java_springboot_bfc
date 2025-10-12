package org.cesarlead.communication.dto;

public record ConfigDTO(
        String version,
        FeatureTogglesDTO featureToggles
) {
}

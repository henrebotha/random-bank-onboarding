package com.randombank.onboarding;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        @Nonnull String country,
        @Nonnull String postalCode,
        @Nonnull String streetAddress
) {
}

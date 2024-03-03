package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.AvailableDeliveryLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvailableDeliveryLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvailableDeliveryLocation.class);
        AvailableDeliveryLocation availableDeliveryLocation1 = getAvailableDeliveryLocationSample1();
        AvailableDeliveryLocation availableDeliveryLocation2 = new AvailableDeliveryLocation();
        assertThat(availableDeliveryLocation1).isNotEqualTo(availableDeliveryLocation2);

        availableDeliveryLocation2.setId(availableDeliveryLocation1.getId());
        assertThat(availableDeliveryLocation1).isEqualTo(availableDeliveryLocation2);

        availableDeliveryLocation2 = getAvailableDeliveryLocationSample2();
        assertThat(availableDeliveryLocation1).isNotEqualTo(availableDeliveryLocation2);
    }
}

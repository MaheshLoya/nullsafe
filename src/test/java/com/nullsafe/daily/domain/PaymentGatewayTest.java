package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.PaymentGatewayTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentGatewayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentGateway.class);
        PaymentGateway paymentGateway1 = getPaymentGatewaySample1();
        PaymentGateway paymentGateway2 = new PaymentGateway();
        assertThat(paymentGateway1).isNotEqualTo(paymentGateway2);

        paymentGateway2.setId(paymentGateway1.getId());
        assertThat(paymentGateway1).isEqualTo(paymentGateway2);

        paymentGateway2 = getPaymentGatewaySample2();
        assertThat(paymentGateway1).isNotEqualTo(paymentGateway2);
    }
}

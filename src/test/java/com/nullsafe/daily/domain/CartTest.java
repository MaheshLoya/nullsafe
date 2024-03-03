package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.CartTestSamples.*;
import static com.nullsafe.daily.domain.ProductTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cart.class);
        Cart cart1 = getCartSample1();
        Cart cart2 = new Cart();
        assertThat(cart1).isNotEqualTo(cart2);

        cart2.setId(cart1.getId());
        assertThat(cart1).isEqualTo(cart2);

        cart2 = getCartSample2();
        assertThat(cart1).isNotEqualTo(cart2);
    }

    @Test
    void productTest() throws Exception {
        Cart cart = getCartRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        cart.setProduct(productBack);
        assertThat(cart.getProduct()).isEqualTo(productBack);

        cart.product(null);
        assertThat(cart.getProduct()).isNull();
    }

    @Test
    void userTest() throws Exception {
        Cart cart = getCartRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        cart.setUser(usersBack);
        assertThat(cart.getUser()).isEqualTo(usersBack);

        cart.user(null);
        assertThat(cart.getUser()).isNull();
    }
}

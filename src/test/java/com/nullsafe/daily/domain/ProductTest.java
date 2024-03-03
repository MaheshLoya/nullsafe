package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.CartTestSamples.*;
import static com.nullsafe.daily.domain.OrdersTestSamples.*;
import static com.nullsafe.daily.domain.ProductTestSamples.*;
import static com.nullsafe.daily.domain.SubCatTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrdersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void subCatTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        SubCat subCatBack = getSubCatRandomSampleGenerator();

        product.setSubCat(subCatBack);
        assertThat(product.getSubCat()).isEqualTo(subCatBack);

        product.subCat(null);
        assertThat(product.getSubCat()).isNull();
    }

    @Test
    void cartTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        product.addCart(cartBack);
        assertThat(product.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getProduct()).isEqualTo(product);

        product.removeCart(cartBack);
        assertThat(product.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getProduct()).isNull();

        product.carts(new HashSet<>(Set.of(cartBack)));
        assertThat(product.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getProduct()).isEqualTo(product);

        product.setCarts(new HashSet<>());
        assertThat(product.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getProduct()).isNull();
    }

    @Test
    void ordersTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Orders ordersBack = getOrdersRandomSampleGenerator();

        product.addOrders(ordersBack);
        assertThat(product.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getProduct()).isEqualTo(product);

        product.removeOrders(ordersBack);
        assertThat(product.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getProduct()).isNull();

        product.orders(new HashSet<>(Set.of(ordersBack)));
        assertThat(product.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getProduct()).isEqualTo(product);

        product.setOrders(new HashSet<>());
        assertThat(product.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getProduct()).isNull();
    }

    @Test
    void subscribedOrdersTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        SubscribedOrders subscribedOrdersBack = getSubscribedOrdersRandomSampleGenerator();

        product.addSubscribedOrders(subscribedOrdersBack);
        assertThat(product.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getProduct()).isEqualTo(product);

        product.removeSubscribedOrders(subscribedOrdersBack);
        assertThat(product.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getProduct()).isNull();

        product.subscribedOrders(new HashSet<>(Set.of(subscribedOrdersBack)));
        assertThat(product.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getProduct()).isEqualTo(product);

        product.setSubscribedOrders(new HashSet<>());
        assertThat(product.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getProduct()).isNull();
    }
}

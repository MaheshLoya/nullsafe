package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.CatTestSamples.*;
import static com.nullsafe.daily.domain.ProductTestSamples.*;
import static com.nullsafe.daily.domain.SubCatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubCatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCat.class);
        SubCat subCat1 = getSubCatSample1();
        SubCat subCat2 = new SubCat();
        assertThat(subCat1).isNotEqualTo(subCat2);

        subCat2.setId(subCat1.getId());
        assertThat(subCat1).isEqualTo(subCat2);

        subCat2 = getSubCatSample2();
        assertThat(subCat1).isNotEqualTo(subCat2);
    }

    @Test
    void catTest() throws Exception {
        SubCat subCat = getSubCatRandomSampleGenerator();
        Cat catBack = getCatRandomSampleGenerator();

        subCat.setCat(catBack);
        assertThat(subCat.getCat()).isEqualTo(catBack);

        subCat.cat(null);
        assertThat(subCat.getCat()).isNull();
    }

    @Test
    void productTest() throws Exception {
        SubCat subCat = getSubCatRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        subCat.addProduct(productBack);
        assertThat(subCat.getProducts()).containsOnly(productBack);
        assertThat(productBack.getSubCat()).isEqualTo(subCat);

        subCat.removeProduct(productBack);
        assertThat(subCat.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getSubCat()).isNull();

        subCat.products(new HashSet<>(Set.of(productBack)));
        assertThat(subCat.getProducts()).containsOnly(productBack);
        assertThat(productBack.getSubCat()).isEqualTo(subCat);

        subCat.setProducts(new HashSet<>());
        assertThat(subCat.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getSubCat()).isNull();
    }
}

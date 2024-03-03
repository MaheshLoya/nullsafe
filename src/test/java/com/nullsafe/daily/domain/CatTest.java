package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.CatTestSamples.*;
import static com.nullsafe.daily.domain.SubCatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cat.class);
        Cat cat1 = getCatSample1();
        Cat cat2 = new Cat();
        assertThat(cat1).isNotEqualTo(cat2);

        cat2.setId(cat1.getId());
        assertThat(cat1).isEqualTo(cat2);

        cat2 = getCatSample2();
        assertThat(cat1).isNotEqualTo(cat2);
    }

    @Test
    void subCatTest() throws Exception {
        Cat cat = getCatRandomSampleGenerator();
        SubCat subCatBack = getSubCatRandomSampleGenerator();

        cat.addSubCat(subCatBack);
        assertThat(cat.getSubCats()).containsOnly(subCatBack);
        assertThat(subCatBack.getCat()).isEqualTo(cat);

        cat.removeSubCat(subCatBack);
        assertThat(cat.getSubCats()).doesNotContain(subCatBack);
        assertThat(subCatBack.getCat()).isNull();

        cat.subCats(new HashSet<>(Set.of(subCatBack)));
        assertThat(cat.getSubCats()).containsOnly(subCatBack);
        assertThat(subCatBack.getCat()).isEqualTo(cat);

        cat.setSubCats(new HashSet<>());
        assertThat(cat.getSubCats()).doesNotContain(subCatBack);
        assertThat(subCatBack.getCat()).isNull();
    }
}

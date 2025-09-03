package com.volavis.dbanonym.backend.database.jdbc.types;

import com.volavis.dbanonym.backend.database.attributes.rdbmsunspecific.UnknownAttribute;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcDecimalTypeTest {

    @Test
    public void testGenerateMinOrMax() {

        int precision = 6;
        int scale = 3;
        BigDecimal correctValue = new BigDecimal("-999.999");
        BigDecimal wrongValue = new BigDecimal("999.999");

        UnknownAttribute unknownAttribute = new UnknownAttribute();
        unknownAttribute.setPrecision(precision);
        unknownAttribute.setScale(scale);

        Assert.assertEquals(correctValue, JdbcDecimalType.generateMinOrMax(true, unknownAttribute));
        Assert.assertNotEquals(wrongValue, JdbcDecimalType.generateMinOrMax(true, unknownAttribute));
    }
}
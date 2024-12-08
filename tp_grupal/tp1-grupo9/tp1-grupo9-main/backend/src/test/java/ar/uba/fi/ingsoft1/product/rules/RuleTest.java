package ar.uba.fi.ingsoft1.product.rules;

import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.rules.exceptions.actionexceptions.LessThanActionException;
import ar.uba.fi.ingsoft1.product.rules.model.Rule;
import ar.uba.fi.ingsoft1.product.rules.model.RuleFactory;
import ar.uba.fi.ingsoft1.product.rules.service.RuleValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest
@ActiveProfiles("test")
public class RuleTest {

    private List<Product> createProductList(int maxCant, List<String> names, Map<String, String> attributes, Long idSet) {
        Product p1 = new Product(names.getFirst(), "rica y refrescante", 20, attributes);
        p1.setId(idSet);
        Product p2 = new Product(names.get(1), "...", 10, null);
        p2.setId(idSet + 1);
        Product p3 = new Product(names.get(2), "Cocholatoso", 5, null);
        p3.setId(idSet + 2);

        List<Product> products = new ArrayList<Product>();
        for (int i = 0; i < maxCant; i++) {
            products.add(p1);
        }

        for (int i = 0; i < 3; i++) {
            products.add(p2);
        }
        products.add(p3);

        return products;
    }

    private List<Rule> createRuleList(String attribute, Set<Object> values, String operator, String scope) {
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule1 = RuleFactory.getRule(attribute, operator, values, scope);
        rules.add(rule1);
        return rules;
    }

    @Test
    void testIsProductQuantityGreaterThanNIsInvalidOrder() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("weight", "2.0");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products1 = createProductList(4, names, attributes, 1L);
        List<Product> products2 = createProductList(3, names, attributes, 4L);
        RuleValidator ruleValidator1 = new RuleValidator(products1, 4, 10);
        RuleValidator ruleValidator2 = new RuleValidator(products2, 2, 10);

        Set<Object> values = new HashSet<Object>();
        values.add(3);

        Assertions.assertTrue(ruleValidator1.isValidOrder(createRuleList("", values, "<", "PRODUCT")), "Expected invalid order for products1");
        Assertions.assertFalse(ruleValidator2.isValidOrder(createRuleList("", values, "<", "PRODUCT")), "Expected invalid order for products2");
    }

    @Test
    void testIsOrderQuantityGreaterThanNIsInvalidOrder() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("weight", "2.0");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products1 = createProductList(4, names, attributes, 1L);
        List<Product> products2 = createProductList(3, names, attributes, 4L);
        RuleValidator ruleValidator1 = new RuleValidator(products1, 4, 10);
        RuleValidator ruleValidator2 = new RuleValidator(products2, 3, 5);

        Set<Object> values = new HashSet<Object>();
        values.add(3);
        
        Assertions.assertTrue(ruleValidator1.isValidOrder(createRuleList("", values, "<", "PRODUCT")), "Expected invalid order for products1");
        Assertions.assertFalse(ruleValidator2.isValidOrder(createRuleList("", values, "<", "PRODUCT")), "Expected invalid order for products2");
    }

    @Test
    void testIsItemWeightLessOrEqualThanMaxProductWeightIsValidOrder() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("weight", "5.0");
        attributes2.put("weight", "8.0");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products1 = createProductList(4, names, attributes1,1L);
        List<Product> products2 = createProductList(4, names, attributes2, 2L);
        RuleValidator ruleValidator1 = new RuleValidator(products1, 4, 10);
        RuleValidator ruleValidator2 = new RuleValidator(products2, 4, 10);

        Set<Object> values = new HashSet<Object>();
        values.add(8.0);

        Assertions.assertTrue(ruleValidator1.isValidOrder(createRuleList("weight", values, "<=", "PRODUCT")), "Expected invalid order for products");
        Assertions.assertTrue(ruleValidator2.isValidOrder(createRuleList("weight", values, "<=", "PRODUCT")), "Expected invalid order for products");
    }

    @Test
    void testIsItemWeightGreaterThanMaxProductWeightIsInvalidOrder() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("weight", "10.0");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products = createProductList(4, names, attributes, 1L);
        RuleValidator ruleValidator = new RuleValidator(products, 4, 10);

        Set<Object> values = new HashSet<Object>();
        values.add(8.0);

//        Assertions.assertFalse(ruleValidator.isValidOrder(createRuleList("weight", values, "<", "PRODUCT")), "Expected invalid order for products");
        LessThanActionException exception = Assertions.assertThrows(
                LessThanActionException.class,
                () -> ruleValidator.isValidOrder(createRuleList("weight", values, "<", "PRODUCT")),
                "Expected LessThanActionException for invalid weight comparison"
        );

        // Verificar el mensaje de la excepción
        String expectedMessage = "The value 10,00 is not less than (<) 8,00";
        Assertions.assertEquals(expectedMessage, exception.getMessage(), "Unexpected exception message");
    }

    @Test
    void testIsItemWeightEqualThanValuesOfRuleIsValidOrder() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("weight", "5.0");
        attributes2.put("weight", "10.0");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products1 = createProductList(4, names, attributes1,1L);
        List<Product> products2 = createProductList(4, names, attributes2, 2L);
        RuleValidator ruleValidator1 = new RuleValidator(products1, 4, 10);
        RuleValidator ruleValidator2 = new RuleValidator(products2, 4, 10);

        Set<Object> values = new HashSet<Object>();
        values.add(8.0);
        values.add(5.0);
        values.add(15.0);

        Assertions.assertTrue(ruleValidator1.isValidOrder(createRuleList("weight", values, "==", "PRODUCT")), "Expected invalid order for products");
        Assertions.assertFalse(ruleValidator2.isValidOrder(createRuleList("weight", values, "==", "PRODUCT")), "Expected invalid order for products");
    }

    @Test
    void testIsItemWeightNotEqualThanValuesOfRuleIsValidOrder() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("weight", "3.0");
        attributes2.put("weight", "15.0");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products1 = createProductList(4, names, attributes1,1L);
        List<Product> products2 = createProductList(4, names, attributes2, 2L);
        RuleValidator ruleValidator1 = new RuleValidator(products1, 4, 10);
        RuleValidator ruleValidator2 = new RuleValidator(products2, 4, 10);

        Set<Object> values = new HashSet<Object>();
        values.add(5.0);
        values.add(8.0);
        values.add(15.0);

        Assertions.assertTrue(ruleValidator1.isValidOrder(createRuleList("weight", values, "!=", "PRODUCT")), "Expected invalid order for products");
        Assertions.assertFalse(ruleValidator2.isValidOrder(createRuleList("weight", values, "!=", "PRODUCT")), "Expected invalid order for products");
    }

    @Test
    void testIsItemColorEqualThanValuesOfRuleIsValidOrder() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("color", "RED");
        attributes2.put("color", "GREEN");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products1 = createProductList(4, names, attributes1,1L);
        List<Product> products2 = createProductList(4, names, attributes2, 2L);
        RuleValidator ruleValidator1 = new RuleValidator(products1, 4, 10);
        RuleValidator ruleValidator2 = new RuleValidator(products2, 4, 10);

        Set<Object> values = new HashSet<Object>();
        values.add("RED");
        values.add("BLUE");
        values.add("PINK");

        Assertions.assertTrue(ruleValidator1.isValidOrder(createRuleList("color", values, "==", "PRODUCT")), "Expected invalid order for products");
        Assertions.assertFalse(ruleValidator2.isValidOrder(createRuleList("color", values, "==", "PRODUCT")), "Expected invalid order for products");
    }

    @Test
    void testIsItemColorNotEqualThanValuesOfRuleIsValidOrder() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("color", "GREEN");
        attributes2.put("color", "RED");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products1 = createProductList(4, names, attributes1,1L);
        List<Product> products2 = createProductList(4, names, attributes2, 2L);
        RuleValidator ruleValidator1 = new RuleValidator(products1, 4, 10);
        RuleValidator ruleValidator2 = new RuleValidator(products2, 4, 10);

        Set<Object> values = new HashSet<Object>();
        values.add("RED");
        values.add("BLUE");
        values.add("PINK");

        Assertions.assertTrue(ruleValidator1.isValidOrder(createRuleList("color", values, "!=", "PRODUCT")), "Expected invalid order for products");
        Assertions.assertFalse(ruleValidator2.isValidOrder(createRuleList("color", values, "!=", "PRODUCT")), "Expected invalid order for products");
    }


    /*
    @Test
    void testIsOrderWeightGreaterThanMaxOrderWeightIsInvalidOrder() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("weight", "10.0");
        List<String> names = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<Product> products = createProductList(4, names, attributes, 1L);
        RuleValidator ruleValidator = new RuleValidator(products, 4, 10);
        
        Set<Object> values = new HashSet<Object>();
        values.add(20.0);
        
        Assertions.assertFalse(ruleValidator.isValidOrder(createRuleList("weight", values, "<", "ORDER")), "Expected invalid order for products");
    }
    */
    
    @Test
    void testOrderWithProductLiquidAndGaseousTypesIsInvalid() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("type", "liquid");
        attributes2.put("type", "gaseous");
        List<String> names1 = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<String> names2 = Arrays.asList("Poet", "Eso", "Queso");

        List<Product> products1 = createProductList(4, names1, attributes1, 1L);
        List<Product> products2 = createProductList(4, names2, attributes2, 4L);

        products1.addAll(products2);

        RuleValidator ruleValidator = new RuleValidator(products1, 10, 20);

        Set<Object> values = new HashSet<Object>();
        values.add("liquid");
        values.add("gaseous");

        Assertions.assertFalse(ruleValidator.isValidOrder(createRuleList("type", values, "MUTUAL_EXCLUSION", "ORDER")), "Expected invalid order for products");
    }

    @Test
    void testOrderWithProductLiquidAndGaseousTypesIsValid() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("type", "liquid");
        attributes2.put("type", "liquid");
        List<String> names1 = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<String> names2 = Arrays.asList("Pecsi", "Eso", "Queso");

        List<Product> products1 = createProductList(4, names1, attributes1, 1L);
        List<Product> products2 = createProductList(4, names2, attributes2, 4L);

        products1.addAll(products2);

        RuleValidator ruleValidator = new RuleValidator(products1, 10, 20);

        Set<Object> values = new HashSet<Object>();
        values.add("liquid");
        values.add("gaseous");

        Assertions.assertTrue(ruleValidator.isValidOrder(createRuleList("type", values, "MUTUAL_EXCLUSION", "ORDER")), "Expected valid order for products");
    }
    
    /*
    @Test
    void testOrderWithLiquidTypeInProductIsValid() {
        Map<String, String> attributes1 = new HashMap<String, String>();
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes1.put("type", "liquid");
        attributes2.put("type", "gaseous");
        List<String> names1 = Arrays.asList("Coca-Cola", "Sprite", "Chocolatada");
        List<String> names2 = Arrays.asList("Pecsi", "Eso", "Queso");

        List<Product> products1 = createProductList(4, names1, attributes1, 1L);
        List<Product> products2 = createProductList(4, names2, attributes2, 4L);

        products1.addAll(products2);

        RuleValidator ruleValidator = new RuleValidator(products1, 10, 20);

        Set<Object> values = new HashSet<Object>();
        values.add("liquid");
        // values.add("gaseous");

        Assertions.assertTrue(ruleValidator.isValidOrder(createRuleList("type", values, "==", "ORDER")), "Expected valid order for products");
    }
    */
}

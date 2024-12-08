package ar.uba.fi.ingsoft1.product.rules.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OperatorUtils {

    public static boolean compareNumbers(Object attributeValue, Object ruleValue,
                                         DoubleComparisonOperator operator) throws IllegalArgumentException {

        double attrVal = convertToDouble(attributeValue, "attributeValue");

        double ruleVal = convertToDouble(ruleValue, "ruleValue");

        return operator.compare(attrVal, ruleVal);
    }

    public static boolean compareStrings(Object attributeValue, Object ruleValue,
                                         StringComparisonOperator operator) throws IllegalArgumentException {
        String attrVal;
        String ruleVal;

        // Convierte attributeValue a String
        if (attributeValue instanceof String) {
            attrVal = (String) attributeValue;
        } else {
            throw new IllegalArgumentException("attributeValue must be a String");
        }

        // Convierte ruleValue a String
        if (ruleValue instanceof String) {
            ruleVal = (String) ruleValue;
        } else {
            throw new IllegalArgumentException("ruleValue must be a String");
        }

        // Realiza la comparación
        return operator.compare(attrVal, ruleVal);
    }

    public interface DoubleComparisonOperator {
        boolean compare(double a, double b);
    }

    public interface StringComparisonOperator {
        boolean compare(String a, String b);
    }
    
    public static Optional<Number> getMaxNumber(Set<Object> set) throws IllegalArgumentException {
        List<Number> numbers = filterNumbersFrom(set);
        
        if (numbers.isEmpty()) 
            return Optional.empty();

        Number max = Collections.max(numbers, (a, b) -> Double.compare(a.doubleValue(), b.doubleValue()));
        return Optional.of(max);
    }

    public static Optional<Number> getMinNumber(Set<Object> set) throws IllegalArgumentException {
        List<Number> numbers = filterNumbersFrom(set);
        
        if (numbers.isEmpty()) 
            return Optional.empty();

        Number min = Collections.min(numbers, (a, b) -> Double.compare(a.doubleValue(), b.doubleValue()));
        return Optional.of(min);
    }

    private static List<Number> filterNumbersFrom(Set<Object> set) throws IllegalArgumentException {
        List<Number> numbers = new ArrayList<>();
        for (Object obj : set) {
            if (obj instanceof Number) {
                numbers.add((Number) obj);
            } else if (obj instanceof String) {
                try {
                    numbers.add(Double.parseDouble((String) obj));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format: " + obj, e);
                }
            }
        }
        return numbers;
    }
    
    /**
     * Try to convert the value to a double.
     *
     * @param value The value to convert. Must be String or Number.
     * @param variableName The name of the variable to convert. Used for error messages.
     * @return The value as a double.
     */
    public static double convertToDouble(Object value, String variableName) throws IllegalArgumentException {
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("%s must be a valid number string", variableName), e);
            }
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            throw new IllegalArgumentException(String.format("%s must be a String or Number", variableName));
        }
    }
    
}
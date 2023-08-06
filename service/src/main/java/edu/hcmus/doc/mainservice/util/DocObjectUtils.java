package edu.hcmus.doc.mainservice.util;

import edu.hcmus.doc.mainservice.model.exception.DocMainServiceRuntimeException;
import edu.hcmus.doc.mainservice.model.exception.DocMandatoryFields;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocObjectUtils {

    private static Validator validator;

    @Autowired
    public void setMessageSource(Validator validator) {
        DocObjectUtils.validator = validator;
    }

    public static void copyNonNullProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static <T> void validateObject(T object) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<T> constraintViolation = constraintViolations
                .stream()
                .findAny()
                .orElseThrow(DocMainServiceRuntimeException::new);
            throw new DocMandatoryFields(
                constraintViolation.getPropertyPath().toString(),
                constraintViolation.getMessage()
            );
        }
    }}

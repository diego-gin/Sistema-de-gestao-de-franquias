package com.franquias.api.validation;

import com.franquias.api.exceptions.BadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Valida objetos anotados com Bean Validation (@NotBlank, @Email, @Size, etc.)
 * e lança BadRequestException (HTTP 400) com uma mensagem legível caso
 * alguma regra seja violada.
 */
public final class ValidationUtil {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    private ValidationUtil() {
    }

    public static <T> void validar(T objeto) {
        Set<ConstraintViolation<T>> violacoes = VALIDATOR.validate(objeto);

        if (!violacoes.isEmpty()) {
            String mensagens = violacoes.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            throw new BadRequestException(mensagens);
        }
    }
}

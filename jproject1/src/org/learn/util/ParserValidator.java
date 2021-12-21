package org.learn.util;

import java.util.function.Function;

public class ParserValidator<TField, TResult> extends Validator</*TField,*/ TField, TResult> {

    private final Function<TField, TResult> parser;

    public ParserValidator(Function<TField, TResult> parser) {
        this.parser = parser;
    }

    @Override
    public boolean validate(TField value) {
        try {
            var result = parser.apply(value);

            onCompleteConsumer.accept(result);
            onCompleteActions.forEach(Runnable::run);

            return true;

        } catch (Exception ex) {
            onErrorActions.forEach(Runnable::run);
            return false;
        }
    }
}

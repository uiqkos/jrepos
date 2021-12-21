package org.learn.util;

import java.util.function.Predicate;

public class PredicateValidator</*TObject, */TField> extends Validator</*Void,*/ TField, TField> {

    private final Predicate<TField> predicate;

    public PredicateValidator(Predicate<TField> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean validate(TField value) {
        if (predicate.test(value)) {
            onErrorActions.forEach(Runnable::run);
            return false;
        }

        onCompleteConsumer.accept(value);
        onCompleteActions.forEach(Runnable::run);

        return true;
    }
}

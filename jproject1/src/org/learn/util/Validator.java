package org.learn.util;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Validator </*TObject,*/ TField, TResult> {
    private Supplier<TField> supplierExtractor;
//    private Function<TObject, TField> functionExtractor;
    protected List<Runnable> onErrorActions = new ArrayList<>();
    protected List<Runnable> onCompleteActions = new ArrayList<>();
    protected Consumer<TResult> onCompleteConsumer = tResult -> {};

    public Validator<TField, TResult> extractor(Supplier<TField> extractor) {
        this.supplierExtractor = extractor;
        return this;
    }
//    public Validator<TObject, TField, TResult> extractor(Function<TObject, TField> extractor) {
//        this.functionExtractor = extractor;
//        return this;
//    }

    public Validator<TField, TResult> onError(Runnable runnable) {
        onErrorActions.add(runnable);
        return this;
    }

    public Validator<TField, TResult> onComplete(Consumer<TResult> consumer) {
        onCompleteConsumer = consumer;
        return this;
    }

//    public TResult extractValidate(TObject object) {
//        if (functionExtractor == null) throw new IllegalArgumentException("extractor should be not null");
//
//        return validate(functionExtractor.apply(object));
//    }

    public boolean extractValidate() {
        if (supplierExtractor == null) throw new IllegalArgumentException("extractor should be not null");

        return validate(supplierExtractor.get());
    }

    public abstract boolean validate(TField value);
}


package io.github.jinputprocessor.builder.base;

import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.builder.InputProcessorBuilder;

public abstract class AbstractValidationBuilder<IN, T, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractValidationBuilder<IN, T, B, SELF>>
	extends AbstractIntermediateBuilder<IN, T, B, SELF> {

	protected AbstractValidationBuilder(B builder) {
		super(builder);
	}

	public B then() {
		return builder;
	}

	public final SELF isNotNull() {
		builder = builder.validate(
			value -> value == null
				? new ValidationFailure.ObjectIsNull()
				: null
		);
		return cast();
	}

	public final <U extends T> SELF isInstanceOf(Class<U> clazz) {
		builder = builder.validate(
			value -> !clazz.isInstance(value)
				? new ValidationFailure.ObjectIsNotInstanceOf(clazz)
				: null
		);
		return cast();
	}

}

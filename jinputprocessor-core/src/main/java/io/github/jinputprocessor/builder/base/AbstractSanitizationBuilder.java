package io.github.jinputprocessor.builder.base;

import io.github.jinputprocessor.builder.InputProcessorBuilder;

public abstract class AbstractSanitizationBuilder<IN, T, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractSanitizationBuilder<IN, T, B, SELF>>
	extends AbstractIntermediateBuilder<IN, T, B, SELF> {

	public AbstractSanitizationBuilder(B builder) {
		super(builder);
	}

	public B then() {
		return builder;
	}

	public final SELF defaultIfNull(T nullSafeValue) {
		builder = builder.sanitize(value -> value == null ? nullSafeValue : value);
		return cast();
	}

}

package io.github.jinputprocessor.core.builder.base;

import io.github.jinputprocessor.core.builder.InputProcessorBuilder;

public abstract class AbstractSanitizationBuilder<IN, T, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractSanitizationBuilder<IN, T, B, SELF>>
	extends AbstractIntermediateBuilder<IN, T, B, SELF> {

	public AbstractSanitizationBuilder(B builder) {
		super(builder);
	}

	public SELF defaultIfNull(T nullSafeValue) {
		builder = builder.sanitize(value -> value == null ? nullSafeValue : value);
		return cast();
	}

}

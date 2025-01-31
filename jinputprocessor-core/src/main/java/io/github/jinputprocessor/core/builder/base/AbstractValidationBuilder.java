package io.github.jinputprocessor.core.builder.base;

import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.core.builder.InputProcessorBuilder;

public abstract class AbstractValidationBuilder<IN, T, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractValidationBuilder<IN, T, B, SELF>>
	extends AbstractIntermediateBuilder<IN, T, B, SELF> {

	public AbstractValidationBuilder(B builder) {
		super(builder);
	}

	public SELF isNotNull() {
		builder = builder.validate(
			value -> value == null
				? new ValidationError.ObjectIsNull()
				: null
		);
		return cast();
	}

}

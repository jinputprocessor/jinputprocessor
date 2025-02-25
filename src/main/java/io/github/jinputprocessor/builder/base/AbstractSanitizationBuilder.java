package io.github.jinputprocessor.builder.base;

import io.github.jinputprocessor.builder.InputProcessorBuilder;
import java.util.function.UnaryOperator;

public abstract class AbstractSanitizationBuilder<IN, T, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractSanitizationBuilder<IN, T, B, SELF>>
	extends AbstractIntermediateBuilder<IN, T, B, SELF> {

	protected AbstractSanitizationBuilder(B builder) {
		super(builder);
	}

	public B then() {
		return builder;
	}

	/**
	 * Apply the given function to the input.
	 * 
	 * @param function	Any custom function to apply, <code>null</code> return must be handled appropriately
	 * 
	 * @return this sanitization builder
	 */
	public SELF apply(UnaryOperator<T> function) {
		builder = builder.sanitize(function);
		return cast();
	}

}

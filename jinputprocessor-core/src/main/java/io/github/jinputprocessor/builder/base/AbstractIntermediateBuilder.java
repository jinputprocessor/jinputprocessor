package io.github.jinputprocessor.builder.base;

import io.github.jinputprocessor.builder.InputProcessorBuilder;
import java.util.Objects;

/**
 * 
 * 
 *
 * @param <IN>
 * @param <T>
 * @param <B>
 */
public abstract class AbstractIntermediateBuilder<IN, T, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractIntermediateBuilder<IN, T, B, SELF>> {

	protected B builder;

	public AbstractIntermediateBuilder(B builder) {
		super();
		this.builder = Objects.requireNonNull(builder);
	}

	protected abstract SELF cast();

	public B then() {
		return builder;
	}

}

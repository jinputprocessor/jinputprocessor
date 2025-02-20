package io.github.jinputprocessor.builder.base.types.number;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.builder.base.AbstractInputProcessorBuilder;

public abstract class AbstractNumberInputProcessorBuilder<IN, N extends Number, SELF extends AbstractNumberInputProcessorBuilder<IN, N, SELF>>
	extends AbstractInputProcessorBuilder<IN, N, SELF> {

	protected AbstractNumberInputProcessorBuilder(InputProcessor<IN, N> previous) {
		super(previous);
	}

}

package io.github.jinputprocessor.core.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.core.builder.base.AbstractInputProcessorBuilder;

public class ObjectInputProcessorBuilder<IN, OUT> extends AbstractInputProcessorBuilder<IN, OUT, ObjectInputProcessorBuilder<IN, OUT>> {

	public ObjectInputProcessorBuilder(InputProcessor<IN, OUT> previous) {
		super(previous);
	}

	public static <OUT> ObjectInputProcessorBuilder<OUT, OUT> newInstance() {
		return new ObjectInputProcessorBuilder<>(InputProcessors.noOpProcessor());
	}

	@Override
	protected ObjectInputProcessorBuilder<IN, OUT> cast() {
		return this;
	}

}

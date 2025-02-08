package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.builder.base.AbstractInputProcessorBuilder;

public class ObjectInputProcessorBuilder<IN, OUT> extends AbstractInputProcessorBuilder<IN, OUT, ObjectInputProcessorBuilder<IN, OUT>> {

	public ObjectInputProcessorBuilder(InputProcessor<IN, OUT> previous) {
		super(previous);
	}

	@Override
	protected ObjectInputProcessorBuilder<IN, OUT> newInstance(InputProcessor<IN, OUT> process) {
		return new ObjectInputProcessorBuilder<>(process);
	}

	public static <OUT> ObjectInputProcessorBuilder<OUT, OUT> newInstance() {
		return new ObjectInputProcessorBuilder<>(InputProcessors.noOpProcessor());
	}

	public ObjectSanitizationBuilder<IN, OUT> sanitize() {
		return new ObjectSanitizationBuilder<>(this);
	}

	public ObjectValidationBuilder<IN, OUT> validateThat() {
		return new ObjectValidationBuilder<>(this);
	}

}

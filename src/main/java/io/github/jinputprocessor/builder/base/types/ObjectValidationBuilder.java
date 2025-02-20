package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.builder.base.AbstractValidationBuilder;

public class ObjectValidationBuilder<IN, OUT> extends AbstractValidationBuilder<IN, OUT, ObjectInputProcessorBuilder<IN, OUT>, ObjectValidationBuilder<IN, OUT>> {

	public ObjectValidationBuilder(ObjectInputProcessorBuilder<IN, OUT> builder) {
		super(builder);
	}

}

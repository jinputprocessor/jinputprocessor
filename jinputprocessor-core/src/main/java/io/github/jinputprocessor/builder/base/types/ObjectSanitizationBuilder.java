package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.builder.base.AbstractSanitizationBuilder;

public class ObjectSanitizationBuilder<IN, OUT> extends AbstractSanitizationBuilder<IN, OUT, ObjectInputProcessorBuilder<IN, OUT>, ObjectSanitizationBuilder<IN, OUT>> {

	public ObjectSanitizationBuilder(ObjectInputProcessorBuilder<IN, OUT> builder) {
		super(builder);
	}

	@Override
	protected ObjectSanitizationBuilder<IN, OUT> cast() {
		return this;
	}

}

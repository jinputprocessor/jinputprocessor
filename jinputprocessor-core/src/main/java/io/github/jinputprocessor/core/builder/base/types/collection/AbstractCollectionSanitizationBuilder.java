package io.github.jinputprocessor.core.builder.base.types.collection;

import io.github.jinputprocessor.core.builder.InputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.AbstractSanitizationBuilder;
import java.util.Collection;

public abstract class AbstractCollectionSanitizationBuilder<IN, C extends Collection<T>, T, B extends InputProcessorBuilder<IN, C, B>, SELF extends AbstractCollectionSanitizationBuilder<IN, C, T, B, SELF>>
	extends AbstractSanitizationBuilder<IN, C, B, SELF> {

	public AbstractCollectionSanitizationBuilder(B builder) {
		super(builder);
	}

	// --------------------------------------------------------------------------------------------------------------------

	public SELF filterElements() {
		return cast();
	}

}

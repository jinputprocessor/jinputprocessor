package io.github.jinputprocessor.core.builder;

import io.github.jinputprocessor.core.builder.base.types.ObjectInputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.types.StringInputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.types.collection.ListInputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.types.number.IntegerInputProcessorBuilder;
import java.util.List;

public final class Builder {

	public static final Builder INSTANCE = new Builder();

	private Builder() {
		// 
	}

	@SuppressWarnings("unused")
	public <T> ObjectInputProcessorBuilder<T, T> forClass(Class<T> clazz) {
		return ObjectInputProcessorBuilder.newInstance();
	}

	public StringInputProcessorBuilder<String> forString() {
		return StringInputProcessorBuilder.newInstance();
	}

	public IntegerInputProcessorBuilder<Integer> forInteger() {
		return IntegerInputProcessorBuilder.newInstance();
	}

	@SuppressWarnings("unused")
	public <T> ListInputProcessorBuilder<List<T>, T> forList(Class<T> elementClass) {
		return ListInputProcessorBuilder.newInstance();
	}

}

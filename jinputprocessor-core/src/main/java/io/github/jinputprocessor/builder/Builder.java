package io.github.jinputprocessor.builder;

import io.github.jinputprocessor.builder.base.types.ObjectInputProcessorBuilder;
import io.github.jinputprocessor.builder.base.types.StringInputProcessorBuilder;
import io.github.jinputprocessor.builder.base.types.collection.ListInputProcessorBuilder;
import io.github.jinputprocessor.builder.base.types.collection.SetInputProcessorBuilder;
import io.github.jinputprocessor.builder.base.types.number.IntegerInputProcessorBuilder;
import io.github.jinputprocessor.builder.base.types.number.LongInputProcessorBuilder;
import java.util.List;
import java.util.Set;

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

	public LongInputProcessorBuilder<Long> forLong() {
		return LongInputProcessorBuilder.newInstance();
	}

	@SuppressWarnings("unused")
	public <T> ListInputProcessorBuilder<List<T>, T> forList(Class<T> elementClass) {
		return ListInputProcessorBuilder.newInstance();
	}

	@SuppressWarnings("unused")
	public <T> SetInputProcessorBuilder<Set<T>, T> forSet(Class<T> elementClass) {
		return SetInputProcessorBuilder.newInstance();
	}

}

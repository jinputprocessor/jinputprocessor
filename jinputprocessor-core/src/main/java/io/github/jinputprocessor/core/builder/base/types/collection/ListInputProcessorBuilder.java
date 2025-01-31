package io.github.jinputprocessor.core.builder.base.types.collection;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.core.builder.InputProcessorBuilder;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ListInputProcessorBuilder<IN, T> extends AbstractSequencedCollectionInputProcessorBuilder<IN, List<T>, T, ListInputProcessorBuilder<IN, T>> {

	public ListInputProcessorBuilder(InputProcessor<IN, List<T>> previous) {
		super(previous);
	}

	public static <T> ListInputProcessorBuilder<List<T>, T> newInstance() {
		return new ListInputProcessorBuilder<>(InputProcessorBuilder.noOpProcessor());
	}

	public <OUT> ListInputProcessorBuilder<IN, OUT> processEach(InputProcessor<T, OUT> elementProcessor) {
		return processEach(elementProcessor, Collectors.toUnmodifiableList());
	}

	public <OUT> ListInputProcessorBuilder<IN, OUT> processEach(InputProcessor<T, OUT> elementProcessor, Collector<OUT, ?, List<OUT>> collector) {
		return processEach(elementProcessor, collector, ListInputProcessorBuilder::new);
	}

	@Override
	protected ListInputProcessorBuilder<IN, T> cast() {
		return this;
	}

}

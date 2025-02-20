package io.github.jinputprocessor.builder.base.types.collection;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SetInputProcessorBuilder<IN, T> extends AbstractCollectionInputProcessorBuilder<IN, Set<T>, T, SetInputProcessorBuilder<IN, T>> {

	public SetInputProcessorBuilder(InputProcessor<IN, Set<T>> previous) {
		super(previous);
	}

	@Override
	protected SetInputProcessorBuilder<IN, T> newInstance(InputProcessor<IN, Set<T>> process) {
		return new SetInputProcessorBuilder<>(process);
	}

	public static <T> SetInputProcessorBuilder<Set<T>, T> newInstance() {
		return new SetInputProcessorBuilder<>(InputProcessors.noOpProcessor());
	}

	public <OUT> SetInputProcessorBuilder<IN, OUT> processEach(InputProcessor<T, OUT> elementProcessor) {
		return processEach(elementProcessor, Collectors.toUnmodifiableSet());
	}

	public <OUT> SetInputProcessorBuilder<IN, OUT> processEach(InputProcessor<T, OUT> elementProcessor, Collector<OUT, ?, Set<OUT>> collector) {
		return processEach(elementProcessor, collector, SetInputProcessorBuilder::new);
	}

}

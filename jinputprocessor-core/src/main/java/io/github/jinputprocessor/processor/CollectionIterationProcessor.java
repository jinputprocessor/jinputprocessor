package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collector;

public class CollectionIterationProcessor<C_IN extends Collection<T>, T, C_OUT extends Collection<OUT>, OUT> implements InputProcessor<C_IN, C_OUT> {

	private final @Nonnull InputProcessor<T, OUT> elementProcessor;
	private final @Nonnull Collector<OUT, ?, C_OUT> collector;

	public CollectionIterationProcessor(@Nonnull InputProcessor<T, OUT> elementProcessor, @Nonnull Collector<OUT, ?, C_OUT> collector) {
		this.elementProcessor = Objects.requireNonNull(elementProcessor, "elementProcessor cannot be null");
		this.collector = Objects.requireNonNull(collector, "collector cannot be null");
	}

	@Override
	public ProcessResult<C_OUT> process(C_IN value) {
		var resultList = value.stream()
			.map(elementProcessor::process)
			.toList();

		var errors = resultList.stream()
			.filter(ProcessResult::isFailure)
			.map(ProcessResult::getFailure)
			.toList();
		if (!errors.isEmpty()) {
			return ProcessResult.failure(new ProcessFailure.MultiFailure(errors));
		}

		var newCollection = resultList.stream()
			.filter(ProcessResult::isSuccess)
			.map(ProcessResult::get)
			.collect(collector);

		return ProcessResult.success(newCollection);
	}

}

package io.github.jinputprocessor.core.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.core.result.BaseProcessorResult;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import java.util.SequencedCollection;
import java.util.stream.Collector;
import java.util.stream.IntStream;

public class SequencedCollectionIterationProcessor<C_IN extends SequencedCollection<T>, T, C_OUT extends SequencedCollection<OUT>, OUT> implements InputProcessor<C_IN, C_OUT> {

	private final @Nonnull InputProcessor<T, OUT> elementProcessor;
	private final @Nonnull Collector<OUT, ?, C_OUT> collector;

	public SequencedCollectionIterationProcessor(@Nonnull InputProcessor<T, OUT> elementProcessor, @Nonnull Collector<OUT, ?, C_OUT> collector) {
		this.elementProcessor = Objects.requireNonNull(elementProcessor, "elementProcessor cannot be null");
		this.collector = Objects.requireNonNull(collector, "collector cannot be null");
	}

	@Override
	public ProcessResult<C_OUT> process(C_IN value) {

		var iter = value.iterator();
		var resultList = IntStream.range(0, value.size())
			.mapToObj(index -> processElement(index, iter.next()))
			.toList();

		var errors = resultList.stream()
			.filter(Result::isError)
			.map(result -> new ProcessFailure.IndexedFailure(result.elemIndex, result.error))
			.toList();
		if (!errors.isEmpty()) {
			return BaseProcessorResult.failure(new ProcessFailure.MultiFailure(errors));
		}

		var newCollection = resultList.stream()
			.filter(Result::isSuccess)
			.map(Result::value)
			.collect(collector);
		return BaseProcessorResult.success(newCollection);
	}

	private record Result<OUT>(int elemIndex, OUT value, ProcessFailure error) {

		public boolean isSuccess() {
			return error == null;
		}

		public boolean isError() {
			return error != null;
		}

	}

	private Result<OUT> processElement(int elemIndex, T elem) {
		var elemResult = elementProcessor.process(elem);
		if (elemResult.isFailure()) {
			return new Result<>(elemIndex, null, elemResult.getFailure());
		} else {
			return new Result<>(elemIndex, elemResult.get(), null);
		}
	}

}

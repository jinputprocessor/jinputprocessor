package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.Path;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
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

		var failures = resultList.stream()
			.filter(Result::isFailure)
			.map(result -> result.failure.atPath(Path.createIndexPath(result.elemIndex)))
			.toList();
		if (!failures.isEmpty()) {
			return ProcessResult.failure(new ProcessFailure.MultiFailure(failures));
		}

		var newCollection = resultList.stream()
			.filter(Result::isSuccess)
			.map(Result::value)
			.collect(collector);
		return ProcessResult.success(newCollection);
	}

	private record Result<OUT>(int elemIndex, OUT value, ProcessFailure failure) {

		public boolean isSuccess() {
			return failure == null;
		}

		public boolean isFailure() {
			return failure != null;
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

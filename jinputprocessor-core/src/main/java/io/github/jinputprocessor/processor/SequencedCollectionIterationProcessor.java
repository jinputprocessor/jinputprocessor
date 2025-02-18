package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.Path;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.SequencedCollection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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

		var resultMap = resultList.stream().collect(Collectors.groupingBy(Result::isSuccess));

		var failures = resultMap.getOrDefault(false, List.of()).stream()
			.map(result -> result.failure.atPath(Path.createIndexPath(result.elemIndex)))
			.toList();
		if (!failures.isEmpty()) {
			return ProcessResult.failure(new ProcessFailure.MultiFailure(failures));
		}

		var newCollection = resultMap.getOrDefault(true, List.of()).stream()
			.map(Result::value)
			.collect(collector);
		return ProcessResult.success(newCollection);
	}

	private record Result<OUT>(int elemIndex, OUT value, ProcessFailure failure) {

		public boolean isSuccess() {
			return failure == null;
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

	@Override
	public String toString() {
		return "SequencedCollectionIterationProcessor\n"
			+ elementProcessor.toString().indent(2);
	}

}

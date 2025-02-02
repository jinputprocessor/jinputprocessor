package io.github.jinputprocessor.builder.base.types.collection;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.builder.base.AbstractInputProcessorBuilder;
import io.github.jinputprocessor.processor.SequencedCollectionIterationProcessor;
import java.util.SequencedCollection;
import java.util.function.Function;
import java.util.stream.Collector;

public abstract class AbstractSequencedCollectionInputProcessorBuilder<IN, C extends SequencedCollection<T>, T, SELF extends AbstractSequencedCollectionInputProcessorBuilder<IN, C, T, SELF>>
	extends AbstractInputProcessorBuilder<IN, C, SELF> {

	public AbstractSequencedCollectionInputProcessorBuilder(InputProcessor<IN, C> previous) {
		super(previous);
	}

	public <B extends AbstractSequencedCollectionInputProcessorBuilder<IN, C_OUT, OUT, B>, C_OUT extends SequencedCollection<OUT>, OUT> B processEach(
		InputProcessor<T, OUT> elementProcessor, Collector<OUT, ?, C_OUT> collector, Function<InputProcessor<IN, C_OUT>, B> builderFunction
	) {
		InputProcessor<C, C_OUT> forEachProcess = new SequencedCollectionIterationProcessor<>(elementProcessor, collector);
		return builderFunction.apply(getProcess().andThen(forEachProcess));
	}

}

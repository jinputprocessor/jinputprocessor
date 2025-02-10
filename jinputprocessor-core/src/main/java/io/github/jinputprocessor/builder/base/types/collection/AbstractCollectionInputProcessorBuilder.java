package io.github.jinputprocessor.builder.base.types.collection;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.builder.base.AbstractInputProcessorBuilder;
import io.github.jinputprocessor.processor.CollectionIterationProcessor;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collector;

public abstract class AbstractCollectionInputProcessorBuilder<IN, C extends Collection<T>, T, SELF extends AbstractCollectionInputProcessorBuilder<IN, C, T, SELF>>
	extends AbstractInputProcessorBuilder<IN, C, SELF> {

	public AbstractCollectionInputProcessorBuilder(InputProcessor<IN, C> previous) {
		super(previous);
	}

	public <B extends AbstractCollectionInputProcessorBuilder<IN, C_OUT, OUT, B>, C_OUT extends Collection<OUT>, OUT> B processEach(
		InputProcessor<T, OUT> elementProcessor, Collector<OUT, ?, C_OUT> collector, Function<InputProcessor<IN, C_OUT>, B> builderFunction
	) {
		InputProcessor<C, C_OUT> forEachProcess = new CollectionIterationProcessor<>(elementProcessor, collector);
		return builderFunction.apply(build().andThen(forEachProcess));
	}

}

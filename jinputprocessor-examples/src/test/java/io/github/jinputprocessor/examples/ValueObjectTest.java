package io.github.jinputprocessor.examples;

import io.github.jinputprocessor.examples.ValueObject.LastName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ValueObjectTest {

	@Test
	void test_examples() {
		Assertions.assertThat(LastName.of(null).toString()).isEqualTo("N/A");
		Assertions.assertThat(LastName.of(" Smith  ").toString()).isEqualTo("SMITH");
		Assertions.assertThat(LastName.of("DOE").toString()).isEqualTo("DOE");
		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> LastName.of("")).withMessageContaining("must not be empty");
		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> LastName.of("ThisIsAWayTooLongLastName")).withMessageContaining(" must be 20 chars max, but is 25");
	}

}

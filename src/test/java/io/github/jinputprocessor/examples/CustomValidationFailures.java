package io.github.jinputprocessor.examples;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure.CustomValidationFailure;

public class CustomValidationFailures {

	public static void main(String[] args) {

		/* 
		 * Configure a processor with some validations, returning various types of failure depending on the input
		 */
		var processor = InputProcessor.builder().forString()
			.validate(value -> value.equals("1") ? new MyAppCustomValidationFailure.CustomFailure1() : null)
			.validate(value -> value.equals("2") ? new MyAppCustomValidationFailure.CustomFailure2() : null)
			.validate(value -> value.equals("3") ? new AnotherCustomFailure() : null)
			.validate(value -> value.equals("4") ? new ValidationFailure.ObjectIsNull() : null)
			.sanitize(value -> value.equals("5") ? Integer.valueOf(null).toString() : value) // Integer.valueOf(null) will throw NPE
			.sanitize(value -> value + "-OK")
			.build();

		processAndPrint(processor, "0"); // 0-OK
		processAndPrint(processor, "1"); // Failed with MyApp custom failure 1
		processAndPrint(processor, "2"); // Failed with MyApp custom failure 2
		processAndPrint(processor, "3"); // Failed with another custom validation failure
		processAndPrint(processor, "4"); // Failed with a built-in validation failure
		processAndPrint(processor, "5"); // Failed with other type of failure
	}

	private static void processAndPrint(InputProcessor<String, String> processor, String value) {
		var result = processor.process(value);
		if (result.isSuccess()) {
			System.out.println(value + " --> " + result.get());
		} else {
			System.out.println(value + " --> " + handleCustomValidationFailure(result.getFailure()));
		}
	}

	private static String handleCustomValidationFailure(ProcessFailure failure) {
		return switch (failure) {
			case MyAppCustomValidationFailure myAppFail -> switch (myAppFail) {
				case MyAppCustomValidationFailure.CustomFailure1 myAppFail1 -> "Failed with MyApp custom failure 1";
				case MyAppCustomValidationFailure.CustomFailure2 myAppFail2 -> "Failed with MyApp custom failure 2";
			};
			case CustomValidationFailures otherCustom -> "Failed with another custom validation failure";
			case ValidationFailure defaultValidationFailure -> "Failed with a built-in validation failure";
			default -> "Failed with other type of failure";
		};
	}

	/**
	 * A custom validation failure interface, extending {@link CustomValidationFailures}
	 * and declaring two sub-failure objects: {@link CustomFailure1} and {@link CustomFailure2}.
	 * 
	 *
	 */
	public sealed interface MyAppCustomValidationFailure extends CustomValidationFailure {

		record CustomFailure1() implements MyAppCustomValidationFailure {

		}

		record CustomFailure2() implements MyAppCustomValidationFailure {

		}

	}

	/**
	 * Another custom validation failure object, implementing {@link CustomValidationFailures}.
	 * 
	 *
	 */
	public record AnotherCustomFailure() implements CustomValidationFailure {

	}

}

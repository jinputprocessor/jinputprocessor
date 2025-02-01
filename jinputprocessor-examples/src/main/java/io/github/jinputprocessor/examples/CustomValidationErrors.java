package io.github.jinputprocessor.examples;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.ProcessFailure.ValidationError.CustomValidationError;

public class CustomValidationErrors {

	public static void main(String[] args) {

		/* 
		 * Configure a processor with some validations, returning various types of failure depending on the input
		 */
		var processor = InputProcessor.builder().forString()
			.validate(value -> value.equals("1") ? new MyAppCustomValidationError.CustomError1() : null)
			.validate(value -> value.equals("2") ? new MyAppCustomValidationError.CustomError2() : null)
			.validate(value -> value.equals("3") ? new AnotherCustomError() : null)
			.validate(value -> value.equals("4") ? new ValidationError.ObjectIsNull() : null)
			.sanitize(value -> value.equals("5") ? Integer.valueOf(null).toString() : value) // Integer.valueOf(null) will throw NPE
			.sanitize(value -> value + "-OK")
			.build();

		processAndPrint(processor, "0"); // 0-OK
		processAndPrint(processor, "1"); // Failed with MyApp custom error 1
		processAndPrint(processor, "2"); // Failed with MyApp custom error 2
		processAndPrint(processor, "3"); // Failed with another custom validation error
		processAndPrint(processor, "4"); // Failed with a built-in validation error
		processAndPrint(processor, "5"); // Failed with other type of failure
	}

	private static void processAndPrint(InputProcessor<String, String> processor, String value) {
		var result = processor.process(value);
		if (result.isSuccess()) {
			System.out.println(value + " --> " + result.get());
		} else {
			System.out.println(value + " --> " + handleCustomValidationError(result.getFailure()));
		}
	}

	private static String handleCustomValidationError(ProcessFailure failure) {
		return switch (failure) {
			case MyAppCustomValidationError myAppErr -> switch (myAppErr) {
				case MyAppCustomValidationError.CustomError1 myAppError1 -> "Failed with MyApp custom error 1";
				case MyAppCustomValidationError.CustomError2 myAppError2 -> "Failed with MyApp custom error 2";
			};
			case CustomValidationErrors otherCustom -> "Failed with another custom validation error";
			case ValidationError defaultValidationError -> "Failed with a built-in validation error";
			default -> "Failed with other type of failure";
		};
	}

	/**
	 * A custom validation error interface, extending {@link CustomValidationErrors}
	 * and declaring two sub-error objects: {@link CustomError1} and {@link CustomError2}.
	 * 
	 *
	 */
	public sealed interface MyAppCustomValidationError extends CustomValidationError {

		record CustomError1() implements MyAppCustomValidationError {

		}

		record CustomError2() implements MyAppCustomValidationError {

		}

	}

	/**
	 * Another custom validation error object, implementing {@link CustomValidationErrors}.
	 * 
	 *
	 */
	public record AnotherCustomError() implements CustomValidationError {

	}

}

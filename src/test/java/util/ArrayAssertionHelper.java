package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class ArrayAssertionHelper<T> {
	public HashMap<String, Consumer<T>> tests;
	public ArrayList<T> unmatchedItems;
	public ArrayList<HashMap<String, Throwable>> unmatchedItemErrors;
	public int missingNulls;
	public int extraNulls;

	private int expectedNulls;

	public ArrayAssertionHelper() {
		tests = new HashMap<>();
		expectedNulls = 0;
	}

	public ArrayAssertionHelper<T> add(String name, Consumer<T> test) {
		tests.put(name, test);
		return this;
	}

	public ArrayAssertionHelper<T> addNull(int count) {
		expectedNulls += count;
		return this;
	}

	public ArrayAssertionHelper<T> add(String name, int count, Consumer<T> test) {
		for (int i = 0; i < count; i++) {
			add(name + "[" + count + "]", test);
		}
		return this;
	}

	public ArrayAssertionHelper<T> checkArray(ArrayList<T> array) {
		assertNotNull(array);
		if (tests.size() == 0 && expectedNulls == 0) {
			assertEquals(0, array.size());
			return this;
		}
		assertNotEquals(0, array.size());

		unmatchedItems = new ArrayList<>();
		unmatchedItemErrors = new ArrayList<>();
		ArrayList<String> remainingTests = new ArrayList<>(tests.keySet());

		HashMap<String, Throwable> errors = new HashMap<>();
		int nullCount = 0;
		for (T item : array) {
			if (item == null) {
				nullCount += 1;
				continue;
			}

			boolean testPassed = false;
			for (String testName : remainingTests) {
				Consumer<T> test = tests.get(testName);
				try {
					test.accept(item);
					// Test passed.
					remainingTests.remove(testName);
					testPassed = true;
					break;
				} catch (Throwable e) {
					errors.put(testName, e);
				}
			}
			if (!testPassed) {
				unmatchedItems.add(item);
				unmatchedItemErrors.add(errors);
				errors = new HashMap<>();
			}
			errors.clear();
		}

		if (remainingTests.size() == 0 && unmatchedItems.size() == 0 && expectedNulls == nullCount) {
			// All tests passed!
			return this;
		}

		missingNulls = Math.max(0, expectedNulls - nullCount);
		extraNulls = Math.max(0, nullCount - expectedNulls);
		String throwMessage = "Array mismatch: " + (unmatchedItems.size() + extraNulls);
		throwMessage += " unexpected items, " + (remainingTests.size() + missingNulls);
		throwMessage += " items not found. See stderr for details.";

		String divider = "================================\n";

		String consoleMessage = divider + throwMessage + "\n";
		if (remainingTests.size() != 0) {
			consoleMessage += "Missing items:\n";
			for (String testName : remainingTests) {
				consoleMessage += "\t- " + testName + "\n";
			}
			if (missingNulls != 0) {
				consoleMessage += "\t- null (" + missingNulls + " times)\n";
			}
			consoleMessage += "\n";
		}
		if (unmatchedItems.size() != 0) {
			String smallDivider = "=========\n";
			consoleMessage += "First unexpected item:\n" + unmatchedItems.get(0).toString() + "\n";
			consoleMessage += "Errors for all tests ran on the item:\n" + smallDivider;
			HashMap<String, Throwable> firstItemErrors = unmatchedItemErrors.get(0);
			for (String testName : firstItemErrors.keySet()) {
				consoleMessage += "Test " + testName + ": ";
				System.err.print(consoleMessage);
				consoleMessage = "";
				firstItemErrors.get(testName).printStackTrace();
				consoleMessage += "\n" + divider;
			}
		} else if (extraNulls != 0) {
			consoleMessage += "Array has " + extraNulls + " extra null values.\n";
		}

		consoleMessage += "For all recorded errors, run the test in debug mode and inspect the ";
		consoleMessage += "fields of ArrayAssertionHelper.";

		throw new RuntimeException(throwMessage);
	}
}

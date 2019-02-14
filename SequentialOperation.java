package generic;

public interface SequentialOperation<E, R> {
	// Java does not support array slices
	// so to avoid copying parts of the array indexes are passed
	abstract R operate(E[] arr, int lo, int hi);
}

package generic;

import java.util.function.BiFunction;

public class Main {
	// this adds numbers together with a bunch of syntactic sugar. wow!
	public static final BiFunction<Integer, Integer, Integer> ADDER = (Integer i1, Integer i2) -> (i1 + i2);
	
	// runs tests for the implemented examples
    public static void main(String[] args) {
    	countStrsTests();
    	lessThan7Tests();
    	parityTests();
    }
    
    /**
     * count strings testing code
     */
    public static void countStrsTests() {
    	System.out.println("Count Strings Tests:");
        String[] arr = {"h", "ee", "llll", "llll", "oo", "llll"};
        
        /**
         * this example illustrates lambda currying to create multiple sequential
         * operation functions out of the same logic
         */
        System.out.println(new GenericParallelArray<>(countStrs("llll"), ADDER).parallel(arr) + " should be 3.");
        System.out.println(new GenericParallelArray<>(countStrs("h"), ADDER).parallel(arr) + " should be 1.");
        System.out.println();
    }
    
    public static int countStrsSequential(String[] arr, int lo, int hi, String str) {
    	int result = 0;
    	for (int i = lo; i < hi; i++) {
    		if (arr[i].equals(str)) {
    			result += 1;
    		}
    	}
    	return result;
    }
    
    public static SequentialOperation<String, Integer> countStrs(String str) {
    	return (String[] array, int lo, int hi) -> countStrsSequential(array, lo, hi, str);
    }
    
    /**
     * less than 7 testing code
     */
    public static void lessThan7Tests() {
    	System.out.println("Less Than 7 Tests:");
    	
        Integer[] arr = new Integer[]{21, 7, 6, 8, 17, 1, 7, 7, 1, 1, 7};
        
        SequentialOperation<Integer, Integer> lessThan7 = (Integer[] array, int lo, int hi) -> {
        	int result = 0;
        	for (int i = lo; i < hi; i++) {
        		if (array[i] < (7)) {
        			result += 1;
        		}
        	}
        	return result;
        };
        
        System.out.println(new GenericParallelArray<>(lessThan7, ADDER).parallel(arr) + " should be 4.");
        System.out.println();
    }
    
    /**
     * parity testing code
     */
    public static void parityTests() {
    	System.out.println("Less Than 7 Tests:");
    	        
        SequentialOperation<Integer, Boolean> parityOperation = (Integer[] array, int lo, int hi) -> {
        	int numEven = 0;
        	for (int i = lo; i < hi; i++) {
        		if (array[i] % 2 == 0) {
        			numEven++;
        		}
        	}    
            return numEven % 2 == 0;
        };
        
        BiFunction<Boolean, Boolean, Boolean> booleanEquivalence = (Boolean b1, Boolean b2) -> (b1 == b2);
        GenericParallelArray<Integer, Boolean> parity = new GenericParallelArray<Integer, Boolean>(parityOperation, booleanEquivalence);

        /**
         * this example shows that a single GenericParallelArray instance can be used on multiple inputs,
         * the parallel function is blocking, so these two calls occur sequentially
         */
        System.out.println(parity.parallel(new Integer[]{1, 7, 4, 3, 6}) + " should be true.");
        System.out.println(parity.parallel(new Integer[]{6, 5, 4, 3, 2, 1}) + " should be false.");
        System.out.println();
    }
    
}

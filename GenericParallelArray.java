package generic;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiFunction;

public class GenericParallelArray<E, R> {
    private final int CUTOFF;
    private final ForkJoinPool POOL = new ForkJoinPool();
    private SequentialOperation<E, R> sequentialOperater;
    private BiFunction<R, R, R> joiner;
    
    public GenericParallelArray(SequentialOperation<E, R> operate, BiFunction<R, R, R> join, int cutoff) {
    	sequentialOperater = operate;
    	joiner = join;
    	CUTOFF = cutoff;
    }
    
    public GenericParallelArray(SequentialOperation<E, R> operate, BiFunction<R, R, R> join) {
    	this(operate, join, 1);
    }

    public R parallel(E[] arr) {
        return POOL.invoke(new SplitAndOperateTask(arr, 0, arr.length));
    }

    @SuppressWarnings("serial")
	private class SplitAndOperateTask extends RecursiveTask<R> {
        private E[] arr;
        private int lo, hi;
        
        public SplitAndOperateTask(E[] arr, int lo, int hi) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
        }
        
        @Override
        protected R compute() {
        	if (hi - lo > CUTOFF) {
            	int mid = lo + (hi - lo)/2;
        		ForkJoinTask<R> leftHalf = new SplitAndOperateTask(arr, mid, hi).fork();
        		ForkJoinTask<R> rightHalf = new SplitAndOperateTask(arr, lo, mid).fork();
        		return joiner.apply(leftHalf.join(), rightHalf.join());
        	} else {
        		return sequentialOperater.operate(arr, lo, hi);
        	}
        }
    }
 
}

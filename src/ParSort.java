import java.util.Arrays;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;



/**

 * This code has been fleshed out by Ziyao Qiao. Thanks very much.

 * TODO tidy it up a bit.

 */

class ParSort {



    public static int cutoff = 1000;
    
    private int core=8;

    public static void sort(int[] array, int from, int to,int threadCount) {

        int size = to - from;
        //System.out.println("# threads: "+ ForkJoinPool.commonPool().getRunningThreadCount());
  
        if (size < cutoff) {

            Arrays.sort(array, from, to);

        }
        else if (threadCount<=1) {
        	mergeSort(array);
		}
        else {

            CompletableFuture<int[]> parsort1 = parsort(array, from, from + (to - from) / 2,threadCount/2); // TODO implement me

            CompletableFuture<int[]> parsort2 = parsort(array, from + (to - from) / 2, to,threadCount/2); // TODO implement me

            CompletableFuture<int[]> parsort = parsort1.

                    thenCombine(parsort2, (xs1, xs2) -> {

                        int[] result = new int[xs1.length + xs2.length];

                        // TODO implement me

                        int i = 0;

                        int j = 0;

                        for (int k = 0; k < result.length; k++) {

                            if (i >= xs1.length) {

                                result[k] = xs2[j++];

                            } else if (j >= xs2.length) {

                                result[k] = xs1[i++];

                            } else if (xs2[j] < xs1[i]) {

                                result[k] = xs2[j++];

                            } else {

                                result[k] = xs1[i++];

                            }

                        }

                        return result;

                    });



            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
            //System.out.println("# threads: "+ ForkJoinPool.commonPool().getRunningThreadCount());

            parsort.join();

        }

    }



    private static CompletableFuture<int[]> parsort(int[] array, int from, int to,int count) {

        return CompletableFuture.supplyAsync(

                () -> {

                    int[] result = new int[to - from];

                    // TODO implement me

                    System.arraycopy(array, from, result, 0, result.length);

                    sort(result, 0, to - from,count);
                    //System.out.println("# threads: "+ ForkJoinPool.getCommonPoolParallelism());

                    return result;

                }

        );

    }
    
    public static void mergeSort(int[] a) {
		if (a.length >= 2) {
			int[] left  = Arrays.copyOfRange(a, 0, a.length / 2);
			int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);
			mergeSort(left);
			mergeSort(right);
			merge(left, right, a);
		}
	}
	
	public static void merge(int[] left, int[] right, int[] a) {
		int i1 = 0;
		int i2 = 0;
		for (int i = 0; i < a.length; i++) {
			if (i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
				a[i] = left[i1];
				i1++;
			} else {
				a[i] = right[i2];
				i2++;
			}
		}
	}


}
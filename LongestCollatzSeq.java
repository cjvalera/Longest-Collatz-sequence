import java.util.HashMap;

public class LongestCollatzSeq {

    private static final int size = 1000000;

    public static void main(String[] args){
        LongestCollatzSeq lcs = new LongestCollatzSeq();

        //Uncomment to enable Brute-Force approach
        //lcs.firstSolution(size);

        //Array Memoization approach
        lcs.secondSolution(size);

        //Uncomment to enable HashMap memoization approach
        //lcs.thirdSolution(size);
    }

    /**
     * After reading the problem, this is the solution that immediately came into my mind.
     * First, I need to loop through from 2 to 1000000 and use {@link #calculateSeq(long)} method
     * to find their number of terms/length. Second is to compare the returned value to maxTerms, if
     * the return value is larger, then replace the maxTerm and also save the current i to starting number.
     *
     * This solution is straight forward as it is a brute force approach of finding the longest Collatz sequence
     * from 1 to 1 million. This solution can be time costly, if we look for the longest sequence for numbers
     * larger than 1 million, this solution has O(n^2) time complexity because we are looping from 2 to n and
     * for every 'n' starting number we have to loop through (using the rules) until we find 1. (Assuming Collatz is
     * correct, otherwise were stuck in infinite loop).
     */
    public void firstSolution(int size){
        //calculate how long this solution took;
        long startTime = System.nanoTime();
        //initialize max terms zero and starting number to 0
        int maxTerms = 0,startingNum = 0;
        for(int i = 2; i < size; i++){
            //compare the current starting number's counts
            //if larger than old count,save count and starting number
            int currentTerms = calculateSeq(i);
            if(currentTerms > maxTerms) {
                maxTerms = currentTerms;
                startingNum = i;
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Using solution #1(brute-force): The number " + startingNum +
                            " produces the longest chain with " + maxTerms +
                            " terms and it took " + (endTime - startTime)/1000000 + "ms");
    }

    /**
     * This method is used by the brute-force approach.
     * This method will return the number of sequence given a starting number.
     *  n → n/2 (n is even)
     *  n → 3n + 1 (n is odd)
     *  Using the rules above, increment term(length of sequence)
     *  until n is equal to 1
     *  Using long instead of int is necessary as the numbers can get very large.
     * @param n
     * @return
     */
    public int calculateSeq(long n){
        //sequence starts with the starting number
        int term = 1;
        while(n != 1){
            n = (n % 2 == 0) ? n/2 : 3*n + 1;
            term++;
        }
        return term;
    }

    /**
     * In this solution, I optimized the brute force approach by using memoization. I came up with this approach
     * by realizing that as I loop through the 2 to 1 million, I am encountering a starting number where i recently
     * calculate its sequence terms and count.
     * Given the rules and the assumption that it will always finish at one (thus eventually decrease). Also since
     * I am looping from a smallest number, as I calculate larger number, when a the number becomes smaller than
     * the starting number, I can just look up that number to get its number of terms and add it to current number
     * of terms I previously calculated.
     *
     * Because of this I don't need to calculate the the starting number's terms again until I reach 1.
     * This solution is much faster that the previous solution but it comes with a cost of using space since in this
     * solution I used an array data structure with size of 1 million.
     *
     * @param size
     */
    public void secondSolution(int size){

        //calculate how long this solution took;
        long startTime = System.nanoTime();
        //initialize the array and set the 1 to have 1 term
        int[] terms = new int[size];
        terms[1] = 1;
        //same process as the first solution
        //loop then check to see if there is longer sequence
        int maxTerms = 0,startingNum = 0;
        for(int i = 2;i<size;i++){
            int currentTerms = calculateSeq(i,terms);
            if(currentTerms > maxTerms){
                maxTerms = terms[i];
                startingNum = i;
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Using solution #2(Array memoization): The number " + startingNum +
                        " produces the longest chain with " + maxTerms +
                         " terms and it took " + (endTime - startTime)/1000000 + "ms");

    }

    /**
     * This method is used by solution #2 by overloading the {@link #calculateSeq(long)} method
     * This will populate the array, the index of the array corresponds to the
     * starting number and they will contain their number of terms.
     *
     * The main part of this method is to break out of the loop when we see a number
     * that we previously calculated.
     * Lastly, we return the number of terms to see if that starting number has the longest sequence of terms.
     * @param n
     * @param terms
     * @return
     */
    public int calculateSeq(long n,int[] terms){
        //array has already been initialized terms[1] = 1
        int term = 0;
        long startingNum = n;
        //when we calculate a smaller number than starting number step out of loop
        //and look at the array for its number of terms
        while(startingNum <= n ){
            n = (n % 2 == 0) ? n/2 : 3*n + 1;
            term++;
        }
        //using the smaller number as index, get its number of terms and add it to
        //previous calculated number terms, this will give you the starting number's term count.
        //Lastly, using the starting number as index, save its number of counts.
        terms[(int)startingNum] = terms[(int)n] + term;
        return  terms[(int) startingNum];
    }

    /**
     * After the second solution, I realized that I can also use HashMap to implement the memoization
     * solution of the problem. My goal here is to see if using a HashMap is faster that using an array,
     * since HashMap offer O(1) for insertion and retrieval of elements (though arrays also offer O(1) access,
     * given that you know the index).
     *
     * This third solution is similar to the second solution. First is to check if the starting number
     * already exist in my HashMap, if it is, get its number of sequence and add it to the previously calculated
     * number of sequence.
     *
     * Conclusion: Using HashMap is much slower than using an array.
     * @param size
     */
    public void thirdSolution(int size){
        //calculate how long this solution took;
        long startTime = System.nanoTime();
        //initialize the HashMap and set 1 key to 1 value
        HashMap<Long,Integer> terms = new HashMap<>();
        terms.put(1L,1);
        int maxTerms = 0,startingNum = 0;
        for(int i = 2;i < size;i++){
            int currentTerms = calculateSeq(i,terms);
            if(currentTerms > maxTerms) {
                maxTerms = currentTerms;
                startingNum = i;
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Using solution #3(HashMap memoization): The number " + startingNum +
                        " produces the longest chain with " + maxTerms +
                        " terms and it took " + (endTime - startTime)/1000000 + "ms");
    }

    /**
     * This method is used by the third solution by overloading the {@link #calculateSeq(long)} method.
     * This will update the HashMap, adding new key/value pair. It will also check if a starting number
     * already exist in our HashMap. If already exist, use its value to calculate the current number's number
     * of terms.
     *
     * Lastly, we return the number of terms to see if that starting number has the longest sequence of terms.
     * @param n
     * @param terms
     * @return
     */
    public int calculateSeq(long n,HashMap<Long,Integer> terms){
        //HashMap with starting number(key) 1 has 1 value
        int term = 0;
        long startingNum = n;
        //to check if we have found a starting number in our
        boolean found = false;
        while(!found){
            //check if starting number is in the HashMap
            //if it is add the number of terms update the HashMap, then step out of the loop
            //otherwise, keep following the rules and incrementing terms.
            if(terms.containsKey(n)){
                term += terms.get(n);
                terms.put(startingNum,term);
                found = true;
            }else{
                n = (n % 2 == 0) ? n/2 : 3*n + 1;
                term++;
            }
        }
        return term;
    }
}

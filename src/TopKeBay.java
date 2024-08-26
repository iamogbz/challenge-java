import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


//print  top K frequent element in a data stream
//ex: 
//  ipstream1 = 1,1,1,2,2,3,4,5,6,8,9,9,9,9......
//  k = 2

//methods to execute for testing:
//topKFrequent(2) // -> initialization
//update(1)
//update(1)
//update(1)
//update(2)
//update(2)
//update(3)
//update(4)
//update(5)
//update(6)
//update(8)
//update(9)
//update(9)
//update(9)
//update(9)
//printTopKFrequent // -> Output: [1,9]  // order is not not important
//update(3)
//update(4)
//update(5)
//printTopKFrequent // ->  Output: new result...

// Main class should be named 'Solution' and should not be public.
class Solution {
    public static int K;
    public static Map<Integer, Integer> freqMap = new HashMap<>();
    public static Map<Integer, Set<Integer>> reverseFreqMap = new HashMap<>();
    public static int maxCountSeen = 0;
    
    public static void main(String[] args) {
        topKFrequent(2); // - >initialization
        update(1);
        update(1);
        update(1);
        update(2);
        update(2);
        update(3);
        update(4);
        update(5);
        update(6);
        update(8);
        update(9);
        update(9);
        update(9);
        update(9);
        printTopKFrequent();
        update(3);
        update(4);
        update(5);
        update(5);
        update(5);
        update(5);
        update(5);
        update(5);
        update(5);
        update(5);
        printTopKFrequent();
    }
    
    public static void topKFrequent(int k) {
        Solution.K = k;
    }
    
    public static void update(int n) {
        if (!freqMap.containsKey(n)) {
            freqMap.put(n, 0);
        }
        
        int prevCount = freqMap.get(n);

        if (!reverseFreqMap.containsKey(prevCount)) {
            reverseFreqMap.put(prevCount, new HashSet<>());
        }
        
        if (reverseFreqMap.get(prevCount).contains(n)) {
            reverseFreqMap.get(prevCount).remove(n);
        }
        
        int newCount = prevCount + 1;
        
        freqMap.put(n, newCount);
        
        if (!reverseFreqMap.containsKey(newCount)) {
            reverseFreqMap.put(newCount, new HashSet<>());
        }
        reverseFreqMap.get(newCount).add(n);
        
        if (newCount > maxCountSeen) {
            maxCountSeen = newCount;
        }
    }
    
    public static void printTopKFrequent() {
        // { 1:3, 2:2, 3:1, 4:1, 5:1, 6:1, 8:1, 9:4 }
        // {1:[4,5,6,8], 2:[2], 3:[1], 4:[9]}
        // 4
        List<Integer> topKFreq = new ArrayList<>();
        int currentMax = maxCountSeen;
        while (topKFreq.size() < K) {
            if (currentMax <= 0) {
                break;
            }
            if (reverseFreqMap.containsKey(currentMax)) {
                topKFreq.addAll(reverseFreqMap.get(currentMax));
            }
            currentMax--;
        }
        System.out.println(topKFreq.subList(0, K));
    }
}

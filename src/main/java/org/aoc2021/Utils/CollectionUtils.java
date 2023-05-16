package org.aoc2021.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
    public static int[] StringArr2IntArr(String[] arr){
        return Stream.of(arr).mapToInt(Integer::parseInt).toArray();
    }

    public static String[] IntArr2StringArr(int[] arr){
        return Arrays.stream(arr).mapToObj(String::valueOf).toArray(String[]::new);
    }

    public static List<Integer> StringList2IntList(List<String> list){
        return list.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public static List<String> IntList2StringList(List<Integer> list){
        return list.stream().map(String::valueOf).collect(Collectors.toList());
    }

    public static int[] StringList2IntArr(List<String> list){
        return list.stream().mapToInt(Integer::parseInt).toArray();
    }

    public static <T> List<T> ListWithoutDuplicates(List<T> list){
        return list.stream().distinct().collect(Collectors.toList());
    }

    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each: all) if (!set.add(each)) return true;
        return false;
    }

    public static <T> int countCommonElements(Collection<T> collection1, Collection<T> collection2) {
        Set<T> set1 = new HashSet<>(collection1);
        Set<T> set2 = new HashSet<>(collection2);
        set1.retainAll(set2);
        return set1.size();
    }

    /**
     * Generic function to add to the value of a key in a Map<T, Long>
     */
    public static <T> void addValueToMap(Map<T, Long> map, T key, Long valueToAdd) {
        // Check if the map already has an entry for the key
        if (map.containsKey(key)) {
            // If it does, get its current value and add the valueToAdd to it
            Long currentValue = map.get(key);
            map.put(key, currentValue + valueToAdd);
        } else {
            // If it doesn't, initialize the key's value to valueToAdd
            map.put(key, valueToAdd);
        }
    }

    public static int countBooleanCube(boolean[][][] cube) {
        int count = 0;
        for (boolean[][] plane : cube) {
            for (boolean[] row : plane) {
                for (boolean value : row) {
                    if (value) {
                        count++;
                    }
                }
            }
        }
        return count;
    }


    public static boolean ArrayContains(int[] array, int v) {
        for(int i : array){
            if(i == v){
                return true;
            }
        }
        return false;
    }



    public static <T> T getFromQueue(Queue<T> queue, int index){
        if(index>=queue.size()){
            throw new IndexOutOfBoundsException(queue.size() + " - " + index);
        }
        Queue<T> clone = new LinkedList<T>(queue);
        for(int i=0; i<index; i++){
            clone.remove();
        }
        return clone.peek();
    }


}

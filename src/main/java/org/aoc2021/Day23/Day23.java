package org.aoc2021.Day23;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

//https://adventofcode.com/2021/day/23
public class Day23 {
    private static final String[] part2Addition = new String[]{"  #D#C#B#A#", "  #D#B#A#C#"}; //This will be added to the input when Part 2 is calculated.
    public static void main(String[] args) {
        Run("Day23", false);
    }
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> inputPart1 = Utils.SplitInputByLinebreaks(filename);

        Board part1Board = new Board(inputPart1);
        Gamestate part1Starter = new Gamestate(inputPart1, part1Board);
        int part1Result = RunSearch(part1Starter, silent);
        answers[0] = part1Result;

        List<String> inputPart2 = new ArrayList<>(inputPart1);
        inputPart2.addAll(inputPart1.size() - 2, Arrays.asList(part2Addition));

        Board part2Board = new Board(inputPart2);
        Gamestate part2Starter = new Gamestate(inputPart2, part2Board);
        int part2Result = RunSearch(part2Starter, silent);
        answers[1] = part2Result;

        if(!silent){
            System.out.println("Part 1 answer: " + part1Result);
            System.out.println("Part 2 answer: " + part2Result);
        }

        return answers;
    }

    /**
     * Performs node tree search to explore the gamespace and find the most efficient gamestate.
     * To narrow the searchspace, we make a couple of simplifications:
     * <br> - Each "move" consists of an amphipod moving from one coordinate to another, we don't simulate their movement one tile at a time.
     * <br> - Immediately after each move, all amphipods will attempt to go home. Going home is compulsory and is treated as an action between moves.
     * <br> - Because of the above, all "moves" consist of one amphipod moving from a room to a hallway position.
     * <br>One move might be "B moves from 3, 2 to 10, 3, which unblocks the way for A at 2, 3 to immediately go home, which unblocks the way for D at 1, 3 to immediately go home"
     * @param starterGamestate The starting position of the game
     * @param silent Whether to print the winning game or not
     * @return The score (total energy spent) of the winning game
     */
    private static int RunSearch(Gamestate starterGamestate, boolean silent) {
        //The "scores" map stores every single gamestate we've seen so far, and the minimum amount of energy possible to reach it.
        //After discovering a gamestate, we might discover a more efficient way to reach it later, in which case the entry in this map will be updated to the new best-case.
        //Note that two gamestates are equivalent if they look the same, even if the steps used to reach that state differ.
        Map<Gamestate, Integer> scores = new HashMap<>();
        scores.put(starterGamestate, 0);
        //The queue is much like the map above, except we order it by score, so we always know what the most promising gamestate in the queue is.
        //Note that we remove each entry after processing it.
        PriorityQueue<Map.Entry<Gamestate, Integer>> queue = new PriorityQueue<>(Map.Entry.comparingByValue());
        queue.add(Map.entry(starterGamestate, 0));

        while(queue.size() > 0){
            Map.Entry<Gamestate, Integer> mostPromisingEntry = queue.poll(); //This removes the entry from the queue
            Gamestate winner = mostPromisingEntry.getKey();
            if(winner.IsDone()){
                if(!silent){
                    while(winner != null){
                        System.out.println(winner);
                        System.out.println(scores.get(winner));
                        winner = winner.parent;
                    }
                }
                return mostPromisingEntry.getValue();
            }
            winner.SpawnChildren(scores, queue);
        }
        throw new RuntimeException("Could not find a winning gamestate!");
    }
}

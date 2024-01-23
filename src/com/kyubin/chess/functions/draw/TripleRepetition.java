package com.kyubin.chess.functions.draw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kyubin.chess.object.ChessGame.fen_game_no_half_move;

public class TripleRepetition {
    public static boolean isTripleRepetition(){
        List<String> list = new ArrayList<>(fen_game_no_half_move);

        HashMap<String, Integer> countMap = new HashMap<>();
        for (String item : list) {
            countMap.put(item, countMap.getOrDefault(item, 0) + 1);
        }

        for (int count : countMap.values()) {
            if (count >= 3) {
                return true;
            }
        }

        return false;
    }
}

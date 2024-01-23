package com.kyubin.chess.functions.draw;

import com.kyubin.chess.object.ChessGame;

public class FiftyMovesDraw {
    public static boolean isFiftyMovesDraw(){
        return ChessGame.half_move.peek() == 100;
    }
}

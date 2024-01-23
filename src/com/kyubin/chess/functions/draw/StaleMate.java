package com.kyubin.chess.functions.draw;

import com.kyubin.chess.functions.move.Bishop;
import com.kyubin.chess.functions.move.Knight;
import com.kyubin.chess.functions.move.Queen;
import com.kyubin.chess.functions.move.Rook;
import com.kyubin.chess.functions.move.check.Check;
import com.kyubin.chess.functions.move.king.King;
import com.kyubin.chess.functions.move.pawn.Pawn;
import com.kyubin.chess.object.ChessGame;
import com.kyubin.chess.object.Piece;

public class StaleMate {
    public static boolean isStalemate(boolean is_white){
        if(!is_white) {
            for (Piece p : ChessGame.black_rook) {
                if (!Rook.rookMoves(p.xy, false, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.black_knight) {
                if (!Knight.knightMoves(p.xy, false, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.black_bishop) {
                if (!Bishop.bishopMoves(p.xy, false, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.black_queen) {
                if (!Queen.queenMoves(p.xy, false, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.black_pawn) {
                if (!Pawn.pawnMoves(p.xy, false, false).isEmpty()) {
                    return false;
                }
            }
            if (!King.kingMoves(ChessGame.black_king.xy, false, false).isEmpty()) {
                return false;
            }

            return Check.checkCount(true, false) == 0;
        } else {
            for (Piece p : ChessGame.white_rook) {
                if (!Rook.rookMoves(p.xy, true, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.white_knight) {
                if (!Knight.knightMoves(p.xy, true, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.white_bishop) {
                if (!Bishop.bishopMoves(p.xy, true, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.white_queen) {
                if (!Queen.queenMoves(p.xy, true, false).isEmpty()) {
                    return false;
                }
            }
            for (Piece p : ChessGame.white_pawn) {
                if (!Pawn.pawnMoves(p.xy, true, false).isEmpty()) {
                    return false;
                }
            }
            if (!King.kingMoves(ChessGame.white_king.xy, true, false).isEmpty()) {
                return false;
            }

            return Check.checkCount(false, false) == 0;
        }
    }
}

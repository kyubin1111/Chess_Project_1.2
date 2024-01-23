package com.kyubin.chess.functions.move;

import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.functions.move.check.Check;
import com.kyubin.chess.object.ChessGame;
import com.kyubin.chess.object.Piece;
import com.kyubin.chess.object.xy.XY;

import java.util.ArrayList;
import java.util.List;

import static com.kyubin.chess.functions.BasicFunctions.addAll;

public class Knight {
    public static List<XY> knightMoves(XY knight, boolean is_white, boolean no_repeat) {
        List<XY> allies;

        if(is_white) {
            allies = addAll(ChessGame.white_rook, ChessGame.white_knight, ChessGame.white_bishop,
                    ChessGame.white_queen, ChessGame.white_pawn, ChessGame.white_king);
        } else {
            allies = addAll(ChessGame.black_rook, ChessGame.black_knight, ChessGame.black_bishop,
                    ChessGame.black_queen, ChessGame.black_pawn, ChessGame.black_king);
        }

        List<XY> moves = new ArrayList<>();
        int[][] directions = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        for (int[] d : directions) {
            int x = knight.x + d[0];
            int y = knight.y + d[1];

            // 체스판 범위를 벗어나거나 아군 기물이 있는지 확인
            if (!(x <= 0 || x > 8 || y <= 0 || y > 8)) {
                // 아군 기물이 있는지 확인
                boolean is_piece_this_square=false;

                for(XY piece:allies){
                    if(piece.x==x&&piece.y==y){
                        is_piece_this_square=true;
                        break;
                    }
                }

                if(!is_piece_this_square) moves.add(new XY(x, y));
            }
        }

        List<XY> validMoves = new ArrayList<>();

        if(!no_repeat){
            List<Piece> pieces=is_white? ChessGame.white_knight: ChessGame.black_knight;

            XY xy = knight;

            for (XY move : moves) {
                Piece piece = null;

                for(Piece p:pieces){
                    if(p.xy.equals(xy)){
                        p.xy=move;
                        piece=PieceFunctions.removeEnemyWithPiece(p.xy,is_white);
                        xy=move;
                    }
                }
                if (Check.checkCount(!is_white,true)==0) {
                    validMoves.add(move);
                }
                if(piece!=null){
                    if(piece.type.equals("white_rook")) ChessGame.white_rook.add(piece);
                    if(piece.type.equals("black_rook")) ChessGame.black_rook.add(piece);
                    if(piece.type.equals("white_knight")) ChessGame.white_knight.add(piece);
                    if(piece.type.equals("black_knight")) ChessGame.black_knight.add(piece);
                    if(piece.type.equals("white_bishop")) ChessGame.white_bishop.add(piece);
                    if(piece.type.equals("black_bishop")) ChessGame.black_bishop.add(piece);
                    if(piece.type.equals("white_queen")) ChessGame.white_queen.add(piece);
                    if(piece.type.equals("black_queen")) ChessGame.black_queen.add(piece);
                    if(piece.type.equals("white_pawn")) ChessGame.white_pawn.add(piece);
                    if(piece.type.equals("black_pawn")) ChessGame.black_pawn.add(piece);
                }
            }

            for(Piece p:pieces){
                if(p.xy.equals(xy)){
                    p.xy=knight;
                }
            }

            return validMoves;
        } else {
            return moves;
        }
    }
}

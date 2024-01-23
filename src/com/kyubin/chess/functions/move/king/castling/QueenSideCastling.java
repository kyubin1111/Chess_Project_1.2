package com.kyubin.chess.functions.move.king.castling;

import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.functions.move.check.Check;
import com.kyubin.chess.game.MoveTo;
import com.kyubin.chess.object.ChessGame;
import com.kyubin.chess.object.xy.XY;

public class QueenSideCastling {
    public static boolean isPossibleQueenSideCastling(boolean is_white){
        if(is_white&&!ChessGame.white_is_possible_queenSideCastling) return false;
        if(!is_white&&!ChessGame.black_is_possible_queenSideCastling) return false;

        for(MoveTo move : ChessGame.game){
            if(is_white?move.now_xy.x==8&&move.now_xy.y==8:move.now_xy.x==8&&move.now_xy.y==1){
                return false;
            }
            if(move.type.contains(is_white?"white_king":"black_king")){
                return false;
            }
            if(move.type.contains(is_white?"white_rook":"black_rook")&&is_white?move.pre_xy.x==8&&move.pre_xy.y==8:move.pre_xy.x==8&&move.pre_xy.y==1){
                return false;
            }
        }
        if(is_white){
            XY p = ChessGame.white_king.xy;
            if(Check.checkCount(false, true)>0) return false;
            ChessGame.white_king.xy=new XY(ChessGame.white_king.xy.x-1,ChessGame.white_king.xy.y);
            if(Check.checkCount(false, true)>0) return false;
            ChessGame.white_king.xy=new XY(ChessGame.white_king.xy.x-1,ChessGame.white_king.xy.y);
            if(Check.checkCount(false, true)>0) return false;
            ChessGame.white_king.xy=p;
        } else {
            XY p = ChessGame.black_king.xy;
            if(Check.checkCount(true, true)>0) return false;
            ChessGame.black_king.xy=new XY(ChessGame.black_king.xy.x-1,ChessGame.black_king.xy.y);
            if(Check.checkCount(true, true)>0) return false;
            ChessGame.black_king.xy=new XY(ChessGame.black_king.xy.x-1,ChessGame.black_king.xy.y);
            if(Check.checkCount(true, true)>0) return false;
            ChessGame.black_king.xy=p;
        }

        if(is_white){
            if(PieceFunctions.isPieceInSquare(new XY(ChessGame.white_king.xy.x-1,
                    ChessGame.white_king.xy.y))) return false;
        } else {
            if(PieceFunctions.isPieceInSquare(new XY(ChessGame.black_king.xy.x-1,
                    ChessGame.black_king.xy.y))) return false;
        }
        if(is_white){
            if(PieceFunctions.isPieceInSquare(new XY(ChessGame.white_king.xy.x-2,
                    ChessGame.white_king.xy.y))) return false;
        } else {
            if(PieceFunctions.isPieceInSquare(new XY(ChessGame.black_king.xy.x-2,
                    ChessGame.black_king.xy.y))) return false;
        }
        if(is_white){
            return !PieceFunctions.isPieceInSquare(new XY(ChessGame.white_king.xy.x - 3,
                    ChessGame.white_king.xy.y));
        } else {
            return !PieceFunctions.isPieceInSquare(new XY(ChessGame.black_king.xy.x - 3,
                    ChessGame.black_king.xy.y));
        }
    }
}

package com.kyubin.chess.functions.move.pawn;

import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.functions.move.check.Check;
import com.kyubin.chess.object.xy.XYPlus;
import com.kyubin.chess.game.MoveTo;
import com.kyubin.chess.object.ChessGame;
import com.kyubin.chess.object.Piece;
import com.kyubin.chess.object.xy.XY;

import java.util.ArrayList;
import java.util.List;

import static com.kyubin.chess.functions.BasicFunctions.addAll;

public class Pawn {
    public static List<XYPlus> pawnMoves(XY pawn, boolean is_white, boolean no_repeat) {
        List<XYPlus> moves = new ArrayList<>();
        List<XY> allies;
        List<XY> enemy;

        if(is_white) {
            allies = addAll(ChessGame.white_rook, ChessGame.white_knight, ChessGame.white_bishop,
                    ChessGame.white_queen, ChessGame.white_pawn, ChessGame.white_king);
            enemy = addAll(ChessGame.black_rook, ChessGame.black_knight, ChessGame.black_bishop,
                    ChessGame.black_queen, ChessGame.black_pawn, ChessGame.black_king);
        } else {
            allies = addAll(ChessGame.black_rook, ChessGame.black_knight, ChessGame.black_bishop,
                    ChessGame.black_queen, ChessGame.black_pawn, ChessGame.black_king);
            enemy = addAll(ChessGame.white_rook, ChessGame.white_knight, ChessGame.white_bishop,
                    ChessGame.white_queen, ChessGame.white_pawn, ChessGame.white_king);
        }

        int direction = is_white ? -1 : 1; // 흰색 폰은 위로, 검은색 폰은 아래로 이동
        int startRow = is_white ? 7 : 2; // 흰색 폰은 7번째 줄에서 시작, 검은색 폰은 2번째 줄에서 시작

        // 직진 이동
        XY frontMove = new XY(pawn.x, pawn.y + direction);

        boolean is_contains=false;

        for(XY al:allies){
            if(al.x==frontMove.x&&al.y==frontMove.y){
                is_contains=true;
                break;
            }
        }
        for(XY en:enemy){
            if(en.x==frontMove.x&&en.y==frontMove.y){
                is_contains=true;
                break;
            }
        }

        if (isValidPoint(frontMove) && !is_contains) {
            moves.add(new XYPlus(frontMove,false));

            // 첫 이동인 경우 한 칸 더 이동 가능
            if (pawn.y == startRow) {
                XY doubleMove = new XY(pawn.x, pawn.y + 2 * direction);

                for(XY al:allies){
                    if(al.x==doubleMove.x&&al.y==doubleMove.y){
                        is_contains=true;
                        break;
                    }
                }
                for(XY en:enemy){
                    if(en.x==frontMove.x&&en.y==frontMove.y){
                        is_contains=true;
                        break;
                    }
                }

                if (!is_contains) {
                    moves.add(new XYPlus(doubleMove,false));
                }
            }
        }

        // 대각선 잡기
        XY[] diagonalMoves = {
                new XY(pawn.x + 1, pawn.y + direction),
                new XY(pawn.x - 1, pawn.y + direction)
        };
        for (XY move : diagonalMoves) {
            is_contains=false;

            for(XY en:enemy){
                if(en.x==move.x&&en.y==move.y){
                    is_contains=true;
                    break;
                }
            }
            if (isValidPoint(move) && is_contains) {
                moves.add(new XYPlus(move,false));
            }
        }

        if(!(ChessGame.game.size()==0)){
            if(ChessGame.game.get(ChessGame.game.size()-1).type.contains("pawn")){
                MoveTo moveTo = ChessGame.game.get(ChessGame.game.size()-1);
                if(is_white){
                    if(moveTo.pre_xy.y-moveTo.now_xy.y==-2){
                        if(pawn.x==moveTo.now_xy.x-1||pawn.x==moveTo.now_xy.x+1){
                            moves.add(new XYPlus(new XY(moveTo.now_xy.x,moveTo.now_xy.y-1),true));
                        }
                    }
                } else {
                    if(moveTo.pre_xy.y-moveTo.now_xy.y==2){
                        if(pawn.x==moveTo.now_xy.x-1||pawn.x==moveTo.now_xy.x+1){
                            moves.add(new XYPlus(new XY(moveTo.now_xy.x,moveTo.now_xy.y+1),true));
                        }
                    }
                }
            }
        }

        List<XYPlus> validMoves = new ArrayList<>();

        if(!no_repeat){
            List<Piece> pieces=is_white? ChessGame.white_pawn:ChessGame.black_pawn;

            XY xy = pawn;

            for (XYPlus move : moves) {
                Piece piece = null;

                for(Piece p:pieces){
                    if(p.xy.equals(xy)){
                        p.xy=move.xy;
                        piece=PieceFunctions.removeEnemyWithPiece(p.xy,is_white);
                        xy=move.xy;
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
                    p.xy=pawn;
                }
            }

            return validMoves;
        } else {
            return moves;
        }
    }

    private static boolean isValidPoint(XY p) {
        return !(p.x <= 0 || p.x > 8 || p.y <= 0 || p.y > 8);
    }
}

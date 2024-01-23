package com.kyubin.chess.functions.move;

import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.functions.move.check.Check;
import com.kyubin.chess.object.ChessGame;
import com.kyubin.chess.object.Piece;
import com.kyubin.chess.object.xy.XY;

import java.util.ArrayList;
import java.util.List;

import static com.kyubin.chess.functions.BasicFunctions.addAll;

public class Rook {
    public static List<XY> rookMoves(XY rook,boolean is_white,boolean no_repeat) {
        List<XY> moves = new ArrayList<>();
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

        // 수직 및 수평 이동 경로 계산
        addMovesInDirection(rook, allies, enemy, moves, 1, 0); // 오른쪽
        addMovesInDirection(rook, allies, enemy, moves, -1, 0); // 왼쪽
        addMovesInDirection(rook, allies, enemy, moves, 0, 1); // 위
        addMovesInDirection(rook, allies, enemy, moves, 0, -1); // 아래

        List<XY> validMoves = new ArrayList<>();

        if(!no_repeat){
            List<Piece> pieces=is_white? ChessGame.white_rook: ChessGame.black_rook;

            XY xy = rook;

            for (XY move : moves) {
                Piece piece=null;

                for(Piece p:pieces){
                    if(p.xy.equals(xy)){
                        p.xy=move;
                        piece= PieceFunctions.removeEnemyWithPiece(p.xy,is_white);
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
                    p.xy=rook;
                }
            }

            return validMoves;
        } else {
            return moves;
        }
    }

    private static void addMovesInDirection(XY rook, List<XY> allies,List<XY> enemy, List<XY> moves, int dx, int dy) {
        int x = rook.x;
        int y = rook.y;

        while (true) {
            x += dx;
            y += dy;

            // 체스판 범위를 벗어나면 중단
            if (x <= 0 || x > 8 || y <= 0 || y > 8) {
                break;
            }

            // 아군 기물이 있는지 확인
            boolean is_piece_this_square=false;

            for(XY piece:allies){
                if(piece.x==x&&piece.y==y){
                    is_piece_this_square=true;
                    break;
                }
            }

            // 적군 기물이 있는지 확인
            for(XY piece:enemy){
                if(piece.x==x&&piece.y==y){
                    is_piece_this_square=true;
                    moves.add(new XY(x, y));
                    break;
                }
            }

            // 아군 또는 적군이 있는 칸이면 while 문 정지
            if(is_piece_this_square){
                break;
            }

            moves.add(new XY(x, y));
        }
    }
}

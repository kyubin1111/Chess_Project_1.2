package com.kyubin.chess.functions.move.analysis.convert;

import com.kyubin.chess.Main;
import com.kyubin.chess.game.MoveTo;
import com.kyubin.chess.object.xy.XY;

import javax.swing.*;

import java.util.Stack;

import static com.kyubin.chess.Main.chessGame;
import static com.kyubin.chess.functions.PieceFunctions.stockFishToXY;
import static com.kyubin.chess.object.ChessGame.*;

public class GetFEN {
    public static void parseFEN(String fen) {
        String[] rows = fen.split(" ")[0].split("/");
        for (int y = 0; y < rows.length; y++) {
            int x = 0;
            for (char c : rows[y].toCharArray()) {
                if (Character.isDigit(c)) {
                    x += Character.getNumericValue(c);
                } else {
                    x++;
                    y++;

                    if (c == 'P') chessGame.Pawn(Main.frame.getContentPane(), true, x,y);
                    else if (c == 'p') chessGame.Pawn(Main.frame.getContentPane(), false, x,y);
                    if (c == 'R') chessGame.Rook(Main.frame.getContentPane(), true, x,y);
                    else if (c == 'r') chessGame.Rook(Main.frame.getContentPane(), false, x,y);
                    if (c == 'N') chessGame.Knight(Main.frame.getContentPane(), true, x,y);
                    else if (c == 'n') chessGame.Knight(Main.frame.getContentPane(), false, x,y);
                    if (c == 'B') chessGame.Bishop(Main.frame.getContentPane(), true, x,y);
                    else if (c == 'b') chessGame.Bishop(Main.frame.getContentPane(), false, x,y);
                    if (c == 'Q') chessGame.Queen(Main.frame.getContentPane(), true, x,y);
                    else if (c == 'q') chessGame.Queen(Main.frame.getContentPane(), false, x,y);
                    if (c == 'K') chessGame.King(Main.frame.getContentPane(), true, x,y);
                    else if (c == 'k') chessGame.King(Main.frame.getContentPane(), false, x,y);

                    x--;
                    y--;

                    x++;
                }
            }
        }

        String[] parts = fen.split(" ");
        white_turn=parts[1].equals("w");

        String castlingRights = parts[2];

        white_is_possible_kingSideCastling=castlingRights.contains("K");
        white_is_possible_queenSideCastling=castlingRights.contains("Q");
        black_is_possible_kingSideCastling=castlingRights.contains("k");
        black_is_possible_queenSideCastling=castlingRights.contains("q");

        if(!parts[3].equals("-")){
            XY pre_enpassant_move=!white_turn?new XY(stockFishToXY(parts[3]).x,stockFishToXY(parts[3]).y-1):
                    new XY(stockFishToXY(parts[3]).x,stockFishToXY(parts[3]).y+1);
            XY now_enpassant_move=!white_turn?new XY(stockFishToXY(parts[3]).x,stockFishToXY(parts[3]).y+1):
                    new XY(stockFishToXY(parts[3]).x,stockFishToXY(parts[3]).y-1);
            game.add(new MoveTo(pre_enpassant_move,now_enpassant_move,"",(white_turn?"black":"white")+"_pawn",false,false,
                    false,new JLabel(),true));
        }

        half_move=new Stack<>();
        half_move.push(Integer.valueOf(parts[4]));
        full_move=new Stack<>();
        full_move.push(Integer.valueOf(parts[5]));
    }
}

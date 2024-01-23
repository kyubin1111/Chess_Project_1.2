package com.kyubin.chess.functions.move.analysis;

import com.kyubin.chess.Main;
import com.kyubin.chess.object.Piece;

import static com.kyubin.chess.object.ChessGame.*;

public class FlippingBoard {
    public static void flipBoard(){
        for(Piece p : white_pawn) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : black_pawn) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : white_rook) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : black_rook) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : white_bishop) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : black_bishop) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : white_knight) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : black_knight) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : white_queen) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);
        for(Piece p : black_queen) p.label.setLocation((7-p.label.getX()/chess_object_size)*chess_object_size,(7-p.label.getY()/chess_object_size)*chess_object_size);

        white_king.label.setLocation((7-white_king.label.getX()/chess_object_size)*chess_object_size
                ,(7-white_king.label.getY()/chess_object_size)*chess_object_size);
        black_king.label.setLocation((7-black_king.label.getX()/chess_object_size)*chess_object_size
                ,(7-black_king.label.getY()/chess_object_size)*chess_object_size);

        Main.is_flip_board=!Main.is_flip_board;
    }
}

package com.kyubin.chess.functions.move.pawn.promotion;

import com.kyubin.chess.Main;
import com.kyubin.chess.functions.BasicFunctions;
import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.object.xy.XY;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.kyubin.chess.object.ChessGame.chess_object_size;

public class Promotion {
    public static PromotionType promotionType=null;

    static int object_grab_count;

    public static void getPromotionType(XY xy,boolean is_white){
        boolean is_flip = false;

        if(is_white){
            if(Main.is_flip_board) is_flip=true;
        } else {
            if(!Main.is_flip_board) is_flip=true;
        }
        JLabel promotion_window;
        if(is_flip){
            promotion_window=BasicFunctions.loadImage((Main.is_flip_board?9-(xy.x+1):(xy.x-1))* chess_object_size,
                    3*chess_object_size, chess_object_size,5*chess_object_size,
                    chess_object_size,5* chess_object_size,"res/promotion_flip.png");
        } else {
            promotion_window=BasicFunctions.loadImage((Main.is_flip_board?9-(xy.x+1):(xy.x-1))* chess_object_size,
                    0, chess_object_size,5* chess_object_size,
                    chess_object_size,5* chess_object_size,"res/promotion.png");
        }

        object_grab_count++;

        Main.frame.getContentPane().add(promotion_window);

        Main.frame.getLayeredPane().add(promotion_window,Integer.valueOf(object_grab_count));

        if(is_white){
            JLabel queen_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/q_w.png");

            object_grab_count++;
            Main.frame.getContentPane().add(queen_label);
            Main.frame.getLayeredPane().add(queen_label,Integer.valueOf(object_grab_count));

            JLabel knight_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y+2):(xy.y)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/n_w.png");

            object_grab_count++;
            Main.frame.getContentPane().add(knight_label);
            Main.frame.getLayeredPane().add(knight_label,Integer.valueOf(object_grab_count));

            JLabel rook_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y+3):(xy.y+1)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.5), (int) (chess_object_size / 1.3),
                    "res/r_w.png");

            object_grab_count++;
            Main.frame.getContentPane().add(rook_label);
            Main.frame.getLayeredPane().add(rook_label,Integer.valueOf(object_grab_count));

            JLabel bishop_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y+4):(xy.y+2)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                    "res/b_w.png");

            object_grab_count++;
            Main.frame.getContentPane().add(bishop_label);
            Main.frame.getLayeredPane().add(bishop_label,Integer.valueOf(object_grab_count));

            JLabel cancel_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y+5):(xy.y+3)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                    "res/cancel.png");

            object_grab_count++;
            Main.frame.getContentPane().add(cancel_label);
            Main.frame.getLayeredPane().add(cancel_label,Integer.valueOf(object_grab_count));

            queen_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.QUEEN;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            knight_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.KNIGHT;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            rook_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.ROOK;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            bishop_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.BISHOP;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            cancel_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.NONE;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
        } else {
            JLabel queen_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/q_b.png");

            object_grab_count++;
            Main.frame.getContentPane().add(queen_label);
            Main.frame.getLayeredPane().add(queen_label,Integer.valueOf(object_grab_count));

            JLabel knight_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y):(xy.y-2)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/n_b.png");

            object_grab_count++;
            Main.frame.getContentPane().add(knight_label);
            Main.frame.getLayeredPane().add(knight_label,Integer.valueOf(object_grab_count));

            JLabel rook_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y-1):(xy.y-3)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.5), (int) (chess_object_size / 1.3),
                    "res/r_b.png");

            object_grab_count++;
            Main.frame.getContentPane().add(rook_label);
            Main.frame.getLayeredPane().add(rook_label,Integer.valueOf(object_grab_count));

            JLabel bishop_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y-2):(xy.y-4)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                    "res/b_b.png");

            object_grab_count++;
            Main.frame.getContentPane().add(bishop_label);
            Main.frame.getLayeredPane().add(bishop_label,Integer.valueOf(object_grab_count));

            JLabel cancel_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                    chess_object_size * (Main.is_flip_board?9-(xy.y-3):(xy.y-5)),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                    "res/cancel.png");

            object_grab_count++;
            Main.frame.getContentPane().add(cancel_label);
            Main.frame.getLayeredPane().add(cancel_label,Integer.valueOf(object_grab_count));

            queen_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.QUEEN;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            knight_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.KNIGHT;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            rook_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.ROOK;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            bishop_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.BISHOP;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
            cancel_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    promotionType=PromotionType.NONE;
                    promotion_window.setVisible(false);
                    queen_label.setVisible(false);
                    knight_label.setVisible(false);
                    rook_label.setVisible(false);
                    bishop_label.setVisible(false);
                    cancel_label.setVisible(false);
                    PieceFunctions.is_promotion=false;
                }
            });
        }
    }
}

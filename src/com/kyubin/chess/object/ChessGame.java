package com.kyubin.chess.object;

import com.kyubin.chess.functions.BasicFunctions;
import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.game.MoveTo;
import com.kyubin.chess.object.xy.XY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ChessGame {
    // 기물 백 리스트 생성
    public static List<Piece> white_rook = new ArrayList<>();
    public static List<Piece> white_knight = new ArrayList<>();
    public static List<Piece> white_bishop = new ArrayList<>();
    public static List<Piece> white_queen = new ArrayList<>();
    public static List<Piece> white_pawn = new ArrayList<>();
    public static Piece white_king;

    // 기물 흑 리스트 생성
    public static List<Piece> black_rook = new ArrayList<>();
    public static List<Piece> black_knight = new ArrayList<>();
    public static List<Piece> black_bishop = new ArrayList<>();
    public static List<Piece> black_queen = new ArrayList<>();
    public static List<Piece> black_pawn = new ArrayList<>();
    public static Piece black_king;

    // 게임 기보 저장
    public static List<MoveTo> game = new ArrayList<>();

    // 게임 위치 저장
    public static List<String> fen_game = new ArrayList<>();
    public static List<String> fen_game_no_half_move = new ArrayList<>();

    // 프레임과 체스 기물 크기 받기
    public JFrame frame;
    public static int chess_object_size;

    // 백 차례인지 흑 차례인지 확인
    public static boolean white_turn=true;

    public static boolean white_is_possible_kingSideCastling=true;
    public static boolean white_is_possible_queenSideCastling=true;
    public static boolean black_is_possible_kingSideCastling=true;
    public static boolean black_is_possible_queenSideCastling=true;

    public static Stack<Integer> half_move=new Stack<>();
    public static Stack<Integer> full_move=new Stack<>();

    // 생성자
    public ChessGame(JFrame frame, int chess_object_size){
        half_move.push(0);
        full_move.push(1);

        this.frame=frame;
        ChessGame.chess_object_size=chess_object_size;
    }

    // 룩 불러오기
    public void Rook(Container container,boolean white, int x, int y){
        JLabel rook_label;
        if(white){
            //백 룩 이미지 불러오기
            rook_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.5), (int) (chess_object_size / 1.3),
                    "res/r_w.png");

            rook_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(rook_label,"white_rook",chess_object_size);
                }
            });

            white_rook.add(new Piece(new XY(x,y),rook_label,"white_rook"));
        } else {
            //흑 룩 이미지 불러오기
            rook_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.5), (int) (chess_object_size / 1.3),
                    "res/r_b.png");

            rook_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(rook_label,"black_rook",chess_object_size);
                }
            });

            black_rook.add(new Piece(new XY(x,y),rook_label,"black_rook"));
        }
        container.add(rook_label);
    }

    // 나이트 불러오기
    public void Knight(Container container,boolean white, int x, int y){
        JLabel knight_label;
        if(white){
            //백 나이트 이미지 불러오기
            knight_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/n_w.png");

            knight_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(knight_label,"white_knight",chess_object_size);
                }
            });

            white_knight.add(new Piece(new XY(x,y),knight_label,"white_knight"));
        } else {
            //흑 나이트 이미지 불러오기
            knight_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/n_b.png");

            knight_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(knight_label,"black_knight",chess_object_size);
                }
            });

            black_knight.add(new Piece(new XY(x,y),knight_label,"black_knight"));
        }
        container.add(knight_label);
    }

    // 비숍 불러오기
    public void Bishop(Container container,boolean white, int x, int y){
        JLabel bishop_label;
        if(white){
            //백 비숍 이미지 불러오기
            bishop_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                    "res/b_w.png");

            bishop_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(bishop_label,"white_bishop",chess_object_size);
                }
            });

            white_bishop.add(new Piece(new XY(x,y),bishop_label,"white_bishop"));
        } else {
            //흑 비숍 이미지 불러오기
            bishop_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                    "res/b_b.png");

            bishop_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(bishop_label,"black_bishop",chess_object_size);
                }
            });

            black_bishop.add(new Piece(new XY(x,y),bishop_label,"black_bishop"));
        }
        container.add(bishop_label);
    }

    // 퀸 불러오기
    public void Queen(Container container,boolean white, int x, int y){
        JLabel queen_label;
        if(white){
            //백 퀸 이미지 불러오기
            queen_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/q_w.png");

            queen_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(queen_label,"white_queen",chess_object_size);
                }
            });

            white_queen.add(new Piece(new XY(x,y),queen_label,"white_queen"));
        } else {
            //흑 퀸 이미지 불러오기
            queen_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/q_b.png");

            queen_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(queen_label,"black_queen",chess_object_size);
                }
            });

            black_queen.add(new Piece(new XY(x,y),queen_label,"black_queen"));
        }
        container.add(queen_label);
    }

    // 폰 불러오기
    public void Pawn(Container container,boolean white, int x, int y){
        JLabel pawn_label;
        if(white){
            //백 폰 이미지 불러오기
            pawn_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.6), (int) (chess_object_size / 1.4),
                    "res/p_w.png");

            pawn_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(pawn_label,"white_pawn",chess_object_size);
                }
            });

            white_pawn.add(new Piece(new XY(x,y),pawn_label,"white_pawn"));
        } else {
            //흑 폰 이미지 불러오기
            pawn_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.6), (int) (chess_object_size / 1.4),
                    "res/p_b.png");

            pawn_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(pawn_label,"black_pawn",chess_object_size);
                }
            });

            black_pawn.add(new Piece(new XY(x,y),pawn_label,"black_pawn"));
        }
        container.add(pawn_label);
    }

    // 킹 불러오기
    public void King(Container container,boolean white, int x, int y){
        JLabel king_label;
        if(white){
            //백 폰 이미지 불러오기
            king_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/k_w.png");

            king_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(king_label,"white_king",chess_object_size);
                }
            });

            white_king=new Piece(new XY(x,y),king_label,"white_king");
        } else {
            //흑 폰 이미지 불러오기
            king_label = BasicFunctions.loadImage(chess_object_size * (x - 1), chess_object_size * (y - 1),
                    chess_object_size, chess_object_size,
                    (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                    "res/k_b.png");

            king_label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PieceFunctions.grab(king_label,"black_king",chess_object_size);
                }
            });

            black_king=new Piece(new XY(x,y),king_label,"black_king");
        }
        container.add(king_label);
    }
}

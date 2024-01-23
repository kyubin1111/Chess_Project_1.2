package com.kyubin.chess;

import com.kyubin.chess.frames.FlipBoardFrame;
import com.kyubin.chess.frames.UndoFrame;
import com.kyubin.chess.frames.chessengine.EngineType;
import com.kyubin.chess.functions.move.analysis.convert.GetFEN;
import com.kyubin.chess.frames.chessengine.stockfish.StockFishEngine;
import com.kyubin.chess.functions.BasicFunctions;
import com.kyubin.chess.object.ChessGame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static int size=500;
    public static int stockfish_x_size=300;
    public static int stockfish_y_size=400;
    public static int undo_x_size=100;
    public static int undo_y_size=100;
    public static int flipboard_x_size=100;
    public static int flipboard_y_size=100;
    public static boolean stockfish_mode=true;
    public static boolean is_playWithChessEngine=true;
    public static int depth=13;
    public static int skillLevel=20;//0 ~ 20
    public static int analysis_chessengine_depth=20;
    public static boolean analysis_chessengine_is_depth_inf=true;
    public static boolean analysis_chessengine_is_easy_to_see=true;
    public static boolean is_flip_board=false;
    public static boolean is_startPos=true;
    public static String FEN="r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/2PP1N2/PP3PPP/RNBQK2R b KQkq - 0 5";
    public static EngineType engine_type=EngineType.STOCKFISH;

    public static String stockfish_file="res/uci_chess_engine/stockfish/stockfish.exe";

    // 한 칸의 크기 계산
    public static int square_size=(int) Math.round(size/7.39);

    public static JFrame frame = new JFrame("Chess");

    public static ChessGame chessGame = new ChessGame(frame,square_size);

    public static JTextPane jl;

    public static void main(String[] args) throws IOException, InterruptedException {
        if(!stockfish_mode){
            stockfish_x_size=0;
            stockfish_y_size=0;
        }

        Chess();
    }

    public static void StockFishFrame() throws IOException {
        JFrame jFrame = new JFrame("Stockfish Choice");
        jFrame.setSize(stockfish_x_size, stockfish_y_size);
        jFrame.setLocation(size+100,0);
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jl = new JTextPane(); // JTextPane 초기화

        JScrollPane scrollPane = new JScrollPane(jl); // JScrollPane에 JTextPane 추가
        jFrame.getContentPane().add(scrollPane); // 프레임에 JScrollPane 추가

        jFrame.setVisible(true);

        StockFishEngine.getStockFishChoice(false);
    }

    public static void Chess() {
        new FlipBoardFrame(flipboard_x_size,flipboard_y_size);
        new UndoFrame(undo_x_size,undo_y_size);

        // 프레임 세팅
        frame.setSize(square_size*8+16,square_size*8+36);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        if(is_startPos){
            chessGame.Rook(Main.frame.getContentPane(),true,1,8);
            chessGame.Rook(Main.frame.getContentPane(),true,8,8);
            chessGame.Rook(Main.frame.getContentPane(),false,1,1);
            chessGame.Rook(Main.frame.getContentPane(),false,8,1);

            chessGame.Knight(Main.frame.getContentPane(),true,2,8);
            chessGame.Knight(Main.frame.getContentPane(),true,7,8);
            chessGame.Knight(Main.frame.getContentPane(),false,2,1);
            chessGame.Knight(Main.frame.getContentPane(),false,7,1);

            chessGame.Bishop(Main.frame.getContentPane(),true,3,8);
            chessGame.Bishop(Main.frame.getContentPane(),true,6,8);
            chessGame.Bishop(Main.frame.getContentPane(),false,3,1);
            chessGame.Bishop(Main.frame.getContentPane(),false,6,1);

            chessGame.Queen(Main.frame.getContentPane(),true,4,8);
            chessGame.Queen(Main.frame.getContentPane(),false,4,1);

            chessGame.Pawn(Main.frame.getContentPane(), true, 1,7);
            chessGame.Pawn(Main.frame.getContentPane(), true, 2,7);
            chessGame.Pawn(Main.frame.getContentPane(), true, 3,7);
            chessGame.Pawn(Main.frame.getContentPane(), true, 4,7);
            chessGame.Pawn(Main.frame.getContentPane(), true, 5,7);
            chessGame.Pawn(Main.frame.getContentPane(), true, 6,7);
            chessGame.Pawn(Main.frame.getContentPane(), true, 7,7);
            chessGame.Pawn(Main.frame.getContentPane(), true, 8,7);
            chessGame.Pawn(Main.frame.getContentPane(), false, 1,2);
            chessGame.Pawn(Main.frame.getContentPane(), false, 2,2);
            chessGame.Pawn(Main.frame.getContentPane(), false, 3,2);
            chessGame.Pawn(Main.frame.getContentPane(), false, 4,2);
            chessGame.Pawn(Main.frame.getContentPane(), false, 5,2);
            chessGame.Pawn(Main.frame.getContentPane(), false, 6,2);
            chessGame.Pawn(Main.frame.getContentPane(), false, 7,2);
            chessGame.Pawn(Main.frame.getContentPane(), false, 8,2);

            chessGame.King(Main.frame.getContentPane(), true, 5,8);
            chessGame.King(Main.frame.getContentPane(), false, 5,1);
        } else {
            GetFEN.parseFEN(FEN);
        }

        // 체스 보드 불러오기
        BasicFunctions.loadImage(0,0,square_size*8,square_size*8,square_size*8,square_size*8,"res/chess_board.png");

        frame.setVisible(true);

        if(stockfish_mode){
            new Thread(()->{
                try {
                    StockFishFrame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
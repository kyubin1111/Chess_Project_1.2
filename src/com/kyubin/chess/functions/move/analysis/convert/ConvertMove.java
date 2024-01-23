package com.kyubin.chess.functions.move.analysis.convert;

import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.game.MoveTo;
import com.kyubin.chess.object.ChessGame;
import com.kyubin.chess.object.xy.XY;

import java.io.*;

import static com.kyubin.chess.Main.FEN;
import static com.kyubin.chess.Main.is_startPos;
import static com.kyubin.chess.functions.PieceFunctions.xyToStockFish;
import static com.kyubin.chess.object.ChessGame.*;

public class ConvertMove {
    public static String toFENWithNoHalfMove() {
        StringBuilder fen = new StringBuilder();

        for (int y = 1; y <= 8; y++) {
            int emptyCount = 0;
            for (int x = 1; x <= 8; x++) {
                String white_piece = PieceFunctions.isPieceInSquareWithStringWithWhite(new XY(x,y),true);
                String black_piece = PieceFunctions.isPieceInSquareWithStringWithWhite(new XY(x,y),false);

                char piece='1';

                if(!white_piece.equals("")&&black_piece.equals("")) {
                    piece=white_piece.toUpperCase().charAt(6);
                    if(white_piece.contains("knight")) piece='N';
                }
                if(white_piece.equals("")&!black_piece.equals("")){
                    piece=black_piece.charAt(6);
                    if(black_piece.contains("knight")) piece='n';
                }

                if (piece == '1') {
                    emptyCount++;
                } else {
                    if (emptyCount != 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece);
                }
            }

            if (emptyCount != 0) {
                fen.append(emptyCount);
            }
            if (y < 8) {
                fen.append('/');
            }
        }

        fen.append(" ").append(white_turn?"w":"b");

        fen.append(" ")
                .append(white_is_possible_kingSideCastling ? "K" : "")
                .append(white_is_possible_queenSideCastling ? "Q" : "")
                .append(black_is_possible_kingSideCastling ? "k" : "")
                .append(black_is_possible_queenSideCastling ? "q" : "");

        if(!white_is_possible_kingSideCastling&&!white_is_possible_queenSideCastling&&
                !black_is_possible_kingSideCastling&&!black_is_possible_queenSideCastling) fen.append("-");

        return fen.toString();
    }

    public static String toFEN() {
        StringBuilder fen = new StringBuilder();

        for (int y = 1; y <= 8; y++) {
            int emptyCount = 0;
            for (int x = 1; x <= 8; x++) {
                String white_piece = PieceFunctions.isPieceInSquareWithStringWithWhite(new XY(x,y),true);
                String black_piece = PieceFunctions.isPieceInSquareWithStringWithWhite(new XY(x,y),false);

                char piece='1';

                if(!white_piece.equals("")&&black_piece.equals("")) {
                    piece=white_piece.toUpperCase().charAt(6);
                    if(white_piece.contains("knight")) piece='N';
                }
                if(white_piece.equals("")&!black_piece.equals("")){
                    piece=black_piece.charAt(6);
                    if(black_piece.contains("knight")) piece='n';
                }

                if (piece == '1') {
                    emptyCount++;
                } else {
                    if (emptyCount != 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece);
                }
            }

            if (emptyCount != 0) {
                fen.append(emptyCount);
            }
            if (y < 8) {
                fen.append('/');
            }
        }

        fen.append(" ").append(white_turn?"w":"b");

        fen.append(" ")
                .append(white_is_possible_kingSideCastling ? "K" : "")
                .append(white_is_possible_queenSideCastling ? "Q" : "")
                .append(black_is_possible_kingSideCastling ? "k" : "")
                .append(black_is_possible_queenSideCastling ? "q" : "");

        if(!white_is_possible_kingSideCastling&&!white_is_possible_queenSideCastling&&
        !black_is_possible_kingSideCastling&&!black_is_possible_queenSideCastling) fen.append("-");

        boolean pawn_moved=false;

        if(!(ChessGame.game.size()==0)){
            if(ChessGame.game.get(ChessGame.game.size()-1).type.contains("pawn")){
                MoveTo moveTo = ChessGame.game.get(ChessGame.game.size()-1);
                if(white_turn){
                    if(moveTo.pre_xy.y-moveTo.now_xy.y==-2){
                        fen.append(" ").append(xyToStockFish(new XY(moveTo.now_xy.x,moveTo.now_xy.y-1)));
                        pawn_moved=true;
                    }
                } else {
                    if(moveTo.pre_xy.y-moveTo.now_xy.y==2){
                        fen.append(" ").append(xyToStockFish(new XY(moveTo.now_xy.x,moveTo.now_xy.y+1)));
                        pawn_moved=true;
                    }
                }
            }
        }

        if(!pawn_moved){
            fen.append(" -");
        }

        fen.append(" ").append(half_move.peek());
        fen.append(" ").append(full_move.peek());

        return fen.toString();
    }

    public static String convertStockFishMove(String premoves, String moves) throws InterruptedException, IOException {
        // 파이썬 스크립트 파일 경로
        String pythonScriptPath = "res/convertpy/convert.py";

        // ProcessBuilder를 사용하여 파이썬 스크립트 실행
        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
        Process process = processBuilder.start();

        // 파이썬 스크립트의 출력을 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        // 파이썬 스크립트로 데이터 전달
        writer.write(premoves+"\n");
        writer.write(moves+"\n");
        if(!is_startPos) {
            writer.write("t\n");
            writer.write(FEN+"\n");
        } else {
            writer.write("f\n");
        }
        writer.flush();
        writer.close();

        String line;
        String result="";
        while ((line = reader.readLine()) != null) {
            result=line;
        }

        process.waitFor();

        return result;
    }
}

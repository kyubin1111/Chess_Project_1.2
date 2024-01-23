package com.kyubin.chess.functions.move.analysis;

import com.kyubin.chess.Main;
import com.kyubin.chess.functions.move.analysis.convert.ConvertMove;
import com.kyubin.chess.frames.chessengine.stockfish.StockFishEngine;
import com.kyubin.chess.functions.BasicFunctions;
import com.kyubin.chess.functions.GameFunctions;
import com.kyubin.chess.functions.PieceFunctions;
import com.kyubin.chess.functions.move.pawn.promotion.PromotionType;
import com.kyubin.chess.game.MoveTo;
import com.kyubin.chess.object.ChessGame;
import com.kyubin.chess.object.Piece;
import com.kyubin.chess.object.xy.XY;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static com.kyubin.chess.functions.move.analysis.convert.ConvertMove.toFEN;
import static com.kyubin.chess.functions.PieceFunctions.*;
import static com.kyubin.chess.object.ChessGame.*;

public class Move {
    public static void MovePiece(boolean is_white, XY xy, XY xy_now, boolean is_king_side_castling, boolean is_queen_side_castling, boolean is_en_passant
    ,String object_type,JLabel object,boolean stockFishMode) {
        //이전 기물 삭제
        removePiece(xy,object_type);

        // 만약 갈 칸에 적군이 있다면 없에기
        String enemy_type=removeEnemy(xy_now,is_white);

        if(object_type.contains("pawn")||!enemy_type.equals("")){
            half_move.push(0);
        } else {
            half_move.push(half_move.peek()+1);
        }

        if(is_king_side_castling){
            if(is_white){
                for(Piece p : ChessGame.white_rook){
                    if(p.xy.x==8&&p.xy.y==8){
                        p.xy.x=6;
                        if(Main.is_flip_board) p.label.setLocation(2*chess_object_size,0);
                        else p.label.setLocation(5*chess_object_size,7*chess_object_size);
                    }
                }
            } else {
                for(Piece p : ChessGame.black_rook){
                    if(p.xy.x==8&&p.xy.y==1){
                        p.xy.x=6;
                        if(Main.is_flip_board) p.label.setLocation(2*chess_object_size,7*chess_object_size);
                        else p.label.setLocation(5*chess_object_size,0);
                    }
                }
            }
        }
        if(is_queen_side_castling){
            if(is_white){
                for(Piece p : ChessGame.white_rook){
                    if(p.xy.x==1&&p.xy.y==8){
                        p.xy.x=4;
                        if(Main.is_flip_board) p.label.setLocation(5*chess_object_size,0);
                        else p.label.setLocation(3*chess_object_size,7*chess_object_size);
                    }
                }
            } else {
                for(Piece p : ChessGame.black_rook){
                    if(p.xy.x==1&&p.xy.y==1){
                        p.xy.x=4;
                        if(Main.is_flip_board) p.label.setLocation(5*chess_object_size,7*chess_object_size);
                        else p.label.setLocation(3*chess_object_size,0);
                    }
                }
            }
        }

        if(is_en_passant){
            if(is_white){
                removeEnemy(new XY(xy_now.x,xy_now.y+1),true);
            } else {
                removeEnemy(new XY(xy_now.x,xy_now.y-1),false);
            }
        }

        ChessGame.game.add(new MoveTo(xy,xy_now,enemy_type,object_type,is_king_side_castling,is_queen_side_castling,is_en_passant,object));

        object.setLocation((!Main.is_flip_board?xy_now.x-1:9-(xy_now.x+1))*chess_object_size,(!Main.is_flip_board?xy_now.y-1:9-(xy_now.y+1))*chess_object_size);

        if(object_type.equals("white_rook")) ChessGame.white_rook.add(new Piece(xy_now,object,"white_rook"));
        if(object_type.equals("black_rook")) ChessGame.black_rook.add(new Piece(xy_now,object,"black_rook"));
        if(object_type.equals("white_knight")) ChessGame.white_knight.add(new Piece(xy_now,object,"white_knight"));
        if(object_type.equals("black_knight")) ChessGame.black_knight.add(new Piece(xy_now,object,"black_knight"));
        if(object_type.equals("white_bishop")) ChessGame.white_bishop.add(new Piece(xy_now,object,"white_bishop"));
        if(object_type.equals("black_bishop")) ChessGame.black_bishop.add(new Piece(xy_now,object,"black_bishop"));
        if(object_type.equals("white_queen")) white_queen.add(new Piece(xy_now,object,"white_queen"));
        if(object_type.equals("black_queen")) black_queen.add(new Piece(xy_now,object,"black_queen"));
        if(object_type.equals("white_pawn")) ChessGame.white_pawn.add(new Piece(xy_now,object,"white_pawn"));
        if(object_type.equals("black_pawn")) ChessGame.black_pawn.add(new Piece(xy_now,object,"black_pawn"));
        if(object_type.equals("white_king")) ChessGame.white_king=new Piece(xy_now,object,"white_king");
        if(object_type.equals("black_king")) ChessGame.black_king=new Piece(xy_now,object,"black_king");

        if(is_white) ChessGame.white_turn=false;
        if(!is_white) ChessGame.white_turn=true;

        if(!is_white) full_move.push(full_move.peek()+1);
        else full_move.push(full_move.peek());

        fen_game.add(toFEN());
        fen_game_no_half_move.add(ConvertMove.toFENWithNoHalfMove());

        StockFishEngine.game=StockFishEngine.game+" "+xyToStockFish(xy)+xyToStockFish(xy_now);

        try {
            if(Main.stockfish_mode&&stockFishMode) StockFishEngine.getStockFishChoice(is_white);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void UndoPiece(boolean is_stockFishMode){
        MoveTo move = game.get(game.size()-1);

        if(move.no_able_undo) return;

        fen_game.remove(fen_game.size()-1);
        fen_game_no_half_move.remove(fen_game_no_half_move.size()-1);

        half_move.pop();
        full_move.pop();

        XY xy = move.pre_xy;
        XY xy_now = move.now_xy;
        boolean is_king_side_castling=move.is_ks;
        boolean is_queen_side_castling=move.is_qs;
        boolean is_en_passant=move.is_ep;
        String object_type=move.type;
        JLabel object=move.object;
        String enemy_type=move.enemy_type;

        boolean is_white = !white_turn;

        removeEnemy(xy_now,is_white);
        removeEnemy(xy_now,!is_white);

        removePiece(xy,object_type);
        removePiece(xy_now,object_type);

        // 만약 갈 칸에 적군이 있다면 추가
        addPiece(enemy_type,xy_now);

        if(is_king_side_castling){
            if(is_white){
                for(Piece p : ChessGame.white_rook){
                    if(p.xy.x==6&&p.xy.y==8){
                        p.xy.x=8;
                        if(Main.is_flip_board) p.label.setLocation(0,0);
                        else p.label.setLocation(7*chess_object_size,7*chess_object_size);
                    }
                }
            } else {
                for(Piece p : ChessGame.black_rook){
                    if(p.xy.x==6&&p.xy.y==1){
                        p.xy.x=8;
                        if(Main.is_flip_board) p.label.setLocation(0,7*chess_object_size);
                        else p.label.setLocation(7*chess_object_size,0);
                    }
                }
            }
        }
        if(is_queen_side_castling){
            if(is_white){
                for(Piece p : ChessGame.white_rook){
                    if(p.xy.x==4&&p.xy.y==8){
                        p.xy.x=1;
                        if(Main.is_flip_board) p.label.setLocation(7*chess_object_size,0);
                        else p.label.setLocation(0,7*chess_object_size);
                    }
                }
            } else {
                for(Piece p : ChessGame.black_rook){
                    if(p.xy.x==4&&p.xy.y==1){
                        p.xy.x=1;
                        if(Main.is_flip_board) p.label.setLocation(7*chess_object_size,7*chess_object_size);
                        else p.label.setLocation(0,0);
                    }
                }
            }
        }

        if(is_en_passant){
            if(is_white){
                addPiece("black_pawn",new XY(xy_now.x,xy_now.y+1));
            } else {
                addPiece("white_pawn",new XY(xy_now.x,xy_now.y-1));
            }
        }

        game.remove(game.size()-1);

        object.setLocation((!Main.is_flip_board?xy.x-1:9-(xy.x+1))*chess_object_size,(!Main.is_flip_board?xy.y-1:9-(xy.y+1))*chess_object_size);

        if(object_type.equals("white_rook")) ChessGame.white_rook.add(new Piece(xy,object,"white_rook"));
        if(object_type.equals("black_rook")) ChessGame.black_rook.add(new Piece(xy,object,"black_rook"));
        if(object_type.equals("white_knight")) ChessGame.white_knight.add(new Piece(xy,object,"white_knight"));
        if(object_type.equals("black_knight")) ChessGame.black_knight.add(new Piece(xy,object,"black_knight"));
        if(object_type.equals("white_bishop")) ChessGame.white_bishop.add(new Piece(xy,object,"white_bishop"));
        if(object_type.equals("black_bishop")) ChessGame.black_bishop.add(new Piece(xy,object,"black_bishop"));
        if(object_type.equals("white_queen")) white_queen.add(new Piece(xy,object,"white_queen"));
        if(object_type.equals("black_queen")) black_queen.add(new Piece(xy,object,"black_queen"));
        if(object_type.equals("white_pawn")) ChessGame.white_pawn.add(new Piece(xy,object,"white_pawn"));
        if(object_type.equals("black_pawn")) ChessGame.black_pawn.add(new Piece(xy,object,"black_pawn"));
        if(object_type.equals("white_king")) ChessGame.white_king=new Piece(xy,object,"white_king");
        if(object_type.equals("black_king")) ChessGame.black_king=new Piece(xy,object,"black_king");

        if(!is_white) ChessGame.white_turn=false;
        if(is_white) ChessGame.white_turn=true;

        new Thread(()->{
            StockFishEngine.game=GameFunctions.removeRight(StockFishEngine.game);

            try {
                if(Main.stockfish_mode&&is_stockFishMode) StockFishEngine.getStockFishChoice(!is_white);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void addPiece(String object_type,XY xy){
        boolean white=object_type.contains("white");

        if(object_type.contains("queen")){
            JLabel queen_label;
            if(white){
                //백 퀸 이미지 불러오기
                queen_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                        "res/q_w.png");

                queen_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(queen_label,"white_queen",chess_object_size);
                    }
                });

                white_queen.add(new Piece(xy,queen_label,"white_queen"));
            } else {
                //흑 퀸 이미지 불러오기
                queen_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                        "res/q_b.png");

                queen_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(queen_label,"black_queen",chess_object_size);
                    }
                });

                black_queen.add(new Piece(xy,queen_label,"black_queen"));
            }
            Main.frame.getContentPane().add(queen_label);
            Main.frame.getContentPane().setComponentZOrder(queen_label,0);
        }
        if(object_type.contains("bishop")){
            JLabel bishop_label;
            if(white){
                //백 비숍 이미지 불러오기
                bishop_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                        "res/b_w.png");

                bishop_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(bishop_label,"white_bishop",chess_object_size);
                    }
                });

                white_bishop.add(new Piece(xy,bishop_label,"white_bishop"));
            } else {
                //흑 비숍 이미지 불러오기
                bishop_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.3),
                        "res/b_b.png");

                bishop_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(bishop_label,"black_bishop",chess_object_size);
                    }
                });

                black_bishop.add(new Piece(xy,bishop_label,"black_bishop"));
            }
            Main.frame.getContentPane().add(bishop_label);
            Main.frame.getContentPane().setComponentZOrder(bishop_label,0);
        }
        if(object_type.contains("knight")){
            JLabel knight_label;
            if(white){
                //백 나이트 이미지 불러오기
                knight_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                        "res/n_w.png");

                knight_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(knight_label,"white_knight",chess_object_size);
                    }
                });

                white_knight.add(new Piece(xy,knight_label,"white_knight"));
            } else {
                //흑 나이트 이미지 불러오기
                knight_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.4), (int) (chess_object_size / 1.4),
                        "res/n_b.png");

                knight_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(knight_label,"black_knight",chess_object_size);
                    }
                });

                black_knight.add(new Piece(xy,knight_label,"black_knight"));
            }
            Main.frame.getContentPane().add(knight_label);
            Main.frame.getContentPane().setComponentZOrder(knight_label,0);
        }
        if(object_type.contains("rook")){
            JLabel rook_label;
            if(white){
                //백 룩 이미지 불러오기
                rook_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.5), (int) (chess_object_size / 1.3),
                        "res/r_w.png");

                rook_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(rook_label,"white_rook",chess_object_size);
                    }
                });

                white_rook.add(new Piece(xy,rook_label,"white_rook"));
            } else {
                //흑 룩 이미지 불러오기
                rook_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.5), (int) (chess_object_size / 1.3),
                        "res/r_b.png");

                rook_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(rook_label,"black_rook",chess_object_size);
                    }
                });

                black_rook.add(new Piece(xy,rook_label,"black_rook"));
            }
            Main.frame.getContentPane().add(rook_label);
            Main.frame.getContentPane().setComponentZOrder(rook_label,0);
        }
        if(object_type.contains("pawn")){
            JLabel pawn_label;
            if(white){
                //백 폰 이미지 불러오기
                pawn_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.6), (int) (chess_object_size / 1.4),
                        "res/p_w.png");

                pawn_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(pawn_label,"white_pawn",chess_object_size);
                    }
                });

                white_pawn.add(new Piece(xy,pawn_label,"white_pawn"));
            } else {
                //흑 폰 이미지 불러오기
                pawn_label = BasicFunctions.loadImage(chess_object_size * (Main.is_flip_board?9-(xy.x+1):(xy.x-1)),
                        chess_object_size * (Main.is_flip_board?9-(xy.y+1):(xy.y-1)),
                        chess_object_size, chess_object_size,
                        (int) (chess_object_size / 1.6), (int) (chess_object_size / 1.4),
                        "res/p_b.png");

                pawn_label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PieceFunctions.grab(pawn_label,"black_pawn",chess_object_size);
                    }
                });

                black_pawn.add(new Piece(xy,pawn_label,"black_pawn"));
            }
            Main.frame.getContentPane().add(pawn_label);
            Main.frame.getContentPane().setComponentZOrder(pawn_label,0);
        }
    }

    public static void MovePromotionPiece(boolean is_white, XY xy, XY xy_now, PromotionType get_promotion_type, JLabel object,boolean is_stockFish_mode){
        // 만약 갈 칸에 적군이 있다면 없에기
        if(!is_white) full_move.push(full_move.peek()+1);
        else full_move.push(full_move.peek());

        String enemy_type = PieceFunctions.isPieceInSquareWithStringWithWhite(xy_now,!is_white);

        removeEnemy(xy_now,is_white);

        game.add(new MoveTo(xy,xy_now,enemy_type,is_white?"white_pawn":"black_pawn",false,false,false,object));

        removePiece(xy,is_white?"white_pawn":"black_pawn");

        if(is_white) ChessGame.white_turn=false;
        if(!is_white) ChessGame.white_turn=true;

        object.setLocation(-100,-100);

        fen_game.add(toFEN());
        fen_game_no_half_move.add(ConvertMove.toFENWithNoHalfMove());

        half_move.push(0);

        StockFishEngine.game=StockFishEngine.game+" "+xyToStockFish(xy)+
                xyToStockFish(xy_now)+String.valueOf(get_promotion_type.toString().charAt(0)).toLowerCase();

        try {
            if(Main.stockfish_mode&&is_stockFish_mode) StockFishEngine.getStockFishChoice(is_white);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

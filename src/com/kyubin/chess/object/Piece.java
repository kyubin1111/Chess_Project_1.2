package com.kyubin.chess.object;

import com.kyubin.chess.object.xy.XY;

import javax.swing.*;

public class Piece {
    public XY xy;
    public JLabel label;
    public String type;

    public Piece(XY xy, JLabel label,String type){
        this.xy=xy;
        this.label=label;
        this.type=type;
    }
}

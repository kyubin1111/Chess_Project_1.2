package com.kyubin.chess.game;

import com.kyubin.chess.object.xy.XY;

import javax.swing.*;

public class MoveTo {
    public XY pre_xy;
    public XY now_xy;
    public String enemy_type;
    public String type;
    public JLabel object;
    public boolean is_ks;
    public boolean is_qs;
    public boolean is_ep;
    public boolean no_able_undo=false;

    public MoveTo(XY pre_xy, XY now_xy, String enemy_type, String type, boolean is_ks, boolean is_qs, boolean is_ep, JLabel object) {
        this.pre_xy = pre_xy;
        this.now_xy = now_xy;
        this.enemy_type = enemy_type;
        this.type = type;
        this.is_ks = is_ks;
        this.is_qs = is_qs;
        this.is_ep = is_ep;
        this.object = object;
    }

    public MoveTo(XY pre_xy, XY now_xy, String enemy_type, String type, boolean is_ks, boolean is_qs, boolean is_ep, JLabel object,boolean no_able_undo) {
        this.pre_xy = pre_xy;
        this.now_xy = now_xy;
        this.enemy_type = enemy_type;
        this.type = type;
        this.is_ks = is_ks;
        this.is_qs = is_qs;
        this.is_ep = is_ep;
        this.object = object;
        this.no_able_undo = no_able_undo;
    }
}

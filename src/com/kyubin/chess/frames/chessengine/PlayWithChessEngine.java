package com.kyubin.chess.frames.chessengine;

import com.kyubin.chess.frames.chessengine.stockfish.StockFishEngine;

import java.io.IOException;

public class PlayWithChessEngine {
    int skillLevel;
    int depth;
    EngineType engine_type;

    public PlayWithChessEngine(int skillLevel, int depth, EngineType engine_type) {
        this.skillLevel = skillLevel;
        this.depth = depth;
        this.engine_type = engine_type;
    }

    public String getMove() throws IOException {
        if(engine_type==EngineType.STOCKFISH) {
            return StockFishEngine.getStockFishChoiceWithLevelAndDepth(skillLevel,depth);
        } else if(engine_type==EngineType.TORCH){
            //TODO 나중에 토치 추가하기
            return "";
        } else if(engine_type==EngineType.KOMODO){
            //TODO 나중에 코모도 드래곤 추가하기
            return "";
        }

        return "";
    }
}

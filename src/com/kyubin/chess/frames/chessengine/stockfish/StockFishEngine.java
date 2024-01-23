package com.kyubin.chess.frames.chessengine.stockfish;

import com.kyubin.chess.Main;
import com.kyubin.chess.functions.move.analysis.convert.ConvertMove;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kyubin.chess.Main.*;

public class StockFishEngine {
    public static String game="";
    public static final AtomicReference<Process> runningProcess = new AtomicReference<>();

    public static String getStockFishChoiceWithLevelAndDepth(int skillLevel, int depth) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(Main.stockfish_file);
        Process stockfishProcess = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
             OutputStreamWriter writer = new OutputStreamWriter(stockfishProcess.getOutputStream())) {

            // UCI 모드 설정
            sendCommand(writer, "uci");
            waitFor("uciok", reader);

            // 새 게임 시작
            sendCommand(writer, "ucinewgame");
            sendCommand(writer, "isready");
            waitFor("readyok", reader);

            // 포지션 설정
            if(is_startPos) sendCommand(writer, "position startpos moves"+game);
            else sendCommand(writer, "position fen "+FEN+" moves"+game);

            sendCommand(writer,"setoption name Skill Level value "+skillLevel);

            // 평가 점수 얻기
            sendCommand(writer, "go depth "+depth);

            try {
                return readBestMoveOnly(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException ignored) {
        }

        return "";
    }

    public static void getStockFishChoice(boolean is_white) throws IOException {
        new Thread(()->{
            ProcessBuilder processBuilder = new ProcessBuilder(Main.stockfish_file);
            Process stockfishProcess;
            try {
                stockfishProcess = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 현재 실행 중인 프로세스를 runningProcess에 저장
            Process existingProcess = runningProcess.getAndSet(stockfishProcess);
            if (existingProcess != null) {
                existingProcess.destroyForcibly();
                try {
                    existingProcess.waitFor();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
                 OutputStreamWriter writer = new OutputStreamWriter(stockfishProcess.getOutputStream())) {

                // UCI 모드 설정
                sendCommand(writer, "uci");
                waitFor("uciok", reader);

                // 새 게임 시작
                sendCommand(writer, "ucinewgame");
                sendCommand(writer, "isready");
                waitFor("readyok", reader);

                // 포지션 설정
                if(is_startPos) sendCommand(writer, "position startpos moves"+game);
                else sendCommand(writer, "position fen "+FEN+" moves"+game);

                // 평가 점수 얻기
                if(analysis_chessengine_is_depth_inf){
                    sendCommand(writer, "go infinite");
                } else {
                    sendCommand(writer, "go depth "+analysis_chessengine_depth);
                }

                try {
                    readCPAndMateOnly(reader, is_white);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException ignored) {
            } finally {
                runningProcess.compareAndSet(stockfishProcess, null);
            }
        }).start();
    }

    public static void sendCommand(OutputStreamWriter writer, String command) throws IOException {
        try {
            writer.write(command + "\n");
            writer.flush();
        } catch (IOException ignored){}
    }

    public static void waitFor(String token, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(token)) {
                break;
            }
        }
    }

    public static String readBestMoveOnly(BufferedReader reader) throws IOException {
        String line;
        String result="";
        while ((line = reader.readLine()) != null) {
            if (line.split(" ")[0].equals("bestmove")) {
                result=line.split(" ")[1];
                break;
            }
        }
        return result;
    }

    public static void readCPAndMateOnly(BufferedReader reader, boolean is_white) throws IOException, InterruptedException {
        List<String> pvs = new ArrayList<>();
        Pattern cpPattern = Pattern.compile("depth (\\d+) .* score cp ([-\\d]+) .* pv (.+)");
        Pattern matePattern2 = Pattern.compile("score mate ([-\\d]+) .* pv (.+)");
        int depth;
        int line_depth=-1;
        int lowestMateMoves = -1;
        if(is_white){
            Integer highestCPScore = null;
            String highestCPScoreLine=null;
            String line;

            while ((line = reader.readLine()) != null) {
                Matcher cpMatcher = cpPattern.matcher(line);
                Matcher mate2Matcher = matePattern2.matcher(line);
                if (mate2Matcher.find()) {
                    int currentMate = Integer.parseInt(mate2Matcher.group(1));
                    String currentPV = mate2Matcher.group(2);

                    if(lowestMateMoves<currentMate){
                        lowestMateMoves=currentMate;
                        highestCPScoreLine=currentPV;

                        if(!analysis_chessengine_is_easy_to_see){
                            jl.setText(jl.getText()+"cp(-mate in "+currentMate+") depth("+currentMate+") : "+ConvertMove.convertStockFishMove(game,currentPV)+"\n");
                        } else {
                            jl.setText(
                                    "highest cp score: " + "-mate in " + lowestMateMoves+"\n"+
                                            "highest cp line:" + ConvertMove.convertStockFishMove(game,highestCPScoreLine)+"\n"+
                                            "highest cp line depth: " + lowestMateMoves+"\n"+
                                            "bestmove: " + ConvertMove.convertStockFishMove(game,highestCPScoreLine.split(" ")[0]));
                        }
                    }
                }
                if (cpMatcher.find()) {
                    depth = Integer.parseInt(cpMatcher.group(1));
                    int currentCPScore = Integer.parseInt(cpMatcher.group(2));
                    String currentPV = cpMatcher.group(3);

                    if(!pvs.contains(currentPV)){
                        if(!analysis_chessengine_is_easy_to_see){
                            jl.setText(jl.getText()+"cp("+-currentCPScore+") depth("+depth+") : "+ConvertMove.convertStockFishMove(game,currentPV)+"\n");
                        }

                        if (highestCPScore == null || currentCPScore > highestCPScore || line_depth < depth) {
                            line_depth=depth;
                            highestCPScore=currentCPScore; // 가장 높은 cp 점수를 저장
                            highestCPScoreLine=currentPV;

                            if(analysis_chessengine_is_easy_to_see){
                                jl.setText(
                                        "highest cp score: " + currentCPScore+"\n"+
                                                "highest cp line:" +ConvertMove.convertStockFishMove(game,highestCPScoreLine)+"\n"+
                                                "highest cp line depth: " + line_depth+"\n"+
                                                "bestmove: " + ConvertMove.convertStockFishMove(game,highestCPScoreLine.split(" ")[0]));
                            }
                        }
                    }

                    pvs.add(currentPV);
                }

                if (line.startsWith("bestmove")) {
                    if(lowestMateMoves!=-1){
                        jl.setText(
                                "highest cp score: " + "-mate in " + lowestMateMoves+"\n"+
                                        "highest cp line:" + highestCPScoreLine+"\n"+
                                        "highest cp line depth: " + lowestMateMoves+"\n");

                        break;
                    }

                    if (highestCPScore != null) {
                        jl.setText(
                                "highest cp score: " + highestCPScore+"\n"+
                                        "highest cp line:" + highestCPScoreLine+"\n"+
                                        "highest cp line depth: " + line_depth+"\n");
                    }
                    break; // bestmove 명령에 도달하면 루프 종료
                }
            }
        } else {
            Integer lowestCPScore = null;
            String lowestCPScoreLine=null;
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher cpMatcher = cpPattern.matcher(line);
                Matcher mate2Matcher = matePattern2.matcher(line);

                if (mate2Matcher.find()) {
                    int currentMate = Integer.parseInt(mate2Matcher.group(1));
                    String currentPV = mate2Matcher.group(2);

                    if(lowestMateMoves<currentMate){
                        lowestMateMoves=currentMate;
                        lowestCPScoreLine=currentPV;

                        if(!analysis_chessengine_is_easy_to_see){
                            jl.setText(jl.getText()+"cp(mate in "+currentMate+") depth("+currentMate+") : "+ConvertMove.convertStockFishMove(game,currentPV)+"\n");
                        } else {
                            jl.setText(
                                    "highest cp score: " + "mate in " + lowestMateMoves+"\n"+
                                            "highest cp line:" +ConvertMove.convertStockFishMove(game,lowestCPScoreLine)+"\n"+
                                            "highest cp line depth: " + lowestMateMoves+"\n"+
                                            "bestmove: " + ConvertMove.convertStockFishMove(game,lowestCPScoreLine.split(" ")[0]));
                        }
                    }
                }
                if (cpMatcher.find()) {
                    depth = Integer.parseInt(cpMatcher.group(1));
                    int currentCPScore = Integer.parseInt(cpMatcher.group(2));
                    String currentPV = cpMatcher.group(3);

                    if(!pvs.contains(currentPV)) {
                        if(!analysis_chessengine_is_easy_to_see){
                            jl.setText(jl.getText()+"cp("+currentCPScore+") depth("+depth+") : "+ConvertMove.convertStockFishMove(game,currentPV)+"\n");
                        }

                        if (lowestCPScore == null || currentCPScore < lowestCPScore || line_depth < depth) {
                            line_depth=depth;
                            lowestCPScore=currentCPScore; // 가장 낮은 cp 점수를 저장
                            lowestCPScoreLine=currentPV;

                            if(analysis_chessengine_is_easy_to_see){
                                jl.setText(
                                        "highest cp score: " + currentCPScore+"\n"+
                                                "highest cp line:" + ConvertMove.convertStockFishMove(game,lowestCPScoreLine)+"\n"+
                                                "highest cp line depth: " + line_depth+"\n"+
                                                "bestmove: " + ConvertMove.convertStockFishMove(game,lowestCPScoreLine.split(" ")[0]));
                            }
                        }
                    }

                    pvs.add(currentPV);
                }

                if (line.startsWith("bestmove")) {
                    if(lowestMateMoves!=-1){
                        jl.setText(
                                "highest cp score: " + "mate in " + lowestMateMoves+"\n"+
                                        "highest cp line:" + lowestCPScoreLine+"\n"+
                                        "highest cp line depth: " + lowestMateMoves+"\n");

                        break;
                    }

                    if (lowestCPScore != null) {
                        jl.setText(
                                "highest cp score: " + lowestCPScore+"\n"+
                                        "highest cp line:" + lowestCPScoreLine+"\n"+
                                        "highest cp line depth: " + line_depth+"\n");
                    }
                    break; // bestmove 명령에 도달하면 루프 종료
                }
            }
        }
    }
}

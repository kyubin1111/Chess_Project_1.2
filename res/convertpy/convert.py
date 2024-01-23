import chess

def lan_to_san_fixed(push_moves,lan_moves,f):
    board = chess.Board()
    san_moves = []

    if f!="":
        board=chess.Board(f)

    words = push_moves.split()
    
    for push_move in words:
        move = chess.Move.from_uci(push_move)
        board.push(move)

    for lan_move in lan_moves:
        # UCI 형식으로 변환 (예: e2e4 -> e2e4, g1f3 -> g1f3)
        move = chess.Move.from_uci(lan_move)

        san_moves.append(board.san(move))
        
        # 움직임 적용
        board.push(move)

    return san_moves

# 예시 LAN 움직임 (e7e8q를 g1f3로 변경)
user_input = input()
user_input2 = input()
user_input3 = input().lower()
fen=""

if user_input3 in ["t","y"]:
    boolean_value = True
elif user_input3 in ["f", "n"]:
    boolean_value = False
if(boolean_value):
    fen_input=input()
    fen=fen_input
lan_moves_example = user_input2.split()
converted_moves = lan_to_san_fixed(user_input,lan_moves_example,fen)

for i in converted_moves:
    print(i,end=" ")
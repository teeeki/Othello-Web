package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ゲーム関連の機能を提供するコントローラ
 * ロビー画面表示、ルーム作成、ルーム参加などの機能を担当します
 */
@Controller
public class GameController {

    /**
     * アクティブなルームの情報を管理するマップ
     * キー: ルームID、値: ホストユーザーID
     * 
     * 注意: 実際のアプリケーションでは、この情報は永続化するかより適切な方法で管理すべきです
     * 現在はサーバーメモリ上に保持しているため、サーバー再起動で失われます
     */
    private Map<String, String> activeRooms = new HashMap<>();

    /**
     * ロビー画面を表示
     * ログインしていない場合はログインページにリダイレクトします
     * 
     * @param session ユーザーセッション（ログイン状態の確認用）
     * @param model   ビューに渡すデータを格納するモデル
     * @return ロビー画面のビュー名、またはログインページへのリダイレクト
     */
    @GetMapping("/lobby")
    public String lobby(HttpSession session, Model model) {
        // セッションからユーザーIDを取得（ログイン状態の確認）
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            // 未ログインの場合はログインページへリダイレクト
            return "redirect:/login";
        }

        // ユーザーIDをモデルに追加してビューに渡す
        model.addAttribute("userId", userId);
        return "game/lobby"; // lobby.htmlテンプレートを表示
    }

    /**
     * ルームを作成するAPI
     * 6桁のランダムなルームIDを生成します
     * 
     * @param session ユーザーセッション（ホストユーザーの識別用）
     * @return ルームID情報を含むJSONレスポンス
     */
    @PostMapping("/create-room")
    @ResponseBody // 戻り値がそのままレスポンスボディになることを示す
    public Map<String, String> createRoom(HttpSession session) {
        // セッションからホストユーザーのIDを取得
        String userId = (String) session.getAttribute("userId");

        // 6桁のランダムなルームIDを生成
        String roomId = generateRoomId();

        // ルーム情報をメモリ上に保存
        activeRooms.put(roomId, userId);

        // レスポンスとしてルームIDを返す
        Map<String, String> response = new HashMap<>();
        response.put("roomId", roomId);
        return response;
    }

    /**
     * ルームに参加するAPI
     * 指定されたルームIDが有効か確認し、参加可能か判断します
     * 
     * @param roomId  参加するルームのID
     * @param session ユーザーセッション（参加ユーザーの識別用）
     * @return 参加結果とルーム情報を含むJSONレスポンス
     */
    @PostMapping("/join-room")
    @ResponseBody
    public Map<String, Object> joinRoom(@RequestParam String roomId, HttpSession session) {
        // セッションからユーザーIDを取得
        String userId = (String) session.getAttribute("userId");
        Map<String, Object> response = new HashMap<>();

        // ルームIDの存在確認
        if (!activeRooms.containsKey(roomId)) {
            // 指定されたルームが存在しない場合はエラーレスポンス
            response.put("success", false);
            response.put("message", "指定されたルームが見つかりません");
            return response;
        }

        // ホストユーザーIDを取得
        String hostUserId = activeRooms.get(roomId);

        // 自分自身が作成したルームには参加できないようにチェック
        if (hostUserId.equals(userId)) {
            response.put("success", false);
            response.put("message", "自分が作成したルームには参加できません");
            return response;
        }

        // ルーム参加成功レスポンス
        response.put("success", true);
        response.put("roomId", roomId);
        response.put("hostUserId", hostUserId);

        return response;
    }

    /**
     * 6桁のランダムなルームIDを生成するヘルパーメソッド
     * 
     * @return 6桁の数字からなるルームID文字列
     */
    private String generateRoomId() {
        Random random = new Random();
        // 100000〜999999の範囲でランダムな数値を生成（6桁の数字）
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}
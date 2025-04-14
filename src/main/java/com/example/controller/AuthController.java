package com.example.controller;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

/**
 * 認証関連のリクエストを処理するコントローラ
 * ログイン、新規登録、ログアウトなどの機能を提供します
 */
@Controller // コントローラクラスであることを示すアノテーション
public class AuthController {

    // ユーザーサービスの依存関係
    private final UserService userService;

    /**
     * コンストラクタインジェクション
     * 
     * @param userService ユーザー関連のビジネスロジックを提供するサービス
     */
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ルートURLへのアクセスをログインページにリダイレクト
     * 
     * @return ログインページへのリダイレクト
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login"; // ログインページへリダイレクト
    }

    /**
     * ログインページを表示
     * 
     * @return ログインページのビュー名
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // login.htmlテンプレートを表示
    }

    /**
     * 新規登録ページを表示
     * 
     * @return 新規登録ページのビュー名
     */
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register"; // register.htmlテンプレートを表示
    }

    /**
     * ユーザー登録処理
     * POSTリクエストで送信されたユーザー情報を処理します
     * 
     * @param userId   登録するユーザーID
     * @param password 登録するパスワード
     * @param model    ビューに渡すデータを格納するモデル
     * @return 登録成功時はログインページへリダイレクト、失敗時は登録ページを再表示
     */
    @PostMapping("/register")
    public String register(@RequestParam String userId,
            @RequestParam String password,
            Model model) {

        // 入力値の基本的な検証
        if (userId.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "ユーザーIDとパスワードは必須です");
            return "auth/register"; // エラーメッセージと共に登録ページを再表示
        }

        // ユーザー登録の実行
        boolean success = userService.registerUser(userId, password);

        if (success) {
            // 登録成功：登録完了パラメータ付きでログインページへリダイレクト
            return "redirect:/login?registered";
        } else {
            // 登録失敗：ユーザーID重複エラー
            model.addAttribute("error", "そのユーザーIDはすでに使用されています");
            return "auth/register"; // エラーメッセージと共に登録ページを再表示
        }
    }

    /**
     * ログイン処理
     * POSTリクエストで送信された認証情報を処理します
     * 
     * @param userId   ログインするユーザーID
     * @param password 入力されたパスワード
     * @param session  HTTPセッション（ログイン状態の保存用）
     * @param model    ビューに渡すデータを格納するモデル
     * @return 認証成功時はロビーページへリダイレクト、失敗時はログインページを再表示
     */
    @PostMapping("/login")
    public String login(@RequestParam String userId,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        // ユーザー認証の実行
        boolean isValid = userService.validateUser(userId, password);

        if (isValid) {
            // 認証成功：セッションにユーザーIDを保存してロビーページへリダイレクト
            session.setAttribute("userId", userId);
            return "redirect:/lobby";
        } else {
            // 認証失敗：エラーメッセージを表示
            model.addAttribute("error", "ユーザーIDまたはパスワードが正しくありません");
            return "auth/login"; // エラーメッセージと共にログインページを再表示
        }
    }

    /**
     * ログアウト処理
     * セッションを無効化してログインページに戻ります
     * 
     * @param session 無効化するHTTPセッション
     * @return ログインページへのリダイレクト
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // セッションを無効化（ログアウト処理）
        return "redirect:/login"; // ログインページへリダイレクト
    }
}
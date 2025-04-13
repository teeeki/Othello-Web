package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * ユーザー関連のビジネスロジックを実装するサービスクラス
 * ユーザーの登録、認証などの機能を提供します
 */
@Service // サービスクラスであることを示すアノテーション
public class UserService {

    // 依存するコンポーネント
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * コンストラクタインジェクション
     * 依存するコンポーネントを自動的に注入します
     * 
     * @param userRepository  ユーザーデータアクセス用リポジトリ
     * @param passwordEncoder パスワードハッシュ化用エンコーダ
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 新規ユーザーを登録します
     * 
     * @param userId   登録するユーザーID
     * @param password 登録するパスワード（平文）
     * @return 登録成功時はtrue、ユーザーIDが既に使用されている場合はfalse
     */
    public boolean registerUser(String userId, String password) {
        // ユーザーIDの重複チェック
        if (userRepository.existsByUserId(userId)) {
            return false; // 既に存在するユーザーID
        }

        // 新規ユーザーエンティティの作成
        User user = new User();
        user.setUserId(userId);

        // パスワードをハッシュ化して保存
        // BCryptによりソルトが自動的に生成されるため、同じパスワードでも異なるハッシュ値になります
        user.setPassword(passwordEncoder.encode(password));

        // ユーザー情報を保存
        userRepository.save(user);
        return true; // 登録成功
    }

    /**
     * ユーザーIDからユーザー情報を検索します
     * 
     * @param userId 検索するユーザーID
     * @return 見つかったユーザー情報、見つからない場合はnull
     */
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * ユーザー認証を行います
     * 
     * @param userId   認証するユーザーID
     * @param password 入力されたパスワード（平文）
     * @return 認証成功時はtrue、ユーザーが存在しないまたはパスワードが一致しない場合はfalse
     */
    public boolean validateUser(String userId, String password) {
        // ユーザーIDからユーザー情報を取得
        User user = userRepository.findByUserId(userId);

        // ユーザーが存在しない場合は認証失敗
        if (user == null) {
            return false;
        }

        // パスワードの一致を確認
        // BCryptEncoderのmatchesメソッドにより、平文パスワードとハッシュ値の比較を行います
        return passwordEncoder.matches(password, user.getPassword());
    }
}
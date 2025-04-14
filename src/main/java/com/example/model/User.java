package com.example.model;

import jakarta.persistence.*;

/**
 * ユーザー情報を表すエンティティクラス
 * データベースの'users'テーブルと対応します
 */
@Entity // JPA エンティティであることを示すアノテーション
@Table(name = "users") // マッピングするテーブル名を指定
public class User {

    /**
     * ユーザーの内部ID（自動生成）
     * データベース内での主キーとして使用されます
     */
    @Id // 主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動採番（AUTO INCREMENT）
    private Long id;

    /**
     * ユーザーID（ログイン時に使用）
     * システム内で一意である必要があります
     */
    @Column(unique = true, nullable = false) // 一意制約とNOT NULL制約を設定
    private String userId;

    /**
     * ユーザーのパスワード（ハッシュ化して保存）
     */
    @Column(nullable = false) // NOT NULL制約を設定
    private String password;

    // ゲッターとセッター
    /**
     * 内部IDを取得します
     * 
     * @return 内部ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 内部IDを設定します
     * 
     * @param id 設定する内部ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * ユーザーIDを取得します
     * 
     * @return ユーザーID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定します
     * 
     * @param userId 設定するユーザーID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * パスワード（ハッシュ値）を取得します
     * 
     * @return ハッシュ化されたパスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定します
     * 注意: この段階ではハッシュ化されていないため、
     * 実際に保存する前にサービス層でハッシュ化する必要があります
     * 
     * @param password 設定するパスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ユーザー情報のデータアクセスを担当するリポジトリインターフェース
 * Spring Data JPAによって自動的に実装が生成されます
 */
@Repository // リポジトリであることを示すアノテーション
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * ユーザーIDからユーザー情報を検索します
     * メソッド名からSpring Data JPAが自動的にクエリを生成します
     * 
     * @param userId 検索するユーザーID
     * @return 見つかったユーザー情報、見つからない場合はnull
     */
    User findByUserId(String userId);

    /**
     * 指定されたユーザーIDが既に存在するかをチェックします
     * 
     * @param userId チェックするユーザーID
     * @return 存在する場合はtrue、存在しない場合はfalse
     */
    boolean existsByUserId(String userId);
}
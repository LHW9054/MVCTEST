# MVCTEST

JSP / Servlet (MVC) + PostgreSQL で作ったショッピングサイトの学習用プロジェクトです。

## 実行準備
1. `src/main/java/db.properties.example` を `db.properties` にコピーし、DB アカウントを入力します（Git には含めません）。
2. パスワードは平文ではなく **PBKDF2 ハッシュ** で保存します（約 83 文字）。`userpw` カラムは 100 文字以上にしてください。
   ```sql
   ALTER TABLE user_table ALTER COLUMN userpw TYPE VARCHAR(100);
   ```
   以前に平文で保存されたアカウントは、次回ログイン成功時に自動でハッシュに変換されます。

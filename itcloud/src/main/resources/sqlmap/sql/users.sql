/* PostgreSQL 사용 */

/* auto_increment 대신 시퀀스 테이블을 새로 생성하여 적용 */
create sequence user_num;


CREATE TABLE IF NOT EXISTS users (
    no int PRIMARY KEY NOT null default nextval('user_num'),
    id VARCHAR(30) NOT NULL,
    password VARCHAR(200) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(50),
    administrative BOOLEAN,
    login_count INT,
    block_time VARCHAR(14)
);


INSERT INTO users (no, id, password, administrative, name, email, login_count, block_time)
SELECT 1, 'admin', 'admin!123', true, 'administrator', 'admin@office.com', 0, '' as test
WHERE NOT EXISTS (SELECT no, id, password, administrative, name, email, login_count, block_time FROM users WHERE no = 1);

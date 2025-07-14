-- 建立資料庫指令
/*
DROP DATABASE IF EXISTS finalProj;
CREATE DATABASE finalProj;
*/

Use finalProj;
/*
DROP TABLE IF EXISTS units;
DROP TABLE IF EXISTS community;
DROP TABLE if exists units_users;
DROP TABLE if exists accounts;
DROP TABLE if exists users;
DROP TABLE if exists roles;
*/

------------------------------------------------------------------------------------
-- 建立community
create table community(
    id	INT PRIMARY KEY IDENTITY(1,1),				--社區流水號	
    name	VARCHAR(max),							--社區名稱	
    address	VARCHAR(max),							--地址	
    create_time	 DATETIME DEFAULT GETDATE(),		--創建時間	
    [function] Bigint								--使用功能	
)
INSERT INTO community (name, address,[function])
VALUES 
(N'台中社區一號', N'台中市北區中清路100號',15),
(N'高雄社區二號', N'高雄市三民區建國一路200號',15),
(N'新北社區三號', N'新北市板橋區文化路300號',15),
(N'台南社區四號', N'台南市東區林森路400號',15),
(N'桃園社區五號', N'桃園市中壢區中山東路500號',15);
------------------------------------------------------------------------------------
CREATE TABLE roles
(
    roles_id INT PRIMARY KEY IDENTITY(1,1),
    description NVARCHAR(100) NOT NULL,
    note NVARCHAR(255),
    community_id INT,
    -- 外鍵欄位

    FOREIGN KEY (community_id) REFERENCES community(id)
);

-- Insert into roles
INSERT INTO roles (description, note, community_id) VALUES
(N'住戶', N'一般住戶', 1), -- 住戶為1
-- (N'管理員', N'社區管理員', 1),  -- 或管委會
(N'保全', N'巡邏及收件', 1), -- 保全為2
(N'訪客', N'臨時進入者', 1); -- 訪客為3
------------------------------------------------------------------------------------
CREATE TABLE users
(
    users_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    gender NVARCHAR(10),
    contact_info NVARCHAR(255),
    emergency_contact_name NVARCHAR(100),
    emergency_contact_phone NVARCHAR(50),
    emergency_contact_relation NVARCHAR(50),
    line_id NVARCHAR(100),
    state NVARCHAR(50),
    create_at DATETIME DEFAULT GETDATE(),
    last_alter_at DATETIME NOT NULL,
    -- ast_alter_at 是否not null?
    photo NVARCHAR(500),  -- 儲存圖片 URL
    email NVARCHAR(100)UNIQUE,
    -- 唯一索引
    community_id INT,
    -- 外鍵欄位
	password NVARCHAR(100) NOT NULL default 'P@ssw0rd',
    states INT DEFAULT 0,
    login_fail_times INT DEFAULT 0,
    last_failed_login DATETIME,
    account_locked_until DATETIME,

    FOREIGN KEY (community_id) REFERENCES community(id)
);

-- Insert into users
INSERT INTO users (name, gender, contact_info, emergency_contact_name, emergency_contact_phone, emergency_contact_relation, line_id, state, last_alter_at, photo, email, community_id)
VALUES
(N'張大明', N'男', N'0912345678', N'張媽媽', N'0987654321', N'母親', N'damingline', N'啟用', GETDATE(), N'https://example.com/photos/u1.jpg', N'daming@example.com', 1),
(N'陳小芳', N'女', N'0922333444', N'陳爸爸', N'0977555666', N'父親', N'xiaofangline', N'啟用', GETDATE(), N'https://example.com/photos/u2.jpg', N'fang@example.com', 1),
(N'李曉華', N'女', NULL, NULL, NULL, NULL, NULL, N'停用', '2025-06-23', NULL, N'xiao@example.com', 1),
(N'王保全', N'男', N'02-87654321', N'保全公司', N'02-12345678', N'保全公司', NULL, N'啟用', GETDATE(), N'https://example.com/photos/u4.jpg', N'safe@example.com', 1),
(N'林育正', N'男', N'0911222333', N'林太太', N'0933444555', N'配偶', N'yuzhengline', N'啟用', GETDATE(), N'https://example.com/photos/u5.jpg', N'yuzheng@example.com', 1),
(N'蔡佩真', N'女', N'0933666777', N'蔡媽媽', N'0966888999', N'母親', N'peizhenline', N'啟用', GETDATE(), N'https://example.com/photos/u6.jpg', N'peizhen@example.com', 1),
(N'洪文昌', N'男', N'0922111222', N'洪太太', N'0911999888', N'配偶', N'wenchangline', N'啟用', GETDATE(), N'https://example.com/photos/u7.jpg', N'wenchang@example.com', 1),
(N'鄭欣怡', N'女', N'0955666777', N'鄭爸爸', N'0977888999', N'父親', N'xinyi_line', N'啟用', GETDATE(), N'https://example.com/photos/u8.jpg', N'xinyi@example.com', 1);

------------------------------------------------------------------------------------
-- 建立units
CREATE TABLE units
(
    units_id INT PRIMARY KEY IDENTITY(1,1),
    unit NVARCHAR(10) NOT NULL,
    floor NVARCHAR(10) NOT NULL,
    building NVARCHAR(10),
    ping DECIMAL(6, 2) NOT NULL,
    point INT DEFAULT 0,
    community_id INT,

	CONSTRAINT UQ_unit_floor UNIQUE (unit, floor),
    FOREIGN KEY (community_id) REFERENCES community(id)
);

-- Insert into units
INSERT INTO units (unit, floor, building, ping, point, community_id)
VALUES
(N'10', N'3F', N'A棟', 35.5, 10, 1),
(N'10', N'5F', N'B棟', 42.0, 20, 1),
(N'12', N'1F', N'C棟', 28.7, 0, 1),
(N'14', N'5F', N'D棟', 42.0, 20, 1),
(N'16', N'1F', N'E棟', 28.7, 0, 1),
(N'18', N'2F', N'F棟', 33.3, 5, 1),
(N'20', N'6F', N'G棟', 38.8, 15, 1),
(N'22', N'7F', N'H棟', 40.1, 30, 1);
------------------------------------------------------------------------------------

CREATE TABLE units_users
(
    unitsusers_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    unit_id INT NOT NULL,
    community_id INT,

    FOREIGN KEY (user_id) REFERENCES users(users_id),

    FOREIGN KEY (unit_id) REFERENCES units(units_id),

    FOREIGN KEY (community_id) REFERENCES community(id)
);

-- Insert into units_users
INSERT INTO units_users (user_id, unit_id, community_id)
VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 1),
(7, 7, 1),
(8, 8, 1);

------------------------------------------------------------------------------------



/*
DROP TABLE IF EXISTS [point_transactions];
DROP TABLE IF EXISTS [point_accounts];
DROP TABLE IF EXISTS [facility_reservations];
DROP TABLE IF EXISTS [facility_images];
DROP TABLE IF EXISTS [facilities];
*/


-- 公共設施表
CREATE TABLE facilities (
	facility_id INT IDENTITY(1,1) PRIMARY KEY,           -- 公設ID，自動遞增
	community_id INT NOT NULL,                           -- 社區識別欄位
    facility_name NVARCHAR(255) NOT NULL,                -- 公設名稱
	max_users INT,                                       -- 使用人數上限
    facility_description NVARCHAR(255),                  -- 公設描述
    facility_location NVARCHAR(255),                     -- 公設地點
    open_time TIME NOT NULL,                             -- 開始營運時間
    close_time TIME NOT NULL,                            -- 結束營運時間
    reservable_duration INT,                             -- 單次可預約時長（單位：分鐘）
    fee INT DEFAULT 0 NOT NULL,                          -- 每次預約費用（單位：點數或元）
	facility_status NVARCHAR(255) DEFAULT 'active',      -- 使用狀態：啟用、停用、維修中等    
    created_at DATETIME DEFAULT GETDATE(),               -- 建立時間
    updated_at DATETIME                                  -- 最後更新時間
	FOREIGN KEY (community_id) REFERENCES community(id),
);

-- 公共設施圖片
CREATE TABLE facility_images (
    image_id INT IDENTITY(1,1) PRIMARY KEY,           -- 照片主鍵
    facility_id INT NOT NULL,                         -- 關聯公設
    image_url NVARCHAR(255),                          -- 照片使用url儲存
    image_description NVARCHAR(255),                  -- 照片說明（可選）
    sort_order INT DEFAULT 0,                         -- 照片排序值（小的排前面）
    created_at DATETIME DEFAULT GETDATE(),            -- 建立時間
	updated_at DATETIME                               -- 最後更新時間
    FOREIGN KEY (facility_id) REFERENCES facilities(facility_id)
);

-- 公設預約表
CREATE TABLE facility_reservations (
    reservation_id INT IDENTITY(1,1) PRIMARY KEY,          -- 預約紀錄 ID    
	community_id INT NOT NULL,                             -- 社區識別欄位
    unit_id INT NOT NULL,                                  -- 預約者所屬住戶單位 ID    
    facility_id INT NOT NULL,                              -- 預約的設施 ID    
    number_of_users INT,                                   -- 使用人數
    reservation_start_time DATETIME NOT NULL,              -- 預約起始時間
    reservation_end_time DATETIME NOT NULL,                -- 預約結束時間    
    is_admin BIT NOT NULL DEFAULT 0,                       -- 是否為公用
    required_points INT NOT NULL,                          -- 預約所需點數
    actual_used_points INT NOT NULL,                       -- 實際扣除點數
	point_expire_at DATETIME,                              -- 紀錄扣除該筆點數時的過期日期
    remark NVARCHAR(255),                                  -- 備註
    reservation_status NVARCHAR(255) DEFAULT 'APPROVED',   -- 狀態（APPROVED、CANCELLED）
    created_at DATETIME DEFAULT GETDATE(),                 -- 建立時間
    updated_at DATETIME,                                   -- 最後修改時間   
    canceled_at DATETIME,                                  -- 取消時間（可為 NULL）
    cancel_reason NVARCHAR(255),                           -- 取消原因    
	FOREIGN KEY (community_id) REFERENCES community(id),
    FOREIGN KEY (unit_id) REFERENCES units(units_id),
    FOREIGN KEY (facility_id) REFERENCES facilities(facility_id),
);


-- 住戶點數帳戶表 (point_accounts)，記錄每戶當前可用點數
CREATE TABLE point_accounts (
    account_id INT IDENTITY(1,1) PRIMARY KEY,        -- 帳戶編號
    community_id INT,                                -- 所屬社區
    unit_id INT NOT NULL,                            -- 所屬住戶單位
    total_balance INT DEFAULT 0,                     -- 總可用點數
	expired_at DATETIME NOT NULL,                    -- 到期時間
	is_active BIT DEFAULT 1,                         -- 紀錄用戶是否被刪除
    updated_at DATETIME DEFAULT GETDATE(),           -- 最後更新時間    
    FOREIGN KEY (community_id) REFERENCES community(id),
    FOREIGN KEY (unit_id) REFERENCES units(units_id),
	CONSTRAINT UQ_unit_community UNIQUE (unit_id, community_id)
);


-- 點數交易紀錄表 (point_transactions)
CREATE TABLE point_transactions (
    transaction_id INT IDENTITY(1,1) PRIMARY KEY,      -- 交易 ID
    community_id INT NOT NULL,                         -- 所屬社區
    unit_id INT NOT NULL,                              -- 當事住戶單位    
    transaction_type NVARCHAR(255) NOT NULL,           -- reservation / cancel / topup / transfer_in / transfer_out...
    amount INT NOT NULL,                               -- 正為加點，負為扣點
    related_unit_id INT NULL,                          -- 轉移點數時的對象住戶
	related_reservation_id INT NULL,                   -- 扣除點數時的預約id
    transaction_description NVARCHAR(255),             -- 備註說明
    created_at DATETIME DEFAULT GETDATE(),             -- 建立時間
	FOREIGN KEY (community_id) REFERENCES community(id),    
    FOREIGN KEY (related_reservation_id) REFERENCES facility_reservations(reservation_id),
	FOREIGN KEY (unit_id) REFERENCES units(units_id)
);


--TRUNCATE TABLE facilities;
INSERT INTO facilities (
    community_id, facility_name, max_users, facility_description, facility_location,
    open_time, close_time, reservable_duration, fee, facility_status, created_at
) VALUES
(1, N'交誼廳', NULL, N'可供住戶聚會、會議或活動使用', N'A棟1樓', '09:00', '21:00', 120, 15, 'active', GETDATE()),
(1, N'健身房', 10, N'配有跑步機、啞鈴、飛輪等設備', N'B棟地下1樓', '06:00', '22:00', 120, 10, 'active', GETDATE()),
(1, N'游泳池', 30, N'僅限住戶使用，請著泳裝並遵守規定', N'戶外區', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'桌球室B101', NULL, N'提供標準雙人桌球場地，限時使用', N'B1-B101', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'桌球室B102', NULL, N'提供標準雙人桌球場地，限時使用', N'B1-B102', '09:00', '21:00', 120, 10, 'active', GETDATE()),
(1, N'撞球室', NULL, N'提供撞球桌與器材，需預約使用', N'B2撞球室', '09:00', '21:00', 60, 120, 'active', GETDATE()),
(1, N'停車格A1', NULL, N'地下停車場 A1 車位，可短時預約停車', N'地下1樓-A1', '00:00', '23:59', 120, 5, 'active', GETDATE()),
(1, N'停車格A2', NULL, N'地下停車場 A2 車位，可短時預約停車', N'地下1樓-A2', '00:00', '23:59', 120, 5, 'active', GETDATE());


--TRUNCATE TABLE facility_images;
INSERT INTO facility_images (facility_id, image_url, image_description, sort_order)
VALUES 
(1, '/images/facilities/lounge.png', N'交誼廳外觀', 0),
(2, '/images/facilities/gym.png', N'健身房設備照', 0),
(3, '/images/facilities/pool.png', N'游泳池環境', 0),
(4, '/images/facilities/tabletennis1.png', N'桌球室B101外觀', 0),
(4, '/images/facilities/tabletennis2.png', N'桌球室B102外觀', 1),
(5, '/images/facilities/tabletennis3.png', N'桌球室B102外觀', 0),
(6, '/images/facilities/snooker1.png', N'撞球室環境', 0),
(6, '/images/facilities/snooker2.png', N'撞球室環境', 1),
(6, '/images/facilities/snooker3.png', N'撞球室環境', 2),
(7, '/images/facilities/parking_grid.png', N'地下停車場 A1 車位', 0),
(8, '/images/facilities/parking_grid.png', N'地下停車場 A2 車位', 0);


-- 宣告月底時間（避免每筆都重算）
DECLARE @endOfMonth DATETIME = DATEADD(
    SECOND, -1,
    DATEADD(MONTH, 1, CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATETIME))
);
--TRUNCATE TABLE facility_reservations;
INSERT INTO facility_reservations (
    community_id, unit_id, facility_id, number_of_users, reservation_start_time, reservation_end_time,
    is_admin, required_points, actual_used_points, point_expire_at, remark, reservation_status, created_at)
VALUES
(1, 1, 1, 5, '2025-06-25 10:00', '2025-06-25 11:00', 0, 15, 15, @endOfMonth, N'首次預約交誼廳', 'APPROVED', GETDATE()),
(1, 2, 2, 2, '2025-06-25 08:00', '2025-06-25 09:00', 0, 10, 10, @endOfMonth, N'早晨健身', 'APPROVED', GETDATE()),
(1, 3, 3, 1, '2025-06-25 17:00', '2025-06-25 18:00', 0, 10, 10, @endOfMonth, N'傍晚游泳', 'APPROVED', GETDATE()),
(1, 1, 4, 2, '2025-06-26 14:00', '2025-06-26 15:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 2, 5, 2, '2025-06-26 15:00', '2025-06-26 16:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 6, 4, '2025-06-27 19:00', '2025-06-27 20:00', 0, 20, 20, @endOfMonth, N'週五撞球', 'APPROVED', GETDATE()),
(1, 1, 2, 1, '2025-06-28 07:00', '2025-06-28 08:00', 0, 10, 10, @endOfMonth, N'健身重訓', 'APPROVED', GETDATE()),
(1, 2, 1, 6, '2025-06-28 18:00', '2025-06-28 19:00', 0, 15, 15, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 3, 2, '2025-06-29 12:00', '2025-06-29 13:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 4, 2, '2025-06-29 20:00', '2025-06-29 21:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 4, 1, 3, '2025-06-30 09:00', '2025-06-30 10:00', 0, 15, 15, @endOfMonth, N'交誼廳早會', 'APPROVED', GETDATE()),
(1, 5, 2, 2, '2025-07-01 07:00', '2025-07-01 08:00', 0, 10, 10, @endOfMonth, N'健身有氧', 'APPROVED', GETDATE()),
(1, 6, 3, 1, '2025-07-01 18:00', '2025-07-01 19:00', 0, 10, 10, @endOfMonth, N'游泳課', 'APPROVED', GETDATE()),
(1, 7, 4, 2, '2025-07-02 15:00', '2025-07-02 16:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 8, 5, 3, '2025-07-02 20:00', '2025-07-02 21:00', 0, 10, 10, @endOfMonth, N'桌球比賽', 'APPROVED', GETDATE()),
(1, 1, 6, 2, '2025-07-03 19:00', '2025-07-03 20:00', 0, 20, 20, @endOfMonth, N'撞球練習', 'APPROVED', GETDATE()),
(1, 2, 1, 4, '2025-07-04 10:00', '2025-07-04 11:00', 0, 15, 15, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 2, 1, '2025-07-04 08:00', '2025-07-04 09:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 4, 3, 2, '2025-07-05 17:00', '2025-07-05 18:00', 0, 10, 10, @endOfMonth, N'假日游泳', 'APPROVED', GETDATE()),
(1, 5, 4, 3, '2025-07-05 13:00', '2025-07-05 14:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 6, 5, 1, '2025-07-06 14:00', '2025-07-06 15:00', 0, 10, 10, @endOfMonth, N'休閒桌球', 'APPROVED', GETDATE()),
(1, 7, 6, 2, '2025-07-06 19:00', '2025-07-06 20:00', 0, 20, 20, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 8, 1, 5, '2025-07-07 10:00', '2025-07-07 11:00', 0, 15, 15, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 1, 2, 2, '2025-07-07 07:00', '2025-07-07 08:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 2, 3, 3, '2025-07-08 17:00', '2025-07-08 18:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 3, 4, 2, '2025-07-08 15:00', '2025-07-08 16:00', 0, 10, 10, @endOfMonth, N'打電動', 'APPROVED', GETDATE()),
(1, 4, 5, 1, '2025-07-09 16:00', '2025-07-09 17:00', 0, 10, 10, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 5, 6, 4, '2025-07-09 19:00', '2025-07-09 20:00', 0, 20, 20, @endOfMonth, NULL, 'APPROVED', GETDATE()),
(1, 6, 2, 3, '2025-07-10 08:00', '2025-07-10 09:00', 0, 10, 10, @endOfMonth, N'晨間健身', 'APPROVED', GETDATE()),
(1, 7, 3, 2, '2025-08-02 17:00', '2025-08-02 18:00', 0, 10, 10, @endOfMonth, N'八月游泳開跑', 'APPROVED', GETDATE());




--TRUNCATE TABLE point_accounts;
-- 宣告月底時間（避免每筆都重算）

DECLARE @endOfMonth DATETIME = DATEADD(
    SECOND, -1,
    DATEADD(MONTH, 1, CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATETIME))
);

-- 插入資料
INSERT INTO point_accounts (
    community_id, unit_id, total_balance, expired_at, updated_at
) VALUES
(1, 1, 100, @endOfMonth, GETDATE()),   -- 張三：剛領 100 系統點
(1, 2, 150, @endOfMonth, GETDATE()),   -- 李四：有儲值 50 點
(1, 3, 80, @endOfMonth, GETDATE()),    -- 王五：系統點已用部分
(1, 4, 200, @endOfMonth, GETDATE()),   -- 陳六：用戶很活躍
(1, 5, 0, @endOfMonth, GETDATE()),     -- 林七：尚未領點
(1, 6, 120, @endOfMonth, GETDATE()),   -- 馬八：剛領完點尚未使用
(1, 7, 60, @endOfMonth, GETDATE()),    -- 趙九：點數所剩不多
(1, 8, 180, @endOfMonth, GETDATE());   -- 孫十：儲值大戶


--TRUNCATE TABLE point_transactions;
INSERT INTO point_transactions (
    community_id, unit_id, transaction_type, amount,
    related_unit_id, related_reservation_id, transaction_description, created_at
) VALUES
(1, 1, 'RESERVATION', -15, NULL, 1, N'預約交誼廳扣點', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 2, N'預約健身房扣點', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 3, N'預約游泳池扣點', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 4, N'預約電玩室扣點', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 5, N'預約桌球室扣點', GETDATE()),
(1, 3, 'RESERVATION', -20, NULL, 6, N'預約撞球室扣點', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 7, N'早晨健身房', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 8, N'交誼廳活動', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 9, N'游泳', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 10, N'桌球', GETDATE()),
(1, 4, 'RESERVATION', -15, NULL, 11, N'交誼廳早會', GETDATE()),
(1, 5, 'RESERVATION', -10, NULL, 12, N'健身有氧', GETDATE()),
(1, 6, 'RESERVATION', -10, NULL, 13, N'游泳課', GETDATE()),
(1, 7, 'RESERVATION', -10, NULL, 14, N'電玩時間', GETDATE()),
(1, 8, 'RESERVATION', -10, NULL, 15, N'桌球對戰', GETDATE()),
(1, 1, 'RESERVATION', -20, NULL, 16, N'撞球練習', GETDATE()),
(1, 2, 'RESERVATION', -15, NULL, 17, N'交誼廳聊天', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 18, N'健身早晨', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 19, N'游泳週末', GETDATE()),
(1, 5, 'RESERVATION', -10, NULL, 20, N'電玩', GETDATE()),
(1, 6, 'RESERVATION', -10, NULL, 21, N'桌球練習', GETDATE()),
(1, 7, 'RESERVATION', -20, NULL, 22, N'撞球日常', GETDATE()),
(1, 8, 'RESERVATION', -15, NULL, 23, N'交誼聚會', GETDATE()),
(1, 1, 'RESERVATION', -10, NULL, 24, N'晨間健身', GETDATE()),
(1, 2, 'RESERVATION', -10, NULL, 25, N'游泳補習', GETDATE()),
(1, 3, 'RESERVATION', -10, NULL, 26, N'電玩活動', GETDATE()),
(1, 4, 'RESERVATION', -10, NULL, 27, N'桌球爭霸', GETDATE()),
(1, 5, 'RESERVATION', -20, NULL, 28, N'撞球邀請賽', GETDATE()),
(1, 6, 'RESERVATION', -10, NULL, 29, N'健身特訓', GETDATE()),
(1, 7, 'RESERVATION', -10, NULL, 30, N'八月游泳開跑', GETDATE()),
(1, 2, 'CANCEL', +10, NULL, 2, N'取消健身房退還點數', GETDATE()),
(1, 1, 'TOPUP', +50, NULL, NULL, N'管理室加值 50 點', GETDATE()),
(1, 5, 'TOPUP', +100, NULL, NULL, N'系統儲值', GETDATE()),
(1, 4, 'TRANSFER_OUT', -30, 1, NULL, N'轉給張三', GETDATE()),
(1, 1, 'TRANSFER_IN', +30, 4, NULL, N'陳六轉點', GETDATE());

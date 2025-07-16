-- Julie 停車場的部分

-- 要放在振宇哥的社區公告後面 !!

INSERT INTO [finalProj].[dbo].[bulletin_category] (
    bulletin_category_name,
    community_id
)
VALUES ( N'抽籤', 1);

-- 這邊 IDENTITY 的 id 出來之後要跟車位抽籤的 id 對應
SET IDENTITY_INSERT bulletin ON;
INSERT INTO bulletin (
    bulletin_id, bulletin_description, bulletin_is_pinned, bulletin_modify_time,
    bulletin_post_status, bulletin_post_time, bulletin_remove_time,
    bulletin_title, bulletin_category_id, community_id, user_id
)
VALUES
(20, '請參考車位抽籤活動頁面：112年電動車位抽籤', NULL, '2025-07-14 18:03:33', 0, NULL, '2025-07-14 14:25:00', N'112年汽車位抽籤', 1, 1, 1),
(21, '請參考車位抽籤活動頁面：114年機車位抽籤', NULL, '2025-07-14 17:37:44', 1, '2025-07-15 12:37:00', '2025-07-15 14:37:00', N'114年機車位抽籤', 1, 1, 1),
(22, '請參考車位抽籤活動頁面：113年汽車位抽籤', NULL, '2025-07-14 17:20:11', 0, NULL, '2025-07-06 15:06:00', N'113年機車位抽籤', 1, 1, 1),
(23, '請參考車位抽籤活動頁面：114年殘障車位抽籤', NULL, '2025-07-14 17:24:05', 1, '2025-07-16 14:20:00', '2025-07-16 17:20:00', N'114年電動車位抽籤', 1, 1, 1);
SET IDENTITY_INSERT bulletin OFF;


CREATE TABLE parking_type (
    id INT IDENTITY(1,1) PRIMARY KEY,         -- 流水號

    community_id INT NOT NULL,                -- 所屬社區
    [type] NVARCHAR(10) NOT NULL,               -- 車位種類

    -- FK 約束
    CONSTRAINT FK_parking_type_community FOREIGN KEY (community_id)
        REFERENCES community(id)
);

INSERT INTO parking_type (type, community_id)
VALUES
(N'汽車', 1),
(N'機車', 1),
(N'電動車', 1),
(N'殘障車位', 1);

CREATE TABLE parking_slot (
    id INT IDENTITY(1,1) PRIMARY KEY, -- 流水號

    community_id INT NOT NULL,        -- 所屬社區
    parking_type_id INT NOT NULL,     -- 車位種類
    users_id INT NOT NULL,            -- 擁有人（可為 null）
    
    slot_number NVARCHAR(10) NOT NULL,  -- 車位代碼
    location NVARCHAR(20),              -- 位置
    license_plate NVARCHAR(10),         -- 登記車牌
    is_rentable BIT NOT NULL,           -- 是否可承租

    -- FK 約束
    CONSTRAINT FK_parking_slot_community FOREIGN KEY (community_id) 
        REFERENCES community(id),

    CONSTRAINT FK_parking_slot_parking_type FOREIGN KEY (parking_type_id) 
        REFERENCES parking_type(id),

    CONSTRAINT FK_parking_slot_users FOREIGN KEY (users_id) 
        REFERENCES users(users_id),

    -- 唯一約束：同一社區內車位代碼唯一
    CONSTRAINT UQ_parking_slot_community_slot UNIQUE (community_id, slot_number)
);

INSERT INTO parking_slot (
    slot_number, [location], parking_type_id,
    users_id, license_plate, is_rentable, community_id
)
VALUES
(N'B1-001', N'B1 A區', 1, 2, N'ABC-1234', 0, 1),
(N'B1-002', N'B1 A區', 1, 4, NULL, 1, 1),
(N'B1-003', N'B1 A區', 1, 4, NULL, 0, 1),
(N'B1-004', N'B1 A區', 2, 4, NULL, 1, 1),
(N'B1-005', N'B1 A區', 2, 4, NULL, 0, 1),
(N'B1-006', N'B1 A區', 2, 4, NULL, 1, 1),
(N'B1-007', N'B1 A區', 3, 4, NULL, 0, 1),
(N'B1-008', N'B1 A區', 3, 4, NULL, 1, 1),
(N'B1-009', N'B1 A區', 4, 4, NULL, 0, 1),
(N'B1-010', N'B1 A區', 4, 4, NULL, 1, 1),
(N'B1-011', N'B1 B區', 1, 4, NULL, 0, 1),
(N'B1-012', N'B1 B區', 1, 4, NULL, 1, 1),
(N'B1-013', N'B1 B區', 1, 4, NULL, 0, 1),
(N'B1-014', N'B1 B區', 2, 4, NULL, 1, 1),
(N'B1-015', N'B1 B區', 2, 3, N'KFC-179', 0, 1),
(N'B1-016', N'B1 B區', 2, 4, NULL, 1, 1),
(N'B1-017', N'B1 B區', 3, 4, NULL, 0, 1),
(N'B1-018', N'B1 B區', 3, 4, NULL, 1, 1),
(N'B1-019', N'B1 B區', 4, 4, NULL, 0, 1),
(N'B1-020', N'B1 B區', 4, 4, NULL, 1, 1),
(N'B1-021', N'B1 C區', 1, 4, NULL, 0, 1),
(N'B1-022', N'B1 C區', 1, 4, NULL, 1, 1),
(N'B1-023', N'B1 C區', 1, 4, NULL, 0, 1),
(N'B1-024', N'B1 C區', 2, 4, NULL, 1, 1),
(N'B1-025', N'B1 C區', 2, 2, N'ABC-5678', 0, 1),
(N'B1-026', N'B1 C區', 2, 4, NULL, 1, 1),
(N'B1-027', N'B1 C區', 3, 4, NULL, 0, 1),
(N'B1-028', N'B1 C區', 3, 4, NULL, 1, 1),
(N'B1-029', N'B1 C區', 4, 4, NULL, 0, 1),
(N'B1-030', N'B1 C區', 4, 4, NULL, 1, 1),
(N'B2-001', N'B2 A區', 1, 4, NULL, 0, 1),
(N'B2-002', N'B2 A區', 1, 4, NULL, 1, 1),
(N'B2-003', N'B2 A區', 1, 4, NULL, 0, 1),
(N'B2-004', N'B2 A區', 2, 4, NULL, 1, 1),
(N'B2-005', N'B2 A區', 2, 4, NULL, 0, 1),
(N'B2-006', N'B2 A區', 2, 4, NULL, 1, 1),
(N'B2-007', N'B2 A區', 3, 3, N'LHP-99', 0, 1),
(N'B2-008', N'B2 A區', 3, 4, NULL, 1, 1),
(N'B2-009', N'B2 A區', 4, 4, NULL, 0, 1),
(N'B2-010', N'B2 A區', 4, 4, NULL, 1, 1),
(N'B2-011', N'B2 B區', 1, 3, N'999-KG', 0, 1),
(N'B2-012', N'B2 B區', 1, 4, NULL, 1, 1),
(N'B2-013', N'B2 B區', 1, 4, NULL, 0, 1),
(N'B2-014', N'B2 B區', 2, 4, NULL, 1, 1),
(N'B2-015', N'B2 B區', 2, 4, NULL, 0, 1),
(N'B2-016', N'B2 B區', 2, 4, NULL, 1, 1),
(N'B2-017', N'B2 B區', 3, 4, NULL, 0, 1),
(N'B2-018', N'B2 B區', 3, 4, NULL, 1, 1),
(N'B2-019', N'B2 B區', 4, 4, NULL, 0, 1),
(N'B2-020', N'B2 B區', 4, 4, NULL, 1, 1),
(N'B2-021', N'B2 C區', 1, 4, NULL, 0, 1),
(N'B2-022', N'B2 C區', 1, 4, NULL, 1, 1),
(N'B2-023', N'B2 C區', 1, 4, NULL, 0, 1),
(N'B2-024', N'B2 C區', 2, 4, NULL, 1, 1),
(N'B2-025', N'B2 C區', 2, 4, NULL, 0, 1),
(N'B2-026', N'B2 C區', 2, 4, NULL, 1, 1),
(N'B2-027', N'B2 C區', 3, 4, NULL, 0, 1),
(N'B2-028', N'B2 C區', 3, 4, NULL, 1, 1),
(N'B2-029', N'B2 C區', 4, 1, N'UFO-88', 0, 1),
(N'B2-030', N'B2 C區', 4, 4, NULL, 1, 1);

CREATE TABLE parking_rentals (
    id INT IDENTITY(1,1) PRIMARY KEY,              -- 流水號

    community_id INT NOT NULL,                    -- 所屬社區
    parking_slot_id INT NOT NULL,                 -- 車位資料
    users_id INT NOT NULL,                        -- 承租者
    approved_id INT,                              -- 審核人，可為 NULL

    rent_buy_start DATE NOT NULL,                 -- 承租起始日
    rent_end DATE NOT NULL,                       -- 承租截止日
    license_plate NVARCHAR(10) NOT NULL,          -- 登記車牌
    status BIT NOT NULL,                          -- 繳費狀態
    approved BIT NOT NULL DEFAULT 0,              -- 是否已審核

    updated_at DATETIME,                          -- 最後更新時間
    created_at DATETIME DEFAULT GETDATE(),                          -- 創建時間

    -- 外鍵約束
    CONSTRAINT FK_rentals_community FOREIGN KEY (community_id)
        REFERENCES community(id),

    CONSTRAINT FK_rentals_slot FOREIGN KEY (parking_slot_id)
        REFERENCES parking_slot(id),

    CONSTRAINT FK_rentals_user FOREIGN KEY (users_id)
        REFERENCES users(users_id),

    CONSTRAINT FK_rentals_approver FOREIGN KEY (approved_id)
        REFERENCES users(users_id)
);

INSERT INTO parking_rentals (
    approved, created_at, license_plate,
    rent_buy_start, rent_end, [status], updated_at,
    approved_id, community_id, parking_slot_id, users_id
)
VALUES
(0, '2025-07-12 14:36:21.000', N'UFO-88', '2025-07-01 00:00:00', '2025-07-31 00:00:00', 0, '2025-07-12 15:41:26', NULL, 1, 42, 1),
(0, '2025-07-12 15:36:21.000', N'KFC-99', '2025-07-01 00:00:00', '2025-07-31 00:00:00', 1, '2025-07-12 15:36:46', NULL, 1, 2, 1),
(1, '2025-07-12 15:36:27.000', N'MCD-66', '2025-08-01 00:00:00', '2025-09-30 00:00:00', 0, '2025-07-12 15:36:44', 4, 1, 42, 1);


CREATE TABLE lottery_events (
    bulletin_id INT PRIMARY KEY,                          -- 與 Bulletin 共用主鍵

    users_id INT NOT NULL,                                -- 創建人
    parking_type_id INT NOT NULL,                         -- 車位種類

    title NVARCHAR(50) NOT NULL,                          -- 標題
    started_at DATETIME NOT NULL,                         -- 開始時間
    ended_at DATETIME NOT NULL,                           -- 結束時間
    created_at DATETIME DEFAULT GETDATE(),                -- 創建時間
	rental_start DATETIME NOT NULL,
	rental_end DATETIME NOT NULL,
    status BIT NOT NULL,                                  -- 是否已抽籤

    -- 外鍵約束
    CONSTRAINT FK_lottery_event_bulletin FOREIGN KEY (bulletin_id)
        REFERENCES [dbo].[bulletin](bulletin_id),                          -- bulletin.id 為主鍵

    CONSTRAINT FK_lottery_event_users FOREIGN KEY (users_id)
        REFERENCES users(users_id),

    CONSTRAINT FK_lottery_event_parking_type FOREIGN KEY (parking_type_id)
        REFERENCES parking_type(id)
);

INSERT INTO lottery_events (
    bulletin_id, created_at, ended_at, started_at,
    [status], title, parking_type_id, users_id, rental_start, rental_end
)
VALUES
(20, '2025-07-14 18:03:33', '2025-07-14 14:25:00', '2025-07-14 14:24:00', 1, N'112年電動車位抽籤', 3, 1, '2023-07-01', '2023-07-31'),
(21, '2025-07-14 17:37:44', '2025-07-15 14:37:00', '2025-07-15 14:37:00', 0, N'114年機車位抽籤', 2, 1, '2025-07-01', '2025-07-31'),
(22, '2025-07-14 17:20:11', '2025-07-06 15:06:00', '2025-07-06 15:06:00', 0, N'113年汽車位抽籤', 1, 1, '2024-07-01 ', '2024-07-31'),
(23, '2025-07-14 17:24:05', '2025-07-16 17:20:00', '2025-07-15 17:20:00', 0, N'114年殘障車位抽籤', 4, 1, '2025-07-01', '2025-07-31');

CREATE TABLE lottery_event_spaces (
    id INT IDENTITY(1,1) PRIMARY KEY,               -- 流水號

    lottery_events_id INT NOT NULL,                -- 所屬抽籤活動（對應 bulletin_id）
    parking_slot_id INT NOT NULL,                  -- 對應車位資料

    -- 外鍵約束
    CONSTRAINT FK_event_space_event FOREIGN KEY (lottery_events_id)
        REFERENCES lottery_events(bulletin_id),

    CONSTRAINT FK_event_space_slot FOREIGN KEY (parking_slot_id)
        REFERENCES parking_slot(id)
);

INSERT INTO lottery_event_spaces (
    lottery_events_id, parking_slot_id
)
VALUES
(20, 17),
(20, 7),
(20, 8),
(20, 27),
(20, 18),
(21, 34),
(21, 24),
(21, 14),
(21, 25),
(22, 21),
(22, 31),
(22, 11),
(22, 1),
(22, 31),
(23, 19),
(23, 9);


CREATE TABLE lottery_apply (
    id INT IDENTITY(1,1) PRIMARY KEY,               -- 流水號

    users_id INT NOT NULL,                          -- 申請人
    lottery_events_id INT NOT NULL,                 -- 所屬抽籤活動（對應 bulletin_id）
    lottery_event_spaces_id INT,               -- 中籤車位，可為 NULL（尚未抽中）
    
    applied_at DATETIME                             -- 申請時間（可為 NULL）

    -- 外鍵約束
    CONSTRAINT FK_lottery_apply_users FOREIGN KEY (users_id)
        REFERENCES users(users_id),

    CONSTRAINT FK_lottery_apply_event FOREIGN KEY (lottery_events_id)
        REFERENCES lottery_events(bulletin_id),

    CONSTRAINT FK_lottery_apply_space FOREIGN KEY (lottery_event_spaces_id)
        REFERENCES lottery_event_spaces(id)
);


INSERT INTO lottery_apply (
    applied_at, lottery_event_spaces_id,
    lottery_events_id, users_id
)
VALUES
('2025-07-14 18:02:56', NULL, 20, 1),
('2025-07-14 18:02:56', 7, 21, 2),
('2025-07-14 18:02:56', 12, 22, 3),
('2025-07-14 18:02:56', NULL, 20, 1),
('2025-07-14 18:02:56', 8, 21, 2),
('2025-07-14 18:02:56', 13, 22, 3);
-- CREATE DATABASE finalProj COLLATE Chinese_Taiwan_Stroke_CI_AS;
-- drop database finalProj
--use finalProj

DROP TABLE if exists units_users
DROP TABLE if exists units
DROP TABLE if exists accounts
DROP TABLE if exists users
DROP TABLE if exists roles


create table community(
    id	INT PRIMARY KEY IDENTITY(1,1),	--社區流水號	
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

------------------------------------------

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
(N'王保全', N'男', N'02-87654321', N'保全公司', N'02-12345678', N'保全公司', NULL, N'啟用', GETDATE(), N'https://example.com/photos/u4.jpg', N'safe@example.com', 1);

------------------------------------------

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
(N'10-1', N'3F', N'A棟', 35.5, 10, 1),
(N'14', N'5F', N'B棟', 42.0, 20, 1),
(N'16', N'1F', N'C棟', 28.7, 0, 1);

------------------------------------------

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
(3, 3, 1);

------------------------------------------

CREATE TABLE roles_users
(
    rolesusers_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    community_id INT,

    FOREIGN KEY (user_id) REFERENCES users(users_id),

    FOREIGN KEY (role_id) REFERENCES roles(roles_id),

    FOREIGN KEY (community_id) REFERENCES community(id)
);

-- Insert into roles_users
INSERT INTO roles_users (user_id, role_id, community_id)
VALUES
(1, 1, 1),
(2, 1, 1),
(3, 3, 1), -- 已停用
(4, 2, 1);

































-- DROP TABLE if exists Packages

CREATE TABLE packages
(
    packages_id INT PRIMARY KEY IDENTITY(1,1),
    unit_id INT,
    piece INT NOT NULL DEFAULT 1,
    arrival_time DATETIME DEFAULT GETDATE(),
    pickup_time DATETIME NULL,
    status NVARCHAR(30),
    type NVARCHAR(30),
    sign NVARCHAR(500),
    -- 儲存圖片 URL
    place NVARCHAR(30),
    photo NVARCHAR(500),
    -- 儲存圖片 URL
    community_id INT,

    FOREIGN KEY (unit_id) REFERENCES units(units_id),

    FOREIGN KEY (community_id) REFERENCES community(id)
);





























DROP TABLE if exists units_notifications
DROP TABLE if exists users_notifications
DROP TABLE if exists notifications



CREATE TABLE notifications (
    notifications_id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(500) NOT NULL,
    description NVARCHAR(500),
    url NVARCHAR(500),
    created_time DATETIME DEFAULT GETDATE(),
    community_id INT,

    FOREIGN KEY (community_id) REFERENCES community(id)
);

CREATE TABLE units_notifications (
    units_notifications_id INT PRIMARY KEY IDENTITY(1,1),
    notifications_id INT NOT NULL,
    units_id INT NOT NULL,
    is_read INT DEFAULT 0,
    read_time DATETIME,
    
    FOREIGN KEY (notifications_id)
        REFERENCES notifications(notifications_id),

    FOREIGN KEY (units_id)
        REFERENCES Units(units_id)
);

CREATE TABLE users_notifications (
    users_notifications_id INT PRIMARY KEY IDENTITY(1,1),
    notifications_id INT NOT NULL,
    users_id INT NOT NULL,
    is_read INT DEFAULT 0,
    read_time DATETIME,

    FOREIGN KEY (notifications_id) REFERENCES notifications(notifications_id),
    FOREIGN KEY (users_id) REFERENCES users(users_id)
);







create table ticket (
    id INT PRIMARY KEY IDENTITY(1,1),				 -- ticket流水號
    reporter_id INT NULL,							 -- 創建者ID（FK to Users.id）
    title NVARCHAR(255) NOT NULL,					 -- 問題標題
    assigner_id INT NULL ,							 -- 被指派者（可為 NULL）
    [status] NVARCHAR(255) NOT NULL DEFAULT 'to do', -- 問題狀態
    issue_description NVARCHAR(MAX) NOT NULL,		 -- 問題敘述
    Cost INT ,										 -- 修繕費用
    action_time DATETIME,							 -- 更改時間
    action_by INT,									 -- 誰更動的（FK）
    [start_date] DATETIME NOT NULL DEFAULT GETDATE(),-- 創建時間
    end_date DATETIME,								 -- 結束時間
    notes VARCHAR(255),								 -- 備註
    community_id INT NOT NULL,						 -- 所屬社區（FK）
    FOREIGN KEY (reporter_id) REFERENCES users(users_id)ON DELETE SET NULL,
    FOREIGN KEY (assigner_id) REFERENCES users(users_id),
    FOREIGN KEY (action_by) REFERENCES users(users_id),
    FOREIGN KEY (community_id) REFERENCES community(id)
);

INSERT INTO ticket (reporter_id, title, issue_description, Cost, notes, community_id)
VALUES
(1, N'電梯卡住', N'住戶反映電梯在3樓停止不動，門無法開啟。', 2000, '已聯絡維修廠商', 1),
(2, N'水管漏水', N'廚房水管接縫處滲水，地板濕滑。', 1500, '需要拆除部分牆面處理', 1),
(3, N'監視器畫面異常', N'中庭監視器無影像，疑似鏡頭損壞或線路問題。', 3000, '報價中', 1),
(4, N'地下室排水異味', N'聞到刺鼻氣味，懷疑排水管堵塞。', 800, '建議定期清潔', 1)


---------------------------------------------------------------------------------------------------------------------


create table issue_type(
    id INT PRIMARY KEY IDENTITY(1,1),					--問題類別流水號
    issue_type_name NVARCHAR(255)						--問題類別名稱
)


INSERT INTO issue_type (issue_type_name) VALUES (N'電梯');
INSERT INTO issue_type (issue_type_name) VALUES (N'水管');
INSERT INTO issue_type (issue_type_name) VALUES (N'水電');
INSERT INTO issue_type (issue_type_name) VALUES (N'瓦斯');
INSERT INTO issue_type (issue_type_name) VALUES (N'門窗');
INSERT INTO issue_type (issue_type_name) VALUES (N'冷氣');
INSERT INTO issue_type (issue_type_name) VALUES (N'照明');
INSERT INTO issue_type (issue_type_name) VALUES (N'網路');
INSERT INTO issue_type (issue_type_name) VALUES (N'地板');
INSERT INTO issue_type (issue_type_name) VALUES (N'牆面滲水');

---------------------------------------------------------------------------------------------------------------------


create table issue_type_and_ticket(
    id INT PRIMARY KEY IDENTITY(1,1),	
    ticket_id	INT,									--ticket流水號
    issue_type_id INT,									--問題類別流水號	
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (issue_type_id) REFERENCES issue_type(id)
)


---------------------------------------------------------------------------------------------------------------------
create table ticket_comment(
    id INT PRIMARY KEY IDENTITY(1,1),					--ticket_comment流水號
    ticket_id INT,										--ticket流水號
    comment_time DATETIME NOT NULL DEFAULT getDate(),	--留言時間
    commenter_id INT ,									--留言ID
    comment	VARCHAR(255),								--留言
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (commenter_id) REFERENCES users(users_id)ON DELETE SET NULL
)


---------------------------------------------------------------------------------------------------------------------

create table ticket_attachment(
    id	INT PRIMARY KEY	IDENTITY(1,1),					--ticket_attachment流水號
    uploaded_by	INT NOT NULL,							--上傳者
    ticket_id INT,										--ticket流水號
    comment_ID	INT,									--留言ID
    [file_name] NVARCHAR(255),							--檔案名稱	
    [file] VARBINARY(MAX),								--檔案
    image_url	VARBINARY(MAX),							--圖	
    uploaded_at	DATETIME NOT NULL DEFAULT getDate(),	--上傳時間
    FOREIGN KEY (uploaded_by) REFERENCES users(users_id),
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (comment_ID) REFERENCES ticket_comment(id)
)


---------------------------------------------------------------------------------------------------------------------



create table vendor(
    vendor_ID INT PRIMARY KEY IDENTITY(1,1),			--廠商 ID	
    vendor_name	VARCHAR(255),							--廠商名稱	
    contact_person VARCHAR(255),						--聯絡人姓名	
    phone_number VARCHAR(255),							--聯絡電話	
    [address] VARCHAR(255),								--地址		
    notes VARCHAR(255),									--其他備註	
    tax_ID_number	int									--廠商統編	
);

INSERT INTO vendor (vendor_name, contact_person, phone_number, [address], notes, tax_ID_number)
VALUES 
(N'台灣水電工程有限公司', N'陳大明', '0912-345-678', N'台北市信義區松仁路1號', N'配合社區水電維修', 12345678),
(N'安心保全股份有限公司', N'林淑芬', '0987-654-321', N'新北市新店區北新路200號', N'保全駐點合作廠商', 87654321),
(N'環保清潔公司', N'張清潔', '0922-111-333', N'台中市西屯區市政北七路99號', N'每週定期清潔社區', 45678901),
(N'東陽消防設備行', N'李安全', '0933-222-444', N'高雄市苓雅區五福一路123號', N'消防器材巡檢', 78901234),
(N'三重電梯維修中心', N'黃文德', '0955-666-888', N'新北市三重區重新路三段168號', N'社區電梯維保合約', 23456789);



------------------------------------------------------------------------------------------------------------------------------



create table ticket_to_administrator(
    id	INT PRIMARY KEY IDENTITY(1,1),					--流水號	
    ticket_id	INT,									--ticket流水號	
    vendor_ID	INT,									--廠商id	
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)


------------------------------------------------------------------------------------------------------------------------------

create table ticket_issue_cost_attachment(
    id	INT PRIMARY KEY IDENTITY(1,1),					--Cost_attachment流水號	
    ticket_id INT NOT NULL,								--ticket流水號
    cost INT NOT NULL,									--修繕費用
    [file_name]	NVARCHAR(255) NOT NULL,					--檔案名稱	
    [file]	VARBINARY(MAX) NOT NULL,					--檔案		
    vendor_ID	INT,									--廠商id	
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)
DROP TABLE if exists faq_faq_keyword;
DROP TABLE if exists poll_vote;
DROP TABLE if exists poll_option;
DROP TABLE if exists poll;
DROP TABLE if exists bulletin_comment_like;
DROP TABLE if exists bulletin_comment;
DROP TABLE if exists bulletin_attachment;
DROP TABLE if exists bulletin;
DROP TABLE if exists feedback_status_history;
DROP TABLE if exists feedback_attachment;
DROP TABLE if exists feedback_reply;
DROP TABLE if exists feedback;
DROP TABLE if exists feedback_category;
DROP TABLE if exists faq_keyword;
DROP TABLE if exists faq;
DROP TABLE if exists faq_category;
DROP TABLE if exists bulletin_category;


CREATE TABLE bulletin_category (
        bulletin_category_id INT IDENTITY(1,1) PRIMARY KEY,   
bulletin_category_name NVARCHAR(20) not null,
	community_id INT ,										--社區流水號
    -- 外鍵設定
	FOREIGN KEY (community_id) REFERENCES community(id),
);

GO

----公告主表----

CREATE TABLE bulletin (
    bulletin_id INT IDENTITY(1,1) PRIMARY KEY,                        -- 公告流水號，自動遞增主鍵
    bulletin_title NVARCHAR(50) NOT NULL,                             -- 標題
    bulletin_description NVARCHAR(MAX),                               -- 內容
    bulletin_category_id INT,                                     -- 分類（外鍵）
    user_id INT NOT NULL,                                             -- 使用者流水號（發布人）
    bulletin_is_pinned BIT NOT NULL DEFAULT 0,                        -- 是否置頂，預設為 0（否）
    bulletin_post_time DATETIME NOT NULL DEFAULT GETDATE(),           -- 發布時間，預設為目前時間
    bulletin_modify_time DATETIME NOT NULL DEFAULT GETDATE(),         -- 修改時間，預設為目前時間
    bulletin_remove_time DATETIME NOT NULL,                           -- 截止時間
    bulletin_post_status BIT NOT NULL DEFAULT 0,                      -- 是否上架，預設為 0（否）
		community_id INT ,										--社區流水號
    -- 外鍵設定
	FOREIGN KEY (community_id) REFERENCES community(id),
    FOREIGN KEY (bulletin_category_id) REFERENCES bulletin_category(bulletin_category_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id)
);

GO


----公告附件----
CREATE TABLE bulletin_attachment (
    bulletin_attachment_id INT IDENTITY(1,1) PRIMARY KEY,           -- 附件流水號，自動遞增主鍵
    bulletin_id INT NOT NULL,                                       -- 公告流水號（外鍵）
    bulletin_attachment_file_name NVARCHAR(50) NOT NULL,            -- 附件名稱
    bulletin_attachment VARBINARY(MAX) NOT NULL,                -- 附件檔案（最大 2MB 二進位資料）
    bulletin_attachment_mime_type VARCHAR(20) NOT NULL,             -- MIME Type（如 image/jpeg）

    -- 外鍵約束
    FOREIGN KEY (bulletin_id) REFERENCES bulletin(bulletin_id)
);
GO



----公告留言----

CREATE TABLE bulletin_comment (
    bulletin_comment_id INT IDENTITY(1,1) PRIMARY KEY,              -- 留言流水號，自動遞增主鍵
    bulletin_id INT NOT NULL,                                       -- 公告流水號（外鍵）
    bulletin_comment NVARCHAR(1000),                                 -- 留言內容，可為 NULL
    user_id INT NOT NULL,                                           -- 使用者流水號（留言者）
    bulletin_comment_time DATETIME NOT NULL DEFAULT GETDATE(),     -- 留言時間，預設為目前
    bulletin_comment_is_alive BIT NOT NULL DEFAULT 1,              -- 是否存在，預設為 1（代表有效）
	parent_comment_id INT ,

    -- 外鍵約束
    FOREIGN KEY (bulletin_id) REFERENCES bulletin(bulletin_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id),
	FOREIGN KEY (parent_comment_id) REFERENCES bulletin_comment(bulletin_comment_id)
);
GO



----留言案讚----
CREATE TABLE bulletin_comment_like (
    bulletin_comment_id INT NOT NULL,                            -- 留言流水號（FK + PK）
    user_id INT NOT NULL,                                     -- 使用者流水號（按讚者，FK + PK）
    bulletin_comment_like_is_valid BIT NOT NULL DEFAULT 1,            -- 按讚是否有效，預設為有效

    -- 複合主鍵
    PRIMARY KEY (bulletin_comment_id, user_id),

    -- 外鍵約束
    FOREIGN KEY (bulletin_comment_id) REFERENCES bulletin_comment(bulletin_comment_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id)
);
GO



----投票主表----
CREATE TABLE poll (
    poll_id INT NOT NULL UNIQUE,                            -- 對應公告流水號（PK + FK + UNIQUE）
    poll_title NVARCHAR(50) NOT NULL,                           -- 投票標題
    poll_start DATETIME NOT NULL DEFAULT GETDATE(),             -- 建立時間（預設為現在）
    poll_end DATETIME NOT NULL,                                 -- 截止時間
    poll_is_multiple BIT NOT NULL DEFAULT 0,                    -- 是否多選，預設為否

    -- 主鍵與外鍵
    PRIMARY KEY (poll_id),
    FOREIGN KEY (poll_id) REFERENCES bulletin(bulletin_id)
);
GO



----投票選項----

CREATE TABLE poll_option (
    poll_option_id INT IDENTITY(1,1) PRIMARY KEY,          -- 投票選項流水號，自動遞增主鍵
    poll_id INT NOT NULL,                                  -- 投票表對應 bulletin_id（外鍵）
    poll_option_text NVARCHAR(50) NOT NULL,                -- 選項文字

    -- 外鍵約束
    FOREIGN KEY (poll_id) REFERENCES poll(poll_id)
);
GO



----投票紀錄----
CREATE TABLE poll_vote (
    poll_vote_id INT IDENTITY(1,1) PRIMARY KEY,           -- 投票紀錄流水號，自動遞增主鍵
    poll_id INT NOT NULL,                                 -- 投票表 ID（外鍵）
    user_id INT NOT NULL,                                 -- 使用者流水號（外鍵）
    poll_option_id INT NOT NULL,                          -- 被選擇的選項（外鍵）
    poll_vote_is_checked BIT NOT NULL DEFAULT 1,          -- 是否已選，預設為 1

    -- 外鍵約束
    FOREIGN KEY (poll_id) REFERENCES poll(poll_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id),
    FOREIGN KEY (poll_option_id) REFERENCES poll_option(poll_option_id)
);
----FAQ分類----

CREATE TABLE faq_category (
    faq_category_id INT PRIMARY KEY IDENTITY,
	faq_category_name NVARCHAR(20) ,
	community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
);

GO
----FAQ----

CREATE TABLE faq (
    faq_id INT IDENTITY(1,1) PRIMARY KEY,                      -- FAQ流水號，自動遞增主鍵
    faq_question NVARCHAR(50) NOT NULL,                        -- 問題，不可為 NULL
    faq_answer NVARCHAR(MAX) NOT NULL,                         -- 回答，不可為 NULL
    faq_category_id INT,                               -- 分類，可為 NULL，外鍵對應其他表格
    faq_last_modified DATETIME NOT NULL DEFAULT GETDATE(),     -- 建立/更新時間，預設為當下時間
    faq_post_status BIT NOT NULL DEFAULT 0,                   -- 是否上架，預設為未上架 (0)
	community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
    FOREIGN KEY (faq_category_id) REFERENCES faq_category(faq_category_id) -- 外鍵關聯
);

GO
----FAQ關鍵字----

CREATE TABLE faq_keyword (
    faq_keyword_id INT IDENTITY(1,1) PRIMARY KEY,  -- 關鍵字流水號，自動遞增主鍵
    faq_keyword NVARCHAR(50) NOT NULL  ,            -- 關鍵字內容，不可為 NULL
		community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
);


GO
----中介-FAQ-關鍵字----

CREATE TABLE faq_faq_keyword (
    faq_id INT NOT NULL,                             -- FAQ流水號，外鍵
    faq_keyword_id INT NOT NULL,                         -- 關鍵字流水號，外鍵
    PRIMARY KEY (faq_id, faq_keyword_id),                -- 複合主鍵
    FOREIGN KEY (faq_id) REFERENCES faq(faq_id),         -- 參考 FAQ 表的主鍵
    FOREIGN KEY (faq_keyword_id) REFERENCES faq_keyword(faq_keyword_id) -- 參考 keyword 表的主鍵
);



GO
----意見分類----

CREATE TABLE feedback_category (
    feedback_category_id INT IDENTITY(1,1) PRIMARY KEY,         -- 意見分類流水號，自動遞增主鍵
    feedback_category_name NVARCHAR(20) NOT NULL,      -- 意見分類名稱
    feedback_category_description NVARCHAR(100)  ,               -- 描述，可為 NULL
	community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
);


GO

----意見反映主表----

CREATE TABLE feedback (
    feedback_id INT IDENTITY(1,1) PRIMARY KEY,               -- 意見流水號，自動遞增主鍵
    feedback_title NVARCHAR(50) NOT NULL,                    -- 意見主旨
    feedback_description NVARCHAR(2000) NOT NULL,            -- 意見內容
    feedback_category_id INT NOT NULL,                          -- 意見分類（外鍵）
    user_id INT NOT NULL,                                    -- 反映者（外鍵）
    feedback_submit_at DATETIME NOT NULL DEFAULT GETDATE(),  -- 提交時間，預設為現在
    handler_id INT,                                          -- 承辦人員（外鍵，可為 NULL）
    feedback_status NVARCHAR(20) NOT NULL DEFAULT '待處理',                   -- 處理狀態
    feedback_last_updated_at DATETIME NOT NULL DEFAULT GETDATE(), -- 最後更新時間
    feedback_resolved_at DATETIME,                           -- 結案時間，可為 NULL
    feedback_user_rating TINYINT,                            -- 滿意度評分，可為 NULL
		community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
    -- 外鍵約束
    FOREIGN KEY (feedback_category_id) REFERENCES feedback_category(feedback_category_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id),
    FOREIGN KEY (handler_id) REFERENCES [users](users_id)
);


GO
----意見回復----

CREATE TABLE feedback_reply (
    feedback_reply_id INT IDENTITY(1,1) PRIMARY KEY,               -- 意見回復流水號，自動遞增主鍵
    feedback_id INT NOT NULL,                                     -- 意見主表流水號，外鍵
    user_id INT NOT NULL,                                         -- 回覆人員，外鍵
    feedback_reply NVARCHAR(2000) NOT NULL,                       -- 回覆內容
    feedback_reply_replied_at DATETIME NOT NULL DEFAULT GETDATE(),-- 回復時間

    -- 外鍵設定
    FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id)
);



GO

----意見附件----

CREATE TABLE feedback_attachment (
    feedback_attachment_id INT IDENTITY(1,1) PRIMARY KEY,         -- 附件流水號，自動遞增主鍵
    feedback_id INT NOT NULL,                                     -- 意見主表流水號，外鍵
    feedback_attachment_file_name NVARCHAR(50) NOT NULL,          -- 附件名稱
    feedback_attachment VARBINARY(MAX) NOT NULL,                  -- 附件檔案（二進位資料）
    feedback_attachment_mime_type VARCHAR(20) NOT NULL,           -- 檔案類型（MIME Type）
    feedback_attachment_file_size INT NOT NULL,                   -- 檔案大小（長度，單位：Byte）

    -- 外鍵關聯至 feedback 表
    FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id)
);



GO
----意見狀態變更紀錄----

CREATE TABLE feedback_status_history (
    feedback_status_history_id INT IDENTITY(1,1) PRIMARY KEY,       -- 變更記錄流水號，自動遞增主鍵
    feedback_id INT NOT NULL,                                      -- 意見主表流水號（外鍵）
    feedback_status_history_old_status NVARCHAR(20),                -- 變更前狀態，可為 NULL
    feedback_status_history_new_status NVARCHAR(20) NOT NULL,       -- 變更後狀態，不可為 NULL
    changed_by_user_id INT NOT NULL,                               -- 操作人員（外鍵）
    feedback_status_history_changed_at DATETIME NOT NULL DEFAULT GETDATE(), -- 變更時間，預設為現在
		community_id INT ,										--社區流水號
    -- 外鍵約束
	FOREIGN KEY (community_id) REFERENCES community(id),
    FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id),
    FOREIGN KEY (changed_by_user_id) REFERENCES [users](users_id)
);




-- FAQ 分類
INSERT INTO faq_category (faq_category_name, community_id) VALUES
(N'社區管理系統', 1),
(N'住戶服務', 1),
(N'公共設備', 1),
(N'社區設施', 1),
(N'安全與門禁', 1);
GO

-- FAQ 問答
INSERT INTO faq (faq_question, faq_answer, faq_category_id, faq_last_modified, faq_post_status, community_id) VALUES
(N'如何修改社區 App 密碼？', N'請至 App 的帳號設定中選擇「修改密碼」，輸入舊密碼與新密碼後確認即可。', 1, '2025-06-10 10:00:00', 1, 1),
(N'申請訪客車位需要提前多久？', N'建議至少提前一天透過 App 或管理室登記，以確保車位保留。', 2, '2025-06-11 10:00:00', 1, 1),
(N'地下室燈光太暗可以反映嗎？', N'可至反映專區提出建議，設施組將依實際情況調整照明。', 3, '2025-06-12 10:00:00', 1, 1),
(N'社區健身房使用時間為何？', N'每日早上 6 點至晚上 10 點開放，請攜帶住戶卡入場。', 4, '2025-06-13 10:00:00', 1, 1),
(N'出入門禁卡遺失怎麼辦？', N'請立即聯絡管理室掛失，並攜帶證件辦理補發手續。', 5, '2025-06-14 10:00:00', 1, 1);
GO

-- FAQ 關鍵字
INSERT INTO faq_keyword (faq_keyword, community_id) VALUES
(N'App', 1),
(N'車位申請', 1),
(N'照明問題', 1),
(N'健身房', 1),
(N'門禁卡', 1);
GO

-- FAQ 關鍵字連結
INSERT INTO faq_faq_keyword (faq_id, faq_keyword_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);
GO

-- Feedback 分類
INSERT INTO feedback_category (feedback_category_name, feedback_category_description, community_id) VALUES
(N'設備維修', N'設備損壞、異常或故障相關問題', 1),
(N'環境清潔', N'垃圾、異味、水漬等環境問題', 1),
(N'管理服務', N'管理員服務態度、效率等問題', 1),
(N'停車空間', N'車位劃分、違停、使用規範等', 1),
(N'其他建議', N'任何不屬於上述分類的反映事項', 1);
GO

-- Feedback 主表（含 user_id, handler_id）
INSERT INTO feedback (feedback_title, feedback_description, feedback_category_id, user_id, handler_id, feedback_status, feedback_last_updated_at, feedback_submit_at, feedback_resolved_at, feedback_user_rating, community_id) VALUES
(N'地下室感應燈壞了', N'晚間回家時整個地下停車場都很暗，感應燈沒有啟動。', 1, 1, 2, N'待處理', '2025-06-13 10:00:00', '2025-06-11 10:00:00', NULL, 3, 1),
(N'大廳地板有積水', N'最近下雨後大廳靠窗位置常出現積水，容易滑倒。', 2, 2, 3, N'待處理', '2025-06-12 10:00:00', '2025-06-11 10:00:00', NULL, 5, 1),
(N'管理員服務不佳', N'昨日下午詢問事情時，值班人員態度不耐煩，建議加強教育訓練。', 3, 3, 4, N'待處理', '2025-06-14 10:00:00', '2025-06-14 10:00:00', NULL, 4, 1),
(N'機車停車格不足', N'近期有許多新住戶，機車格不足導致常有亂停情形。', 4, 4, 1, N'待處理', '2025-06-15 10:00:00', '2025-06-12 10:00:00', NULL, 5, 1),
(N'希望加裝戶外遮雨棚', N'希望在管理室外空地設置遮雨棚，讓等待訪客或外送不會淋雨。', 5, 1, 2, N'待處理', '2025-06-15 10:00:00', '2025-06-13 10:00:00', NULL, 5, 1);
GO

-- 公告分類
INSERT INTO bulletin_category (bulletin_category_name, community_id) VALUES
(N'停水公告', 1),
(N'活動訊息', 1),
(N'維修通知', 1),
(N'社區公告', 1),
(N'設備更新', 1);
GO

-- 公告內容
INSERT INTO bulletin ( bulletin_title, bulletin_description, bulletin_category_id, user_id, bulletin_is_pinned, bulletin_post_time, bulletin_modify_time, bulletin_remove_time, bulletin_post_status, community_id) VALUES
( N'本週三社區將進行停水作業', N'由於管線檢修，將於 6/12(三) 上午9點至下午3點暫停供水，請提前儲水備用。', 1, 1, 0, '2025-06-11 10:00:00', '2025-06-12 10:00:00', '2025-06-13 10:00:00', 1, 1),
( N'中庭週末舉辦社區野餐活動', N'本週六下午4點起於中庭舉辦住戶野餐，歡迎全家參加，現場備有小點與飲品。', 2, 2, 1, '2025-06-13 10:00:00', '2025-06-14 10:00:00', '2025-06-16 10:00:00', 1, 1),
( N'B棟電梯維修通知', N'B棟南側電梯將於 6/15~6/17 進行維修保養，請改搭北側電梯，造成不便敬請見諒。', 3, 3, 0, '2025-06-12 10:00:00', '2025-06-14 10:00:00', '2025-06-17 10:00:00', 1, 1),
( N'新住戶報到須知', N'提醒近期搬入的新住戶，請至管理室登記並領取門禁卡及垃圾分類手冊。', 4, 4, 0, '2025-06-10 10:00:00', '2025-06-11 10:00:00', '2025-06-15 10:00:00', 1, 1),
( N'健身房將汰換新設備', N'下週一健身房將更換跑步機與啞鈴設備，期間暫停開放一天，敬請見諒。', 5, 1, 1, '2025-06-15 10:00:00', '2025-06-16 10:00:00', '2025-06-19 10:00:00', 1, 1);
GO

-- 公告留言（主留言）
INSERT INTO bulletin_comment (bulletin_id, bulletin_comment, user_id, bulletin_comment_time, bulletin_comment_is_alive, parent_comment_id) VALUES
(1, N'請問停水範圍包含A棟嗎？', 2, '2025-06-10 10:00:00', 1, NULL),
(2, N'太好了，小孩一定會喜歡這活動！', 3, '2025-06-11 10:00:00', 1, NULL),
(3, N'維修期間會提供臨時指引嗎？', 4, '2025-06-12 10:00:00', 1, NULL),
(4, N'剛入住一週，感謝提醒。', 1, '2025-06-13 10:00:00', 1, NULL),
(5, N'希望新設備能有說明書可參考。', 2, '2025-06-14 10:00:00', 1, NULL);
GO

-- 公告留言子留言
INSERT INTO bulletin_comment (bulletin_id, bulletin_comment, user_id, bulletin_comment_time, bulletin_comment_is_alive, parent_comment_id) VALUES
(1, N'A棟也會一起停水，建議當天備水喔。', 1, '2025-06-11 10:00:00', 1, 1),
(2, N'當天也會設有氣墊床，孩子應該很開心！', 4, '2025-06-12 10:00:00', 1, 2),
(3, N'會在大廳張貼告示指引，請留意。', 1, '2025-06-13 10:00:00', 1, 3),
(4, N'歡迎入住，有任何問題都可以聯絡管理室。', 3, '2025-06-14 10:00:00', 1, 4),
(5, N'會提供操作指導圖卡，感謝建議。', 4, '2025-06-15 10:00:00', 1, 5);
GO

-- 留言按讚
INSERT INTO bulletin_comment_like (bulletin_comment_id, user_id, bulletin_comment_like_is_valid) VALUES
(1, 3, 1),
(2, 1, 1),
(3, 2, 1),
(4, 3, 1),
(5, 4, 1);
GO

-- 公告附件
INSERT INTO bulletin_attachment (bulletin_id, bulletin_attachment_file_name, bulletin_attachment, bulletin_attachment_mime_type) VALUES
(1, N'停水區域示意圖.jpg', 0x1234, 'image/jpeg'),
(2, N'活動流程表.pdf', 0x1234, 'application/pdf'),
(3, N'電梯維修公告.png', 0x1234, 'image/png'),
(4, N'報到流程說明.jpg', 0x1234, 'image/jpeg'),
(5, N'設備更新清單.pdf', 0x1234, 'application/pdf');
GO

-- 投票主題（每篇公告對應一題）
INSERT INTO poll (poll_id, poll_title, poll_start, poll_end, poll_is_multiple) VALUES
(1, N'您是否備妥停水所需水量？', '2025-06-10 10:00:00', '2025-06-16 10:00:00', 0),
(2, N'您是否會參加社區野餐活動？', '2025-06-11 10:00:00', '2025-06-17 10:00:00', 0),
(3, N'您對電梯維修安排是否滿意？', '2025-06-12 10:00:00', '2025-06-18 10:00:00', 0),
(4, N'新住戶報到流程是否清楚？', '2025-06-11 10:00:00', '2025-06-16 10:00:00', 0),
(5, N'您最期待哪些健身設備更新？', '2025-06-13 10:00:00', '2025-06-19 10:00:00', 1);
GO

-- 投票選項
INSERT INTO poll_option (poll_id, poll_option_text) VALUES
(1, N'已準備完成'), (1, N'尚未準備'),
(2, N'會參加'), (2, N'不會參加'),
(3, N'滿意'), (3, N'不滿意'),
(4, N'非常清楚'), (4, N'有點不清楚'),
(5, N'跑步機'), (5, N'啞鈴'), (5, N'飛輪車');
GO

-- 投票紀錄
INSERT INTO poll_vote (poll_id, user_id, poll_option_id, poll_vote_is_checked) VALUES
(1, 1, 1, 1),
(2, 2, 3, 1),
(3, 3, 5, 1),
(4, 4, 7, 1),
(5, 1, 9, 1),
(5, 2, 10, 1);
GO

INSERT INTO feedback_reply (feedback_id, user_id, feedback_reply, feedback_reply_replied_at) VALUES
(1, 3, N'您好，社區會在本週安排水電檢查，請放心。', '2025-06-15 10:00:00'),
(2, 4, N'已通知清潔團隊，將加強公共區域整理。', '2025-06-16 10:00:00'),
(3, 1, N'我們已安排師傅前往檢修，請保持手機暢通。', '2025-06-17 10:00:00'),
(4, 2, N'此問題已提報委員會，將列入下次會議討論。', '2025-06-18 10:00:00'),
(5, 3, N'謝謝您的反映，將由客服專員持續追蹤進度。', '2025-06-19 10:00:00');
GO

INSERT INTO feedback_attachment (feedback_id, feedback_attachment_file_name, feedback_attachment, feedback_attachment_mime_type, feedback_attachment_file_size) VALUES
(1, N'反映附件1.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(2, N'反映附件2.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(3, N'反映附件3.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(4, N'反映附件4.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(5, N'反映附件5.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200);
GO
-- 建立資料庫指令
/*
use master;
DROP DATABASE IF EXISTS finalProj;
CREATE DATABASE finalProj;
*/

Use finalProj;

/*
DROP TABLE IF EXISTS community;
*/

------------------------------------------------------------------------------------johnson開始
-- 建立community
create table community(
    id	INT PRIMARY KEY IDENTITY(1,1),				--社區流水號	
    name	VARCHAR(max),							--社區名稱	
    address	VARCHAR(max),							--地址	
    create_time	 DATETIME DEFAULT GETDATE(),		--創建時間	
    [function] Bigint								--使用功能	
)
------------------------------------------------------------------------------------johnson結束

------------------------------------------------------------------------------------tim開始

/*
DROP TABLE if exists units_users;
DROP TABLE if exists roles_users;
DROP TABLE IF EXISTS units;
DROP TABLE if exists users;
DROP TABLE if exists roles;
*/
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
    last_alter_at DATETIME NOT NULL,    -- ast_alter_at 是否not null?
    photo NVARCHAR(500),  -- 儲存圖片 URL
    email NVARCHAR(100) UNIQUE,    -- 唯一索引
    community_id INT,    -- 外鍵欄位
	password NVARCHAR(100) default 'P@ssw0rd',
    states INT DEFAULT 0,
    login_fail_times INT DEFAULT 0,
    last_failed_login DATETIME,
    account_locked_until DATETIME,

    FOREIGN KEY (community_id) REFERENCES community(id)
);


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
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------tim package開始
/*
DROP TABLE if exists Packages
*/
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
------------------------------------------------------------------------------------tim notification開始
/*
DROP TABLE if exists units_notifications
DROP TABLE if exists notifications
*/

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

------------------------------------------------------------------------------------tim結束
------------------------------------------------------------------------------------javert開始
/*
DROP TABLE IF EXISTS [point_transactions];
DROP TABLE IF EXISTS [point_accounts];
DROP TABLE IF EXISTS [facility_reservations];
DROP TABLE IF EXISTS [facility_images];
DROP TABLE IF EXISTS [facilities];
*/

------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------javert結束



------------------------------------------------------------------------------johnson開始
/*
DROP TABLE ticket_issue_cost_attachment;
DROP TABLE ticket_to_administrator;
DROP TABLE ticket_attachment;
DROP TABLE ticket_comment;
DROP TABLE issue_type_and_ticket;
DROP TABLE ticket;
DROP TABLE issue_type;
DROP TABLE vendor;
*/
------------------------------------------------------------------------------
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

------------------------------------------------------------------------------

create table issue_type(
    id INT PRIMARY KEY IDENTITY(1,1),					--問題類別流水號
    issue_type_name NVARCHAR(255)						--問題類別名稱
)
------------------------------------------------------------------------------

create table issue_type_and_ticket(
    id INT PRIMARY KEY IDENTITY(1,1),	
    ticket_id	INT,									--ticket流水號
    issue_type_id INT,									--問題類別流水號	
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (issue_type_id) REFERENCES issue_type(id)
)
------------------------------------------------------------------------------
create table ticket_comment(
    id INT PRIMARY KEY IDENTITY(1,1),					--ticket_comment流水號
    ticket_id INT,										--ticket流水號
    comment_time DATETIME NOT NULL DEFAULT getDate(),	--留言時間
    commenter_id INT ,									--留言ID
    comment	VARCHAR(255),								--留言
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (commenter_id) REFERENCES users(users_id)ON DELETE SET NULL
)
------------------------------------------------------------------------------

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
------------------------------------------------------------------------------
create table vendor(
    vendor_ID INT PRIMARY KEY IDENTITY(1,1),			--廠商 ID	
    vendor_name	VARCHAR(255),							--廠商名稱	
    contact_person VARCHAR(255),						--聯絡人姓名	
    phone_number VARCHAR(255),							--聯絡電話	
    [address] VARCHAR(255),								--地址		
    notes VARCHAR(255),									--其他備註	
    tax_ID_number	int									--廠商統編	
);
------------------------------------------------------------------------------
create table ticket_to_administrator(
    id	INT PRIMARY KEY IDENTITY(1,1),					--流水號	
    ticket_id	INT,									--ticket流水號	
    vendor_ID	INT,									--廠商id	
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)
------------------------------------------------------------------------------
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
------------------------------------------------------------------------------johnson結束

/*
------------------------------------------------------------------------------------yu開始

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

------------------------------------------------------------------------------------
CREATE TABLE bulletin_category (
        bulletin_category_id INT IDENTITY(1,1) PRIMARY KEY,   
bulletin_category_name NVARCHAR(20) not null,
	community_id INT ,										--社區流水號
    -- 外鍵設定
	FOREIGN KEY (community_id) REFERENCES community(id),
);

GO
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------

----公告附件----
CREATE TABLE bulletin_attachment (
    bulletin_attachment_id INT IDENTITY(1,1) PRIMARY KEY,           -- 附件流水號，自動遞增主鍵
    bulletin_id INT NOT NULL,                                       -- 公告流水號（外鍵）
    bulletin_attachment_file_name NVARCHAR(50) NOT NULL,            -- 附件名稱
    bulletin_attachment VARBINARY(MAX) NOT NULL,                -- 附件檔案（最大 2MB 二進位資料）
    bulletin_attachment_mime_type VARCHAR(50) NOT NULL,             -- MIME Type（如 image/jpeg）

    -- 外鍵約束
    FOREIGN KEY (bulletin_id) REFERENCES bulletin(bulletin_id)
);
GO
------------------------------------------------------------------------------------
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

------------------------------------------------------------------------------------

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
------------------------------------------------------------------------------------


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
------------------------------------------------------------------------------------


----投票選項----

CREATE TABLE poll_option (
    poll_option_id INT IDENTITY(1,1) PRIMARY KEY,          -- 投票選項流水號，自動遞增主鍵
    poll_id INT NOT NULL,                                  -- 投票表對應 bulletin_id（外鍵）
    poll_option_text NVARCHAR(50) NOT NULL,                -- 選項文字

    -- 外鍵約束
    FOREIGN KEY (poll_id) REFERENCES poll(poll_id)
);
GO
------------------------------------------------------------------------------------


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
------------------------------------------------------------------------------------
CREATE TABLE faq_category (
    faq_category_id INT PRIMARY KEY IDENTITY,
	faq_category_name NVARCHAR(20) ,
	community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
);

GO
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------
----FAQ關鍵字----

CREATE TABLE faq_keyword (
    faq_keyword_id INT IDENTITY(1,1) PRIMARY KEY,  -- 關鍵字流水號，自動遞增主鍵
    faq_keyword NVARCHAR(50) NOT NULL  ,            -- 關鍵字內容，不可為 NULL
		community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
);


GO
------------------------------------------------------------------------------------
----中介-FAQ-關鍵字----

CREATE TABLE faq_faq_keyword (
    faq_id INT NOT NULL,                             -- FAQ流水號，外鍵
    faq_keyword_id INT NOT NULL,                         -- 關鍵字流水號，外鍵
    PRIMARY KEY (faq_id, faq_keyword_id),                -- 複合主鍵
    FOREIGN KEY (faq_id) REFERENCES faq(faq_id),         -- 參考 FAQ 表的主鍵
    FOREIGN KEY (faq_keyword_id) REFERENCES faq_keyword(faq_keyword_id) -- 參考 keyword 表的主鍵
);



GO
------------------------------------------------------------------------------------
----意見分類----

CREATE TABLE feedback_category (
    feedback_category_id INT IDENTITY(1,1) PRIMARY KEY,         -- 意見分類流水號，自動遞增主鍵
    feedback_category_name NVARCHAR(20) NOT NULL,      -- 意見分類名稱
    feedback_category_description NVARCHAR(100)  ,               -- 描述，可為 NULL
	community_id INT ,										--社區流水號
	FOREIGN KEY (community_id) REFERENCES community(id),   --社區外鍵
);


GO
------------------------------------------------------------------------------------

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
------------------------------------------------------------------------------------
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
------------------------------------------------------------------------------------

----意見附件----

CREATE TABLE feedback_attachment (
    feedback_attachment_id INT IDENTITY(1,1) PRIMARY KEY,         -- 附件流水號，自動遞增主鍵
    feedback_id INT NOT NULL,                                     -- 意見主表流水號，外鍵
    feedback_attachment_file_name NVARCHAR(50) NOT NULL,          -- 附件名稱
    feedback_attachment VARBINARY(MAX) NOT NULL,                  -- 附件檔案（二進位資料）
    feedback_attachment_mime_type VARCHAR(50) NOT NULL,           -- 檔案類型（MIME Type）
    feedback_attachment_file_size INT NOT NULL,                   -- 檔案大小（長度，單位：Byte）

    -- 外鍵關聯至 feedback 表
    FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id)
);



GO
------------------------------------------------------------------------------
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

------------------------------------------------------------------------------yu結束
------------------------------------------------------------------------------julie開始

CREATE TABLE parking_type (
    id INT IDENTITY(1,1) PRIMARY KEY,         -- 流水號
    community_id INT NOT NULL,                -- 所屬社區
    [type] NVARCHAR(10) NOT NULL,               -- 車位種類
    -- FK 約束
    CONSTRAINT FK_parking_type_community FOREIGN KEY (community_id) REFERENCES community(id)
);

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

CREATE TABLE lottery_events (
    bulletin_id INT PRIMARY KEY,                          -- 與 Bulletin 共用主鍵

    users_id INT NOT NULL,                                -- 創建人
    parking_type_id INT NOT NULL,                         -- 車位種類

    title NVARCHAR(50) NOT NULL,                          -- 標題
    started_at DATETIME NOT NULL,                         -- 開始時間
    ended_at DATETIME NOT NULL,                           -- 結束時間
    created_at DATETIME DEFAULT GETDATE(),                         -- 創建時間
	rental_start DATETIME NOT NULL,
	rental_end DATETIME NOT NULL,
	status BIT NOT NULL,                                  -- 是否已抽籤

    -- 外鍵約束
    CONSTRAINT FK_lottery_event_bulletin FOREIGN KEY (bulletin_id)
        REFERENCES bulletin(bulletin_id),                          -- bulletin.id 為主鍵

    CONSTRAINT FK_lottery_event_users FOREIGN KEY (users_id)
        REFERENCES users(users_id),

    CONSTRAINT FK_lottery_event_parking_type FOREIGN KEY (parking_type_id)
        REFERENCES parking_type(id)
);


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
------------------------------------------------------------------------------julie結束
------------------------------------------------------------------------------Sara開始

-- 費用類別 table
IF OBJECT_ID('finance_FeeType', 'U') IS NULL
BEGIN
    CREATE TABLE finance_FeeType (
        fee_type_id      INT IDENTITY(1,1) NOT NULL PRIMARY KEY,      -- 主鍵
        description      VARCHAR(50) NOT NULL,                        -- 費用類別名稱
        fee_code         VARCHAR(20) NOT NULL UNIQUE,                 -- 費用代碼
        amountPerUnit    DECIMAL(8,2),                                -- 每單位費用
        frequency        VARCHAR(20),                                 -- 費用頻率（例如：1、6、12）
        unit             VARCHAR(20),                                 -- 計費單位（例如：坪數、戶數）

        -- BaseEntity 共用欄位
        created_at       DATETIME NOT NULL DEFAULT SYSDATETIME(),
        last_updated     DATETIME NOT NULL DEFAULT SYSDATETIME(),
        created_by       INT,
        updated_by       INT,
        status           BIT NOT NULL DEFAULT 1,
        note             VARCHAR(MAX),
        community_id     INT

    );
END

-- 期別表 table
IF OBJECT_ID('finance_BillingPeriod', 'U') IS NULL
BEGIN
CREATE TABLE finance_BillingPeriod (
    billing_period_id INT IDENTITY(1,1) PRIMARY KEY,               -- 繳交期別流水號
    period_name       VARCHAR(50) NOT NULL UNIQUE,                -- 期別名稱
    period_code       VARCHAR(20) NOT NULL UNIQUE,                -- 期別代碼
    start_date        DATE NOT NULL,                              -- 期別開始日
    end_date          DATE NOT NULL,                              -- 期別結束日
    due_date          DATETIME,                                   -- 預設繳費截止日

    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    fee_type_id      INT NOT NULL,
    community_id     INT,

    FOREIGN KEY (fee_type_id) REFERENCES finance_FeeType(fee_type_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END


-- 應收帳款（請款單）table
IF OBJECT_ID('finance_Invoice', 'U') IS NULL
BEGIN
CREATE TABLE finance_Invoice (
    invoice_id         INT IDENTITY(1,1) PRIMARY KEY,             -- 請款單主鍵
    amount_due         DECIMAL(10,2) NOT NULL,                   -- 應收金額
    period_name        VARCHAR(20),                              -- 期別名稱（文字）
    deadline           DATETIME,                                 -- 繳費截止日
    payment_plan       VARCHAR(20),                              -- 繳費計畫（如 monthly）
    
    -- 👉 單位數（如坪數、戶數、車位數）
    unit_count         DECIMAL(8,2),                            

    -- 👉 每單位費用（複製自 FeeType.amountPerUnit 以紀錄當時價格）
    unit_price         DECIMAL(8,2),                            

    -- 👉 總金額
    total_amount       DECIMAL(10,2),                            

    paymentStatus      VARCHAR(20),                              -- 付款狀態：UNPAID, PENDING, PAID

    -- BaseEntity 共用欄位
    created_at         DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by         INT,
    updated_by         INT,
    status             BIT        NOT NULL DEFAULT 1,
    note               VARCHAR(MAX),
    community_id       INT,

    -- 關聯欄位
    users_id           INT NOT NULL,
    fee_type_id        INT NOT NULL,
    billing_period_id  INT NOT NULL,

    FOREIGN KEY (users_id) REFERENCES users(users_id),
    FOREIGN KEY (fee_type_id) REFERENCES finance_FeeType(fee_type_id),
    FOREIGN KEY (billing_period_id) REFERENCES finance_BillingPeriod(billing_period_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END


-- 請款單回覆 table
IF OBJECT_ID('finance_Invoice_Response', 'U') IS NULL
BEGIN
CREATE TABLE finance_Invoice_Response (
    invoice_response_id INT IDENTITY(1,1) PRIMARY KEY,     -- 回覆主鍵
    last_response_time  DATETIME,                          -- 回覆時間
    account_code        VARCHAR(10),                       -- 匯款帳號後五碼
    last_response       VARCHAR(MAX),                      -- 回覆內容
    verified            BIT,                               -- 是否已審核
    verified_time       DATETIME,                          -- 審核時間
    verified_by         INT,                               -- 審核人員（userId）

    users_id            INT NOT NULL,                      -- 回覆人（用戶）
    invoice_id          INT NOT NULL,                      -- 對應發票 ID

    -- 關聯欄位
    FOREIGN KEY (users_id) REFERENCES users(users_id),
    FOREIGN KEY (invoice_id) REFERENCES finance_Invoice(invoice_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END


-- 財務支出 table
IF OBJECT_ID('finance_Expense', 'U') IS NULL
BEGIN
CREATE TABLE Expense (
    expense_id     INT IDENTITY(1,1) PRIMARY KEY,
    category       VARCHAR(20),
    amount         INT NOT NULL,
    paid_on        DATETIME,
    paid_by        VARCHAR(20),
    receipt        VARBINARY(MAX),
    vendor_id      INT,
    -- BaseEntity 共用欄位
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by       INT,
    updated_by       INT,
    status           BIT        NOT NULL DEFAULT 1,
    note             VARCHAR(MAX),
    community_id     INT
    --FOREIGN KEY (vendor_id) REFERENCES Vendor(vendor_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END

-- 實收明細 table
IF OBJECT_ID('finance_Receipt', 'U') IS NULL
BEGIN
CREATE TABLE finance_Receipt (
    receipt_id        INT IDENTITY(1,1) PRIMARY KEY,
    receipt_num       VARCHAR(50),             -- 收據編號
    payment_method    VARCHAR(20),             -- 付款方式
    paid_at           DATETIME,                -- 實際付款時間
    debit_at          DATETIME,                -- 入帳時間
    amount_pay        DECIMAL(10,2),           -- 實付金額
    installments      VARCHAR(10),             -- 分期資訊

    -- BaseEntity 共用欄位
    created_at        DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    last_updated      DATETIME   NOT NULL DEFAULT SYSDATETIME(),
    created_by        INT,
    updated_by        INT,
    status            BIT        NOT NULL DEFAULT 1,
    note              VARCHAR(MAX),
    invoice_id        INT NOT NULL,            -- 對應發票
    users_id          INT NOT NULL,            -- 使用者 ID
    community_id      INT,

    FOREIGN KEY (invoice_id) REFERENCES finance_Invoice(invoice_id),
    FOREIGN KEY (users_id) REFERENCES users(users_id)
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
------------------------------------------------------------------------------sara結束
*/
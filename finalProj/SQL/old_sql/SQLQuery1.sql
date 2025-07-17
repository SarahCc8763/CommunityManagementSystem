create database finalProj

use finalProj

-- ↓↓↓ 只調整了資料表名稱為小寫，其餘內容完全保持原樣 ↓↓↓

create table community(
    id	INT PRIMARY KEY IDENTITY(1,1),--社區流水號	
    name	VARCHAR(max),--社區名稱	
    address	VARCHAR(max),--地址	
    create_time	 DATETIME,--創建時間	
    [function] int--使用功能	
)

create table ticket (
    id INT PRIMARY KEY IDENTITY(1,1),				 -- ticket流水號
    reporter_id INT NOT NULL,						 -- 創建者ID（FK to Users.id）
    title NVARCHAR(255) NOT NULL,					 -- 問題標題
    assigner_id INT NULL,							 -- 被指派者（可為 NULL）
    issue_type NVARCHAR(255) NOT NULL,				 -- 問題類別
    [status] NVARCHAR(255) NOT NULL DEFAULT 'to do', -- 問題狀態
    issue_description NVARCHAR(MAX) NOT NULL,		 -- 問題敘述
    ticket_attachment_id INT NULL,					 -- 附件（FK）
    Cost INT NULL,									 -- 修繕費用
    action_time DATETIME NULL,						 -- 更改時間
    action_by INT NULL,								 -- 誰更動的（FK）
    [start_date] DATETIME NOT NULL DEFAULT GETDATE(),-- 創建時間
    end_date DATETIME, -- 結束時間
    notes VARCHAR(255),								 -- 備註
    community_id INT NOT NULL				         -- 所屬社區（FK）
    --FOREIGN KEY (reporter_id) REFERENCES users(id),
    --FOREIGN KEY (assigner_id) REFERENCES users(id),
    --FOREIGN KEY (ticket_attachment_id) REFERENCES ticket_attachment(id),
    --FOREIGN KEY (action_by) REFERENCES users(id),
    --FOREIGN KEY (community_id) REFERENCES community(id)
);

create table issue_type_and_ticket(
    ticket_id	INT,						--ticket流水號
    issue_type_id INT						--問題類別流水號	
    --FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    --FOREIGN KEY (issue_type_id) REFERENCES issue_type(id)
)

create table issue_type(
    id INT PRIMARY KEY IDENTITY(1,1),			--問題類別流水號
    issue_type_name NVARCHAR(255)				--問題類別名稱
)

create table ticket_comment(
    id INT PRIMARY KEY IDENTITY(1,1),			--ticket_comment流水號
    ticket_id INT,								--ticket流水號
    comment_time DATETIME NOT NULL DEFAULT getDate(),--留言時間
    commenter_id INT ,							--留言ID
    comment	VARCHAR(255)						--留言
    --FOREIGN KEY (ticket_id) REFERENCES ticket_id(id),
    --FOREIGN KEY (commenter_id) REFERENCES user(id)
)

create table icket_attachment(
    id	INT PRIMARY KEY	IDENTITY(1,1),				--ticket_attachment流水號
    uploaded_by	INT NOT NULL,						--上傳者
    ticket_id INT NOT NULL,							--ticket流水號
    comment_ID	INT NOT NULL,						--留言ID
    [file_name] NVARCHAR(255),						--檔案名稱	
    [file] VARBINARY(MAX),							--檔案
    image_url1	VARBINARY(MAX),						--圖	
    image_url2	VARBINARY(MAX),						--圖			
    image_url3	VARBINARY(MAX),						--圖			
    image_url4	VARBINARY(MAX),						--圖	
    image_url5	VARBINARY(MAX),						--圖	
    uploaded_at	DATETIME NOT NULL DEFAULT getDate() --上傳時間
    --FOREIGN KEY (uploaded_by) REFERENCES user(id),
    --FOREIGN KEY (ticket_id) REFERENCES ticket_id(id),
    --FOREIGN KEY (comment_ID) REFERENCES ticket_comment(id)
)

create table ticket_issue_cost_attachment(
    id	INT PRIMARY KEY IDENTITY(1,1),			--Cost_attachment流水號	
    ticket_id INT NOT NULL,						--ticket流水號
    cost INT NOT NULL,							--修繕費用
    [file_name]	NVARCHAR(255) NOT NULL,			--檔案名稱	
    [file]	VARBINARY(MAX) NOT NULL,			--檔案
    total_cost	INT,							--總金額			
    vendor_ID	INT								--廠商id	
    --FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    --FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)

create table tticket_to_administrator(
    id	INT PRIMARY KEY IDENTITY(1,1),			--流水號	
    ticket_id	INT,							--ticket流水號	
    vendor_ID	INT								--廠商id	
    --FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    --FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)

create table vendor(
    vendor_ID INT PRIMARY KEY IDENTITY(1,1),		--廠商 ID	
    vendor_name	VARCHAR(255),						--廠商名稱	
    contact_person VARCHAR(255),					--聯絡人姓名	
    phone_number VARCHAR(255),						--聯絡電話	
    [address] VARCHAR(255),							--地址		
    notes VARCHAR(255),								--其他備註	
    tax_ID_number	int								--廠商統編	
);





IF OBJECT_ID('fee_type', 'U') IS NULL
BEGIN
--費用類別 table
CREATE TABLE fee_type (
    fee_type_id      INT             IDENTITY(1,1)      NOT NULL PRIMARY KEY,  -- 主鍵
    [description]   VARCHAR(50)     NOT NULL,
    fee_code         VARCHAR(20)     NOT NULL,
    amount         DECIMAL(8,2)             NOT NULL,
    frequency       VARCHAR(20),
    unit            VARCHAR(20),

    --以下為base entity
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(), -- 自動填入建立時間
    last_updated     DATETIME    NOT NULL DEFAULT SYSDATETIME(), -- 自動填入修改時間
    created_by       INT,
    updated_by       INT,
    [status]          BIT             NOT NULL DEFAULT 1,             -- 預設為啟用
    note            VARCHAR(MAX),

    --外鍵
    community_id    INT             , 

    -- UNIQUE約束
    UNIQUE (fee_code),
    -- 外鍵關聯
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
--費用類別 table結束

IF OBJECT_ID('billing_period', 'U') IS NULL
BEGIN
--期別表 table
CREATE TABLE billing_period (
    billing_period_id  INT             IDENTITY(1,1) PRIMARY KEY,  -- 主鍵
    period_name        VARCHAR(50)     NOT NULL,
    period_code        VARCHAR(20)     NOT NULL,
    [start_date]         DATE       NOT NULL,
    end_date           DATE       NOT NULL,
    due_date           DATETIME        NOT NULL,                   
    created_time       DATETIME        NOT NULL DEFAULT SYSDATETIME(),
    update_time        DATETIME        NOT NULL DEFAULT SYSDATETIME(),

    --以下為base entity
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(), -- 自動填入建立時間
    last_updated     DATETIME    NOT NULL DEFAULT SYSDATETIME(), -- 自動填入修改時間
    created_by       INT,
    updated_by       INT,
    [status]          BIT             NOT NULL DEFAULT 1,             -- 預設為啟用
    note            VARCHAR(MAX),

    --外鍵
    fee_type_id         INT            ,         
    community_id    INT  ,
    -- 外鍵關聯
    FOREIGN KEY (fee_type_id) REFERENCES Fee_Type(fee_type_id),
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
--期別表 table 結束


IF OBJECT_ID('invoice', 'U') IS NULL
BEGIN
-- 應收帳款（請款單）table
CREATE TABLE invoice (
    invoice_id        INT             IDENTITY(1,1) PRIMARY KEY,    -- 主鍵
    amount_due         INT             NOT NULL,                     -- 應收金額
    payment_status             VARCHAR(20),                          -- 狀態（如 unpaid, paid,late）
    deadline           DATETIME,                                     -- 繳款期限
    payment_plan       VARCHAR(20),                                   -- 付款方式（如 monthly, full）

    --以下為base entity
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(), -- 自動填入建立時間
    last_updated     DATETIME    NOT NULL DEFAULT SYSDATETIME(), -- 自動填入修改時間
    created_by       INT,
    updated_by       INT,
    [status]          BIT             NOT NULL DEFAULT 1,             -- 預設為啟用
    note            VARCHAR(MAX),
    --外鍵  
    user_id            INT             NOT NULL,
    fee_type_id        INT             NOT NULL,
    billing_period_id          INT             NOT NULL,
    community_id    INT  ,

    -- 外鍵關聯
    FOREIGN KEY (billing_period_id) REFERENCES billing_period(billing_period_id),
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
-- 應收帳款（請款單）結束

IF OBJECT_ID('invoice_response', 'U') IS NULL
BEGIN
-- 請款單回覆table
CREATE TABLE invoice_response (
    invoice_response_id  INT             IDENTITY(1,1) PRIMARY KEY,
    last_response_time   DATETIME,
    account_code         VARCHAR(6),
    last_response        VARCHAR(MAX),

    --外鍵
    user_id            INT             NOT NULL,
    invoice_id         INT             NOT NULL,
    community_id    INT  ,

    -- 外鍵關聯
    FOREIGN KEY (user_id) REFERENCES [User](user_id),
    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id),
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
-- 請款單回覆結束

IF OBJECT_ID('expense', 'U') IS NULL
BEGIN
-- 財務支出table
CREATE TABLE expense (
    expense_id     INT             IDENTITY(1,1) PRIMARY KEY,   -- 主鍵
    category       VARCHAR(20),                                 -- 支出類別
    amount         INT             NOT NULL,                    -- 支出金額
    paid_on        DATETIME,                                    -- 支付日期
    paid_by        VARCHAR(20),                                 -- 支付人
    receipt        VARBINARY(MAX),                              -- 上傳收據檔案（如PDF, 圖片等）
    description    VARCHAR(MAX),                                -- 說明或備註

    --以下為base entity
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(), -- 自動填入建立時間
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(), -- 自動填入修改時間
    created_by       INT,
    updated_by       INT,
    [status]        BIT             NOT NULL DEFAULT 1,             -- 預設為啟用
    note            VARCHAR(MAX),

    --外鍵
    vendor_id      INT             ,                   -- 供應商 ID
    community_id    INT  ,

    -- 外鍵關聯
    --FOREIGN KEY (vendor_id) REFERENCES vendor(vendor_id),
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
-- 財務支出結束

IF OBJECT_ID('receipt', 'U') IS NULL
BEGIN
-- 實收明細 table
CREATE TABLE receipt (
    receipt_id        INT             IDENTITY(1,1) PRIMARY KEY,   -- 主鍵
    receipt_num       INT             NOT NULL,                    -- 收據編號
    payment_method    VARCHAR(20),                                 -- 付款方式（如：現金、轉帳）
    paid_at           DATETIME,                                    -- 實際付款時間
    debit_at          DATETIME,                                    -- 入帳時間
    amount_pay        INT             NOT NULL,                    -- 實付金額
    installments      VARCHAR(10),                                 -- 分期資訊（如：3期）
  
   --以下為base entity
    created_at       DATETIME   NOT NULL DEFAULT SYSDATETIME(), -- 自動填入建立時間
    last_updated     DATETIME   NOT NULL DEFAULT SYSDATETIME(), -- 自動填入修改時間
    created_by       INT,
    updated_by       INT,
    [status]        BIT             NOT NULL DEFAULT 1,             -- 預設為啟用
    note            VARCHAR(MAX),

   --外鍵
    fee_type_ID        INT             NOT NULL,
    user_id           INT             NOT NULL,
    invoice_id        INT             NOT NULL,
    community_id    INT  ,

    -- 外鍵關聯
    FOREIGN KEY (fee_type_ID) REFERENCES Fee_Type(fee_type_id),
    FOREIGN KEY (user_id) REFERENCES [User](user_id),
    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id),
    --FOREIGN KEY (community_id) REFERENCES Community(community_id)
);
END
-- 收據表結束

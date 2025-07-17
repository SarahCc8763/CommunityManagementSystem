-- CREATE DATABASE finalProj COLLATE Chinese_Taiwan_Stroke_CI_AS;
-- drop database finalProj
--use finalProj

DROP TABLE if exists units_users
DROP TABLE if exists units
DROP TABLE if exists accounts
DROP TABLE if exists users
DROP TABLE if exists roles


create table community(
    id	INT PRIMARY KEY IDENTITY(1,1),	--���Ϭy����	
    name	VARCHAR(max),							--���ϦW��	
    address	VARCHAR(max),							--�a�}	
    create_time	 DATETIME DEFAULT GETDATE(),		--�Ыخɶ�	
    [function] Bigint								--�ϥΥ\��	
)
INSERT INTO community (name, address,[function])
VALUES 
(N'�x�����Ϥ@��', N'�x�����_�Ϥ��M��100��',15),
(N'�������ϤG��', N'�������T���ϫذ�@��200��',15),
(N'�s�_���ϤT��', N'�s�_���O���Ϥ�Ƹ�300��',15),
(N'�x�n���ϥ|��', N'�x�n���F�ϪL�˸�400��',15),
(N'�����Ϥ���', N'��饫���c�Ϥ��s�F��500��',15);






CREATE TABLE roles
(
    roles_id INT PRIMARY KEY IDENTITY(1,1),
    description NVARCHAR(100) NOT NULL,
    note NVARCHAR(255),
    community_id INT,
    -- �~�����

    FOREIGN KEY (community_id) REFERENCES community(id)
);

-- Insert into roles
INSERT INTO roles (description, note, community_id) VALUES
(N'���', N'�@����', 1), -- ��ᬰ1
-- (N'�޲z��', N'���Ϻ޲z��', 1),  -- �κީe�|
(N'�O��', N'���ޤΦ���', 1), -- �O����2
(N'�X��', N'�{�ɶi�J��', 1); -- �X�Ȭ�3

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
    -- ast_alter_at �O�_not null?
    photo NVARCHAR(500),  -- �x�s�Ϥ� URL
    email NVARCHAR(100)UNIQUE,
    -- �ߤ@����
    community_id INT,
    -- �~�����
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
(N'�i�j��', N'�k', N'0912345678', N'�i����', N'0987654321', N'����', N'damingline', N'�ҥ�', GETDATE(), N'https://example.com/photos/u1.jpg', N'daming@example.com', 1),
(N'���p��', N'�k', N'0922333444', N'������', N'0977555666', N'����', N'xiaofangline', N'�ҥ�', GETDATE(), N'https://example.com/photos/u2.jpg', N'fang@example.com', 1),
(N'�����', N'�k', NULL, NULL, NULL, NULL, NULL, N'����', '2025-06-23', NULL, N'xiao@example.com', 1),
(N'���O��', N'�k', N'02-87654321', N'�O�����q', N'02-12345678', N'�O�����q', NULL, N'�ҥ�', GETDATE(), N'https://example.com/photos/u4.jpg', N'safe@example.com', 1);

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
(N'10-1', N'3F', N'A��', 35.5, 10, 1),
(N'14', N'5F', N'B��', 42.0, 20, 1),
(N'16', N'1F', N'C��', 28.7, 0, 1);

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
(3, 3, 1), -- �w����
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
    -- �x�s�Ϥ� URL
    place NVARCHAR(30),
    photo NVARCHAR(500),
    -- �x�s�Ϥ� URL
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
    id INT PRIMARY KEY IDENTITY(1,1),				 -- ticket�y����
    reporter_id INT NULL,							 -- �Ыت�ID�]FK to Users.id�^
    title NVARCHAR(255) NOT NULL,					 -- ���D���D
    assigner_id INT NULL ,							 -- �Q�����̡]�i�� NULL�^
    [status] NVARCHAR(255) NOT NULL DEFAULT 'to do', -- ���D���A
    issue_description NVARCHAR(MAX) NOT NULL,		 -- ���D�ԭz
    Cost INT ,										 -- ��µ�O��
    action_time DATETIME,							 -- ���ɶ�
    action_by INT,									 -- �֧�ʪ��]FK�^
    [start_date] DATETIME NOT NULL DEFAULT GETDATE(),-- �Ыخɶ�
    end_date DATETIME,								 -- �����ɶ�
    notes VARCHAR(255),								 -- �Ƶ�
    community_id INT NOT NULL,						 -- ���ݪ��ϡ]FK�^
    FOREIGN KEY (reporter_id) REFERENCES users(users_id)ON DELETE SET NULL,
    FOREIGN KEY (assigner_id) REFERENCES users(users_id),
    FOREIGN KEY (action_by) REFERENCES users(users_id),
    FOREIGN KEY (community_id) REFERENCES community(id)
);

INSERT INTO ticket (reporter_id, title, issue_description, Cost, notes, community_id)
VALUES
(1, N'�q��d��', N'���ϬM�q��b3�Ӱ���ʡA���L�k�}�ҡC', 2000, '�w�p�����׼t��', 1),
(2, N'���޺|��', N'�p�Ф��ޱ��_�B�����A�a�O��ơC', 1500, '�ݭn������𭱳B�z', 1),
(3, N'�ʵ����e�����`', N'���x�ʵ����L�v���A�æ����Y�l�a�νu�����D�C', 3000, '������', 1),
(4, N'�a�U�ǱƤ�����', N'�D�������A�h�ñƤ��ް���C', 800, '��ĳ�w���M��', 1)


---------------------------------------------------------------------------------------------------------------------


create table issue_type(
    id INT PRIMARY KEY IDENTITY(1,1),					--���D���O�y����
    issue_type_name NVARCHAR(255)						--���D���O�W��
)


INSERT INTO issue_type (issue_type_name) VALUES (N'�q��');
INSERT INTO issue_type (issue_type_name) VALUES (N'����');
INSERT INTO issue_type (issue_type_name) VALUES (N'���q');
INSERT INTO issue_type (issue_type_name) VALUES (N'�˴�');
INSERT INTO issue_type (issue_type_name) VALUES (N'����');
INSERT INTO issue_type (issue_type_name) VALUES (N'�N��');
INSERT INTO issue_type (issue_type_name) VALUES (N'�ө�');
INSERT INTO issue_type (issue_type_name) VALUES (N'����');
INSERT INTO issue_type (issue_type_name) VALUES (N'�a�O');
INSERT INTO issue_type (issue_type_name) VALUES (N'�𭱺���');

---------------------------------------------------------------------------------------------------------------------


create table issue_type_and_ticket(
    id INT PRIMARY KEY IDENTITY(1,1),	
    ticket_id	INT,									--ticket�y����
    issue_type_id INT,									--���D���O�y����	
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (issue_type_id) REFERENCES issue_type(id)
)


---------------------------------------------------------------------------------------------------------------------
create table ticket_comment(
    id INT PRIMARY KEY IDENTITY(1,1),					--ticket_comment�y����
    ticket_id INT,										--ticket�y����
    comment_time DATETIME NOT NULL DEFAULT getDate(),	--�d���ɶ�
    commenter_id INT ,									--�d��ID
    comment	VARCHAR(255),								--�d��
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (commenter_id) REFERENCES users(users_id)ON DELETE SET NULL
)


---------------------------------------------------------------------------------------------------------------------

create table ticket_attachment(
    id	INT PRIMARY KEY	IDENTITY(1,1),					--ticket_attachment�y����
    uploaded_by	INT NOT NULL,							--�W�Ǫ�
    ticket_id INT,										--ticket�y����
    comment_ID	INT,									--�d��ID
    [file_name] NVARCHAR(255),							--�ɮצW��	
    [file] VARBINARY(MAX),								--�ɮ�
    image_url	VARBINARY(MAX),							--��	
    uploaded_at	DATETIME NOT NULL DEFAULT getDate(),	--�W�Ǯɶ�
    FOREIGN KEY (uploaded_by) REFERENCES users(users_id),
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (comment_ID) REFERENCES ticket_comment(id)
)


---------------------------------------------------------------------------------------------------------------------



create table vendor(
    vendor_ID INT PRIMARY KEY IDENTITY(1,1),			--�t�� ID	
    vendor_name	VARCHAR(255),							--�t�ӦW��	
    contact_person VARCHAR(255),						--�p���H�m�W	
    phone_number VARCHAR(255),							--�p���q��	
    [address] VARCHAR(255),								--�a�}		
    notes VARCHAR(255),									--��L�Ƶ�	
    tax_ID_number	int									--�t�Ӳνs	
);

INSERT INTO vendor (vendor_name, contact_person, phone_number, [address], notes, tax_ID_number)
VALUES 
(N'�x�W���q�u�{�������q', N'���j��', '0912-345-678', N'�x�_���H�q�ϪQ����1��', N'�t�X���Ϥ��q����', 12345678),
(N'�w�߫O���ѥ��������q', N'�L�Q��', '0987-654-321', N'�s�_���s���ϥ_�s��200��', N'�O���n�I�X�@�t��', 87654321),
(N'���O�M�䤽�q', N'�i�M��', '0922-111-333', N'�x������ٰϥ��F�_�C��99��', N'�C�g�w���M�����', 45678901),
(N'�F�������]�Ʀ�', N'���w��', '0933-222-444', N'�������d���Ϥ��֤@��123��', N'������������', 78901234),
(N'�T���q����פ���', N'����w', '0955-666-888', N'�s�_���T���ϭ��s���T�q168��', N'���Ϲq����O�X��', 23456789);



------------------------------------------------------------------------------------------------------------------------------



create table ticket_to_administrator(
    id	INT PRIMARY KEY IDENTITY(1,1),					--�y����	
    ticket_id	INT,									--ticket�y����	
    vendor_ID	INT,									--�t��id	
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)


------------------------------------------------------------------------------------------------------------------------------

create table ticket_issue_cost_attachment(
    id	INT PRIMARY KEY IDENTITY(1,1),					--Cost_attachment�y����	
    ticket_id INT NOT NULL,								--ticket�y����
    cost INT NOT NULL,									--��µ�O��
    [file_name]	NVARCHAR(255) NOT NULL,					--�ɮצW��	
    [file]	VARBINARY(MAX) NOT NULL,					--�ɮ�		
    vendor_ID	INT,									--�t��id	
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
	community_id INT ,										--���Ϭy����
    -- �~��]�w
	FOREIGN KEY (community_id) REFERENCES community(id),
);

GO

----���i�D��----

CREATE TABLE bulletin (
    bulletin_id INT IDENTITY(1,1) PRIMARY KEY,                        -- ���i�y�����A�۰ʻ��W�D��
    bulletin_title NVARCHAR(50) NOT NULL,                             -- ���D
    bulletin_description NVARCHAR(MAX),                               -- ���e
    bulletin_category_id INT,                                     -- �����]�~��^
    user_id INT NOT NULL,                                             -- �ϥΪ̬y�����]�o���H�^
    bulletin_is_pinned BIT NOT NULL DEFAULT 0,                        -- �O�_�m���A�w�]�� 0�]�_�^
    bulletin_post_time DATETIME NOT NULL DEFAULT GETDATE(),           -- �o���ɶ��A�w�]���ثe�ɶ�
    bulletin_modify_time DATETIME NOT NULL DEFAULT GETDATE(),         -- �ק�ɶ��A�w�]���ثe�ɶ�
    bulletin_remove_time DATETIME NOT NULL,                           -- �I��ɶ�
    bulletin_post_status BIT NOT NULL DEFAULT 0,                      -- �O�_�W�[�A�w�]�� 0�]�_�^
		community_id INT ,										--���Ϭy����
    -- �~��]�w
	FOREIGN KEY (community_id) REFERENCES community(id),
    FOREIGN KEY (bulletin_category_id) REFERENCES bulletin_category(bulletin_category_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id)
);

GO


----���i����----
CREATE TABLE bulletin_attachment (
    bulletin_attachment_id INT IDENTITY(1,1) PRIMARY KEY,           -- ����y�����A�۰ʻ��W�D��
    bulletin_id INT NOT NULL,                                       -- ���i�y�����]�~��^
    bulletin_attachment_file_name NVARCHAR(50) NOT NULL,            -- ����W��
    bulletin_attachment VARBINARY(MAX) NOT NULL,                -- �����ɮס]�̤j 2MB �G�i���ơ^
    bulletin_attachment_mime_type VARCHAR(20) NOT NULL,             -- MIME Type�]�p image/jpeg�^

    -- �~�����
    FOREIGN KEY (bulletin_id) REFERENCES bulletin(bulletin_id)
);
GO



----���i�d��----

CREATE TABLE bulletin_comment (
    bulletin_comment_id INT IDENTITY(1,1) PRIMARY KEY,              -- �d���y�����A�۰ʻ��W�D��
    bulletin_id INT NOT NULL,                                       -- ���i�y�����]�~��^
    bulletin_comment NVARCHAR(1000),                                 -- �d�����e�A�i�� NULL
    user_id INT NOT NULL,                                           -- �ϥΪ̬y�����]�d���̡^
    bulletin_comment_time DATETIME NOT NULL DEFAULT GETDATE(),     -- �d���ɶ��A�w�]���ثe
    bulletin_comment_is_alive BIT NOT NULL DEFAULT 1,              -- �O�_�s�b�A�w�]�� 1�]�N���ġ^
	parent_comment_id INT ,

    -- �~�����
    FOREIGN KEY (bulletin_id) REFERENCES bulletin(bulletin_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id),
	FOREIGN KEY (parent_comment_id) REFERENCES bulletin_comment(bulletin_comment_id)
);
GO



----�d�����g----
CREATE TABLE bulletin_comment_like (
    bulletin_comment_id INT NOT NULL,                            -- �d���y�����]FK + PK�^
    user_id INT NOT NULL,                                     -- �ϥΪ̬y�����]���g�̡AFK + PK�^
    bulletin_comment_like_is_valid BIT NOT NULL DEFAULT 1,            -- ���g�O�_���ġA�w�]������

    -- �ƦX�D��
    PRIMARY KEY (bulletin_comment_id, user_id),

    -- �~�����
    FOREIGN KEY (bulletin_comment_id) REFERENCES bulletin_comment(bulletin_comment_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id)
);
GO



----�벼�D��----
CREATE TABLE poll (
    poll_id INT NOT NULL UNIQUE,                            -- �������i�y�����]PK + FK + UNIQUE�^
    poll_title NVARCHAR(50) NOT NULL,                           -- �벼���D
    poll_start DATETIME NOT NULL DEFAULT GETDATE(),             -- �إ߮ɶ��]�w�]���{�b�^
    poll_end DATETIME NOT NULL,                                 -- �I��ɶ�
    poll_is_multiple BIT NOT NULL DEFAULT 0,                    -- �O�_�h��A�w�]���_

    -- �D��P�~��
    PRIMARY KEY (poll_id),
    FOREIGN KEY (poll_id) REFERENCES bulletin(bulletin_id)
);
GO



----�벼�ﶵ----

CREATE TABLE poll_option (
    poll_option_id INT IDENTITY(1,1) PRIMARY KEY,          -- �벼�ﶵ�y�����A�۰ʻ��W�D��
    poll_id INT NOT NULL,                                  -- �벼����� bulletin_id�]�~��^
    poll_option_text NVARCHAR(50) NOT NULL,                -- �ﶵ��r

    -- �~�����
    FOREIGN KEY (poll_id) REFERENCES poll(poll_id)
);
GO



----�벼����----
CREATE TABLE poll_vote (
    poll_vote_id INT IDENTITY(1,1) PRIMARY KEY,           -- �벼�����y�����A�۰ʻ��W�D��
    poll_id INT NOT NULL,                                 -- �벼�� ID�]�~��^
    user_id INT NOT NULL,                                 -- �ϥΪ̬y�����]�~��^
    poll_option_id INT NOT NULL,                          -- �Q��ܪ��ﶵ�]�~��^
    poll_vote_is_checked BIT NOT NULL DEFAULT 1,          -- �O�_�w��A�w�]�� 1

    -- �~�����
    FOREIGN KEY (poll_id) REFERENCES poll(poll_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id),
    FOREIGN KEY (poll_option_id) REFERENCES poll_option(poll_option_id)
);
----FAQ����----

CREATE TABLE faq_category (
    faq_category_id INT PRIMARY KEY IDENTITY,
	faq_category_name NVARCHAR(20) ,
	community_id INT ,										--���Ϭy����
	FOREIGN KEY (community_id) REFERENCES community(id),   --���ϥ~��
);

GO
----FAQ----

CREATE TABLE faq (
    faq_id INT IDENTITY(1,1) PRIMARY KEY,                      -- FAQ�y�����A�۰ʻ��W�D��
    faq_question NVARCHAR(50) NOT NULL,                        -- ���D�A���i�� NULL
    faq_answer NVARCHAR(MAX) NOT NULL,                         -- �^���A���i�� NULL
    faq_category_id INT,                               -- �����A�i�� NULL�A�~�������L���
    faq_last_modified DATETIME NOT NULL DEFAULT GETDATE(),     -- �إ�/��s�ɶ��A�w�]����U�ɶ�
    faq_post_status BIT NOT NULL DEFAULT 0,                   -- �O�_�W�[�A�w�]�����W�[ (0)
	community_id INT ,										--���Ϭy����
	FOREIGN KEY (community_id) REFERENCES community(id),   --���ϥ~��
    FOREIGN KEY (faq_category_id) REFERENCES faq_category(faq_category_id) -- �~�����p
);

GO
----FAQ����r----

CREATE TABLE faq_keyword (
    faq_keyword_id INT IDENTITY(1,1) PRIMARY KEY,  -- ����r�y�����A�۰ʻ��W�D��
    faq_keyword NVARCHAR(50) NOT NULL  ,            -- ����r���e�A���i�� NULL
		community_id INT ,										--���Ϭy����
	FOREIGN KEY (community_id) REFERENCES community(id),   --���ϥ~��
);


GO
----����-FAQ-����r----

CREATE TABLE faq_faq_keyword (
    faq_id INT NOT NULL,                             -- FAQ�y�����A�~��
    faq_keyword_id INT NOT NULL,                         -- ����r�y�����A�~��
    PRIMARY KEY (faq_id, faq_keyword_id),                -- �ƦX�D��
    FOREIGN KEY (faq_id) REFERENCES faq(faq_id),         -- �Ѧ� FAQ ���D��
    FOREIGN KEY (faq_keyword_id) REFERENCES faq_keyword(faq_keyword_id) -- �Ѧ� keyword ���D��
);



GO
----�N������----

CREATE TABLE feedback_category (
    feedback_category_id INT IDENTITY(1,1) PRIMARY KEY,         -- �N�������y�����A�۰ʻ��W�D��
    feedback_category_name NVARCHAR(20) NOT NULL,      -- �N�������W��
    feedback_category_description NVARCHAR(100)  ,               -- �y�z�A�i�� NULL
	community_id INT ,										--���Ϭy����
	FOREIGN KEY (community_id) REFERENCES community(id),   --���ϥ~��
);


GO

----�N���ϬM�D��----

CREATE TABLE feedback (
    feedback_id INT IDENTITY(1,1) PRIMARY KEY,               -- �N���y�����A�۰ʻ��W�D��
    feedback_title NVARCHAR(50) NOT NULL,                    -- �N���D��
    feedback_description NVARCHAR(2000) NOT NULL,            -- �N�����e
    feedback_category_id INT NOT NULL,                          -- �N�������]�~��^
    user_id INT NOT NULL,                                    -- �ϬM�̡]�~��^
    feedback_submit_at DATETIME NOT NULL DEFAULT GETDATE(),  -- ����ɶ��A�w�]���{�b
    handler_id INT,                                          -- �ӿ�H���]�~��A�i�� NULL�^
    feedback_status NVARCHAR(20) NOT NULL DEFAULT '�ݳB�z',                   -- �B�z���A
    feedback_last_updated_at DATETIME NOT NULL DEFAULT GETDATE(), -- �̫��s�ɶ�
    feedback_resolved_at DATETIME,                           -- ���׮ɶ��A�i�� NULL
    feedback_user_rating TINYINT,                            -- ���N�׵����A�i�� NULL
		community_id INT ,										--���Ϭy����
	FOREIGN KEY (community_id) REFERENCES community(id),   --���ϥ~��
    -- �~�����
    FOREIGN KEY (feedback_category_id) REFERENCES feedback_category(feedback_category_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id),
    FOREIGN KEY (handler_id) REFERENCES [users](users_id)
);


GO
----�N���^�_----

CREATE TABLE feedback_reply (
    feedback_reply_id INT IDENTITY(1,1) PRIMARY KEY,               -- �N���^�_�y�����A�۰ʻ��W�D��
    feedback_id INT NOT NULL,                                     -- �N���D��y�����A�~��
    user_id INT NOT NULL,                                         -- �^�ФH���A�~��
    feedback_reply NVARCHAR(2000) NOT NULL,                       -- �^�Ф��e
    feedback_reply_replied_at DATETIME NOT NULL DEFAULT GETDATE(),-- �^�_�ɶ�

    -- �~��]�w
    FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id),
    FOREIGN KEY (user_id) REFERENCES [users](users_id)
);



GO

----�N������----

CREATE TABLE feedback_attachment (
    feedback_attachment_id INT IDENTITY(1,1) PRIMARY KEY,         -- ����y�����A�۰ʻ��W�D��
    feedback_id INT NOT NULL,                                     -- �N���D��y�����A�~��
    feedback_attachment_file_name NVARCHAR(50) NOT NULL,          -- ����W��
    feedback_attachment VARBINARY(MAX) NOT NULL,                  -- �����ɮס]�G�i���ơ^
    feedback_attachment_mime_type VARCHAR(20) NOT NULL,           -- �ɮ������]MIME Type�^
    feedback_attachment_file_size INT NOT NULL,                   -- �ɮפj�p�]���סA���GByte�^

    -- �~�����p�� feedback ��
    FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id)
);



GO
----�N�����A�ܧ����----

CREATE TABLE feedback_status_history (
    feedback_status_history_id INT IDENTITY(1,1) PRIMARY KEY,       -- �ܧ�O���y�����A�۰ʻ��W�D��
    feedback_id INT NOT NULL,                                      -- �N���D��y�����]�~��^
    feedback_status_history_old_status NVARCHAR(20),                -- �ܧ�e���A�A�i�� NULL
    feedback_status_history_new_status NVARCHAR(20) NOT NULL,       -- �ܧ�᪬�A�A���i�� NULL
    changed_by_user_id INT NOT NULL,                               -- �ާ@�H���]�~��^
    feedback_status_history_changed_at DATETIME NOT NULL DEFAULT GETDATE(), -- �ܧ�ɶ��A�w�]���{�b
		community_id INT ,										--���Ϭy����
    -- �~�����
	FOREIGN KEY (community_id) REFERENCES community(id),
    FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id),
    FOREIGN KEY (changed_by_user_id) REFERENCES [users](users_id)
);




-- FAQ ����
INSERT INTO faq_category (faq_category_name, community_id) VALUES
(N'���Ϻ޲z�t��', 1),
(N'���A��', 1),
(N'���@�]��', 1),
(N'���ϳ]�I', 1),
(N'�w���P���T', 1);
GO

-- FAQ �ݵ�
INSERT INTO faq (faq_question, faq_answer, faq_category_id, faq_last_modified, faq_post_status, community_id) VALUES
(N'�p��ק���� App �K�X�H', N'�Ц� App ���b���]�w����ܡu�ק�K�X�v�A��J�±K�X�P�s�K�X��T�{�Y�i�C', 1, '2025-06-10 10:00:00', 1, 1),
(N'�ӽгX�Ȩ���ݭn���e�h�[�H', N'��ĳ�ܤִ��e�@�ѳz�L App �κ޲z�ǵn�O�A�H�T�O����O�d�C', 2, '2025-06-11 10:00:00', 1, 1),
(N'�a�U�ǿO���ӷt�i�H�ϬM�ܡH', N'�i�ܤϬM�M�ϴ��X��ĳ�A�]�I�ձN�̹�ڱ��p�վ�ө��C', 3, '2025-06-12 10:00:00', 1, 1),
(N'���ϰ����Шϥήɶ�����H', N'�C�馭�W 6 �I�ܱߤW 10 �I�}��A����a���d�J���C', 4, '2025-06-13 10:00:00', 1, 1),
(N'�X�J���T�d�򥢫���H', N'�ХߧY�p���޲z�Ǳ����A����a�ҥ��z�ɵo����C', 5, '2025-06-14 10:00:00', 1, 1);
GO

-- FAQ ����r
INSERT INTO faq_keyword (faq_keyword, community_id) VALUES
(N'App', 1),
(N'����ӽ�', 1),
(N'�ө����D', 1),
(N'������', 1),
(N'���T�d', 1);
GO

-- FAQ ����r�s��
INSERT INTO faq_faq_keyword (faq_id, faq_keyword_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);
GO

-- Feedback ����
INSERT INTO feedback_category (feedback_category_name, feedback_category_description, community_id) VALUES
(N'�]�ƺ���', N'�]�Ʒl�a�B���`�άG�٬������D', 1),
(N'���ҲM��', N'�U���B�����B���{�����Ұ��D', 1),
(N'�޲z�A��', N'�޲z���A�ȺA�סB�Ĳv�����D', 1),
(N'�����Ŷ�', N'���칺���B�H���B�ϥγW�d��', 1),
(N'��L��ĳ', N'�����ݩ�W�z�������ϬM�ƶ�', 1);
GO

-- Feedback �D��]�t user_id, handler_id�^
INSERT INTO feedback (feedback_title, feedback_description, feedback_category_id, user_id, handler_id, feedback_status, feedback_last_updated_at, feedback_submit_at, feedback_resolved_at, feedback_user_rating, community_id) VALUES
(N'�a�U�ǷP���O�a�F', N'�߶��^�a�ɾ�Ӧa�U���������ܷt�A�P���O�S���ҰʡC', 1, 1, 2, N'�ݳB�z', '2025-06-13 10:00:00', '2025-06-11 10:00:00', NULL, 3, 1),
(N'�j�U�a�O���n��', N'�̪�U�B��j�U�a����m�`�X�{�n���A�e���ƭˡC', 2, 2, 3, N'�ݳB�z', '2025-06-12 10:00:00', '2025-06-11 10:00:00', NULL, 5, 1),
(N'�޲z���A�Ȥ���', N'�Q��U�ȸ߰ݨƱ��ɡA�ȯZ�H���A�פ��@�СA��ĳ�[�j�Ш|�V�m�C', 3, 3, 4, N'�ݳB�z', '2025-06-14 10:00:00', '2025-06-14 10:00:00', NULL, 4, 1),
(N'���������椣��', N'������\�h�s���A�����椣���ɭP�`���ð����ΡC', 4, 4, 1, N'�ݳB�z', '2025-06-15 10:00:00', '2025-06-12 10:00:00', NULL, 5, 1),
(N'�Ʊ�[�ˤ�~�B�B��', N'�Ʊ�b�޲z�ǥ~�Ŧa�]�m�B�B�סA�����ݳX�ȩΥ~�e���|�O�B�C', 5, 1, 2, N'�ݳB�z', '2025-06-15 10:00:00', '2025-06-13 10:00:00', NULL, 5, 1);
GO

-- ���i����
INSERT INTO bulletin_category (bulletin_category_name, community_id) VALUES
(N'�������i', 1),
(N'���ʰT��', 1),
(N'���׳q��', 1),
(N'���Ϥ��i', 1),
(N'�]�Ƨ�s', 1);
GO

-- ���i���e
INSERT INTO bulletin ( bulletin_title, bulletin_description, bulletin_category_id, user_id, bulletin_is_pinned, bulletin_post_time, bulletin_modify_time, bulletin_remove_time, bulletin_post_status, community_id) VALUES
( N'���g�T���ϱN�i�氱���@�~', N'�ѩ�޽u�˭סA�N�� 6/12(�T) �W��9�I�ܤU��3�I�Ȱ��Ѥ��A�д��e�x���ƥΡC', 1, 1, 0, '2025-06-11 10:00:00', '2025-06-12 10:00:00', '2025-06-13 10:00:00', 1, 1),
( N'���x�g���|����ϳ��\����', N'���g���U��4�I�_�󤤮x�|���᳥�\�A�w����a�ѥ[�A�{���Ʀ��p�I�P���~�C', 2, 2, 1, '2025-06-13 10:00:00', '2025-06-14 10:00:00', '2025-06-16 10:00:00', 1, 1),
( N'B�ɹq����׳q��', N'B�ɫn���q��N�� 6/15~6/17 �i����׫O�i�A�Ч�f�_���q��A�y�����K�q�Ш��̡C', 3, 3, 0, '2025-06-12 10:00:00', '2025-06-14 10:00:00', '2025-06-17 10:00:00', 1, 1),
( N'�s�����춷��', N'��������h�J���s���A�Цܺ޲z�ǵn�O�û�����T�d�ΩU��������U�C', 4, 4, 0, '2025-06-10 10:00:00', '2025-06-11 10:00:00', '2025-06-15 10:00:00', 1, 1),
( N'�����бN�O���s�]��', N'�U�g�@�����бN�󴫶]�B���P�׹a�]�ơA�����Ȱ��}��@�ѡA�q�Ш��̡C', 5, 1, 1, '2025-06-15 10:00:00', '2025-06-16 10:00:00', '2025-06-19 10:00:00', 1, 1);
GO

-- ���i�d���]�D�d���^
INSERT INTO bulletin_comment (bulletin_id, bulletin_comment, user_id, bulletin_comment_time, bulletin_comment_is_alive, parent_comment_id) VALUES
(1, N'�аݰ����d��]�tA�ɶܡH', 2, '2025-06-10 10:00:00', 1, NULL),
(2, N'�Ӧn�F�A�p�Ĥ@�w�|���w�o���ʡI', 3, '2025-06-11 10:00:00', 1, NULL),
(3, N'���״����|�����{�ɫ��޶ܡH', 4, '2025-06-12 10:00:00', 1, NULL),
(4, N'��J��@�g�A�P�´����C', 1, '2025-06-13 10:00:00', 1, NULL),
(5, N'�Ʊ�s�]�Ư঳�����ѥi�ѦҡC', 2, '2025-06-14 10:00:00', 1, NULL);
GO

-- ���i�d���l�d��
INSERT INTO bulletin_comment (bulletin_id, bulletin_comment, user_id, bulletin_comment_time, bulletin_comment_is_alive, parent_comment_id) VALUES
(1, N'A�ɤ]�|�@�_�����A��ĳ��ѳƤ���C', 1, '2025-06-11 10:00:00', 1, 1),
(2, N'��Ѥ]�|�]����ԧɡA�Ĥl���ӫܶ}�ߡI', 4, '2025-06-12 10:00:00', 1, 2),
(3, N'�|�b�j�U�i�K�i�ܫ��ޡA�Яd�N�C', 1, '2025-06-13 10:00:00', 1, 3),
(4, N'�w��J��A��������D���i�H�p���޲z�ǡC', 3, '2025-06-14 10:00:00', 1, 4),
(5, N'�|���Ѿާ@���ɹϥd�A�P�«�ĳ�C', 4, '2025-06-15 10:00:00', 1, 5);
GO

-- �d�����g
INSERT INTO bulletin_comment_like (bulletin_comment_id, user_id, bulletin_comment_like_is_valid) VALUES
(1, 3, 1),
(2, 1, 1),
(3, 2, 1),
(4, 3, 1),
(5, 4, 1);
GO

-- ���i����
INSERT INTO bulletin_attachment (bulletin_id, bulletin_attachment_file_name, bulletin_attachment, bulletin_attachment_mime_type) VALUES
(1, N'�����ϰ�ܷN��.jpg', 0x1234, 'image/jpeg'),
(2, N'���ʬy�{��.pdf', 0x1234, 'application/pdf'),
(3, N'�q����פ��i.png', 0x1234, 'image/png'),
(4, N'����y�{����.jpg', 0x1234, 'image/jpeg'),
(5, N'�]�Ƨ�s�M��.pdf', 0x1234, 'application/pdf');
GO

-- �벼�D�D�]�C�g���i�����@�D�^
INSERT INTO poll (poll_id, poll_title, poll_start, poll_end, poll_is_multiple) VALUES
(1, N'�z�O�_�Ƨ������һݤ��q�H', '2025-06-10 10:00:00', '2025-06-16 10:00:00', 0),
(2, N'�z�O�_�|�ѥ[���ϳ��\���ʡH', '2025-06-11 10:00:00', '2025-06-17 10:00:00', 0),
(3, N'�z��q����צw�ƬO�_���N�H', '2025-06-12 10:00:00', '2025-06-18 10:00:00', 0),
(4, N'�s������y�{�O�_�M���H', '2025-06-11 10:00:00', '2025-06-16 10:00:00', 0),
(5, N'�z�̴��ݭ��ǰ����]�Ƨ�s�H', '2025-06-13 10:00:00', '2025-06-19 10:00:00', 1);
GO

-- �벼�ﶵ
INSERT INTO poll_option (poll_id, poll_option_text) VALUES
(1, N'�w�ǳƧ���'), (1, N'�|���ǳ�'),
(2, N'�|�ѥ['), (2, N'���|�ѥ['),
(3, N'���N'), (3, N'�����N'),
(4, N'�D�`�M��'), (4, N'���I���M��'),
(5, N'�]�B��'), (5, N'�׹a'), (5, N'������');
GO

-- �벼����
INSERT INTO poll_vote (poll_id, user_id, poll_option_id, poll_vote_is_checked) VALUES
(1, 1, 1, 1),
(2, 2, 3, 1),
(3, 3, 5, 1),
(4, 4, 7, 1),
(5, 1, 9, 1),
(5, 2, 10, 1);
GO

INSERT INTO feedback_reply (feedback_id, user_id, feedback_reply, feedback_reply_replied_at) VALUES
(1, 3, N'�z�n�A���Ϸ|�b���g�w�Ƥ��q�ˬd�A�Щ�ߡC', '2025-06-15 10:00:00'),
(2, 4, N'�w�q���M��ζ��A�N�[�j���@�ϰ��z�C', '2025-06-16 10:00:00'),
(3, 1, N'�ڭ̤w�w�Ʈv�ūe���˭סA�ЫO������Z�q�C', '2025-06-17 10:00:00'),
(4, 2, N'�����D�w�����e���|�A�N�C�J�U���|ĳ�Q�סC', '2025-06-18 10:00:00'),
(5, 3, N'���±z���ϬM�A�N�ѫȪA�M������l�ܶi�סC', '2025-06-19 10:00:00');
GO

INSERT INTO feedback_attachment (feedback_id, feedback_attachment_file_name, feedback_attachment, feedback_attachment_mime_type, feedback_attachment_file_size) VALUES
(1, N'�ϬM����1.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(2, N'�ϬM����2.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(3, N'�ϬM����3.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(4, N'�ϬM����4.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200),
(5, N'�ϬM����5.jpg', 0xABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABABAB, 'image/jpeg', 200);
GO
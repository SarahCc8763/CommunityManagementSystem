create database finalProj

use finalProj

-- ������ �u�վ�F��ƪ�W�٬��p�g�A��l���e�����O����� ������

create table community(
    id	INT PRIMARY KEY IDENTITY(1,1),--���Ϭy����	
    name	VARCHAR(max),--���ϦW��	
    address	VARCHAR(max),--�a�}	
    create_time	 DATETIME,--�Ыخɶ�	
    [function] int--�ϥΥ\��	
)

create table ticket (
    id INT PRIMARY KEY IDENTITY(1,1),				 -- ticket�y����
    reporter_id INT NOT NULL,						 -- �Ыت�ID�]FK to Users.id�^
    title NVARCHAR(255) NOT NULL,					 -- ���D���D
    assigner_id INT NULL,							 -- �Q�����̡]�i�� NULL�^
    issue_type NVARCHAR(255) NOT NULL,				 -- ���D���O
    [status] NVARCHAR(255) NOT NULL DEFAULT 'to do', -- ���D���A
    issue_description NVARCHAR(MAX) NOT NULL,		 -- ���D�ԭz
    ticket_attachment_id INT NULL,					 -- ����]FK�^
    Cost INT NULL,									 -- ��µ�O��
    action_time DATETIME NULL,						 -- ���ɶ�
    action_by INT NULL,								 -- �֧�ʪ��]FK�^
    [start_date] DATETIME NOT NULL DEFAULT GETDATE(),-- �Ыخɶ�
    end_date DATETIME, -- �����ɶ�
    notes VARCHAR(255),								 -- �Ƶ�
    community_id INT NOT NULL				         -- ���ݪ��ϡ]FK�^
    --FOREIGN KEY (reporter_id) REFERENCES users(id),
    --FOREIGN KEY (assigner_id) REFERENCES users(id),
    --FOREIGN KEY (ticket_attachment_id) REFERENCES ticket_attachment(id),
    --FOREIGN KEY (action_by) REFERENCES users(id),
    --FOREIGN KEY (community_id) REFERENCES community(id)
);

create table issue_type_and_ticket(
    ticket_id	INT,						--ticket�y����
    issue_type_id INT						--���D���O�y����	
    --FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    --FOREIGN KEY (issue_type_id) REFERENCES issue_type(id)
)

create table issue_type(
    id INT PRIMARY KEY IDENTITY(1,1),			--���D���O�y����
    issue_type_name NVARCHAR(255)				--���D���O�W��
)

create table ticket_comment(
    id INT PRIMARY KEY IDENTITY(1,1),			--ticket_comment�y����
    ticket_id INT,								--ticket�y����
    comment_time DATETIME NOT NULL DEFAULT getDate(),--�d���ɶ�
    commenter_id INT ,							--�d��ID
    comment	VARCHAR(255)						--�d��
    --FOREIGN KEY (ticket_id) REFERENCES ticket_id(id),
    --FOREIGN KEY (commenter_id) REFERENCES user(id)
)

create table icket_attachment(
    id	INT PRIMARY KEY	IDENTITY(1,1),				--ticket_attachment�y����
    uploaded_by	INT NOT NULL,						--�W�Ǫ�
    ticket_id INT NOT NULL,							--ticket�y����
    comment_ID	INT NOT NULL,						--�d��ID
    [file_name] NVARCHAR(255),						--�ɮצW��	
    [file] VARBINARY(MAX),							--�ɮ�
    image_url1	VARBINARY(MAX),						--��	
    image_url2	VARBINARY(MAX),						--��			
    image_url3	VARBINARY(MAX),						--��			
    image_url4	VARBINARY(MAX),						--��	
    image_url5	VARBINARY(MAX),						--��	
    uploaded_at	DATETIME NOT NULL DEFAULT getDate() --�W�Ǯɶ�
    --FOREIGN KEY (uploaded_by) REFERENCES user(id),
    --FOREIGN KEY (ticket_id) REFERENCES ticket_id(id),
    --FOREIGN KEY (comment_ID) REFERENCES ticket_comment(id)
)

create table ticket_issue_cost_attachment(
    id	INT PRIMARY KEY IDENTITY(1,1),			--Cost_attachment�y����	
    ticket_id INT NOT NULL,						--ticket�y����
    cost INT NOT NULL,							--��µ�O��
    [file_name]	NVARCHAR(255) NOT NULL,			--�ɮצW��	
    [file]	VARBINARY(MAX) NOT NULL,			--�ɮ�
    total_cost	INT,							--�`���B			
    vendor_ID	INT								--�t��id	
    --FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    --FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)

create table tticket_to_administrator(
    id	INT PRIMARY KEY IDENTITY(1,1),			--�y����	
    ticket_id	INT,							--ticket�y����	
    vendor_ID	INT								--�t��id	
    --FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    --FOREIGN KEY (vendor_ID) REFERENCES vendor(vendor_ID)
)

create table vendor(
    vendor_ID INT PRIMARY KEY IDENTITY(1,1),		--�t�� ID	
    vendor_name	VARCHAR(255),						--�t�ӦW��	
    contact_person VARCHAR(255),					--�p���H�m�W	
    phone_number VARCHAR(255),						--�p���q��	
    [address] VARCHAR(255),							--�a�}		
    notes VARCHAR(255),								--��L�Ƶ�	
    tax_ID_number	int								--�t�Ӳνs	
);


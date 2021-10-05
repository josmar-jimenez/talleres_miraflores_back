ALTER TABLE role_action ADD COLUMN allowed VARCHAR(50);

UPDATE role_action SET allowed = 'ALL' WHERE role_id=1;
UPDATE role_action SET allowed = 'STORE' WHERE role_id=2;
UPDATE role_action SET allowed = 'USER' WHERE role_id=3;


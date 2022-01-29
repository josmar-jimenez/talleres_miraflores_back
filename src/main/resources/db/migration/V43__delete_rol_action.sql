DELETE FROM role_action WHERE operative_id=7 AND action_id=3;
DELETE FROM role_action WHERE operative_id=7 AND action_id=2;

DELETE FROM sale_detail;
DELETE FROM stock_movement;
DELETE FROM inventory_detail;
DELETE FROM sale;

DELETE FROM users WHERE id>1;
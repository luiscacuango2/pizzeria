DROP PROCEDURE IF EXISTS take_random_pizza_order;

DELIMITER $$

CREATE DEFINER=`root`@`%` PROCEDURE `take_random_pizza_order`(
    IN id_customer VARCHAR(15),
    IN delivery_method CHAR(1),
    IN v_username VARCHAR(50),
    OUT order_taken BOOL
)
BEGIN
    DECLARE id_random_pizza INT;
    DECLARE price_random_pizza DECIMAL(5,2);
    DECLARE current_order_id INT;

    -- Manejador de errores
    DECLARE WITH_ERRORS BOOL DEFAULT FALSE;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET WITH_ERRORS = TRUE;

    -- 1. Selección de pizza
SELECT id_pizza, price INTO id_random_pizza, price_random_pizza
FROM pizza WHERE available = 1 ORDER BY RAND() LIMIT 1;

START TRANSACTION;

-- 2. Insertar en PIZZA_ORDER (Cabecera)
INSERT INTO pizza_order (
    id_customer, order_date, total_value, delivery_method,
    status, additional_notes, created_by, created_date
)
VALUES (
           id_customer, SYSDATE(), (price_random_pizza * 0.80), delivery_method,
           'P', '20% OFF RANDOM PROMOTION', v_username, SYSDATE()
       );

SET current_order_id = LAST_INSERT_ID();

    -- 3. Insertar en ORDER_ITEM (Detalle) con AUDITORÍA
    -- Incluimos los campos de auditoría que mencionaste
INSERT INTO order_item (
    id_item,
    id_order,
    id_pizza,
    quantity,
    price,
    created_by,     -- Campo de auditoría
    created_date,   -- Campo de auditoría
    modified_by,    -- Campo de auditoría inicial
    modified_date   -- Campo de auditoría inicial
)
VALUES (
           1,
           current_order_id,
           id_random_pizza,
           1,
           price_random_pizza,
           v_username,     -- Usuario que viene de la API
           SYSDATE(),      -- Fecha actual
           v_username,     -- En la creación, el modificador es el mismo
           SYSDATE()
       );

-- 4. Finalización
IF WITH_ERRORS THEN
        SET order_taken = FALSE;
ROLLBACK;
ELSE
        SET order_taken = TRUE;
COMMIT;
END IF;
SELECT order_taken;
END$$

DELIMITER ;
-- 1. Crear la tabla de Clientes primero (ya que pizza_order depende de ella)
CREATE TABLE customer (
      id_customer INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(100) NOT NULL,
      address VARCHAR(255),
      email VARCHAR(150) UNIQUE,
      phone_number VARCHAR(20)
) ENGINE=InnoDB;

-- 2. Crear la tabla de Pizzas
CREATE TABLE pizza (
       id_pizza INT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       description TEXT,
       price DECIMAL(5,2) NOT NULL,
       is_vegetarian TINYINT(1) DEFAULT 0,
       is_vegan TINYINT(1) DEFAULT 0,
       is_available TINYINT(1) DEFAULT 1
) ENGINE=InnoDB;

-- 3. Crear la tabla de Órdenes
CREATE TABLE pizza_order (
     id_order INT AUTO_INCREMENT PRIMARY KEY,
     id_customer INT NOT NULL,
     order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
     total_value DECIMAL(10,2), -- Usamos mayor precisión para el total acumulado
     delivery_method CHAR(1) COMMENT 'T: Para llevar, R: Recoger, E: En el lugar',
     additional_notes TEXT,
     CONSTRAINT fk_order_customer FOREIGN KEY (id_customer) REFERENCES customer(id_customer)
) ENGINE=InnoDB;

-- 4. Crear la tabla de Ítems de la Orden (Relación intermedia)
CREATE TABLE order_item (
        id_item INT NOT NULL,
        id_order INT NOT NULL,
        id_pizza INT NOT NULL,
        quantity INT NOT NULL,
        price DECIMAL(5,2) NOT NULL,
        PRIMARY KEY (id_item, id_order), -- Clave primaria compuesta
        CONSTRAINT fk_item_order FOREIGN KEY (id_order) REFERENCES pizza_order(id_order),
        CONSTRAINT fk_item_pizza FOREIGN KEY (id_pizza) REFERENCES pizza(id_pizza)
) ENGINE=InnoDB;
-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: db
-- Tiempo de generación: 08-01-2026 a las 09:09:55
-- Versión del servidor: 9.4.0
-- Versión de PHP: 8.3.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `pizzeria`
--
CREATE DATABASE IF NOT EXISTS `pizzeria` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `pizzeria`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `customer`
--

CREATE TABLE `customer` (
  `id_customer` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(150) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `customer`
--

INSERT INTO `customer` (`id_customer`, `name`, `address`, `email`, `phone_number`) VALUES
(110410415, 'Mercedes Balor', 'Ap #720-1833 Curabitur Av.', 'mercedesbalorclub@hotmail.com', '(688) 944-6619'),
(182120056, 'Matthew Heyman', 'Ap #268-1749 Id St.', 'heymanboss@hotmail.com', '(185) 738-9267'),
(192758012, 'Drew Watson', '705-6031 Aliquam Street', 'wangwatson@icloud.com', '(362) 881-5943'),
(262132898, 'Karl Austin', '241-9121 Fames St.', 'stonecold@icloud.com', '(559) 596-3381'),
(303265780, 'Shelton Owens', 'Ap #206-5413 Vivamus St.', 'figthowens@platzi.com', '(821) 880-6661'),
(363677933, 'Bianca Neal', 'Ap #937-4424 Vestibulum. Street', 'bianca0402@platzi.com', '(792) 406-8858'),
(394022487, 'Becky Alford', 'P.O. Box 341, 7572 Odio Rd.', 'beckytwobelts@icloud.com', '(559) 398-7689'),
(474771564, 'Johanna Reigns', '925-3988 Purus. St.', 'johareigns@outlook.com', '(801) 370-4041'),
(531254932, 'Clarke Wyatt', '461-4278 Dignissim Av.', 'wyattplay@google.co', '(443) 263-8555'),
(617684636, 'Alexa Morgan', 'Ap #732-8087 Dui. Road', 'aleximorgan@hotmail.com', '(830) 212-2247'),
(644337170, 'Sami Rollins', 'Ap #308-4700 Mollis Av.', 'elgenerico@outlook.com', '(508) 518-2967'),
(762085429, 'Cody Rollins', '177-1125 Consequat Ave', 'codyforchamp@google.com', '(740) 271-3631'),
(782668115, 'Charlotte Riddle', 'Ap #696-6846 Ullamcorper Avenue', 'amityrogers@outlook.com', '(744) 344-7768'),
(863264988, 'Drake Theory', 'P.O. Box 136, 4534 Lacinia St.', 'draketheory@hotmail.com', '(826) 607-2278'),
(885583622, 'Brock Alford', '9063 Aliquam, Road', 'brockalford595@platzi.com', '(732) 218-4844');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `order_item`
--

CREATE TABLE `order_item` (
  `id_item` int NOT NULL,
  `id_order` int NOT NULL,
  `id_pizza` int NOT NULL,
  `quantity` decimal(2,1) NOT NULL,
  `price` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `order_item`
--

INSERT INTO `order_item` (`id_item`, `id_order`, `id_pizza`, `quantity`, `price`) VALUES
(1, 1, 1, 1.0, 23.00),
(1, 2, 2, 1.0, 18.50),
(1, 3, 3, 1.0, 22.00),
(1, 4, 8, 2.0, 42.00),
(1, 5, 10, 0.5, 11.00),
(1, 6, 11, 1.0, 23.00),
(2, 1, 4, 1.0, 19.95),
(2, 2, 6, 1.0, 24.00),
(2, 5, 12, 0.5, 9.50),
(3, 2, 7, 1.0, 19.50);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pizza`
--

CREATE TABLE `pizza` (
  `id_pizza` int NOT NULL,
  `name` varchar(30) NOT NULL,
  `description` varchar(150) NOT NULL,
  `price` decimal(5,2) NOT NULL,
  `available` tinyint NOT NULL,
  `vegan` tinyint DEFAULT NULL,
  `vegetarian` tinyint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `pizza`
--

INSERT INTO `pizza` (`id_pizza`, `name`, `description`, `price`, `available`, `vegan`, `vegetarian`) VALUES
(1, 'Pepperoni', 'Pepperoni, Homemade Tomato Sauce & Mozzarella.', 23.00, 1, 0, 0),
(2, 'Margherita', 'Fior de Latte, Homemade Tomato Sauce, Extra Virgin Olive Oil & Basil.', 18.50, 1, 0, 1),
(3, 'Vegan Margherita', 'Fior de Latte, Homemade Tomato Sauce, Extra Virgin Olive Oil & Basil.', 22.00, 1, 1, 1),
(4, 'Avocado Festival', 'Hass Avocado, House Red Sauce, Sundried Tomatoes, Basil & Lemon Zest.', 19.95, 1, 0, 1),
(5, 'Hawaiian', 'Homemade Tomato Sauce, Mozzarella, Pineapple & Ham.', 20.50, 0, 0, 0),
(6, 'Goat Chesse', 'Portobello Mushrooms, Mozzarella, Parmesan & Goat Cheeses with Alfredo Sauce.', 24.00, 1, 0, 0),
(7, 'Mother Earth', 'Artichokes, Roasted Peppers, Rapini, Sundried Tomatoes, Onion, Shaved Green Bell Peppers & Sunny Seasoning.', 19.50, 1, 0, 1),
(8, 'Meat Lovers', 'Mild Italian Sausage, Pepperoni, Bacon, Homemade Tomato Sauce & Mozzarella.', 21.00, 1, 0, 0),
(9, 'Marinated BBQ Chicken', 'Marinated Chicken with Cilantro, Red Onions, Gouda, Parmesan & Mozzarella Cheeses.', 20.95, 0, 0, 0),
(10, 'Truffle Cashew Cream', 'Wild mushrooms, Baby Kale, Shiitake Bacon & Lemon Vinaigrette. Soy free.', 22.00, 1, 1, 1),
(11, 'Rico Mor', 'Beef Chorizo, Sundried Tomatoes, Salsa Verde, Pepper, Jalapeno & pistachios', 23.00, 1, 0, 0),
(12, 'Spinach Artichoke', 'Fresh Spinach, Marinated Artichoke Hearts, Garlic, Fior de Latte, Mozzarella & Parmesan.', 18.95, 1, 0, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pizza_order`
--

CREATE TABLE `pizza_order` (
  `id_order` int NOT NULL,
  `id_customer` int NOT NULL,
  `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_value` decimal(6,2) NOT NULL,
  `delivery_method` char(1) NOT NULL,
  `additional_notes` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `pizza_order`
--

INSERT INTO `pizza_order` (`id_order`, `id_customer`, `order_date`, `total_value`, `delivery_method`, `additional_notes`) VALUES
(1, 192758012, '2026-01-02 17:29:09', 42.95, 'D', 'Don\'t be late pls.'),
(2, 474771564, '2026-01-03 17:29:09', 62.00, 'S', NULL),
(3, 182120056, '2026-01-04 17:29:09', 22.00, 'C', NULL),
(4, 617684636, '2026-01-05 17:29:09', 42.00, 'S', NULL),
(5, 192758012, '2026-01-06 17:29:09', 20.50, 'D', 'Please bring the jalapeños separately.'),
(6, 782668115, '2026-01-07 17:29:09', 23.00, 'D', NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id_customer`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indices de la tabla `order_item`
--
ALTER TABLE `order_item`
  ADD PRIMARY KEY (`id_item`,`id_order`),
  ADD KEY `fk_item_order` (`id_order`),
  ADD KEY `fk_item_pizza` (`id_pizza`);

--
-- Indices de la tabla `pizza`
--
ALTER TABLE `pizza`
  ADD PRIMARY KEY (`id_pizza`);

--
-- Indices de la tabla `pizza_order`
--
ALTER TABLE `pizza_order`
  ADD PRIMARY KEY (`id_order`),
  ADD KEY `fk_order_customer` (`id_customer`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `customer`
--
ALTER TABLE `customer`
  MODIFY `id_customer` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=885583623;

--
-- AUTO_INCREMENT de la tabla `pizza`
--
ALTER TABLE `pizza`
  MODIFY `id_pizza` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `pizza_order`
--
ALTER TABLE `pizza_order`
  MODIFY `id_order` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `order_item`
--
ALTER TABLE `order_item`
  ADD CONSTRAINT `fk_item_order` FOREIGN KEY (`id_order`) REFERENCES `pizza_order` (`id_order`),
  ADD CONSTRAINT `fk_item_pizza` FOREIGN KEY (`id_pizza`) REFERENCES `pizza` (`id_pizza`);

--
-- Filtros para la tabla `pizza_order`
--
ALTER TABLE `pizza_order`
  ADD CONSTRAINT `fk_order_customer` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id_customer`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

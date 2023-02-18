-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-02-2023 a las 01:10:54
-- Versión del servidor: 10.4.24-MariaDB
-- Versión de PHP: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `puntoventa`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `dni_cliente` varchar(11) NOT NULL,
  `nombre_cliente` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `dni_cliente`, `nombre_cliente`) VALUES
(1, '12345678', 'Jose Maria'),
(2, '45734583', 'Maria Jose');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_venta`
--

CREATE TABLE `detalle_venta` (
  `id` int(12) NOT NULL,
  `id_producto` varchar(50) NOT NULL,
  `cantidad` int(12) NOT NULL,
  `precio` float(6,2) NOT NULL,
  `id_venta` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `detalle_venta`
--

INSERT INTO `detalle_venta` (`id`, `id_producto`, `cantidad`, `precio`, `id_venta`) VALUES
(1, 'sdf', 1, 4.33, 0),
(2, 'sdf2', 4, 23.00, 0),
(3, 'sdf', 1, 4.33, 0),
(4, 'sdf', 1, 4.33, 7),
(5, 'sdf', 1, 4.33, 8),
(6, 'sdf2', 1, 23.00, 8),
(7, 'sdf2', 3, 23.00, 8),
(8, 'sdf', 1, 4.33, 9),
(9, 'sdf2', 1, 23.00, 9),
(10, 'sdf', 4, 4.33, 9),
(11, 'sdf', 3, 4.33, 9),
(12, 'sdf2', 3, 23.00, 9),
(13, 'sdf', 1, 4.33, 10),
(14, 'sdf', 1, 4.33, 11),
(15, 'sdf2', 3, 23.00, 11),
(16, 'sdf', 2, 4.33, 11),
(17, 'sdf2', 1, 23.00, 12),
(18, 'sdf', 3, 4.33, 12),
(19, 'sdf2', 3, 23.00, 12),
(20, 'sdf', 1, 4.33, 13),
(21, 'sdf', 1, 4.33, 14),
(22, 'sdf2', 3, 23.00, 14),
(23, 'sdf', 4, 4.33, 15),
(24, 'sdf', 4, 4.33, 16),
(25, 'sdf', 2, 4.33, 17),
(26, 'sdf2', 3, 23.00, 17),
(27, 'sdf', 3, 4.33, 18),
(28, 'sdf', 2, 4.33, 19),
(29, 'sdf2', 3, 23.00, 19),
(30, 'sdf', 3, 4.33, 20),
(31, 'sdf2', 3, 23.00, 20);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id_empleado` int(11) NOT NULL,
  `nombre_empleado` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `ape_paterno_empleado` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `ape_materno_empleado` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `documento_empleado` int(15) NOT NULL,
  `celular_empleado` int(9) NOT NULL,
  `fec_naciminto_empleado` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `fec_ingreso_empleado` varchar(15) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id_empleado`, `nombre_empleado`, `ape_paterno_empleado`, `ape_materno_empleado`, `documento_empleado`, `celular_empleado`, `fec_naciminto_empleado`, `fec_ingreso_empleado`) VALUES
(1, 'Anita', 'Lopez', 'Hurtado', 7654321, 987654321, '321/654', '654sdf'),
(2, 'andre', 'lopez', 'Rivera', 7654321, 123456789, '45645', 'sdfsd'),
(7, 'Daniela', 'aaaaaaaaa', 'sdfsdf', 654, 654645, 'sdfsdf', 'dfgdfg'),
(8, 'Carlos', 'Rivera', 'Rios', 654, 654, '654', 'fghfgh');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` int(11) NOT NULL,
  `nombre_producto` text COLLATE utf8_unicode_ci NOT NULL,
  `categoria_producto` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `descripcion_producto` text COLLATE utf8_unicode_ci NOT NULL,
  `costo_producto` float NOT NULL,
  `venta_producto` float NOT NULL,
  `stock_producto` int(5) NOT NULL,
  `codigo_producto` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `foto_producto` varchar(10) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre_producto`, `categoria_producto`, `descripcion_producto`, `costo_producto`, `venta_producto`, `stock_producto`, `codigo_producto`, `foto_producto`) VALUES
(2, 'agujas', 'Joyas', 'agujas para perforaciones', 2.5, 3.9, 150, 'asf654', 'foto'),
(3, 'tinta', 'Tatuajes', 'tinta para tatuar', 1.255, 6.025, 25, '13254', 'dfgdfg'),
(5, 'nuevo', 'Otros', 'descripcion del nuevo producto	', 1, 4.333, 12, 'sdf', 'sdf'),
(6, 'aaaaa', 'Joyas', 'aaaaaaaa', 1, 23, 45, 'sdf2', 'sdf');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `nombre_usuario` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `correo_usuario` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `contrasena_usuario` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre_usuario`, `correo_usuario`, `contrasena_usuario`) VALUES
(1, 'gerente', 'gerente@gmail.com', '123'),
(2, 'Secretaria', 'secretaria@gmail.com', '456');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id_venta` int(10) NOT NULL,
  `fecha` date NOT NULL,
  `dni_cliente` varchar(12) NOT NULL,
  `nombre_cliente` varchar(30) NOT NULL,
  `nombre_empleado` varchar(30) NOT NULL,
  `descripcion_venta` varchar(500) NOT NULL,
  `monto_total` float(6,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `ventas`
--

INSERT INTO `ventas` (`id_venta`, `fecha`, `dni_cliente`, `nombre_cliente`, `nombre_empleado`, `descripcion_venta`, `monto_total`) VALUES
(1, '2023-02-18', '84734753', 'Sebastian', 'Lorenzo', 'Punto de venta tatu realizado el dia de hoy', 300.00),
(2, '2023-02-22', '87435745', 'Marcelo', 'Marcos', 'Venta 2', 50.00),
(3, '2023-02-16', '12345678', 'Jose Maria', 'Carlos', '', 73.33),
(4, '2023-02-17', '12345678', 'Jose Maria', 'Carlos', '', 73.33),
(5, '2023-02-17', '12345678', 'Jose Maria', 'Carlos', '', 96.33),
(6, '2023-02-17', '12345678', 'Jose Maria', 'andre', '', 4.33),
(7, '2023-02-17', '12345678', 'Jose Maria', 'Anita', '', 4.33),
(8, '2023-02-17', '12345678', 'Jose Maria', 'andre', '', 96.33),
(9, '2023-02-17', '12345678', 'Jose Maria', 'Daniela', '', 44.67),
(10, '2023-02-17', '12345678', 'Jose Maria', 'Anita', 'Este piercing fue hecho a Jose Maria el día de hoy 17 de febrero del 2023 para antes del día de san valentín porque quiere impresionar a su crush que también tiene un fierro en un parpado izquuierdo', 4.33),
(11, '2023-02-17', '12345678', 'Jose Maria', 'andre', 'Esta es una venta', 82.00),
(12, '2023-02-17', '12345678', 'Jose Maria', 'andre', 'Esta es otra venta x2', 105.00),
(13, '2023-02-17', '12345678', 'Jose Maria', 'andre', 'Esta es venta', 4.33),
(14, '2023-02-17', '12345678', 'Jose Maria', 'Anita', '', 73.33),
(15, '2023-02-17', '12345678', 'Jose Maria', 'Daniela', '', 17.33),
(16, '2023-02-17', '12345678', 'Jose Maria', 'Daniela', '', 17.33),
(17, '2023-02-17', '12345678', 'Jose Maria', 'Daniela', 'producto de prueba', 77.67),
(18, '2023-02-17', '12345678', 'Jose Maria', 'Daniela', 'prueba 3', 13.00),
(19, '2023-02-17', '12345678', 'Jose Maria', 'Daniela', 'Esta es una venta realizada hoy 17 de febrero', 77.67),
(20, '2023-02-17', '12345678', 'Jose Maria', 'andre', '', 81.99);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`);

--
-- Indices de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id_empleado`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id_venta`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  MODIFY `id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id_empleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `ventas`
--
ALTER TABLE `ventas`
  MODIFY `id_venta` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

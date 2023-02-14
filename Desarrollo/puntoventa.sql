-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 30-01-2023 a las 20:13:25
-- Versión del servidor: 10.1.38-MariaDB
-- Versión de PHP: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
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
(8, 'Carlos', 'Rivera', 'Rios', 654, 654, '654', 'fghfgh'),
(9, 'Carlos', 'Rivera', 'Rios', 654, 654, '654', 'fghfgh');

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
(6, 'aaaaa', 'Joyas', 'aaaaaaaa', 1, 23, 45, 'sdf', 'sdf');

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

--
-- Índices para tablas volcadas
--

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
-- AUTO_INCREMENT de las tablas volcadas
--

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

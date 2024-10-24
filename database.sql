-- MySQL dump 10.13  Distrib 8.0.39, for Linux (x86_64)
--
-- Host: localhost    Database: Manuel
-- ------------------------------------------------------
-- Server version	8.0.39-0ubuntu0.24.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `entradas`
--

DROP TABLE IF EXISTS `entradas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entradas` (
  `idUsuario` int NOT NULL,
  `idEntrada` timestamp NOT NULL,
  PRIMARY KEY (`idEntrada`),
  KEY `idUsuario` (`idUsuario`),
  CONSTRAINT `entradas_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entradas`
--

LOCK TABLES `entradas` WRITE;
/*!40000 ALTER TABLE `entradas` DISABLE KEYS */;
INSERT INTO `entradas` VALUES (1,'2024-10-21 00:00:00'),(1,'2024-10-22 00:00:00'),(1,'2024-10-22 02:58:20'),(1,'2024-10-22 03:06:29'),(1,'2024-10-22 03:08:13'),(5,'2024-10-22 02:32:04'),(6,'2024-10-22 02:36:46'),(7,'2024-10-22 15:28:32'),(7,'2024-10-22 15:51:30'),(8,'2024-10-22 15:30:08'),(8,'2024-10-22 15:31:26'),(8,'2024-10-22 15:36:13'),(8,'2024-10-22 15:36:22'),(8,'2024-10-22 15:50:46'),(8,'2024-10-22 15:51:24'),(9,'2024-10-22 15:48:47'),(10,'2024-10-24 22:50:27');
/*!40000 ALTER TABLE `entradas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombreLogin` varchar(12) DEFAULT NULL,
  `contrasena` varchar(32) DEFAULT NULL,
  `nombreCompleto` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `nombreLogin` (`nombreLogin`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Manuel','10400c6faf166902b52fb97042f1e0eb','Manuel Ejemplo'),(4,'Magomez','12cdb9b24211557ef1802bf5a875fd2c','Manuel Gómez'),(5,'hola','4d186321c1a7f0f354b297e8914ab240','hola'),(6,'Finish','d79695776a5b40f7cadbee1f91a85c82','ultimo'),(7,'Manuel','c4ca4238a0b923820dcc509a6f75849b','Manuel Gonzalez 2'),(8,'1','c4ca4238a0b923820dcc509a6f75849b','1'),(9,'2','C81E728D9D4C2F636F067F89CC14862C','Manuel'),(10,'pepe','C4CA4238A0B923820DCC509A6F75849B','pepe');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-24 20:57:33

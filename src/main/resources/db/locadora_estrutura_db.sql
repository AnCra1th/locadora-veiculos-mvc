-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: locadora_veiculos
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.24.04.1

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
-- Table structure for table `categoria_veiculo`
--

DROP TABLE IF EXISTS `categoria_veiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria_veiculo` (
  `id_categoria_veiculo` int NOT NULL AUTO_INCREMENT,
  `nome_categoria` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` text COLLATE utf8mb4_unicode_ci,
  `valor_diaria_base` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_categoria_veiculo`),
  UNIQUE KEY `nome_categoria` (`nome_categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cpf` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cnh` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_validade_cnh` date DEFAULT NULL,
  `telefone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco_rua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco_numero` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco_complemento` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco_bairro` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco_cidade` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco_estado` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco_cep` varchar(9) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `cnh` (`cnh`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario` (
  `id_funcionario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cpf` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_funcionario`),
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `locacao`
--

DROP TABLE IF EXISTS `locacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locacao` (
  `id_locacao` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `placa_veiculo` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_funcionario_retirada` int NOT NULL,
  `id_funcionario_devolucao` int DEFAULT NULL,
  `id_reserva` int DEFAULT NULL,
  `data_retirada` datetime NOT NULL,
  `data_prevista_devolucao` datetime NOT NULL,
  `data_efetiva_devolucao` datetime DEFAULT NULL,
  `valor_diaria_locacao` decimal(10,2) NOT NULL,
  `valor_caucao` decimal(10,2) DEFAULT '0.00',
  `valor_seguro` decimal(10,2) DEFAULT '0.00',
  `valor_multa_atraso` decimal(10,2) DEFAULT '0.00',
  `valor_total_previsto` decimal(10,2) DEFAULT NULL,
  `valor_total_final` decimal(10,2) DEFAULT NULL,
  `status_locacao` enum('ativa','finalizada','finalizada_com_pendencia','cancelada') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ativa',
  `observacoes_retirada` text COLLATE utf8mb4_unicode_ci,
  `observacoes_devolucao` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id_locacao`),
  UNIQUE KEY `id_reserva` (`id_reserva`),
  KEY `id_cliente` (`id_cliente`),
  KEY `placa_veiculo` (`placa_veiculo`),
  KEY `id_funcionario_retirada` (`id_funcionario_retirada`),
  KEY `id_funcionario_devolucao` (`id_funcionario_devolucao`),
  CONSTRAINT `locacao_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `locacao_ibfk_2` FOREIGN KEY (`placa_veiculo`) REFERENCES `veiculo` (`placa`),
  CONSTRAINT `locacao_ibfk_3` FOREIGN KEY (`id_funcionario_retirada`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `locacao_ibfk_4` FOREIGN KEY (`id_funcionario_devolucao`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `locacao_ibfk_5` FOREIGN KEY (`id_reserva`) REFERENCES `reserva` (`id_reserva`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pagamento`
--

DROP TABLE IF EXISTS `pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamento` (
  `id_pagamento` int NOT NULL AUTO_INCREMENT,
  `id_locacao` int DEFAULT NULL,
  `id_reserva` int DEFAULT NULL,
  `id_funcionario` int DEFAULT NULL,
  `valor_pagamento` decimal(10,2) NOT NULL,
  `data_pagamento` datetime DEFAULT CURRENT_TIMESTAMP,
  `forma_pagamento` enum('dinheiro','cartao_credito','cartao_debito','pix','boleto','transferencia','caucao_reserva','caucao_locacao') COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numero_parcelas` int DEFAULT '1',
  `status_pagamento` enum('pendente','aprovado','recusado','estornado','processando') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'aprovado',
  PRIMARY KEY (`id_pagamento`),
  KEY `id_locacao` (`id_locacao`),
  KEY `id_reserva` (`id_reserva`),
  KEY `id_funcionario` (`id_funcionario`),
  CONSTRAINT `pagamento_ibfk_1` FOREIGN KEY (`id_locacao`) REFERENCES `locacao` (`id_locacao`) ON DELETE SET NULL,
  CONSTRAINT `pagamento_ibfk_2` FOREIGN KEY (`id_reserva`) REFERENCES `reserva` (`id_reserva`) ON DELETE SET NULL,
  CONSTRAINT `pagamento_ibfk_3` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reembolso`
--

DROP TABLE IF EXISTS `reembolso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reembolso` (
  `id_reembolso` int NOT NULL AUTO_INCREMENT,
  `id_pagamento_original` int DEFAULT NULL,
  `id_locacao` int DEFAULT NULL,
  `id_reserva` int DEFAULT NULL,
  `id_funcionario` int DEFAULT NULL,
  `valor_reembolso` decimal(10,2) NOT NULL,
  `data_reembolso` datetime DEFAULT CURRENT_TIMESTAMP,
  `forma_reembolso` enum('dinheiro','transferencia','estorno_cartao','credito_loja') COLLATE utf8mb4_unicode_ci NOT NULL,
  `motivo_reembolso` text COLLATE utf8mb4_unicode_ci,
  `status_reembolso` enum('solicitado','aprovado','processando','efetuado','recusado') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'solicitado',
  PRIMARY KEY (`id_reembolso`),
  KEY `id_pagamento_original` (`id_pagamento_original`),
  KEY `id_locacao` (`id_locacao`),
  KEY `id_reserva` (`id_reserva`),
  KEY `id_funcionario` (`id_funcionario`),
  CONSTRAINT `reembolso_ibfk_1` FOREIGN KEY (`id_pagamento_original`) REFERENCES `pagamento` (`id_pagamento`) ON DELETE SET NULL,
  CONSTRAINT `reembolso_ibfk_2` FOREIGN KEY (`id_locacao`) REFERENCES `locacao` (`id_locacao`) ON DELETE SET NULL,
  CONSTRAINT `reembolso_ibfk_3` FOREIGN KEY (`id_reserva`) REFERENCES `reserva` (`id_reserva`) ON DELETE SET NULL,
  CONSTRAINT `reembolso_ibfk_4` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reserva`
--

DROP TABLE IF EXISTS `reserva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva` (
  `id_reserva` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `id_categoria_veiculo` int NOT NULL,
  `id_funcionario` int DEFAULT NULL,
  `data_reserva` datetime DEFAULT CURRENT_TIMESTAMP,
  `data_prevista_retirada` datetime NOT NULL,
  `data_prevista_devolucao` datetime NOT NULL,
  `valor_estimado` decimal(10,2) DEFAULT NULL,
  `valor_sinal_reserva` decimal(10,2) DEFAULT '0.00',
  `status_reserva` enum('pendente_pagamento_sinal','confirmada','cancelada_cliente','cancelada_sistema','nao_compareceu','utilizada') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pendente_pagamento_sinal',
  `observacoes` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id_reserva`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_categoria_veiculo` (`id_categoria_veiculo`),
  KEY `id_funcionario` (`id_funcionario`),
  CONSTRAINT `reserva_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `reserva_ibfk_2` FOREIGN KEY (`id_categoria_veiculo`) REFERENCES `categoria_veiculo` (`id_categoria_veiculo`),
  CONSTRAINT `reserva_ibfk_3` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `veiculo`
--

DROP TABLE IF EXISTS `veiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `veiculo` (
  `placa` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_categoria_veiculo` int NOT NULL,
  `modelo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `marca` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ano_fabricacao` int DEFAULT NULL,
  `cor` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chassi` varchar(17) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `renavam` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status_veiculo` enum('disponivel','locado','reservado','manutencao','inativo') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'disponivel',
  `observacoes` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`placa`),
  UNIQUE KEY `chassi` (`chassi`),
  UNIQUE KEY `renavam` (`renavam`),
  KEY `id_categoria_veiculo` (`id_categoria_veiculo`),
  CONSTRAINT `veiculo_ibfk_1` FOREIGN KEY (`id_categoria_veiculo`) REFERENCES `categoria_veiculo` (`id_categoria_veiculo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-22 23:25:31

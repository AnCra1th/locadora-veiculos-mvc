-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: locadora_veiculos
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.24.04.1

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

-- Garante que o script pare em caso de erro e use a codificação correta.
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema locadora_veiculos
-- -----------------------------------------------------
-- Cria o banco de dados apenas se ele não existir, para evitar erros.
CREATE SCHEMA IF NOT EXISTS `locadora_veiculos` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `locadora_veiculos` ;

-- -----------------------------------------------------
-- Tabela `categoria_veiculo`
-- Armazena as categorias dos veículos (ex: Popular, SUV, Luxo).
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `categoria_veiculo` (
  `id_categoria_veiculo` INT NOT NULL AUTO_INCREMENT,
  `nome_categoria` VARCHAR(50) NOT NULL,
  `descricao` TEXT NULL,
  `valor_diaria_base` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id_categoria_veiculo`),
  UNIQUE INDEX `nome_categoria_UNIQUE` (`nome_categoria` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabela `cliente`
-- Armazena os dados dos clientes da locadora.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cliente` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(150) NOT NULL,
  `cpf` VARCHAR(14) NOT NULL,
  `cnh` VARCHAR(20) NOT NULL,
  `data_validade_cnh` DATE NULL,
  `telefone` VARCHAR(20) NULL,
  `email` VARCHAR(100) NULL,
  `endereco_rua` VARCHAR(200) NULL,
  `endereco_numero` VARCHAR(10) NULL,
  `endereco_complemento` VARCHAR(100) NULL,
  `endereco_bairro` VARCHAR(100) NULL,
  `endereco_cidade` VARCHAR(100) NULL,
  `endereco_estado` VARCHAR(2) NULL,
  `endereco_cep` VARCHAR(9) NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC) VISIBLE,
  UNIQUE INDEX `cnh_UNIQUE` (`cnh` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabela `funcionario`
-- Armazena os dados dos funcionários que operam o sistema.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `funcionario` (
  `id_funcionario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(150) NOT NULL,
  `cpf` VARCHAR(14) NOT NULL,
  `telefone` VARCHAR(20) NULL,
  `email` VARCHAR(100) NULL,
  PRIMARY KEY (`id_funcionario`),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabela `veiculo`
-- Armazena os dados de cada veículo individual da frota.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `veiculo` (
  `placa` VARCHAR(10) NOT NULL,
  `id_categoria_veiculo` INT NOT NULL,
  `modelo` VARCHAR(50) NOT NULL,
  `marca` VARCHAR(50) NOT NULL,
  `ano_fabricacao` INT NULL,
  `cor` VARCHAR(30) NULL,
  `chassi` VARCHAR(17) NULL,
  `renavam` VARCHAR(11) NULL,
  `status_veiculo` ENUM('disponivel', 'locado', 'reservado', 'manutencao', 'inativo') NOT NULL DEFAULT 'disponivel',
  `observacoes` TEXT NULL,
  PRIMARY KEY (`placa`),
  UNIQUE INDEX `chassi_UNIQUE` (`chassi` ASC) VISIBLE,
  UNIQUE INDEX `renavam_UNIQUE` (`renavam` ASC) VISIBLE,
  INDEX `fk_veiculo_categoria_veiculo_idx` (`id_categoria_veiculo` ASC) VISIBLE,
  CONSTRAINT `fk_veiculo_categoria_veiculo`
    FOREIGN KEY (`id_categoria_veiculo`)
    REFERENCES `categoria_veiculo` (`id_categoria_veiculo`)
    ON DELETE RESTRICT ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabela `reserva`
-- Armazena as reservas de uma CATEGORIA de veículo para um cliente.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reserva` (
  `id_reserva` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `id_categoria_veiculo` INT NOT NULL,
  `id_funcionario` INT NULL,
  `data_reserva` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `data_prevista_retirada` DATETIME NOT NULL,
  `data_prevista_devolucao` DATETIME NOT NULL,
  `valor_estimado` DECIMAL(10,2) NULL,
  `valor_sinal_reserva` DECIMAL(10,2) NULL DEFAULT '0.00',
  `status_reserva` ENUM('pendente_pagamento_sinal', 'confirmada', 'cancelada_cliente', 'cancelada_sistema', 'nao_compareceu', 'utilizada') NOT NULL DEFAULT 'pendente_pagamento_sinal',
  `observacoes` TEXT NULL,
  PRIMARY KEY (`id_reserva`),
  INDEX `fk_reserva_cliente_idx` (`id_cliente` ASC) VISIBLE,
  INDEX `fk_reserva_categoria_veiculo_idx` (`id_categoria_veiculo` ASC) VISIBLE,
  INDEX `fk_reserva_funcionario_idx` (`id_funcionario` ASC) VISIBLE,
  CONSTRAINT `fk_reserva_cliente`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `cliente` (`id_cliente`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_reserva_categoria_veiculo`
    FOREIGN KEY (`id_categoria_veiculo`)
    REFERENCES `categoria_veiculo` (`id_categoria_veiculo`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_reserva_funcionario`
    FOREIGN KEY (`id_funcionario`)
    REFERENCES `funcionario` (`id_funcionario`)
    ON DELETE SET NULL ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabela `locacao`
-- Armazena os dados de uma locação efetiva de um VEÍCULO específico.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `locacao` (
  `id_locacao` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `placa_veiculo` VARCHAR(10) NOT NULL,
  `id_funcionario_retirada` INT NOT NULL,
  `id_funcionario_devolucao` INT NULL,
  `id_reserva` INT NULL,
  `data_retirada` DATETIME NOT NULL,
  `data_prevista_devolucao` DATETIME NOT NULL,
  `data_efetiva_devolucao` DATETIME NULL,
  `valor_diaria_locacao` DECIMAL(10,2) NOT NULL,
  `valor_caucao` DECIMAL(10,2) NULL DEFAULT '0.00',
  `valor_seguro` DECIMAL(10,2) NULL DEFAULT '0.00',
  `valor_multa_atraso` DECIMAL(10,2) NULL DEFAULT '0.00',
  `valor_total_previsto` DECIMAL(10,2) NULL,
  `valor_total_final` DECIMAL(10,2) NULL,
  `status_locacao` ENUM('ativa', 'finalizada', 'finalizada_com_pendencia', 'cancelada') NOT NULL DEFAULT 'ativa',
  `observacoes_retirada` TEXT NULL,
  `observacoes_devolucao` TEXT NULL,
  PRIMARY KEY (`id_locacao`),
  UNIQUE INDEX `id_reserva_UNIQUE` (`id_reserva` ASC) VISIBLE,
  INDEX `fk_locacao_cliente_idx` (`id_cliente` ASC) VISIBLE,
  INDEX `fk_locacao_veiculo_idx` (`placa_veiculo` ASC) VISIBLE,
  INDEX `fk_locacao_funcionario_retirada_idx` (`id_funcionario_retirada` ASC) VISIBLE,
  INDEX `fk_locacao_funcionario_devolucao_idx` (`id_funcionario_devolucao` ASC) VISIBLE,
  CONSTRAINT `fk_locacao_cliente`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `cliente` (`id_cliente`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_locacao_veiculo`
    FOREIGN KEY (`placa_veiculo`)
    REFERENCES `veiculo` (`placa`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_locacao_funcionario_retirada`
    FOREIGN KEY (`id_funcionario_retirada`)
    REFERENCES `funcionario` (`id_funcionario`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_locacao_funcionario_devolucao`
    FOREIGN KEY (`id_funcionario_devolucao`)
    REFERENCES `funcionario` (`id_funcionario`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_locacao_reserva`
    FOREIGN KEY (`id_reserva`)
    REFERENCES `reserva` (`id_reserva`)
    ON DELETE SET NULL ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- DADOS INICIAIS (AMOSTRA)
-- Adiciona dados iniciais para que a aplicação não comece vazia.
-- -----------------------------------------------------
INSERT INTO `categoria_veiculo` (`nome_categoria`, `descricao`, `valor_diaria_base`) VALUES
('Popular', 'Veículos econômicos que cabem no seu bolso', 100.00),
('Executivo', 'Veículos executivos confortáveis e profissionais', 250.00),
('SUV', 'Veículos utilitários esportivos para toda a família', 300.00);

INSERT INTO `funcionario` (`nome`, `cpf`, `telefone`, `email`) VALUES
('Geraldo de Rívia', '11122233344', '11999998888', 'geraldo.rivia@locadora.com'),
('Eva Lakatos', '55566677788', '11977776666', 'eva.lakatos@locadora.com');

-- Dump completed on 2025-05-22 23:25:31


-- Dump completed on 2025-05-22 23:25:31

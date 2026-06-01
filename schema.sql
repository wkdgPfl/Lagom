-- MySQL dump 10.13  Distrib 9.2.0, for macos15.2 (arm64)
--
-- Host: 127.0.0.1    Database: lagom
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `account_id` bigint NOT NULL AUTO_INCREMENT,
  `balance` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `end_date` date DEFAULT NULL,
  `goal_amount` bigint DEFAULT NULL,
  `goal_type` enum('AMOUNT','PERIOD') NOT NULL,
  `is_completed` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  KEY `FK7m8ru44m93ukyb61dfxw0apf6` (`user_id`),
  CONSTRAINT `FK7m8ru44m93ukyb61dfxw0apf6` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expense` (
  `expense_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint NOT NULL,
  `category` enum('ALLOWANCE','BEAUTY','BONUS','CULTURE','EDUCATION','EXPENSE_ETC','FINANCIAL','FOOD','HEALTH','HOUSING','INCOME_ETC','SALARY','SHOPPING','SIDE_INCOME','TRANSPORT') NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `emotion` enum('CALM','DEPRESSED','EXCITED','IMPULSIVE','JOY','STRESSED') DEFAULT NULL,
  `evaluation` int DEFAULT NULL,
  `is_recurring` bit(1) NOT NULL,
  `is_reevaluated` bit(1) NOT NULL,
  `last_generated_at` date DEFAULT NULL,
  `memo` varchar(300) DEFAULT NULL,
  `payment_at` datetime(6) NOT NULL,
  `reevaluation_at` datetime(6) DEFAULT NULL,
  `repeat_cycle` enum('DAILY','MONTHLY','WEEKLY') DEFAULT NULL,
  `repeat_days` varchar(100) DEFAULT NULL,
  `repeat_end_date` date DEFAULT NULL,
  `repeat_start_date` date DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` enum('EXPENSE','INCOME') NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `FK758h5dgdblrpwoaaycbmn29i0` (`user_id`),
  CONSTRAINT `FK758h5dgdblrpwoaaycbmn29i0` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `memo` varchar(300) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `type` enum('HAPPY','RECOVER') NOT NULL,
  `account_id` bigint NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `FK6g20fcr3bhr6bihgy24rq1r1b` (`account_id`),
  CONSTRAINT `FK6g20fcr3bhr6bihgy24rq1r1b` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `kakao_id` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK4tp32nb01jmfcirpipti37lfs` (`kakao_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-26 23:46:39

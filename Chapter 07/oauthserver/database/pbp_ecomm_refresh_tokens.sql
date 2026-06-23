-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: pbp_ecomm
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `token_hash` varchar(255) NOT NULL,
  `device_info` varchar(500) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `expires_at` timestamp NOT NULL,
  `is_revoked` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_hash` (`token_hash`),
  KEY `idx_refresh_user_id` (`user_id`),
  KEY `idx_refresh_token_hash` (`token_hash`),
  KEY `idx_refresh_expires` (`expires_at`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES ('3458d927-973a-4915-839e-d1e9879bd669','2a211b93-2460-4e17-adda-2aaa8ed73f45','3bgSF+syxNiFNXmBZTSseN8cev7g5ZLeFlKp7FRwaHM=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-06 21:55:55',0,'2026-05-03 09:55:55'),('3bc19fbe-922c-48cc-9af6-d2bc88720eb8','2a211b93-2460-4e17-adda-2aaa8ed73f45','dZbKa+bgMC2l5lN4+EuMYFZtWLZRZ3ccWiGn8ioim9s=',NULL,NULL,'2026-05-04 08:26:01',0,'2026-04-30 20:26:01'),('914451d9-865e-4c64-93c8-0898cd8d7785','2a211b93-2460-4e17-adda-2aaa8ed73f45','HMtQt9cDXAWK58fg0jsAQNJSqqRY2XKWSsoyJuQeN6U=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-04 08:27:11',0,'2026-04-30 20:27:11'),('965919f6-ff6a-491a-84b2-792dc4750367','2a211b93-2460-4e17-adda-2aaa8ed73f45','CrIe4h9cQu6MCdNdxu8oul1bVBra3guBbdUAI1Ti/N8=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-06 20:53:52',0,'2026-05-03 08:53:52'),('b76ae81a-7272-463b-bac1-85f29189f28b','2a211b93-2460-4e17-adda-2aaa8ed73f45','sDN7fKGj3fGWny+nv5qEXs6Xgr4hQg2cA0WjdgE/nqk=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-06 01:53:02',0,'2026-05-02 13:53:02'),('be348bad-f5c8-4772-b036-29a74271fc2b','2a211b93-2460-4e17-adda-2aaa8ed73f45','IvJRqqj5vHejyWpcXcgDvNXLEJ2Mdfe7WvBBZi100No=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-06 21:38:30',0,'2026-05-03 09:38:30'),('cf84f53b-e0bc-495d-8b76-78519e685224','2a211b93-2460-4e17-adda-2aaa8ed73f45','GeZY67aSTGnCvfdor0lQnqktUfyo30TNkejBRPnhgV4=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-06 20:32:26',0,'2026-05-03 08:32:26'),('d03ada56-ae1b-46cf-8460-96efce9987f2','2a211b93-2460-4e17-adda-2aaa8ed73f45','lFnw2XJXFO2iCrdbQZBw2V3o0CcqR5EKWdjzSwAK6bo=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-06 22:11:34',0,'2026-05-03 10:11:34'),('f3e57e35-0975-4537-afac-8f8ac910b1e3','34fd2aa9-146f-464c-9fc9-337af7e94c00','zUKhCW+I5OvuUui5mNXQob8HgzzM+7g9xyTRge+fQBU=',NULL,NULL,'2026-05-08 09:34:22',0,'2026-05-04 21:34:22'),('f60ccf01-ea36-420e-a621-314eafd8ee12','2a211b93-2460-4e17-adda-2aaa8ed73f45','PUukDKMJBIxzEMxYMeQCDwOtY+OssXTrhZB7aD8o4VM=','PostmanRuntime/7.51.1','0:0:0:0:0:0:0:1','2026-05-06 21:14:45',0,'2026-05-03 09:14:45');
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-12  1:15:12

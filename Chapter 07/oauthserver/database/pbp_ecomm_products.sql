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
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `category_id` int DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `price` decimal(12,2) NOT NULL,
  `sku` varchar(100) NOT NULL,
  `image_urls` varchar(255) DEFAULT NULL,
  `attributes` json DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `average_rating` decimal(3,2) DEFAULT '0.00',
  `discount_percent` decimal(3,2) DEFAULT '0.00',
  `review_count` int DEFAULT '0',
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku` (`sku`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('1',4,'iPhone 14','Apple iPhone 14 128GB',79999.00,'IP14-128-BLK','https://images.unsplash.com/photo-1661961112958-2f7bde2b6a0a','{\"ram\": \"6GB\", \"brand\": \"Apple\", \"color\": \"Black\", \"storage\": \"128GB\"}',1,'2026-05-04 21:54:23',4.60,1.00,245,NULL),('10',4,'iPhone 14 plus','Apple iPhone 14 128GB',79999.00,'IP14-plus-128-BLK','https://images.unsplash.com/photo-1661961112958-2f7bde2b6a0a','{\"ram\": \"6GB\", \"brand\": \"Apple\", \"color\": \"Black\", \"storage\": \"128GB\"}',1,'2026-05-04 21:56:27',4.60,0.00,245,NULL),('2',4,'Samsung Galaxy S23','Samsung flagship smartphone',74999.00,'SGS23-256-GRN','https://images.unsplash.com/photo-1678911820864-e2c2c2a5c1c4','{\"ram\": \"8GB\", \"brand\": \"Samsung\", \"color\": \"Green\", \"storage\": \"256GB\"}',1,'2026-05-04 21:57:02',4.50,0.00,180,NULL),('3',5,'MacBook Air M2','Apple M2 lightweight laptop',109999.00,'MBA-M2-256','https://images.unsplash.com/photo-1517336714731-489689fd1ca8','{\"ram\": \"8GB\", \"brand\": \"Apple\", \"storage\": \"256GB SSD\"}',1,'2026-05-04 21:57:02',4.80,0.00,320,NULL),('3bbd9948-cab8-4f0f-a223-c541ea7c0229',1,'Nike Air Max 230170','Lightweight running shoe with Air Max cushioning.',11000.00,'SHOE-NK-AM270-42','https://cdn.shop.com/nike-am270-front.jpg','{\"size\": \"42\", \"brand\": \"Nike\", \"color\": \"Red/Black\", \"gender\": \"Unisex\", \"material\": \"Mesh\"}',1,NULL,0.00,NULL,0,NULL),('4',5,'Dell XPS 13','Premium ultrabook',99999.00,'DXPS13-512','https://images.unsplash.com/photo-1518770660439-4636190af475','{\"ram\": \"16GB\", \"brand\": \"Dell\", \"storage\": \"512GB SSD\"}',1,'2026-05-04 21:57:02',4.40,0.00,150,NULL),('41c5bdb6-fb90-441b-a1a1-c0e79cbc70dc',1,'Nike Air Max 230180','Lightweight running shoe with Air Max cushioning.',900.00,'SHOE-NK-AM270-43','https://cdn.shop.com/nike-am270-front.jpg','{\"size\": \"42\", \"brand\": \"Nike\", \"color\": \"Red/Black\", \"gender\": \"Unisex\", \"material\": \"Mesh\"}',1,NULL,0.00,NULL,0,NULL),('5',9,'Nike Air Max','Comfortable running shoes',5999.00,'NIKE-AM-42','https://images.unsplash.com/photo-1528701800489-20be3c2ea55c','{\"size\": \"42\", \"brand\": \"Nike\", \"color\": \"White\"}',1,'2026-05-04 21:57:02',4.30,0.00,90,NULL),('6',9,'Adidas Ultraboost','High performance sneakers',8999.00,'ADI-UB-43','https://images.unsplash.com/photo-1519741497674-611481863552','{\"size\": \"43\", \"brand\": \"Adidas\", \"color\": \"Black\"}',1,'2026-05-04 21:57:02',4.60,0.00,110,NULL),('7',10,'Wooden Sofa Set','Premium wooden sofa',25999.00,'SOFA-WOOD-01','https://images.unsplash.com/photo-1501045661006-fcebe0257c3f','{\"seater\": \"3\", \"material\": \"Wood\"}',1,'2026-05-04 21:57:02',4.20,0.00,60,NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-12  1:15:13

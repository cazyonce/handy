create database if not exists handy;

CREATE TABLE `api_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL,
  `method` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  `describe` varchar(255) NOT NULL DEFAULT '',
  `response_code` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0（禁用）|1（启用）',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `api_mapping_header` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_mapping_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(255) NOT NULL DEFAULT '',
  `header_type` varchar(10) NOT NULL COMMENT 'REUQEST|RESPONSE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4;
-- ----------------------------
-- Table structure for tob_order_main
-- ----------------------------
DROP TABLE IF EXISTS `tob_order_main`;
CREATE TABLE `tob_order_main` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_type` int(11) NOT NULL COMMENT '订单类型 1 支付请求 2 提款请求',
  `order_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `order_state` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '联合order_type字段使用\r\n支付—订单状态(1 创建、2 待确认收款、3 已确认收款、0、已作废0)\r\n提款—订单状态(1 创建、2 待处理、3 已处理、0、已作废0)',
  `order_finish_time` datetime DEFAULT NULL COMMENT '订单完成时间',
  `merchant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户账户Id 关联tob_order_merchant',
  `merchant_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户姓名',
  `merchant_contact` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户联系方式',
  `merchant_account_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户账户 关联tob_order_merchant_account',
  `source_currency` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '源币种',
  `target_currency` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标币种',
  `source_currency_money` double NOT NULL DEFAULT '0' COMMENT '源币种金额',
  `target_currency_money` double NOT NULL DEFAULT '0' COMMENT '目标币种兑付金额',
  `exchange_rate` double NOT NULL DEFAULT '0' COMMENT '汇率',
  `user_pay_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户支付方式(支付宝alipay、银行卡bank_card)',
  `user_pay_area_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户支付区域区号',
  `user_pay_account_id` varchar(32) DEFAULT NULL COMMENT '平台账户Id 关联order_platform_account',
  `user_pay_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户支付账号',
  `user_pay_account_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户支付账号真实姓名',
  `user_pay_bank` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户支付账号开户行',
  `user_pay_bank_branch` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户支付账号开户网点',
  `order_text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `notify_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '确认收款之后，商户的回调地址',
  `merchant_collection_method` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户收款方式(支付宝alipay、银行卡bank_card)',
  `merchant_collection_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户收款账号',
  `merchant_collection_account_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户收款账号真实姓名',
  `merchant_collection_bank` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户收款账号开户行',
  `merchant_collection_bank_branch` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户收款账号开户网点',
  `merchant_collection_area_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户收款区域区号',
  `assurer_cny_money` double DEFAULT NULL COMMENT '承兑商需支付的人民币金额',
  `assurer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商ID',
  `assurer_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商名称',
  `assurer_collection_method` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商收款方式(支付宝alipay、银行卡bank_card)',
  `assurer_collection_account_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商收款账号ID',
  `assurer_collection_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商收款账号',
  `assurer_collection_account_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商收款账号真实姓名',
  `assurer_collection_bank` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商收款账号开户行',
  `assurer_collection_bank_branch` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商收款账号开户网点',
  `assurer_collection_time` datetime DEFAULT NULL COMMENT '承兑商确认收款时间',
  `assurer_pay_method` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商支付方式(支付宝alipay、银行卡bank_card)',
  `assurer_pay_account_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商支付账号id',
  `assurer_pay_account_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商支付账号真实姓名',
  `assurer_pay_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商支付账号',
  `assurer_pay_voucher` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商支付凭证(图片url,中文逗号分隔)',
  `assurer_pay_bank` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商支付账号开户行',
  `assurer_pay_bank_branch` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '承兑商支付账号开户网点',
  `assurer_pay_time` datetime DEFAULT NULL COMMENT '承兑商兑付时间',
  `auto_dispatch_state` int(11) DEFAULT NULL COMMENT '自动分配承兑商状态 0 成功 1失败 ',
  `auto_dispatch_text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '自动分配失败说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tob_order_merchant
-- ----------------------------
DROP TABLE IF EXISTS `tob_order_merchant`;
CREATE TABLE `tob_order_merchant` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
  `merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户名称',
  `country_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户国家码',
  `online_state` int(11) DEFAULT '0' COMMENT '在线状态  0 离线 1在线',
  `merchant_state` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'to_audit' COMMENT '商户状态(待审核(to_audit)、正常接单(normal)、余额不足(lack_balance)、禁止接单(ban_order))',
  `merchant_rate` double DEFAULT '0' COMMENT '费率',
  `can_use_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '可用额度',
  `used_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '已用额度',
  `total_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '总额度=已用额度+可用额度',
  `merchant_strategy` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'one' COMMENT '策略(单一制(one)、多单制(more))',
  `pay_lock_money` double(11,0) NOT NULL DEFAULT '0' COMMENT '支付锁定金额',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `merchant_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `ensure_money` double DEFAULT '0' COMMENT '保证金',
  `lease_money` double DEFAULT '0' COMMENT '租赁金余额',
  `ensure_proportion` double DEFAULT '1' COMMENT '保证金比例',
  `merchant_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '照片',
  `card_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '1' COMMENT '证件类型 1-身份证 2-军官证 3-护照 4-驾照',
  `card_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '证件号码',
  `card_front_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '证件正面照',
  `card_back_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '证件反面照',
  `card_hold_picture` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手持证件照',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tob_order_merchant_account
-- ----------------------------
DROP TABLE IF EXISTS `tob_order_merchant_account`;
CREATE TABLE `tob_order_merchant_account` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户ID',
  `account_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'alipay' COMMENT '账户类型(支付宝(alipay)、银行卡(bank_card))',
  `account_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户',
  `open_bank` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '开户行',
  `open_bank_branch` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '开户网点',
  `area_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区号',
  `real_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '真实姓名',
  `use_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'pay_collection' COMMENT '使用方式 (支付(pay)、收款(collection)、支付+收款(pay_collection))',
  `pay_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '每日支付限额',
  `pay_used_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '支付已用额度',
  `pay_can_use_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '支付可用额度(日)',
  `collection_used_limit` double(11,0) NOT NULL DEFAULT '0' COMMENT '收款已用额度(日)',
  `pay_lock_money` double(11,0) NOT NULL DEFAULT '0' COMMENT '支付锁定金额',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `sub_account` int(11) DEFAULT '1' COMMENT '是否自动分账 1 是 0 否',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_merchantId_areaCode` (`merchant_id`,`area_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tob_order_merchant_money_change
-- ----------------------------
DROP TABLE IF EXISTS `tob_order_merchant_money_change`;
CREATE TABLE `tob_order_merchant_money_change` (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户ID',
  `merchant_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商户名称',
  `merchant_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `change_money` double DEFAULT '0' COMMENT '变动金额',
  `change_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '增加、减少',
  `change_class` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '变动种类 保证金、租赁金',
  `flag` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'init' COMMENT '标记 待处理、已处理',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `change_text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单编号',
  `error_text` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报错异常',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

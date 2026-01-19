# 个人积分接口测试指南

## 1. 获取积分记录

**接口路径**: `GET /api/client/points/records?page=1&size=10`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": "20005",
        "type": "exchange",
        "points": -500,
        "remark": "兑换礼品：景区门票优惠券",
        "createTime": "2023-12-01T15:00:00"
      },
      {
        "id": "20004",
        "type": "be_liked",
        "points": 3,
        "remark": "被点赞",
        "createTime": "2023-12-01T14:00:00"
      },
      {
        "id": "20003",
        "type": "comment",
        "points": 5,
        "remark": "评论打卡记录",
        "createTime": "2023-12-01T11:00:00"
      },
      {
        "id": "20002",
        "type": "like",
        "points": 5,
        "remark": "点赞打卡记录",
        "createTime": "2023-12-01T10:30:00"
      },
      {
        "id": "20001",
        "type": "checkin",
        "points": 10,
        "remark": "打卡景点：入口广场",
        "createTime": "2023-12-01T09:00:00"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

## 2. 获取礼品列表

**接口路径**: `GET /api/client/gifts`

**响应示例**:
```json
{
  "code": 200,
  "data": [
    {
      "id": "10001",
      "name": "景区门票优惠券",
      "points": 500,
      "stock": 100,
      "image": "https://example.com/images/ticket.jpg",
      "desc": "景区门票优惠券，可享受门票8折优惠",
      "status": 1
    },
    {
      "id": "10002",
      "name": "景区纪念品",
      "points": 800,
      "stock": 50,
      "image": "https://example.com/images/souvenir.jpg",
      "desc": "景区特色纪念品，精美工艺",
      "status": 1
    },
    {
      "id": "10003",
      "name": "餐饮代金券",
      "points": 300,
      "stock": 200,
      "image": "https://example.com/images/voucher.jpg",
      "desc": "景区餐饮代金券，满100减30",
      "status": 1
    },
    {
      "id": "10004",
      "name": "导游服务",
      "points": 1200,
      "stock": 20,
      "image": "https://example.com/images/guide.jpg",
      "desc": "专业导游服务，时长2小时",
      "status": 1
    },
    {
      "id": "10005",
      "name": "VIP体验券",
      "points": 2000,
      "stock": 10,
      "image": "https://example.com/images/vip.jpg",
      "desc": "VIP体验券，享受景区VIP通道和专属服务",
      "status": 1
    }
  ]
}
```

## 3. 兑换礼品

**接口路径**: `POST /api/client/gifts/exchange`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**:
```json
{
  "giftId": "10001"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "兑换申请提交成功"
}
```

## 4. 获取兑换记录

**接口路径**: `GET /api/client/gifts/records?page=1&size=10`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": "30005",
        "giftName": "VIP体验券",
        "giftNumber": 1,
        "points": 2000,
        "applyTime": "2023-12-05T16:00:00",
        "status": "rejected"
      },
      {
        "id": "30004",
        "giftName": "导游服务",
        "giftNumber": 1,
        "points": 1200,
        "applyTime": "2023-12-04T09:00:00",
        "status": "received"
      },
      {
        "id": "30003",
        "giftName": "餐饮代金券",
        "giftNumber": 2,
        "points": 600,
        "applyTime": "2023-12-03T14:00:00",
        "status": "delivered"
      },
      {
        "id": "30002",
        "giftName": "景区纪念品",
        "giftNumber": 1,
        "points": 800,
        "applyTime": "2023-12-02T10:00:00",
        "status": "pending"
      },
      {
        "id": "30001",
        "giftName": "景区门票优惠券",
        "giftNumber": 1,
        "points": 500,
        "applyTime": "2023-12-01T15:00:00",
        "status": "approved"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

## 测试步骤

1. **导入测试数据**:
   执行 `mysql/points_test_data.sql` 文件，导入测试数据

2. **获取用户Token**:
   使用用户登录接口获取Token，例如使用用户ID为1的用户登录

3. **测试获取积分记录**:
   调用 `GET /api/client/points/records` 接口，验证返回的积分记录

4. **测试获取礼品列表**:
   调用 `GET /api/client/gifts` 接口，验证返回的礼品列表

5. **测试兑换礼品**:
   调用 `POST /api/client/gifts/exchange` 接口，使用礼品ID进行兑换
   注意：确保用户有足够的积分

6. **测试获取兑换记录**:
   调用 `GET /api/client/gifts/records` 接口，验证返回的兑换记录

## 注意事项

1. 所有接口都需要在请求头中携带有效的JWT Token
2. 兑换礼品时会检查用户积分是否足够，以及礼品库存是否充足
3. 兑换成功后会扣除用户积分和礼品库存，并生成积分记录和兑换记录
4. 积分记录中的type字段含义：
   - checkin: 打卡获得积分
   - like: 点赞获得积分
   - comment: 评论获得积分
   - be_liked: 被点赞获得积分
   - exchange: 兑换礼品消耗积分
5. 兑换记录中的status字段含义：
   - pending: 待审核
   - approved: 已核销
   - delivered: 已发货
   - received: 已收货
   - rejected: 已拒绝
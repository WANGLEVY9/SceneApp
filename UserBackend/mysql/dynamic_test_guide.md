# 动态与推荐接口测试指南

## 测试数据准备

确保已执行以下SQL文件中的测试数据：
- checkin_test_data.sql（打卡相关测试数据）
- merchant_test_data.sql（商家测试数据，如果存在）

## 接口测试

### 1. 获取推荐打卡点

**请求地址：**
```
GET /api/client/recommend/checkin?sort=distance&page=1&size=10&lat=32.0605112&lng=118.796877
```

**请求头：**
```
无
```

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sort | String | 是 | 排序方式：distance-按距离排序，hot-按热度排序 |
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页数量，默认10 |
| lat | Double | 是 | 用户纬度 |
| lng | Double | 是 | 用户经度 |

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "spotId": "1",
        "name": "中山陵",
        "distance": "1.23km",
        "hot": 15
      },
      {
        "spotId": "2",
        "name": "明孝陵",
        "distance": "2.45km",
        "hot": 12
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 2. 获取推荐商家

**请求地址：**
```
GET /api/client/recommend/merchants?sort=hot&page=1&size=10&lat=32.0605112&lng=118.796877
```

**请求头：**
```
无
```

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sort | String | 是 | 排序方式：hot-按热度排序 |
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页数量，默认10 |
| lat | Double | 是 | 用户纬度 |
| lng | Double | 是 | 用户经度 |

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": "1",
        "name": "夫子庙小吃街",
        "distance": "3.67km"
      },
      {
        "id": "2",
        "name": "老门东美食城",
        "distance": "4.12km"
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 3. 获取动态流

**请求地址：**
```
GET /api/client/dynamic?page=1&size=10
```

**请求头：**
```
Authorization: Bearer {token}
```

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页数量，默认10 |

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": "1",
        "userId": "1001",
        "nickname": "游客小明",
        "content": "今天天气真好，中山陵风景如画！",
        "image": "https://example.com/image1.jpg",
        "time": "2023-05-15T10:30:00",
        "likeCount": 5
      },
      {
        "id": "2",
        "userId": "1002",
        "nickname": "旅行者小红",
        "content": "明孝陵的历史感让人震撼",
        "image": "https://example.com/image2.jpg",
        "time": "2023-05-15T09:15:00",
        "likeCount": 3
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 4. 评论动态

**请求地址：**
```
POST /api/client/dynamic/comment
```

**请求头：**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体：**
```json
{
  "dynamicId": "1",
  "content": "确实很美，我也去过！"
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "points": 5
  }
}
```

## 测试工具推荐

1. **Postman**：图形化界面，方便保存和管理API请求
2. **Swagger UI**：通过浏览器访问 http://localhost:8080/swagger-ui.html
3. **curl命令**：命令行工具，适合快速测试

## curl命令示例

### 获取推荐打卡点
```bash
curl -X GET "http://localhost:8080/api/client/recommend/checkin?sort=distance&page=1&size=10&lat=32.0605112&lng=118.796877"
```

### 获取推荐商家
```bash
curl -X GET "http://localhost:8080/api/client/recommend/merchants?sort=hot&page=1&size=10&lat=32.0605112&lng=118.796877"
```

### 获取动态流
```bash
curl -X GET "http://localhost:8080/api/client/dynamic?page=1&size=10" \
-H "Authorization: Bearer 1001"
```

### 评论动态
```bash
curl -X POST "http://localhost:8080/api/client/dynamic/comment" \
-H "Authorization: Bearer 1001" \
-H "Content-Type: application/json" \
-d '{
  "dynamicId": "1",
  "content": "确实很美，我也去过！"
}'
```

## 注意事项

1. 测试时请确保服务已启动，端口为8080（或根据实际情况调整）
2. token参数目前简化处理，直接使用用户ID作为token，实际项目中应使用JWT
3. 测试数据需要提前准备，确保打卡记录和商家数据存在
4. 评论功能需要有效的打卡记录ID，否则会返回错误
### 创建数据库和用户

> 一定要用admin用户执行

```sql
use cutejava
db.createUser({
    user: "cutejava",
    pwd: "123456",
    roles: [{
        role: "dbOwner",
        db: "cutejava"
    }]
})
```